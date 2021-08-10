package com.itridtechnologies.resturantapp.UiViews.Activities.Frags;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.itridtechnologies.resturantapp.Adapters.AdapterHistoryFragment;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.HistoryDetails;
import com.itridtechnologies.resturantapp.UiViews.Activities.MainActivity;
import com.itridtechnologies.resturantapp.UiViews.Activities.Menu;
import com.itridtechnologies.resturantapp.UiViews.Activities.Settings;
import com.itridtechnologies.resturantapp.UiViews.Activities.help;
import com.itridtechnologies.resturantapp.model.NewHistory;
import com.itridtechnologies.resturantapp.models.historyNew.NewHistoryWithTotals;
import com.itridtechnologies.resturantapp.models.orderHistory.ItemsItem;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class FragmentHistory extends Fragment {

    private RecyclerView mHistoryRecyclerView; //Main RecyclerView
    private List<ItemsItem> mHistoryList = new ArrayList<>();
    private final ArrayList<NewHistory> histOrderList = new ArrayList<>();
    private ProgressBar mProgressBarHistFull;
    private NestedScrollView mNSVHist;
    private PreferencesManager pm;
    private TextView mNoRecordFound;
    private LinearLayout mLayout;

    //Table Row
    private TableRow mTRDatePicker;

    //Illustrations When No History is avaiable
    private ImageView mNoHistoryImage;
    private TextView mTVEmpty;

    //Radio Button
    private RadioGroup mRadioGroup;
    private RadioButton mShowAll;

    //Working Variables
    private Double mTotalValue = 0.00;

    //Calender View
    private TextView mDateET;
    private String datee = "";

    //Swipe Refresh Layout
    private SwipeRefreshLayout mSwipeHistory;

    //Adapter
    AdapterHistoryFragment adapter;

    //Starting and End Date For Filtering
    private String mStartDate = "";
    private String mEndDate = "";
    private String mStartMonth;
    private String mEndMonth;
    private String mStartDay;
    private String mEndDay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        mHistoryRecyclerView = root.findViewById(R.id.recycler_view_history);
        mProgressBarHistFull = root.findViewById(R.id.pb_hist_center);
        pm = new PreferencesManager(requireContext());
        mNSVHist = root.findViewById(R.id.nsv_history);
        mLayout = root.findViewById(R.id.noOrder);
        mDateET = root.findViewById(R.id.etDate);
        mRadioGroup = root.findViewById(R.id.radioGroup);
        mShowAll = root.findViewById(R.id.rbshowall);
        mNoRecordFound = root.findViewById(R.id.tvNoRecordFound);
        mNoHistoryImage = root.findViewById(R.id.ic_noOrder_hist);
        mTVEmpty = root.findViewById(R.id.tv_no_order_hist);

        mShowAll.setChecked(true);
        //Swipe Refresh Layout
        mSwipeHistory = root.findViewById(R.id.srl_hist);
        //setting name of toolbar
        Toolbar mToolbarHist = root.findViewById(R.id.action_bar_history);

        ///Header Name
        mToolbarHist.setTitle("History");

        //Setting Toolbar Navigation Bar
        mToolbarHist.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_availability: {
                    Intent intent = new Intent(getContext(), Menu.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.settings: {
                    Intent intent = new Intent(getContext(), Settings.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.call_support: {
                    String mCellNumber = AppManager.getBusinessDetails().getData().getResults().getPhoneNumber();
                    String uri = "tel:" + mCellNumber.trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                    return false;
                }
                case R.id.over_flow_log_out:{
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    pm.clearSharedPref();
                    pm.saveMyDataBool("login", false);
                    startActivity(intent);
                    return false;
                }
                default: {
                    Intent intent = new Intent(getContext(), help.class);
                    startActivity(intent);
                    return false;
                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        Log.e(TAG, "SimpleDateFormat" + currentDateandTime);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeHistory.setProgressViewOffset(true, 10, 180);
        //Pull to Swipe
        mSwipeHistory.setOnRefreshListener(() -> {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                mSwipeHistory.setRefreshing(false);

            }, 2000);
        });
        getFullHistoryOrders();
    }


    @Override
    public void onStart() {
        super.onStart();

        //date Range Picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();

        builder.setTitleText("Select a Range to get Orders");
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);

//        DatePickerDialog.
        final MaterialDatePicker materialDatePicker = builder.build();

        //////For taking date from calender
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rbselectDate) {
                histOrderList.clear();
                mDateET.setVisibility(View.VISIBLE);
                mDateET.setEnabled(false);
                Log.e("TAG", "onCheckedChanged: " + checkedId);
                //Calender View
                mDateET.setOnClickListener(v -> {
                    mDateET.setEnabled(false);
                    //DateRange Picker
                    materialDatePicker.show(getChildFragmentManager(), "Range Picker");

                });

                //DateRange Picker
                materialDatePicker.show(getChildFragmentManager(), "Range Picker");

            } else {
                mStartDate = "all";
                mEndDate = "all";

                mNoHistoryImage.setVisibility(View.GONE);
                mTVEmpty.setVisibility(View.GONE);

                mDateET.setText("");
                mDateET.setVisibility(View.GONE);
                Log.e("TAG", "onCheckedChanged: " + checkedId);
                mDateET.setVisibility(View.GONE);
                if (histOrderList.isEmpty() || histOrderList.size() == 0) {
                    mNoRecordFound.setVisibility(View.VISIBLE);
                    mNoHistoryImage.setVisibility(View.VISIBLE);
                }
                mNoRecordFound.setVisibility(View.GONE);
                mNoHistoryImage.setVisibility(View.GONE);
                getFullHistoryOrders();
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Log.e(TAG, "onStart: " + materialDatePicker.getHeaderText());
            datee = materialDatePicker.getHeaderText();
            mDateET.setText(datee);

            mDateET.setEnabled(true);

            //Split
            String[] range = datee.split(" – ");

            mStartDate = range[0];
            mEndDate = range[1];

            ////Spliting again for start month and day
            String[] sDate = mStartDate.split(" ");
            mStartMonth = sDate[0];
            mStartDay = sDate[1];

            ////Spliting again for end month and day
            String[] eDate = mEndDate.split(" ");
            mEndMonth = eDate[0];
            mEndDay = eDate[1];

            convertIntoNumberStartMonth(mStartMonth);
            convertIntoNumberEndMonth(mEndMonth);

            Calendar cal = Calendar.getInstance();
            mStartDate = cal.get(Calendar.YEAR) + "-" + mStartMonth + "-" + mStartDay;
            mEndDate = cal.get(Calendar.YEAR) + "-" + mEndMonth + "-" + mEndDay;
            getHistoryOrders(mStartDate, mEndDate);

            Log.e(TAG, "onStart: Start Date " + mStartDate);
            Log.e(TAG, "onStart: End Date " + mEndDate);


//                        adapter.getFilter().filter(datee);
        });

    }

    private void convertIntoNumberStartMonth(String startMonth) {
        switch (startMonth) {
            case "Jan": {
                mStartMonth = "01";
                break;
            }
            case "Feb": {
                mStartMonth = "02";
                break;
            }
            case "Mar": {
                mStartMonth = "03";
                break;
            }
            case "Apr": {
                mStartMonth = "04";
                break;
            }
            case "May": {
                mStartMonth = "05";
                break;
            }
            case "Jun": {
                mStartMonth = "06";
                break;
            }
            case "Jul": {
                mStartMonth = "07";
                break;
            }
            case "Aug": {
                mStartMonth = "08";
                break;
            }
            case "Sep": {
                mStartMonth = "09";
                break;
            }
            case "Oct": {
                mStartMonth = "10";
                break;
            }
            case "Nov": {
                mStartMonth = "11";
                break;
            }
            case "Dec": {
                mStartMonth = "12";
                break;
            }
            default: {
                mStartMonth = "09";
                break;
            }
        }
    }

    private void convertIntoNumberEndMonth(String endMonth) {
        switch (endMonth) {
            case "Jan": {
                mEndMonth = "01";
                break;
            }
            case "Feb": {
                mEndMonth = "02";
                break;
            }
            case "Mar": {
                mEndMonth = "03";
                break;
            }
            case "Apr": {
                mEndMonth = "04";
                break;
            }
            case "May": {
                mEndMonth = "05";
                break;
            }
            case "Jun": {
                mEndMonth = "06";
                break;
            }
            case "Jul": {
                mEndMonth = "07";
                break;
            }
            case "Aug": {
                mEndMonth = "08";
                break;
            }
            case "Sep": {
                mEndMonth = "09";
                break;
            }
            case "Oct": {
                mEndMonth = "10";
                break;
            }
            case "Nov": {
                mEndMonth = "11";
                break;
            }
            case "Dec": {
                mEndMonth = "12";
                break;
            }
            default: {
                mEndMonth = "10";
                break;
            }
        }
    }

    ///Updating user Interface
    //Setting Adapter
    private void adapter() {
        try {
            mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext()));
            adapter = new AdapterHistoryFragment(histOrderList, requireContext());
            mHistoryRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(position -> {
                Intent intent = new Intent(requireContext(), HistoryDetails.class);
                pm.saveMyData("orderIdHis", histOrderList.get(position).getmOrderNumber());
                pm.saveMyData("orderHisPos", String.valueOf(position));
                startActivity(intent);
            });

        } catch (Exception ignored) {
        }

    }//UI

    //Getting History from Server (API)
    private void getHistoryOrders(String sDate, String eDate) {
        histOrderList.clear();
        //Getting token
        String token = AppManager.getBusinessDetails().getData().getToken();
        Call<NewHistoryWithTotals> call = RetrofitNetMan.getRestApiService().getHistory(token, sDate, eDate);

        call.enqueue(new Callback<NewHistoryWithTotals>() {
            @Override
            public void onResponse(Call<NewHistoryWithTotals> call, Response<NewHistoryWithTotals> response) {

                if (response.isSuccessful() && response.body() != null) {

                    DecimalFormat format = new DecimalFormat("0.00");

                    //collect data and update UI
                    if (!response.body().getMessage().equals("No records found")) {

                        for (int i = 0; i < response.body().getData().getResults().size(); i++) {

                            //CALCULATING TOTAL
                            for (int j = 0; j < response.body().getData().getResults().get(i).getOrderTotal().size(); j++) {
                                mTotalValue = mTotalValue + Double.parseDouble(response.body().getData().getResults().get(i).getOrderTotal().get(j).getValue());
                            }

                            Log.e(TAG, "onResponse: " + mTotalValue);

                            histOrderList.add(new NewHistory(
                                    String.valueOf(response.body().getData().getResults().get(i).getId()),
                                    response.body().getData().getResults().get(i).getFirstName() + " " + response.body().getData().getResults().get(i).getLastName(),
                                    String.valueOf(response.body().getData().getResults().get(i).getItemCount()),
                                    String.valueOf(format.format(mTotalValue)),
                                    response.body().getData().getResults().get(i).getPickuptime()
                            ));
                            mTotalValue = 0.00;

                            Log.e(TAG, "onResponse: Cleared" + mTotalValue);
                        }


                        mNoHistoryImage.setVisibility(View.GONE);
                        mTVEmpty.setText("No Orders in this period");
                        mTVEmpty.setVisibility(View.GONE);
                    } else {
                        mNoHistoryImage.setVisibility(View.VISIBLE);
                        mTVEmpty.setText("No Orders in this period");
                        mTVEmpty.setVisibility(View.VISIBLE);
                    }
                    mNSVHist.setVisibility(View.VISIBLE);
                    adapter();
                }
                else {
                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
                mProgressBarHistFull.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewHistoryWithTotals> call, Throwable t) {
                AppManager.toast("No Or Poor Internet Connection");
            }
        });
    }

    //Getting History from Server (API)
    private void getFullHistoryOrders() {
        histOrderList.clear();
        //Getting token
        String token = AppManager.getBusinessDetails().getData().getToken();
        Call<NewHistoryWithTotals> call = RetrofitNetMan.getRestApiService().getFullHistory(token);

        call.enqueue(new Callback<NewHistoryWithTotals>() {
            @Override
            public void onResponse(Call<NewHistoryWithTotals> call, Response<NewHistoryWithTotals> response) {

                if (response.isSuccessful() && response.body() != null) {

                    //collect data and update UI
                    if (!response.body().getMessage().equals("No records found")) {

                        try {
                            DecimalFormat format = new DecimalFormat("0.00");

                            //collect data and update UI
                            Log.e(TAG, "onResponse: " + response.body().getData().getResults().size());
                            for (int i = 0; i < response.body().getData().getResults().size(); i++) {

                                //CALCULATING TOTAL
                                for (int j = 0; j < response.body().getData().getResults().get(i).getOrderTotal().size(); j++) {
                                    mTotalValue = mTotalValue + Double.parseDouble(response.body().getData().getResults().get(i).getOrderTotal().get(j).getValue());
                                }
                                Log.e(TAG, "onResponse: " + mTotalValue);

                                histOrderList.add(new NewHistory(
                                        String.valueOf(response.body().getData().getResults().get(i).getId()),
                                        response.body().getData().getResults().get(i).getFirstName() + " " +
                                                response.body().getData().getResults().get(i).getLastName(),
                                        String.valueOf(response.body().getData().getResults().get(i).getItemCount()),
                                        String.valueOf(format.format(mTotalValue)),
                                        response.body().getData().getResults().get(i).getPickuptime()
                                ));
                                mTotalValue = 0.00;
                                Log.e(TAG, "onResponse: Cleared" + mTotalValue);
                            }
                            mNSVHist.setVisibility(View.VISIBLE);
                            adapter();
                        } catch (Exception exception) {
                            Log.e(TAG, "onResponse: " + exception.getMessage());
                        }
                    }
                    else {
                        mNoHistoryImage.setVisibility(View.VISIBLE);
                        mTVEmpty.setText("No records found");
                        mTVEmpty.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
                mProgressBarHistFull.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<NewHistoryWithTotals> call, @NotNull Throwable t) {
                AppManager.toast("No Or Poor Internet Connection");
            }
        });
    }


}