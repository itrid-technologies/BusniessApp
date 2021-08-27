package com.itridtechnologies.resturantapp.UiViews.Activities.Frags;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.beeline09.daterangepicker.date.DateRangePickerFragment;
import com.itridtechnologies.resturantapp.Adapters.AdapterHistoryFragment;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.HistoryDetails;
import com.itridtechnologies.resturantapp.UiViews.Activities.MainActivity;
import com.itridtechnologies.resturantapp.UiViews.Activities.Menu;
import com.itridtechnologies.resturantapp.UiViews.Activities.Settings;
import com.itridtechnologies.resturantapp.UiViews.Activities.help;
import com.itridtechnologies.resturantapp.model.NewHistory;
import com.itridtechnologies.resturantapp.models.historyNew.NewHistoryWithTotals;
import com.itridtechnologies.resturantapp.models.historyNew.ResultsItem;
import com.itridtechnologies.resturantapp.models.orderHistory.ItemsItem;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Constants;
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

public class FragmentHistory extends Fragment {

    private RecyclerView mHistoryRecyclerView; //Main RecyclerView
    private List<ItemsItem> mHistoryList = new ArrayList<>();
    private final ArrayList<NewHistory> histOrderList = new ArrayList<>();
    private ProgressBar mProgressBarHistFull;
    private ProgressBar mProgressBarHistPagination;
    private NestedScrollView mNSVHist;
    private PreferencesManager pm;
    private TextView mNoRecordFound;
    private LinearLayout mLayout;

    private boolean isDated;


    //page size
    private final int pageSize = 10;
    //pagination var
    private int page_no = 1;
    //flags
    private boolean isLastPage;
    private boolean isLoading;
    //checking last pos
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    //layout manager
    private LinearLayoutManager manager;

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
    //context
    private Context mContext;

    //Swipe Refresh Layout
    private SwipeRefreshLayout mSwipeHistory;

    //Adapter
    private AdapterHistoryFragment adapter;
    private boolean isScrolling = false;

    //Starting and End Date For Filtering
    private String mStartDate = "";
    private String mEndDate = "";

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        //getting context
        mContext = requireContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        mHistoryRecyclerView = root.findViewById(R.id.recycler_view_history);
        mProgressBarHistFull = root.findViewById(R.id.pb_hist_center);
        mProgressBarHistPagination = root.findViewById(R.id.PBHistoryPagination);
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
                    Intent intent = new Intent(mContext, Menu.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.settings: {
                    Intent intent = new Intent(mContext, Settings.class);
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
                case R.id.over_flow_log_out: {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    pm.clearSharedPref();
                    pm.saveMyDataBool("login", false);
                    startActivity(intent);
                    return false;
                }
                default: {
                    Intent intent = new Intent(mContext, help.class);
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
//
//        //date Range Picker
//        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
//
//        builder.setTitleText("Select a Range to get Orders");
//        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);

        Calendar c = Calendar.getInstance();
//
////        DatePickerDialog.
//        final MaterialDatePicker materialDatePicker = builder.build();

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
                    DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.Companion.newInstance(
                            (view, yearStart, monthStart, dayStart, yearEnd, monthEnd, dayEnd) -> {
                                mDateET.setEnabled(true);

                                int startMonth = monthStart + 1;
                                int endMonth = monthEnd + 1;
                                Log.e(TAG, "onStart: month" + startMonth + endMonth);

                                datee = "From: " + dayStart + "-" + startMonth + "-" + yearStart + " To : " + dayEnd + "-" + endMonth + "-" + yearEnd;
                                mDateET.setText(datee);


                                mDateET.setEnabled(true);

                                mStartDate = yearStart + "-" + startMonth + "-" + dayStart;
                                mEndDate = yearEnd + "-" + endMonth + "-" + dayEnd;

                                getHistoryOrders(mStartDate, mEndDate);

                                Log.e(TAG, "onStart: Start Date " + mStartDate);
                                Log.e(TAG, "onStart: End Date " + mEndDate);

                            });

                    dateRangePickerFragment.setThemeDark(Constants.IS_DARK_MODE);

                    dateRangePickerFragment.setMaxDate(c);
                    dateRangePickerFragment.setOnCancelListener(dialog -> mDateET.setEnabled(true));
                    dateRangePickerFragment.show(getChildFragmentManager(), "dateRangePicker");

                    //DateRange Picker
//                    materialDatePicker.show(getChildFragmentManager(), "Range Picker");

                });


                DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.Companion.newInstance(
                        (view, yearStart, monthStart, dayStart, yearEnd, monthEnd, dayEnd) -> {

                            mDateET.setEnabled(true);


                            int startMonth = monthStart + 1;
                            int endMonth = monthEnd + 1;
                            Log.e(TAG, "onStart: month" + startMonth + endMonth);

//                            Log.e(TAG, "onStart: " + materialDatePicker.getHeaderText());
                            datee = "From: " + dayStart + "-" + startMonth + "-" + yearStart + " To : " + dayEnd + "-" + endMonth + "-" + yearEnd;
                            mDateET.setText(datee);

                            mDateET.setEnabled(true);

                            mStartDate = yearStart + "-" + startMonth + "-" + dayStart;
                            mEndDate = yearEnd + "-" + endMonth + "-" + dayEnd;

                            getHistoryOrders(mStartDate, mEndDate);

                            Log.e(TAG, "onStart: Start Date " + mStartDate);
                            Log.e(TAG, "onStart: End Date " + mEndDate);

                        });


                dateRangePickerFragment.setThemeDark(Constants.IS_DARK_MODE);
                dateRangePickerFragment.setMaxDate(c);
                dateRangePickerFragment.setOnCancelListener(dialog -> mDateET.setEnabled(true));
                dateRangePickerFragment.show(getChildFragmentManager(), "dateRangePicker");


                //DateRange Picker
//                materialDatePicker.show(getChildFragmentManager(), "Range Picker");

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

//        materialDatePicker.addOnNegativeButtonClickListener(v -> mDateET.setEnabled(true));
//
//        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
//            Log.e(TAG, "onStart: " + materialDatePicker.getHeaderText());
//            datee = materialDatePicker.getHeaderText();
//            mDateET.setText(datee);
//
//            mDateET.setEnabled(true);
//
//            //Split
//            String[] range = datee.split(" – ");
//
//            mStartDate = range[0];
//            mEndDate = range[1];
//
//            ////Spliting again for start month and day
//            String[] sDate = mStartDate.split(" ");
//            mStartMonth = sDate[0];
//            mStartDay = sDate[1];
//
//            ////Spliting again for end month and day
//            String[] eDate = mEndDate.split(" ");
//            mEndMonth = eDate[0];
//            mEndDay = eDate[1];
//
//            convertIntoNumberStartMonth(mStartMonth);
//            convertIntoNumberEndMonth(mEndMonth);
//
//            Calendar cal = Calendar.getInstance();
//            mStartDate = cal.get(Calendar.YEAR) + "-" + mStartMonth + "-" + mStartDay;
//            mEndDate = cal.get(Calendar.YEAR) + "-" + mEndMonth + "-" + mEndDay;
//            getHistoryOrders(mStartDate, mEndDate);
//
//            Log.e(TAG, "onStart: Start Date " + mStartDate);
//            Log.e(TAG, "onStart: End Date " + mEndDate);
//
//        });

    }

    ///Updating user Interface
    //Setting Adapter
    private void adapter(List<ResultsItem> historyList) {

        Log.e(TAG, "adapter: inside adapter" );

        try {
            manager = new LinearLayoutManager(mContext);
            adapter = new AdapterHistoryFragment( historyList, mContext);
            mHistoryRecyclerView.setAdapter(adapter);
            mHistoryRecyclerView.setLayoutManager(manager);

            adapter.setOnItemClickListener(position -> {
                Intent intent = new Intent(mContext, HistoryDetails.class);
                pm.saveMyData("orderIdHis", histOrderList.get(position).getmOrderNumber());
                pm.saveMyData("pageNo", String.valueOf(page_no));
                pm.saveMyData("orderHisPos", String.valueOf(position));
                startActivity(intent);
            });

            //checking for last item LastItem
            LastItem();

        } catch (Exception ignored) {
        }

    }//UI


    private void LastItem() {

        mHistoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.e(TAG, "onScrollStateChanged: " + "");

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }


                visibleItemCount = manager.getChildCount();
                totalItemCount = manager.getItemCount();
                firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {

                    if (isScrolling && (visibleItemCount + firstVisibleItem) == totalItemCount)
                    {
                        page_no ++;
                        LoadMoreItems();
                    }

                    if ((visibleItemCount + firstVisibleItem) == totalItemCount &&
                            firstVisibleItem >= 0 && totalItemCount >= pageSize) {

                        mProgressBarHistPagination.setVisibility(View.VISIBLE);

                        page_no++;

                        Log.e(TAG, "onScrolled: " + "last item" + page_no);

                        LoadMoreItems();

                    }
                } // end if loading and islastpage



            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.e(TAG, "onScrolled: " + "");

            }
        });

    }


    private void LoadMoreItems() {

        isLoading = true;

        Log.e(TAG, "LoadMoreItems: pdate" + mStartDate + mEndDate + page_no);

        if (isDated) {

            Log.e(TAG, "LoadMoreItems: p undated" + mStartDate + mEndDate + page_no);

            //Getting token
            String token = AppManager.getBusinessDetails().getData().getToken();
            Call<NewHistoryWithTotals> call = RetrofitNetMan.getRestApiService().getHistory(token, mStartDate, mEndDate, page_no);

            call.enqueue(new Callback<NewHistoryWithTotals>() {
                @Override
                public void onResponse(@NonNull Call<NewHistoryWithTotals> call, @NonNull Response<NewHistoryWithTotals> response) {

                    isLoading = false;

                    if (response.isSuccessful() && response.body() != null) {

                        DecimalFormat format = new DecimalFormat("0.00");

                        //collect data and update UI
                        if (!response.body().getMessage().equals("No records found")) {

                                adapter.AddData(response.body().getData().getResults());
                                isLastPage = response.body().getData().getResults().size() < pageSize;

                                mNoHistoryImage.setVisibility(View.GONE);
                                mTVEmpty.setVisibility(View.GONE);

                            }

                    }

                    else {
                        Log.e(TAG, "onResponse: Some went Wrong " );
                    }
                    mProgressBarHistFull.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<NewHistoryWithTotals> call, @NonNull Throwable t) {
                    isLoading = false;
                    AppManager.toast("No Or Poor Internet Connection");
                }
            });
        }
        else {

            Log.e(TAG, "LoadMoreItems: p" + mStartDate + mEndDate + page_no);

            Call<NewHistoryWithTotals> call = RetrofitNetMan.getRestApiService().getFullHistory(AppManager.getBusinessDetails().getData().getToken(), page_no);
            call.enqueue(new Callback<NewHistoryWithTotals>() {
                @Override
                public void onResponse(@NonNull Call<NewHistoryWithTotals> call, @NonNull Response<NewHistoryWithTotals> response) {

                    isLoading = false;

                    if (response.isSuccessful()) {

                        if (response.body() != null) {

                            if (response.body().isSuccess()) {

                                if (response.body().getData().getResults().size() > 0) {


//                                    adapter.AddData(histOrderList);.adapter();
                                    adapter(response.body().getData().getResults());
                                    isLastPage = response.body().getData().getResults().size() < pageSize;

                                } else {
                                    isLastPage = true;
                                }

                            }

                        }

                    }


                    else {

                        Log.e(TAG, "onResponse:load more " + "something is wrong");

                    }

                }

                @Override
                public void onFailure(@NonNull Call<NewHistoryWithTotals> call, @NonNull Throwable t) {

                    Log.e(TAG, "onFailure: " + t.getMessage());
                    isLoading = false;

                }
            });
        }

        mProgressBarHistPagination.setVisibility(View.GONE);

    }

    //Getting History from Server (API)
    private void getHistoryOrders(String sDate, String eDate) {
        histOrderList.clear();
        //Getting token
        String token = AppManager.getBusinessDetails().getData().getToken();
        Call<NewHistoryWithTotals> call = RetrofitNetMan.getRestApiService().getHistory(token, sDate, eDate, page_no);

        call.enqueue(new Callback<NewHistoryWithTotals>() {
            @Override
            public void onResponse(@NonNull Call<NewHistoryWithTotals> call, @NonNull Response<NewHistoryWithTotals> response) {

                if (response.isSuccessful() && response.body() != null) {

                    //collect data and update UI
                    if (!response.body().getMessage().equals("No records found")) {

                        mNoHistoryImage.setVisibility(View.GONE);
                        mTVEmpty.setVisibility(View.GONE);
                    } else {
                        mNoHistoryImage.setVisibility(View.VISIBLE);
                        mTVEmpty.setText("No Orders in this period");
                        mTVEmpty.setVisibility(View.VISIBLE);
                    }
                    mNSVHist.setVisibility(View.VISIBLE);
                    isDated = true;
                    adapter.AddData(response.body().getData().getResults());
                }
                mProgressBarHistFull.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<NewHistoryWithTotals> call, @NonNull Throwable t) {
                AppManager.toast("No Or Poor Internet Connection");
            }
        });
    }

    //Getting History from Server (API)
    private void getFullHistoryOrders() {
        histOrderList.clear();
        //Getting token
        String token = AppManager.getBusinessDetails().getData().getToken();
        Call<NewHistoryWithTotals> call = RetrofitNetMan.getRestApiService().getFullHistory(token, page_no);

        call.enqueue(new Callback<NewHistoryWithTotals>() {
            @Override
            public void onResponse(@NonNull Call<NewHistoryWithTotals> call, @NonNull Response<NewHistoryWithTotals> response) {

                if (response.isSuccessful() && response.body() != null) {

                    //collect data and update UI
                    if (!response.body().getMessage().equals("No records found")) {

                        try {
                            DecimalFormat format = new DecimalFormat("0.00");

                            //collect data and update UI
                            Log.e(TAG, "onResponse: " + response.body().getData().getResults().size());
                            mNSVHist.setVisibility(View.VISIBLE);
                            isDated = false;

                            adapter(response.body().getData().getResults());
                        } catch (Exception exception) {
                            Log.e(TAG, "onResponse: " + exception.getMessage());
                        }
                    } else {
                        mNoHistoryImage.setVisibility(View.VISIBLE);
                        mTVEmpty.setText("No records found");
                        mTVEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
                mProgressBarHistFull.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<NewHistoryWithTotals> call, @NotNull Throwable t) {
                AppManager.toast("No Or Poor Internet Connection");
            }
        });
    }//getFullHistoryOrders


    @Override
    public void onPause() {
        super.onPause();
        page_no = 1;

        Log.e(TAG, "onPause: ");

    }

    @Override
    public void onResume() {
        super.onResume();
        page_no = 1;

        Log.e(TAG, "onResume: ");
        Log.e(TAG, "onResume: i m resumed");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        page_no = 1;

        Log.e(TAG, "onDestroy: ");
    }

}