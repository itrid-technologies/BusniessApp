package com.itridtechnologies.resturantapp.UiViews.Frags;

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


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.itridtechnologies.resturantapp.Adapters.MainAdapter;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.MainActivity;
import com.itridtechnologies.resturantapp.UiViews.Activities.Menu;
import com.itridtechnologies.resturantapp.UiViews.Activities.NewOrder;
import com.itridtechnologies.resturantapp.UiViews.Activities.Settings;
import com.itridtechnologies.resturantapp.UiViews.Activities.help;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.Pagination.PaginationResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Internet;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProcessing extends Fragment {

    private static final String TAG = "FragmentProcessing";

    private RecyclerView mRvOrders;
    private ProgressBar mProgressBar;
    private ProgressBar mPageProgressBar;
    private ConstraintLayout mRootContainer;
    private LinearLayout mErrorContainer;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    //..
    private int pageNo = 1;
    private boolean shouldPaginate = true;
    private List<OrdersItem> mOrdersItemList;

    //context
    private Context mContext;
    //layout manager
    private LinearLayoutManager layoutManager;

    //Adapter
    private MainAdapter adapter;

    //Preference Manager
    private PreferencesManager pm;


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        //getting context
        mContext = requireContext();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_processing, container, false);
        Toolbar mToolbar = root.findViewById(R.id.nav_bar_PO);
        pm = new PreferencesManager(mContext);

        ///Header Name
        mToolbar.setTitle("Preparing");
        //Setting Toolbar Navigation Bar
        mToolbar.setOnMenuItemClickListener(item -> {
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
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //find views
        mRvOrders = view.findViewById(R.id.rv_orders_in_progress);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mPageProgressBar = view.findViewById(R.id.pg_progress_bar);
        mRootContainer = view.findViewById(R.id.content_container);
        mErrorContainer = view.findViewById(R.id.error_container);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        nestedScrollView = view.findViewById(R.id.nested_scroll_processing);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (Internet.isAvailable(mContext)) {
                refresh();
            } else {
                //hide main content
                //show error container
                mErrorContainer.setVisibility(View.VISIBLE);
                mRootContainer.setVisibility(View.GONE);
            }
        });

        // adding on scroll change listener method for our nested scroll view.
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                if (shouldPaginate) {
                    mPageProgressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    //paginate after 1 sec
                    handler.postDelayed(this::loadMoreItems, 1000);
                }
            }
        });

    }//onViewCreated

    @Override
    public void onStart() {
        super.onStart();

        if (Internet.isAvailable(mContext)) {
            refresh();
        } else {
            //hide main content
            //show error container
            mErrorContainer.setVisibility(View.VISIBLE);
            mRootContainer.setVisibility(View.GONE);
        }

    }//onStart

    private void getOrders(int pageNo) {
        //hit api with page no 1 and default params
        Call<PaginationResponse> call = RetrofitNetMan.getRestApiService().getPaginationReadyOrders
                (
                        AppManager.getBusinessDetails().getData().getToken(),
                        pageNo
                );
        call.enqueue(new Callback<PaginationResponse>() {
            @Override
            public void onResponse(@NonNull Call<PaginationResponse> call, @NonNull Response<PaginationResponse> response) {
                mProgressBar.setVisibility(View.GONE);
                mErrorContainer.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
                mRootContainer.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() == null) {//TODO: ye list ka check ha
                        if (mOrdersItemList.isEmpty()) {
                            mErrorContainer.setVisibility(View.VISIBLE);
                        } else {
                            mErrorContainer.setVisibility(View.GONE);
                        }
                    } else {
                        //orders found
                        mOrdersItemList = new ArrayList<>();
                        mOrdersItemList.addAll(response.body().getData().getOrders());
                        setupRecyclerView();
                    }
                }//success
                else if (response.code() == 401) {
                    logout();
                }

            }//onResponse

            @Override
            public void onFailure(@NonNull Call<PaginationResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                mProgressBar.setVisibility(View.GONE);
                mErrorContainer.setVisibility(View.VISIBLE);
                mRootContainer.setVisibility(View.GONE);
            }
        });
    }//getOrders

    private void loadMoreItems() {

        pageNo++;

        Call<PaginationResponse> call = RetrofitNetMan.getRestApiService().getPaginationReadyOrders
                (
                        AppManager.getBusinessDetails().getData().getToken(),
                        pageNo
                );
        call.enqueue(new Callback<PaginationResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<PaginationResponse> call, @NonNull Response<PaginationResponse> response) {
                mPageProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() == null) {//TODO: ye list ka chk ha
                    } else {
                        //orders found
                        if (!response.body().getMessage().equals("No records found")) {
                            mOrdersItemList.addAll(response.body().getData().getOrders());
                            adapter.notifyDataSetChanged();
                        } else {
                            shouldPaginate = false;
                        }
                    }
                }//success
                else if (response.code() == 401) {
                    logout();
                }

            }//onResponse

            @Override
            public void onFailure(@NonNull Call<PaginationResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                mPageProgressBar.setVisibility(View.GONE);
            }
        });

    }//loadMoreItems

    private void refresh() {
        pageNo = 1;
        mProgressBar.setVisibility(View.VISIBLE);
        mRootContainer.setVisibility(View.VISIBLE);
        mErrorContainer.setVisibility(View.GONE);
        mPageProgressBar.setVisibility(View.GONE);
        getOrders(pageNo);
    }//refresh

    private void setupRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        adapter = new MainAdapter(mOrdersItemList, mContext);
        mRvOrders.setLayoutManager(layoutManager);
        mRvOrders.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(mContext, NewOrder.class);
            Log.e(TAG, "setUpRecView: " + mOrdersItemList.get(position).getId());
            intent.putExtra("orderId", String.valueOf(mOrdersItemList.get(position).getId()));
            intent.putExtra("detailType", "processing");
            startActivity(intent);
        });

    }//setupRecyclerView

    @Override
    public void onPause() {
        super.onPause();
        pageNo = 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pageNo = 1;
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNo = 1;
    }
}//end class
