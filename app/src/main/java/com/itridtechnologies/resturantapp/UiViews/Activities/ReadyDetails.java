package com.itridtechnologies.resturantapp.UiViews.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.itridtechnologies.resturantapp.Adapters.AdapterBusinessOrders;
import com.itridtechnologies.resturantapp.Adapters.AdapterTotal;
import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.Work.OrderWorker;
import com.itridtechnologies.resturantapp.model.OrderDetailModel;
import com.itridtechnologies.resturantapp.model.TotalModel;
import com.itridtechnologies.resturantapp.models.OrderDelivered.SetToDelivered;
import com.itridtechnologies.resturantapp.models.OrderSubItems.AddonItemsItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.DataItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.OrderAddonsItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.SubItems;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.newOrder.NewOrderResponse;
import com.itridtechnologies.resturantapp.models.newOrder.OrderAddressItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItemsItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderTotalsItem;
import com.itridtechnologies.resturantapp.models.orderFeesSetting.FeeUpdateResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class ReadyDetails extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mCallCustomerRD;
    private TextView mCallPartnerRD;
    private TextView mCustName;
    private TextView mPaymentStatus;
    private TextView mAddress;
    private TextView mTVDeliveryNote;
    private TextView mTVSubTotalAmount;
    private TextView mPartner;
    private EditText mOrderNote;
    private NestedScrollView mNSVReadyDetails;
    private EditText mDeliveryNote;
    private TextView mPrint;
    private String or;
    private String str = "";
    private AppCompatButton mDelivered;
    private List<OrderItemsItem> mOrderItemList = new ArrayList<>();
    private List<OrderDetailModel> mOrders = new ArrayList<>();
    private String mCellNumber;
    private RecyclerView mReadyOrderRV;
    private ProgressBar mPBReadyOrder;
    String token = AppManager.getBusinessDetails().getData().getToken();
    private int mIsPickUp;

    //Table row
    private TableRow mRiderRow;

    ///Room database
    RoomDB databaseRoom;

    //Database
    private double mRemainTime = 50000.00;
    private String mSavingTime;

    //Order item
    private OrdersItem mOrderItem = null;

    //WorkManager
    OneTimeWorkRequest bgWork;

    //Recycler View Totals and Adapter
    private RecyclerView mRCVTotals;

    //Data Item (Order Items 1)
    private List<DataItem> mOrdersReady = new ArrayList<>();
    //Addon Name of Order
    private List<OrderAddonsItem> mOrderAddons = new ArrayList<>();
    //Addons details
    private List<AddonItemsItem> mOrderAddonDetails = new ArrayList<>();

    //Calculating Totals
    private Double mTotalAmount = 00.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_details);
        //Database Context
        //Context for Room
        //initializing database
        databaseRoom = RoomDB.getInstance(ReadyDetails.this);
        AppManager.hideStatusBar(ReadyDetails.this);
        setVariables();
        CallFunctions();
        toolbarfun();
        //Calneder instance
        Calendar calendar = Calendar.getInstance();

        //Getting time from system
        //getting current time from system
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        mSavingTime = "Current Time : " + mdformat.format(calendar.getTime());
        Log.e(TAG, "onCreateView: time" + mSavingTime);



        //get orders from server
        or = String.valueOf(getIntent().getStringExtra("orderId"));
        mToolbar.setTitle("#" + or);
        //Calling API for Order Details
        getOrderDetails(or);
        totalFun();
        Listener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getDetails();

        //Printing Recipt

        //Setting Click listener and print intent
        mPrint.setOnClickListener(v -> {
            Intent intent = new Intent(ReadyDetails.this,Reciept.class);
            intent.putExtra("orderId",or);
            intent.putExtra("isTest","0");
            startActivity(intent);
        });

        mOrderItem = getOrderFromDB(or);
        OrderAddressItem mOrderAddress = getOrderAddressFromDB(or);
        OrderTotalsItem mOrderTotal = getOrderTotalFromDB(or);

    }

    //Set to history
    private void setToDelivered(String id)
    {

        Call<SetToDelivered> feeUpdate = RetrofitNetMan.getRestApiService().setToDelivered(token, id);
        feeUpdate.enqueue(new Callback<SetToDelivered>() {
            @Override
            public void onResponse(@NotNull Call<SetToDelivered> call12, @NotNull Response<SetToDelivered> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ///Update Database
                    updateDatabase();

                } else if (response.code() == 400) {
                    Log.e("TAG", "onResponse: " +  response.message() );
                }

                else {
                    Toast.makeText(getApplicationContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

            }

            @Override
            public void onFailure(Call<SetToDelivered> call12, Throwable t) {
                Log.e("TAG", "onResponse: " +  t.getMessage());
            }
        });
    }

    private void updateDatabase()
    {

        Constants.ORDER_ITEM = new OrdersItem(
                mOrderItem.getPickuptime(),
                String.valueOf(mRemainTime),
                mSavingTime,
                mOrderItem.getBusinessTax(),
                mOrderItem.getDateAdded(),
                mOrderItem.getMinPreTime(),
                mOrderItem.getMaxPreTime(),
                mOrderItem.getCourierNotes(),
                mOrderItem.getBusinessId(),
                mOrderItem.getId(),
                "Delivered",
                mOrderItem.getOrderType(),
                mOrderItem.getFirstName(),
                mOrderItem.getBusinessRevShare(),
                mOrderItem.getItemCount(),
                mOrderItem.getBusinessName(),
                mOrderItem.getBusinessNotes(),
                mOrderItem.getPaymentStatus(),
                mOrderItem.getLastName(),
                mOrderItem.getAction(),
                mOrderItem.getDateAdded(),
                mOrderItem.getPaymentType(),
                mOrderItem.getDelay(),
                mOrderItem.getDateModified(),
                mOrderItem.getPhoneNumber(),
                mOrderItem.getCustomerId(),
                mOrderItem.getBusinessId(),
                mOrderItem.getStatus());


        bgWork = new OneTimeWorkRequest.Builder(OrderWorker.class)
                .build();
        WorkManager.getInstance(ReadyDetails.this).enqueue(bgWork);

        WorkManager.getInstance(ReadyDetails.this).getWorkInfoByIdLiveData(bgWork.getId())
                .observe(this, info -> {
                    if (info != null && info.getState().isFinished()) {
                        Intent intent = new Intent(ReadyDetails.this, FeedbackActivity.class);
                        intent.putExtra("ORDER_ID",or);
                        startActivity(intent);
                    } else {
                        Log.e(TAG, "onResponse: no info returning from live data");
                    }
                });
    }

    private void getOrderDetails(String orderId) {
        Call<SubItems> call = RetrofitNetMan.getRestApiService().getOrderDetail(token, orderId);
        call.enqueue(new Callback<SubItems>() {
            @Override
            public void onResponse(@NotNull Call<SubItems> call, @NotNull Response<SubItems> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //List 1 (Orders Names)
                    mOrdersReady = response.body().getData().subList(0, response.body().getData().size());

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        mTotalAmount = mTotalAmount + Double.parseDouble(response.body().getData().get(i).getItemPrice());
                    }
                    Constants.ORDER_NAMES = mOrdersReady;
                    if (response.body().getData().get(0).getOrderAddons().size() >= 1) {
                        //List 2 (Order Addon Names)
                        mOrderAddons = response.body().getData().get(0).getOrderAddons();
                        Constants.ORDER_ADDON_NAME = mOrderAddons;
                        if (response.body().getData().get(0).getOrderAddons().size() >= 1) {
                            //List 3 (Order Addon Items_
                            mOrderAddonDetails = response.body().getData().get(0).getOrderAddons().get(0).getAddonItems();
                            Constants.ORDER_ADDON_ITEMS = response.body().getData().get(0).getOrderAddons().get(0).getAddonItems();
                        } else {
                            AppManager.toast("No Addons Available");
                        }
                    }

                    mIsPickUp = mOrderItem.getOrderType();

                    UpdateUI
                            (
                                    mOrderItem.getBusinessNotes(),
                                    mOrderItem.getCourierNotes(),
                                    mOrderItem.getFirstName(),
                                    mOrderItem.getLastName(),
                                    mOrderItem.getPhoneNumber(),
                                    String.valueOf(mOrderItem.getPaymentStatus()),
                                    "Not Available",
                                    mTotalAmount,
                                    "Not Available",
                                    mIsPickUp
                            );

                }
                else {
                    Toast.makeText(getApplicationContext(), "Token Expired " + response.message(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                UpdateOrders();
            }

            @Override
            public void onFailure(@NotNull Call<SubItems> call, @NotNull Throwable t) {
            }
        });
    }

    public void UpdateOrders() {
        //Setting Adapter
        AdapterBusinessOrders adapter;
        mReadyOrderRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterBusinessOrders(mOrdersReady, ReadyDetails.this);
        mReadyOrderRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        
        mPBReadyOrder.setVisibility(View.GONE);
        mNSVReadyDetails.setVisibility(View.VISIBLE);
    }

    public void UpdateUI(String bNote, String dNote
            , String fName, String lName, String pNo
            , String status, String Address
            , Double tPrice,String partnerName, int pickup) {


        //Setting Name of Customer
        Log.e(TAG, "UpdateUI: " + fName + " " + lName );
        if (!fName.isEmpty() && !lName.isEmpty()) {
            String Name = fName + " " + lName;
            mCustName.setText(Name);
            Log.d("API", "Adding Data 2");

        }

        if (pickup == 1)
        {
            mRiderRow.setVisibility(View.GONE);
        }

        ///Partner Name
        mPartner.setText(partnerName);


        ///Setting Phone Number
        if (!pNo.isEmpty()) {
            mCellNumber = pNo;
        }
        ///Setting Payment Status
        if (!status.isEmpty()) {
            if (status.trim().equals("1")) {
                mPaymentStatus.setText("Paid");
            } else {
                mPaymentStatus.setText("Unpaid");
            }
        }


        //Total and subtotal AMoutn

        if (tPrice != null) {
            mTVSubTotalAmount.setText(String.valueOf(tPrice));
        } else {
            mTVSubTotalAmount.setText("0");
        }


        //set business & delivery notes
        if (!bNote.isEmpty()) {
            mOrderNote.setText(bNote);
        }

        //Set Customer Address
        if (!Address.isEmpty()) {
            mAddress.setText(Address);
        }

        ///Check Delivery Note
        if (!dNote.isEmpty()) {

            if (dNote.trim().equals("Delivery")) {
                mPaymentStatus.setVisibility(View.VISIBLE);
                if (dNote.trim().equals("Self-Delivery")) {
                    mDeliveryNote.setVisibility(View.VISIBLE);
                    mTVDeliveryNote.setVisibility(View.VISIBLE);
                    mTVDeliveryNote.setText(dNote);
                }
            }
        }

    }

    //Setting Onclich Listener to make real call
    //Setting Intent to make real call
    //Setting Click listener to call customer usgin dial intent
    public void CallFunctions() {
        mCallCustomerRD.setOnClickListener(v -> {
            String uri = "tel:" + mCellNumber.trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        });

        mCallPartnerRD.setOnClickListener(v -> {
            String uri = "tel:" + mCellNumber.trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        });

    }


    private OrdersItem getOrderFromDB(String or) {
        return databaseRoom.mainDao().getOrderById(Integer.parseInt(or));
    }


    private OrderAddressItem getOrderAddressFromDB(String or) {
        return databaseRoom.mainDao().getOrderAddressById(Integer.parseInt(or));
    }


    private OrderTotalsItem getOrderTotalFromDB(String or) {
        return databaseRoom.mainDao().getOrderTotalsById(Integer.parseInt(or));
    }

    public void totalFun() {
        mRCVTotals.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AdapterTotal totalAdapter = new AdapterTotal(dummySubTotals(), ReadyDetails.this);
        mRCVTotals.setAdapter(totalAdapter);
        totalAdapter.notifyDataSetChanged();
    }//end total function

    //////dummy data for recycler
    public List<TotalModel> dummySubTotals() {
        //Adding Dummy Data in list
        List<TotalModel> list = new ArrayList<>();
        list.add(new TotalModel("Subtotal", "200.00"));
        list.add(new TotalModel("Delivery Fee", "50.00"));
        list.add(new TotalModel("Service Fee", "256.00"));

        return list;
    }

    //Setting Variables
    public void setVariables() {
        mCallCustomerRD = findViewById(R.id.tv_in_progress_call_customer_RO);
        mCallPartnerRD = findViewById(R.id.tv_in_progress_call_delivery_partner_RO);
        mReadyOrderRV = findViewById(R.id.preparing_details_recycler);
        mPBReadyOrder = findViewById(R.id.PBReadyDetails);
        mCustName = findViewById(R.id.tv_ready_det_customer_name_RO);
        mAddress = findViewById(R.id.tv_address_RO);
        mOrderNote = findViewById(R.id.et_order_note_RO);
        mDeliveryNote = findViewById(R.id.et_delivery_note_RO);
        mPaymentStatus = findViewById(R.id.payStatus);
        mNSVReadyDetails = findViewById(R.id.nsv_readyDetails);
        mTVDeliveryNote = findViewById(R.id.tv_delNote);
        mTVSubTotalAmount = findViewById(R.id.tv_sub_total_amount_RO);
        mRCVTotals = findViewById(R.id.rv_order_totals_ready);
        mDelivered = findViewById(R.id.btn_delivered);
        mPrint = findViewById(R.id.print_ready);
        mPartner = findViewById(R.id.tv_deliver_partner_name_RO);
        mRiderRow = findViewById(R.id.partner);

    }

    ///Click Listener
    public void Listener()
    {
        mDelivered.setOnClickListener(v -> {

            setToDelivered(or);

        });

    }

    //Setting Toolbar
    public void toolbarfun() {
        mToolbar = findViewById(R.id.nav_bar_RO);
        mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ReadyDetails.this, BasicActvity.class);
            startActivity(intent);
        });

        mToolbar.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(ReadyDetails.this, OrderIssues.class);
            startActivity(intent);
            return false;
        });
    }

    //method to get business orders from server
    private void getDetails() {
        //retrieve token from pref
        String token = AppManager.getBusinessDetails().getData().getToken();

        Call<NewOrderResponse> call = RetrofitNetMan.getRestApiService().getOrders(token, "5");
        call.enqueue(new Callback<NewOrderResponse>() {
            @Override
            public void onResponse(@NotNull Call<NewOrderResponse> call, @NotNull Response<NewOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    //collect data & update ui

                    ///Commenting temporily
                    mOrderItemList = response.body().getData().getOrderItems();

                    if (!mOrderItemList.isEmpty()) {
                        Log.d("API", "Adding Data");
                        str = String.valueOf(response.body().getData().getOrder().get(0).getOrderType());
//                        mAddonItemList = response.body().getData().getOrderItems();
                        double orderTotal = 0;
                        int orderSize = response.body().getData().getOrderItems().size();
                        //Calculating Total Amount
                        for (int i = 0; i < orderSize; i++) {
                            orderTotal = orderTotal + Double.parseDouble(response.body().getData().getOrderItems().get(i).getItemPrice());
                        }
//
//                        UpdateUI
//                                (
//                                        response.body().getData().getOrder().get(0).getBusinessNotes(),
//                                        response.body().getData().getOrder().get(0).getCourierNotes(),
//                                        response.body().getData().getOrder().get(0).getFirstName(),
//                                        response.body().getData().getOrder().get(0).getLastName(),
//                                        response.body().getData().getOrder().get(0).getPhoneNumber(),
//                                        String.valueOf(response.body().getData().getOrder().get(0).getPaymentStatus()),
//                                        response.body().getData().getOrderAddress().get(0).getAddress(),
//                                        String.valueOf(orderTotal)
//                                );
                        Log.d("API", "Adding Data 2");
                    }

                    ///Getting Order Details
                    for (int i = 0; i < response.body().getData().getOrderItems().size(); i++) {
                        Log.e("Name", response.body().getData().getOrderItems().get(i).getItemName());
                        mOrders.add(new OrderDetailModel(
                                String.valueOf(response.body().getData().getOrderItems().get(i).getItemQty()),
                                response.body().getData().getOrderItems().get(i).getItemName(),
                                response.body().getData().getOrderItems().get(i).getItemPrice()
                        ));

                    }

                }//not null
                else {
                    Toast.makeText(getApplicationContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(@NotNull Call<NewOrderResponse> call, @NotNull Throwable t) {
                AppManager.toast("No Internet");
            }
        });
    }//end get

}