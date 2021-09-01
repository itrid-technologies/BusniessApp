package com.itridtechnologies.resturantapp.UiViews.Frags;

import static android.content.ContentValues.TAG;

import static com.itridtechnologies.resturantapp.utils.AppManager.logout;

import android.annotation.SuppressLint;
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
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.itridtechnologies.resturantapp.models.historyNew.NewHistoryWithTotals;
import com.itridtechnologies.resturantapp.models.historyNew.ResultsItem;
import com.itridtechnologies.resturantapp.models.orderHistory.ItemsItem;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Constants;
import com.itridtechnologies.resturantapp.utils.Internet;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import org.jetbrains.annotations.NotNull;

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
    private List<ResultsItem> histOrderList = new ArrayList<>();
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
        pm = new PreferencesManager(mContext);
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

            if (Internet.isAvailable(mContext)) {
                getFullHistoryOrders();
            } else {
                //hide main content
                //show error container
                mNoHistoryImage.setVisibility(View.VISIBLE);
                mTVEmpty.setVisibility(View.VISIBLE);
            }
        });


        //Pagination on scrool view
        mNSVHist.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {


            mProgressBarHistPagination.setVisibility(View.VISIBLE);
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                Handler handler = new Handler();
                //paginate after 1 sec
                handler.postDelayed(this::LoadMoreItems, 1000);
            }
        });


    }//onViewCreated


    @Override
    public void onStart() {
        super.onStart();

        if (Internet.isAvailable(mContext)) {
            getFullHistoryOrders();
        } else {//hide main content
            //show error container
            mNoHistoryImage.setVisibility(View.VISIBLE);
            mTVEmpty.setVisibility(View.VISIBLE);
        }

        Calendar c = Calendar.getInstance();

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
                                page_no = 1;
                                getHistoryOrders(mStartDate, mEndDate);

                                Log.e(TAG, "onStart: Start Date " + mStartDate);
                                Log.e(TAG, "onStart: End Date " + mEndDate);

                            });

                    dateRangePickerFragment.setThemeDark(Constants.IS_DARK_MODE);

                    dateRangePickerFragment.setMaxDate(c);
                    dateRangePickerFragment.setOnCancelListener(dialog -> mDateET.setEnabled(true));
                    dateRangePickerFragment.show(getChildFragmentManager(), "dateRangePicker");

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

            }
            else {
                mStartDate = "";
                mEndDate = "";
//
                mNoHistoryImage.setVisibility(View.GONE);
                mTVEmpty.setVisibility(View.GONE);

                mDateET.setText("");
                mDateET.setVisibility(View.GONE);
                Log.e("TAG", "onCheckedChanged: " + checkedId);
                mDateET.setVisibility(View.GONE);
                if (histOrderList.isEmpty()) {
                    mNoRecordFound.setVisibility(View.VISIBLE);
                    mNoHistoryImage.setVisibility(View.VISIBLE);
                } else {
                    histOrderList.size();
                }
                mNoRecordFound.setVisibility(View.GONE);
                mNoHistoryImage.setVisibility(View.GONE);
                getFullHistoryOrders();
            }
        });

    }

    ///Updating user Interface
    //Setting Adapter
    private void adapter(List<ResultsItem> historyList) {

        Log.e(TAG, "adapter: inside adapter");
        mHistoryRecyclerView.setVisibility(View.VISIBLE);

        manager = new LinearLayoutManager(mContext);
        Log.e(TAG, "adapter: inside adapter1");
        adapter = new AdapterHistoryFragment(historyList, mContext);
        Log.e(TAG, "adapter: inside adapter2");
        mHistoryRecyclerView.setAdapter(adapter);
        Log.e(TAG, "adapter: inside adapter3");
        mHistoryRecyclerView.setLayoutManager(manager);
        Log.e(TAG, "adapter: inside adapter4");

        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(mContext, HistoryDetails.class);
            Log.e(TAG, "adapter: position" + position);
            intent.putExtra("orderIdHis", String.valueOf(historyList.get(position).getId()));
            intent.putExtra("pageNo", String.valueOf(page_no));
            startActivity(intent);
        });

        //checking for last item LastItem
//        LastItem();

    }//UI

    private void LoadMoreItems() {

        isLoading = true;

        Log.e(TAG, "LoadMoreItems: pdate" + mStartDate + mEndDate + page_no);

        if (isDated) {
            page_no++;

            Log.e(TAG, "LoadMoreItems: p undated" + mStartDate + mEndDate + page_no);

            //Getting token
            String token = AppManager.getBusinessDetails().getData().getToken();
            Call<NewHistoryWithTotals> call = RetrofitNetMan.getRestApiService().getHistory(token, mStartDate, mEndDate, page_no);

            call.enqueue(new Callback<NewHistoryWithTotals>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<NewHistoryWithTotals> call, @NonNull Response<NewHistoryWithTotals> response) {

                    isLoading = false;

                    if (response.isSuccessful() && response.body() != null) {

                        //collect data and update UI
                        if (!response.body().getMessage().equals("No records found")) {

                            mHistoryRecyclerView.setVisibility(View.VISIBLE);

                            histOrderList.addAll(response.body().getData().getResults());
                            adapter.notifyDataSetChanged();

                            mNoHistoryImage.setVisibility(View.GONE);
                            mTVEmpty.setVisibility(View.GONE);

                        } else {
                            mHistoryRecyclerView.setVisibility(View.GONE);
                        }

                    } else if (response.code() == 401) {
                        logout();
                    } else {
                        Log.e(TAG, "onResponse: Some went Wrong ");
                    }
                    mProgressBarHistFull.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<NewHistoryWithTotals> call, @NonNull Throwable t) {
                    isLoading = false;
                    AppManager.toast("No Or Poor Internet Connection");
                }
            });
        } else {
            page_no++;

            Log.e(TAG, "LoadMoreItems: p" + mStartDate + mEndDate + page_no);

            Call<NewHistoryWithTotals> call = RetrofitNetMan.getRestApiService().getFullHistory(AppManager.getBusinessDetails().getData().getToken(), page_no);
            call.enqueue(new Callback<NewHistoryWithTotals>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<NewHistoryWithTotals> call, @NonNull Response<NewHistoryWithTotals> response) {

                    isLoading = false;

                    if (response.isSuccessful()) {

                        if (response.body() != null) {

                            if (response.body().isSuccess()) {

                                //collect data and update UI
                                if (!response.body().getMessage().equals("No records found")) {
                                    mNoHistoryImage.setVisibility(View.GONE);
                                    mTVEmpty.setVisibility(View.GONE);
                                    isDated = false;
                                    histOrderList.addAll(response.body().getData().getResults());
                                    adapter.notifyDataSetChanged();

                                }
                            }
                        }
                    } else if (response.code() == 401) {
                        logout();
                    } else {

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

    }//LoadMoreItems

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

                        mHistoryRecyclerView.setVisibility(View.VISIBLE);
                        mNoHistoryImage.setVisibility(View.GONE);
                        mTVEmpty.setVisibility(View.GONE);
                        histOrderList = response.body().getData().getResults();
                        adapter(histOrderList);

                    } else {
                        mHistoryRecyclerView.setVisibility(View.GONE);
                        mNoHistoryImage.setVisibility(View.VISIBLE);
                        mProgressBarHistPagination.setVisibility(View.GONE);
                        mTVEmpty.setText("No orders in this period");
                        mTVEmpty.setVisibility(View.VISIBLE);
                    }
                    mNSVHist.setVisibility(View.VISIBLE);
                    isDated = true;
                } else if (response.code() == 401) {
                    logout();
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
        mSwipeHistory.setRefreshing(false);
        histOrderList.clear();
        mProgressBarHistPagination.setVisibility(View.GONE);
        Log.e(TAG, "getFullHistoryOrders: asgfdsgdf");
        //Getting token
        String token = AppManager.getBusinessDetails().getData().getToken();
        Call<NewHistoryWithTotals> call = RetrofitNetMan.getRestApiService().getFullHistory(token, page_no);

        call.enqueue(new Callback<NewHistoryWithTotals>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<NewHistoryWithTotals> call, @NonNull Response<NewHistoryWithTotals> response) {

                if (response.isSuccessful() && response.body() != null) {

                    //collect data and update UI
                    if (!response.body().getMessage().equals("No records found")) {


                        histOrderList = response.body().getData().getResults();
                        mNoHistoryImage.setVisibility(View.GONE);
                        mTVEmpty.setVisibility(View.GONE);

                        isDated = false;
                        adapter(histOrderList);

                    } else {

                        mNoHistoryImage.setVisibility(View.VISIBLE);
                        mTVEmpty.setText("No records found");
                        mTVEmpty.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 401) {
                    logout();
                }
                mProgressBarHistFull.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<NewHistoryWithTotals> call, @NotNull Throwable t) {
                mSwipeHistory.setRefreshing(false);
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