package com.itridtechnologies.resturantapp.UiViews.Activities.Frags;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.itridtechnologies.resturantapp.Adapters.AdapterFirstTime;
import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.MainActivity;
import com.itridtechnologies.resturantapp.UiViews.Activities.Menu;
import com.itridtechnologies.resturantapp.UiViews.Activities.NewOrder;
import com.itridtechnologies.resturantapp.UiViews.Activities.Settings;
import com.itridtechnologies.resturantapp.UiViews.Activities.help;
import com.itridtechnologies.resturantapp.Work.AllOrderWorker;
import com.itridtechnologies.resturantapp.models.Pagination.Data;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.Pagination.PaginationResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Constants;
import com.itridtechnologies.resturantapp.utils.Internet;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProcessing extends Fragment {
    //Variables here
    private RecyclerView mRecyclerView;
    private ProgressBar mPBFull;
    private NestedScrollView mNSVProgress;
    ///Room database
    RoomDB databaseRoom;

    //WorkManager
    OneTimeWorkRequest bgWork;
    ////For Order ID
    private List<OrdersItem> mNewPageOrderItemList = new ArrayList<>();

    ///When No Orders
    private TextView noOrders;
    private ImageView imgNoOrder;

    //Working Variables
    private int mCurrentPage = 1;

    private static final String TAG = "FragmentProcessing";
    
    //Preference Manager
    private PreferencesManager pm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_processing, container, false);
        mRecyclerView = root.findViewById(R.id.in_progress_orders_recycler);
        mPBFull = root.findViewById(R.id.PBProcess);
        mNSVProgress = root.findViewById(R.id.nsv_process);
        Toolbar mToolbar = root.findViewById(R.id.nav_bar_PO);

        pm = new PreferencesManager(requireContext());
        //Context for Room
        //initializing database
        databaseRoom = RoomDB.getInstance(requireContext());

        ///Header Name
        mToolbar.setTitle("Preparing");
        //Setting Toolbar Navigation Bar
        mToolbar.setOnMenuItemClickListener(item -> {
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
                case R.id.over_flow_log_out: {
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
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noOrders = view.findViewById(R.id.tv_no_order_process);
        imgNoOrder = view.findViewById(R.id.ic_noOrder_process);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Database and Internet
        conditionsInternetAndDatabase();
    }

    public void conditionsInternetAndDatabase() {
        ///internet Available with DB
        if (Internet.isAvailable(requireContext()) && !databaseRoom.mainDao().getProcessOrders().isEmpty()) {
            ///get data from DB
            Log.e(TAG, "onResponse: Information is returning on live data");
            //if work is succeceded then we get data from DB
            List<OrdersItem> orders = databaseRoom.mainDao().getProcessOrders();
            Log.e(TAG, "onStart: List size" + orders.size());
            if (!orders.isEmpty()) {
                Log.e(TAG, "onResponse:Processing List in database c0unt " + orders.size());
                setUpRecFirstTime(orders);
                mNSVProgress.setVisibility(View.VISIBLE);
                mPBFull.setVisibility(View.GONE);
            } else {
                Log.e(TAG, "onResponse: No Processing list in Database");
                AppManager.toast("No Processing Order");
                noOrders.setVisibility(View.VISIBLE);
                imgNoOrder.setVisibility(View.VISIBLE);
            }
        }

        //Internet Available With No Database (Hit Api/Show Illustrations)
        else if (Internet.isAvailable(requireContext()) && databaseRoom.mainDao().getProcessOrders().isEmpty()) {
            ///Hit Api and store in database with state accepted
            getOrdersViaState(AppManager.getBusinessDetails().getData().getToken(), mCurrentPage);
        }
        //No Internet No database
        //Show illustrations
        else if (!Internet.isAvailable(requireContext()) && databaseRoom.mainDao().getProcessOrders().isEmpty()) {
            noOrders.setVisibility(View.GONE);
            imgNoOrder.setVisibility(View.GONE);
        }
        //No Internet Database Available
        else {
            ///get data from DB
            Log.e(TAG, "onResponse: Information is returning on live data");
            //if work is succeceded then we get data from DB
            List<OrdersItem> orders = databaseRoom.mainDao().getProcessOrders();
            Log.e(TAG, "onStart: List size" + orders.size());
            if (!orders.isEmpty()) {
                Log.e(TAG, "onResponse:Processing List in database");
                setUpRecFirstTime(orders);
            } else {
                Log.e(TAG, "onResponse: No Processing list in Database");
                AppManager.toast("No Processing Order");
                noOrders.setVisibility(View.VISIBLE);
                imgNoOrder.setVisibility(View.VISIBLE);
            }
            mNSVProgress.setVisibility(View.VISIBLE);
            mPBFull.setVisibility(View.GONE);
        }
    }

    private void getOrdersViaState(String token, int page) {
        mCurrentPage = mCurrentPage + 1;
        Call<PaginationResponse> call = RetrofitNetMan.getRestApiService().getPaginationPreparingOrders(token, page);
        call.enqueue(new Callback<PaginationResponse>() {
            @Override
            public void onResponse(@NotNull Call<PaginationResponse> call, @NotNull Response<PaginationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (!response.body().getMessage().equals("No records found")) {
                        //collect data & update ui
                        mNewPageOrderItemList = response.body().getData().getOrders();
                        try {

                            if (!mNewPageOrderItemList.isEmpty()) {

                                Log.e(TAG, "onResponse: Item List is Not Empty (Condition after API Success)");

                                Log.d("API", "Adding Data");


                                //setUpRecFirstTime(mNewPageOrderItemList);

                                ///Inserting data in database
                                Constants.ORDER_PAGE_LIST = response.body().getData().getOrders();
                                Log.e(TAG, "onResponse: Processing" + Constants.ORDER_PAGE_LIST);

                                ///Saving in database
                                //////For Order Items (Saving in Database---Will execute one time)
                                bgWork = new OneTimeWorkRequest.Builder(AllOrderWorker.class)
                                        .build();
                                WorkManager.getInstance(requireContext()).enqueue(bgWork);

                                ///Getting live data from database
                                WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(bgWork.getId())
                                        .observe(getViewLifecycleOwner(), info -> {
                                            if (info != null && info.getState().isFinished()) {

                                                Log.e(TAG, "onResponse: Information is returning on live data");
                                                //if work is succeceded then we get data from DB
                                                final List<OrdersItem> orders = databaseRoom.mainDao().getAll();
                                                if (!orders.isEmpty()) {
                                                    Log.e(TAG, "onResponse: List in database");
                                                    setUpRecFirstTime(orders);
                                                } else {
                                                    Log.e(TAG, "onResponse: No list in Database");
                                                    noOrders.setVisibility(View.VISIBLE);
                                                    imgNoOrder.setVisibility(View.VISIBLE);
                                                }
                                            } else {
                                                Log.e(TAG, "onResponse: no info returning from live data");
                                            }
                                        });
                                ///get data from DB
                                Log.e(TAG, "onResponse: Information is returning on live data");
                                //if work is succeceded then we get data from DB
                                List<OrdersItem> orders = databaseRoom.mainDao().getProcessOrders();
                                Log.e(TAG, "onStart: List size" + orders.size());
                                if (!orders.isEmpty()) {
                                    mNSVProgress.setVisibility(View.VISIBLE);
                                    mPBFull.setVisibility(View.GONE);
                                    Log.e(TAG, "onResponse:Processing List in database");
                                    setUpRecFirstTime(orders);
                                }
                            }
                            mPBFull.setVisibility(View.GONE);
                            mNSVProgress.setVisibility(View.VISIBLE);

                        } catch (Exception ignored) {
                        }
                        try {
                            if (!response.body().getData().getOrders().isEmpty()) {
                                getOrdersViaState(AppManager.getBusinessDetails().getData().getToken(), mCurrentPage);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(@NotNull Call<PaginationResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setUpRecFirstTime(List<OrdersItem> paginationOrders) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext()));
        AdapterFirstTime adapter = new AdapterFirstTime(paginationOrders, requireContext().getApplicationContext());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(requireContext(), NewOrder.class);
            Log.e(TAG, "setUpRecView: " + paginationOrders.get(position).getId());
            intent.putExtra("orderId", String.valueOf(paginationOrders.get(position).getId()));
            intent.putExtra("detailType", "processing");
            startActivity(intent);
        });
    }//end setup
}
