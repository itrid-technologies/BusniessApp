package com.itridtechnologies.resturantapp.UiViews.Activities.Frags;

import android.content.Context;
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
import com.itridtechnologies.resturantapp.UiViews.Activities.ReadyDetails;
import com.itridtechnologies.resturantapp.UiViews.Activities.Settings;
import com.itridtechnologies.resturantapp.UiViews.Activities.help;
import com.itridtechnologies.resturantapp.Work.AllOrderWorker;
import com.itridtechnologies.resturantapp.model.NewReady;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.Pagination.PaginationResponse;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItem;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Constants;
import com.itridtechnologies.resturantapp.utils.Internet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class FragmentReady extends Fragment {

    private RecyclerView mReadyRecyclerView; //Ready RecyclerView
    private final ArrayList<NewReady> readyList = new ArrayList<>();
    ///Room database
    RoomDB databaseRoom;

    ////For Order ID
    private String mOrderId;
    private List<OrdersItem> mNewPageOrderItemList = new ArrayList<>();

    //Nested Scroll View and progress bar
    private NestedScrollView mNSV;
    private ProgressBar mPB;

    //Order item
    private OrderItem mOrderItem = null;
    //WorkManager
    OneTimeWorkRequest bgWork;

    //If No Order Available
    private TextView mNoOrderText;
    private ImageView mNoOrderImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ready, container, false);
        mReadyRecyclerView = root.findViewById(R.id.recycler_view_ready);
        //Variables here
        mNSV = root.findViewById(R.id.NSVReady);
        mPB = root.findViewById(R.id.PBReady);
        //setting name of toolbar
        Toolbar mToolbar = root.findViewById(R.id.action_bar_ready);
        ///Header Name
        mToolbar.setTitle("Ready");
        //Context for Room
        //initializing database
        databaseRoom = RoomDB.getInstance(requireContext());
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
                    ;
                    String uri = "tel:" + mCellNumber.trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
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
        mNoOrderText = view.findViewById(R.id.tv_no_order_ready);
        mNoOrderImage = view.findViewById(R.id.ic_noOrder_ready);
    }

    @Override
    public void onStart() {
        super.onStart();

        //Internet Available With Database
        if (Internet.isAvailable(requireContext()) && !databaseRoom.mainDao().getReadyOrders().isEmpty()) {

            ///get data from DB
            Log.e(TAG, "onResponse: Information is returning on live data");
            //if work is succeceded then we get data from DB
            List<OrdersItem> orders = databaseRoom.mainDao().getReadyOrders();
            Log.e(TAG, "onStart: List size" + orders.size());
            if (!orders.isEmpty()) {
                Log.e(TAG, "onResponse: List in database");
                mNSV.setVisibility(View.VISIBLE);
                mPB.setVisibility(View.GONE);
                setUpRecFirstTime(orders);
            } else {
                Log.e(TAG, "onResponse: No list in Database");
                mNoOrderImage.setVisibility(View.VISIBLE);
                mNoOrderText.setVisibility(View.VISIBLE);
            }
        }
        //Internet Available With No Database (Hit Api/Show Illustrations)
        else if (Internet.isAvailable(requireContext()) && databaseRoom.mainDao().getReadyOrders().isEmpty()) {
            ///Hit Api and store in database with state accepted
            getOrdersViaState(AppManager.getBusinessDetails().getData().getToken());
        }
        //No Internet No database
        //Show illustrations
        else if (!Internet.isAvailable(requireContext()) && databaseRoom.mainDao().getReadyOrders().isEmpty()) {
            mNoOrderImage.setVisibility(View.VISIBLE);
            mNoOrderText.setVisibility(View.VISIBLE);
        }
        //No Internet Database Available
        else {

            ///get data from DB
            Log.e(TAG, "onResponse: Information is returning on live data");
            //if work is succeceded then we get data from DB
            List<OrdersItem> orders = databaseRoom.mainDao().getReadyOrders();
            Log.e(TAG, "onStart: List size" + orders.size());
            if (!orders.isEmpty()) {
                Log.e(TAG, "onResponse: List in database");
                setUpRecFirstTime(orders);
            } else {
                Log.e(TAG, "onResponse: No list in Database");
                mNoOrderImage.setVisibility(View.VISIBLE);
                mNoOrderText.setVisibility(View.VISIBLE);
            }
            mNSV.setVisibility(View.VISIBLE);
            mPB.setVisibility(View.GONE);
        }
    }

    private void getOrdersViaState(String token) {
        Call<PaginationResponse> call = RetrofitNetMan.getRestApiService().getPaginationReadyOrders(token);
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
                                mOrderId = String.valueOf(response.body().getData().getOrders().get(0).getId());

                                Log.d("API", "Adding Data");

                                ///Inserting data in database
                                Constants.ORDER_PAGE_LIST = response.body().getData().getOrders();
                                Log.e(TAG, "onResponse: Ready" + Constants.ORDER_PAGE_LIST);


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
                                                    mNoOrderImage.setVisibility(View.VISIBLE);
                                                    mNoOrderText.setVisibility(View.VISIBLE);
                                                    Log.e(TAG, "onResponse: No list in Database");
                                                }
                                            } else {
                                                Log.e(TAG, "onResponse: no info returning from live data");
                                            }
                                        });

                                setUpRecFirstTime(response.body().getData().getOrders());

                                mNSV.setVisibility(View.VISIBLE);
                                mPB.setVisibility(View.GONE);

                            }
                        } catch (Exception ignored) {
                        }


                        mNSV.setVisibility(View.VISIBLE);
                        mPB.setVisibility(View.GONE);
                        mNoOrderImage.setVisibility(View.GONE);
                        mNoOrderText.setVisibility(View.GONE);

                    } else {

                        mNSV.setVisibility(View.GONE);
                        mPB.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: text not shown");
                        mNoOrderImage.setVisibility(View.VISIBLE);
                        mNoOrderText.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(@NotNull Call<PaginationResponse> call, @NotNull Throwable t) {

            }
        });
    }

//
//    private void setUpRecView(List<OrderItem> orders) {
//
//        mReadyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext()));
//        RecyclerViewAdapterProcessingOrders adapter = new RecyclerViewAdapterProcessingOrders(orders, requireContext().getApplicationContext());
//        mReadyRecyclerView.setAdapter(adapter);
//
//        adapter.setOnItemClickListener(position -> {
//            Intent intent = new Intent(requireContext(), ReadyDetails.class);
//            Log.e(TAG, "setUpRecView: " + orders.get(position).getId());
//            intent.putExtra("orderId", String.valueOf(orders.get(position).getId()));
//            startActivity(intent);
//        });
//
//    }//end setup

//    public interface SendMessage {
//        void sendData(String message);
//    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }


    private void setUpRecFirstTime(List<OrdersItem> paginationOrders) {

        mReadyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext()));
        AdapterFirstTime adapter = new AdapterFirstTime(paginationOrders, requireContext().getApplicationContext());
        mReadyRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(requireContext(), ReadyDetails.class);
            Log.e(TAG, "setUpRecView: " + paginationOrders.get(position).getId());
            intent.putExtra("orderId", String.valueOf(paginationOrders.get(position).getId()));
            intent.putExtra("detailType", "ready");
            startActivity(intent);
        });

    }//end setup
}