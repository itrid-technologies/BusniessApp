package com.itridtechnologies.resturantapp.UiViews.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;

import com.itridtechnologies.resturantapp.Adapters.AdapterBusinessOrders;
import com.itridtechnologies.resturantapp.Adapters.AdapterTotal;
import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.OrderDetailModel;
import com.itridtechnologies.resturantapp.model.TotalModel;
import com.itridtechnologies.resturantapp.models.OrderDelivered.SetToDelivered;
import com.itridtechnologies.resturantapp.models.OrderSubItems.AddonItemsItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.DataItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.OrderAddonsItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.SubItems;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.newOrder.NewOrderResponse;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItemsItem;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.LogoutViaNotification;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReadyDetails extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mCallCustomerRD;
    private TextView mCallPartnerRD;
    private TextView mCustName;
    private TextView mPaymentStatus;
    private TextView mAddress;
    private TextView mTVSubTotalAmount;
    private TextView mPartner;
    private TextView mOrderNote;
    private NestedScrollView mNSVReadyDetails;
    private TextView mDeliveryNote;
    private TextView mPrint;
    private String or;
    private AppCompatButton mDelivered;
    private List<OrderItemsItem> mOrderItemList = new ArrayList<>();
    private List<OrderDetailModel> mOrders = new ArrayList<>();
    private String mCellNumber;
    private RecyclerView mReadyOrderRV;
    private ProgressBar mPBReadyOrder;
    private double orderTotal = 00.0000;
    String token = AppManager.getBusinessDetails().getData().getToken();
    private int mIsPickUp;
    private List<TotalModel> mTotalList = new ArrayList<>();

    private static final String TAG = "ReadyDetails";
    //Data Item (Order Items 1)
    private List<DataItem> mOrdersDetail = new ArrayList<>();
    //Total amount of order
    private double valueTotal;

    //Table row
    private TableRow mRiderRow;
    private TableRow mOrderNoteTitle;
    private TableRow mDeliveryTitle;
    private TableRow mDeliveryNoteRow;
    private TableRow mPaymentRow;
    private TableRow mAddressCell;

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

    //Decimal Format
    private DecimalFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_details);
        //Database Context
        //Context for Room
        //initializing database
        databaseRoom = RoomDB.getInstance(ReadyDetails.this);
        AppManager.hideStatusBar(ReadyDetails.this);
        format = new DecimalFormat("0.00");
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

        LogoutViaNotification.logoutOnType();
        totalFun();
        Listener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Printing Recipt

        //Setting Click listener and print intent
        mPrint.setOnClickListener(v -> {
            Intent intent = new Intent(ReadyDetails.this, Reciept.class);
            intent.putExtra("orderId", or);
            intent.putExtra("isTest", "0");
            startActivity(intent);
        });

//        mOrderItem = getOrderFromDB(or);
//        OrderAddressItem mOrderAddress = getOrderAddressFromDB(or);
//        OrderTotalsItem mOrderTotal = getOrderTotalFromDB(or);

    }

    //Set to history
    private void setToDelivered(String id) {

        Call<SetToDelivered> feeUpdate = RetrofitNetMan.getRestApiService().setToDelivered(token, id);
        feeUpdate.enqueue(new Callback<SetToDelivered>() {
            @Override
            public void onResponse(@NotNull Call<SetToDelivered> call12, @NotNull Response<SetToDelivered> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ///Update Database
                    mDelivered.setEnabled(true);
                    Intent intent = new Intent(ReadyDetails.this, BasicActvity.class);
                    intent.putExtra("AOR", "History");
                    startActivity(intent);
                } else if (response.code() == 400) {
                    mDelivered.setEnabled(true);
                    if (response.body() != null) {
                        AppManager.SnackBar(ReadyDetails.this,response.body().getMessage());
                    }
                    Log.e("TAG", "onResponse: " + response.message());
                } else {
                    mDelivered.setEnabled(true);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(@NotNull Call<SetToDelivered> call12, @NotNull Throwable t) {
                mDelivered.setEnabled(true);
                Log.e("TAG", "onResponse: " + t.getMessage());
            }
        });
    }

//    private void updateDatabase() {
//
//        Constants.ORDER_ITEM = new OrdersItem(
//                mOrderItem.getPickuptime(),
////                String.valueOf(mRemainTime),
////                mSavingTime,
//                mOrderItem.getBusinessTax(),
//                mOrderItem.getDateAdded(),
//                mOrderItem.getMinPreTime(),
//                mOrderItem.getMaxPreTime(),
//                mOrderItem.getCourierNotes(),
//                mOrderItem.getBusinessId(),
//                mOrderItem.getId(),
//                "Delivered",
//                mOrderItem.getOrderType(),
//                mOrderItem.getFirstName(),
//                mOrderItem.getBusinessRevShare(),
//                mOrderItem.getItemCount(),
//                mOrderItem.getBusinessName(),
//                mOrderItem.getBusinessNotes(),
//                mOrderItem.getPaymentStatus(),
//                mOrderItem.getLastName(),
//                mOrderItem.getAction(),
//                mOrderItem.getDateAdded(),
//                mOrderItem.getPaymentType(),
//                mOrderItem.getDelay(),
//                mOrderItem.getDateModified(),
//                mOrderItem.getPhoneNumber(),
//                mOrderItem.getCustomerId(),
//                mOrderItem.getBusinessId(),
//                mOrderItem.getStatus());
//
//
//        bgWork = new OneTimeWorkRequest.Builder(OrderWorker.class)
//                .build();
//        WorkManager.getInstance(ReadyDetails.this).enqueue(bgWork);
//
//        WorkManager.getInstance(ReadyDetails.this).getWorkInfoByIdLiveData(bgWork.getId())
//                .observe(this, info -> {
//                    if (info != null && info.getState().isFinished()) {
//                        mDelivered.setEnabled(true);
//                        Intent intent = new Intent(ReadyDetails.this, BasicActvity.class);
//                        intent.putExtra("AOR", "History");
//                        startActivity(intent);
//                    } else {
//                        Log.e(TAG, "onResponse: no info returning from live data");
//                    }
//                });
//    }//update Database


    //Getting order details from api
    private void getOrderDetails(String orderId) {

        Log.e(TAG, "getOrderDetails: order details");
        Call<SubItems> call = RetrofitNetMan.getRestApiService().getOrderDetail(token, orderId);
        call.enqueue(new Callback<SubItems>() {
            @Override
            public void onResponse(@NotNull Call<SubItems> call, @NotNull Response<SubItems> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    //List 1 (Orders Names)
//                    mOrdersDetail = response.body().getData().subList(0, response.body().getData().size());
                    //List 1 (Orders Names)
                    mOrdersReady = response.body().getData().subList(0, response.body().getData().size());

                    valueTotal = 0.00;
                    //Saving and calculating total amount of order
                    Log.e(TAG, "onResponse: Total Amount" + response.body().getData().size());
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        valueTotal = valueTotal + Double.parseDouble(response.body().getData().get(i).getItemPrice());
                    }
                    Log.e(TAG, "onResponse: Total Amount" + valueTotal);


                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                UpdateOrders();
            }

            @Override
            public void onFailure(@NotNull Call<SubItems> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


//    private void getOrderDetails(String orderId) {
//        Call<SubItems> call = RetrofitNetMan.getRestApiService().getOrderDetail(token, orderId);
//        call.enqueue(new Callback<SubItems>() {
//            @Override
//            public void onResponse(@NotNull Call<SubItems> call, @NotNull Response<SubItems> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    //List 1 (Orders Names)
//                    mOrdersReady = response.body().getData().subList(0, response.body().getData().size());
//
//                    for (int i = 0; i < response.body().getData().size(); i++) {
//                        mTotalAmount = mTotalAmount + Double.parseDouble(response.body().getData().get(i).getItemPrice());
//                    }
//                    Constants.ORDER_NAMES = mOrdersReady;
//                    if (response.body().getData().get(0).getOrderAddons().size() >= 1) {
//                        //List 2 (Order Addon Names)
//                        mOrderAddons = response.body().getData().get(0).getOrderAddons();
//                        Constants.ORDER_ADDON_NAME = mOrderAddons;
//                        if (response.body().getData().get(0).getOrderAddons().size() >= 1) {
//                            //List 3 (Order Addon Items_
//                            mOrderAddonDetails = response.body().getData().get(0).getOrderAddons().get(0).getAddonItems();
//                            Constants.ORDER_ADDON_ITEMS = response.body().getData().get(0).getOrderAddons().get(0).getAddonItems();
//                        } else {
//                            AppManager.toast("No Addons Available");
//                        }
//                    }
//
//
//                    mTotalList.add(new TotalModel(
//                            response.body().getData().
//                    ))
//
//
//                    Log.e(TAG, "onResponse: delivery note " + mOrderItem.getOrderType());
//
//                    UpdateUI
//                            (
//                                    mOrderItem.getBusinessNotes(),
//                                    mOrderItem.getCourierNotes(),
//                                    mOrderItem.getOrderType(),
//                                    mOrderItem.getFirstName(),
//                                    mOrderItem.getLastName(),
//                                    mOrderItem.getPhoneNumber(),
//                                    String.valueOf(mOrderItem.getPaymentStatus()),
//                                    "Not Available",
//                                    mTotalAmount,
//                                    "Not Available"
//                            );
//
//                } else {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                }
//                UpdateOrders();
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<SubItems> call, @NotNull Throwable t) {
//            }
//        });
//    }

    public void UpdateOrders() {
        //Setting Adapter
        AdapterBusinessOrders adapter;
        mReadyOrderRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterBusinessOrders(mOrdersReady, ReadyDetails.this);
        mReadyOrderRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Hitting api here to calculate totals
        getDetails(or);

    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void UpdateUI(String bNote, String dNote
            , int orderType, String fName, String lName, String pNo
            , String status, String Address
            , Double tPrice, String partnerName, List<TotalModel> totalList) {

        //Setting Name of Customer
        Log.e(TAG, "UpdateUI: " + fName + " " + lName);
        if (!fName.isEmpty() && !lName.isEmpty()) {
            String Name = fName + " " + lName;
            mCustName.setText(Name);
            Log.d("API", "Adding Data 2");

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
                mPaymentStatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.paid_background));
            } else {
                mPaymentStatus.setText("Unpaid");
            }
        }

        Log.e(TAG, "UpdateUI: amount " + tPrice );

//        //Total and subtotal AMoutn
//        if (tPrice != null) {
//            mTVSubTotalAmount.setText(Constants.CURRENCY_SIGN + " " + format.format(tPrice));
//        } else {
//            mTVSubTotalAmount.setText(Constants.CURRENCY_SIGN + " " + "0");
//        }

        //set business & delivery notes
        if (!bNote.isEmpty()) {
            mOrderNote.setText(bNote);
        } else {
            mOrderNoteTitle.setVisibility(View.GONE);
            mOrderNote.setVisibility(View.GONE);
        }

        //Set Customer Address
        if (!Address.isEmpty()) {
            mAddress.setText(Address);
        }


        if (orderType == 1) {
            Log.e(TAG, "UpdateUI: Rider type " + orderType);
            mRiderRow.setVisibility(View.VISIBLE);
            mAddressCell.setVisibility(View.GONE);
        } else if (orderType == 0) {
            Log.e(TAG, "UpdateUI: Self type " + orderType);
            mRiderRow.setVisibility(View.GONE);
            mPaymentRow.setVisibility(View.VISIBLE);
            mAddressCell.setVisibility(View.GONE);
        }
        //set Payment status visible when type is delivery
        else if (orderType == 2) {
            Log.e(TAG, "UpdateUI: Delivery type " + orderType);
            mPaymentRow.setVisibility(View.VISIBLE);
            if (!dNote.isEmpty()) {
                mDeliveryNoteRow.setVisibility(View.VISIBLE);
                mDeliveryTitle.setVisibility(View.VISIBLE);
                mDeliveryNote.setText(dNote);
            }
        } else {
            AppManager.SnackBar(ReadyDetails.this, "Order Type DoesNot Exist");
        }

        mRCVTotals.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AdapterTotal totalAdapter = new AdapterTotal(totalList, ReadyDetails.this);
        mRCVTotals.setAdapter(totalAdapter);
        totalAdapter.notifyDataSetChanged();

        //Making the view visible
        mPBReadyOrder.setVisibility(View.GONE);
        mNSVReadyDetails.setVisibility(View.VISIBLE);

    }//UpdateUI

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

//    private OrdersItem getOrderFromDB(String or) {
//        return databaseRoom.mainDao().getOrderById(Integer.parseInt(or));
//    }
//
//    private OrderAddressItem getOrderAddressFromDB(String or) {
//        return databaseRoom.mainDao().getOrderAddressById(Integer.parseInt(or));
//    }
//
//    private OrderTotalsItem getOrderTotalFromDB(String or) {
//        return databaseRoom.mainDao().getOrderTotalsById(Integer.parseInt(or));
//    }

    public void totalFun() {

    }//end total function

    //Setting Variables
    public void setVariables() {
        mCallCustomerRD = findViewById(R.id.tv_in_progress_call_customer_RO);
        mCallPartnerRD = findViewById(R.id.tv_in_progress_call_delivery_partner_RO);
        mReadyOrderRV = findViewById(R.id.preparing_details_recycler);
        mPBReadyOrder = findViewById(R.id.PBReadyDetails);
        mCustName = findViewById(R.id.tv_ready_det_customer_name_RO);
        mAddress = findViewById(R.id.tv_address_RO);
        mOrderNote = findViewById(R.id.et_order_note_RO);
        mOrderNoteTitle = findViewById(R.id.orderNoteTitle);
        mDeliveryNote = findViewById(R.id.et_delivery_note_RO);
        mPaymentStatus = findViewById(R.id.payStatus);
        mPaymentRow = findViewById(R.id.paymentStatusReadyOrder);
        mNSVReadyDetails = findViewById(R.id.nsv_readyDetails);
        mRCVTotals = findViewById(R.id.rv_order_totals_ready);
        mDelivered = findViewById(R.id.btn_delivered);
        mPrint = findViewById(R.id.print_ready);
        mPartner = findViewById(R.id.tv_deliver_partner_name_RO);
        mRiderRow = findViewById(R.id.partner);
        mDeliveryTitle = findViewById(R.id.tv_delivery_note_PD);
        mDeliveryNoteRow = findViewById(R.id.TR_delivery_note);
        mAddressCell = findViewById(R.id.prepare_time_layout_RO);

    }

    ///Click Listener
    public void Listener() {
        mDelivered.setOnClickListener(v -> {
            mDelivered.setEnabled(false);
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
            Log.e(TAG, "toolbarfun: " + or);
            intent.putExtra("orderNo", or);
            startActivity(intent);
            return false;
        });
    }

    //method to get business orders from server
    private void getDetails(String orderId) {
        Log.e(TAG, "getDetails: persoal details");
        //retrieve token from pref
        String token = AppManager.getBusinessDetails().getData().getToken();

        Call<NewOrderResponse> call = RetrofitNetMan.getRestApiService().getOrders(token, orderId);
        call.enqueue(new Callback<NewOrderResponse>() {
            @Override
            public void onResponse(@NotNull Call<NewOrderResponse> call, @NotNull Response<NewOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    //collect data & update ui
                    ///Commenting temporily
                    mOrderItemList = response.body().getData().getOrderItems();


                    Log.e(TAG, "onResponse: first and last name: " + response.body().getData().getOrder().get(0).getFirstName() +
                            response.body().getData().getOrder().get(0).getLastName());


                    if (response.body().getData().getOrderTotals().size() > 0)
                    {                    //Calculating Total Amount
                        for (int i = 0; i < response.body().getData().getOrderTotals().size(); i++) {
                            mTotalList.add(new TotalModel(
                                    response.body().getData().getOrderTotals().get(i).getLabel(),
                                    response.body().getData().getOrderTotals().get(i).getValue()
                            ));
                            orderTotal = orderTotal + Double.parseDouble(response.body().getData().getOrderTotals().get(i).getValue());
                            Log.e(TAG, "onResponse: order total " + orderTotal);
                        }
                    }

                    String address;

                    if (response.body().getData().getOrderAddress().size() > 0) {
                        address = response.body().getData().getOrderAddress().get(0).getAddressLine1() + " "
                                + response.body().getData().getOrderAddress().get(0).getAddressLine2() + " "
                                + response.body().getData().getOrderAddress().get(0).getCity() + " "
                                + response.body().getData().getOrderAddress().get(0).getState() + " "
                                + response.body().getData().getOrderAddress().get(0).getCountry() + " "
                                + response.body().getData().getOrderAddress().get(0).getPostCode();

                        Log.e(TAG, "onResponse: address" +
                                "" + address);
                    } else {
                        address = "Not Available";
                    }


                    UpdateUI
                            (
                                    response.body().getData().getOrder().get(0).getBusinessNotes(),
                                    response.body().getData().getOrder().get(0).getCourierNotes(),
                                    response.body().getData().getOrder().get(0).getOrderType(),
                                    response.body().getData().getOrder().get(0).getFirstName(),
                                    response.body().getData().getOrder().get(0).getLastName(),
                                    response.body().getData().getOrder().get(0).getPhoneNumber(),
                                    String.valueOf(response.body().getData().getOrder().get(0).getPaymentStatus()),
                                    address,
                                    orderTotal,
                                    "Not Available",
                                    mTotalList
                            );
//                    if (!mOrderItemList.isEmpty()) {
////                        mAddonItemList = response.body().getData().getOrderItems();
//                        double orderTotal = 0;
//                        int orderSize = response.body().getData().getOrderItems().size();
//                        //Calculating Total Amount
//                        for (int i = 0; i < orderSize; i++) {
//                            orderTotal = orderTotal + Double.parseDouble(response.body().getData().getOrderItems().get(i).getItemPrice());
//                        }
////
//
//                        //Calculating Total Amount
//                        for (int i = 0; i < response.body().getData().getOrderTotals().size(); i++) {
//                            mTotalList.add(new TotalModel(
//                                    response.body().getData().getOrderTotals().get(i).getLabel(),
//                                    response.body().getData().getOrderTotals().get(i).getValue()
//                            ));
//                            orderTotal = orderTotal + Double.parseDouble(response.body().getData().getOrder().get(0).getOrderTotalPrice());
//                            Log.e(TAG, "onResponse: order total " + orderTotal);
//                        }
//
//                        String address = response.body().getData().getOrderAddress().get(0).getAddressLine1() + " "
//                                + response.body().getData().getOrderAddress().get(0).getAddressLine2() + " "
//                                + response.body().getData().getOrderAddress().get(0).getCity() + " "
//                                + response.body().getData().getOrderAddress().get(0).getState() + " "
//                                + response.body().getData().getOrderAddress().get(0).getCountry() + " "
//                                + response.body().getData().getOrderAddress().get(0).getPostCode();
//
//                        UpdateUI
//                                (
//                                        response.body().getData().getOrder().get(0).getBusinessNotes(),
//                                        response.body().getData().getOrder().get(0).getCourierNotes(),
//                                        response.body().getData().getOrder().get(0).getOrderType(),
//                                        response.body().getData().getOrder().get(0).getFirstName(),
//                                        response.body().getData().getOrder().get(0).getLastName(),
//                                        response.body().getData().getOrder().get(0).getPhoneNumber(),
//                                        String.valueOf(mOrderItem.getPaymentStatus()),
//                                        address,
//                                        mTotalAmount,
//                                        "Not Available",
//                                        mTotalList
//                                );
//                        Log.d("API", "Adding Data 2");
//                    }

                    ///Getting Order Details
//                    for (int i = 0; i < response.body().getData().getOrderItems().size(); i++) {
//                        Log.e("Name", response.body().getData().getOrderItems().get(i).getItemName());
//                        mOrders.add(new OrderDetailModel(
//                                String.valueOf(response.body().getData().getOrderItems().get(i).getItemQty()),
//                                response.body().getData().getOrderItems().get(i).getItemName(),
//                                response.body().getData().getOrderItems().get(i).getItemPrice()
//                        ));
//
//                    }

                }//not null
                else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(@NotNull Call<NewOrderResponse> call, @NotNull Throwable t) {
                AppManager.toast("No Internet");
            }
        });
    }//end get



    @Override
    protected void onResume() {
        super.onResume();
        LogoutViaNotification.onResumeFun();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogoutViaNotification.onPauseFun();
    }

}