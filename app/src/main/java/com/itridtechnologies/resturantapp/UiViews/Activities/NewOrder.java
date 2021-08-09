package com.itridtechnologies.resturantapp.UiViews.Activities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.JsonObject;
import com.itridtechnologies.resturantapp.Adapters.AdapterBusinessOrders;
import com.itridtechnologies.resturantapp.Adapters.AdapterTotal;
import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.Work.OrderWorker;
import com.itridtechnologies.resturantapp.model.TotalModel;
import com.itridtechnologies.resturantapp.models.ActionOrder.ActionResponse;
import com.itridtechnologies.resturantapp.models.OrderSubItems.DataItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.SubItems;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.SetToReady.SetToReadyResponse;
import com.itridtechnologies.resturantapp.models.newOrder.NewOrderResponse;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItemsItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderTotalsItem;
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

public class NewOrder extends AppCompatActivity {
    private TableRow mPaymentStatusLayout;
    private TextView mTVDeliveryNote;
    private TextView mPayStatus;
    private TextView mTotalPayment;
    private TableRow mETDeliveryNote;
    private TextView mCallCustomer;
    private TextView mCustName;
    private TextView mPrint;
    private TextView mPrepareTimeTV;
    private TextView mPartner;
    private ProgressBar mProgressNO;
    private int mIsRiderAssigned = 0;
    private String or;
    private LinearLayout mBtnLayout;
    private NestedScrollView mNSVNewOrder;
    private String mCellNumber;
    private RecyclerView mNewOrderDetail;
    private List<OrderItemsItem> mOrderItemList = new ArrayList<>();
    private AppCompatButton mBtnAccept;
    private AppCompatButton mBtnReject;
    private String str = "";
    private Toolbar mToolbar;
    String token = AppManager.getBusinessDetails().getData().getToken();
    private TableRow mPrepareTime;
    private String mAccepted = "0";
    private TableRow mDeliveryPartner;
    ///Room database
    RoomDB databaseRoom;

    //Database
    private double mRemainTime = 50000.00;
    private String mSavingTime;
    private Date currentTime;

    //Totals Recycler View + Adapter
    private RecyclerView mRVTotals;
    private AdapterTotal totalAdapter;

    //Button for ready Order
    private LinearLayout mLLReady;
    private AppCompatButton mBtnReadyOrder;

    //Order item
    private OrdersItem mOrderItem = null;

    //WorkManager
    OneTimeWorkRequest bgWork;

    private EditText edtBusinessNote;

    //Data Item (Order Items 1)
    private List<DataItem> mOrders = new ArrayList<>();

    //Total amount of order
    private Double valueTotal;

    //order type
    private int mIsPickUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        //Context for Room
        //initializing database
        databaseRoom = RoomDB.getInstance(NewOrder.this);
        //calling fucntion where all variables are initialized
        setVariable();
        changingView();
        //get order ID from previous activity
        or = String.valueOf(getIntent().getStringExtra("orderId"));
        Log.e(TAG, "onCreate: order id" + or);
        //hide status bar
        AppManager.hideStatusBar(this);


        //Calwender instance for live date
        //Calneder instance
        Calendar calendar = Calendar.getInstance();

        //Getting time from system
        //getting current time from system
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        mSavingTime = "Current Time : " + mdformat.format(calendar.getTime());
        Log.e(TAG, "onCreateView: time" + mSavingTime);



        //Dialing fucntion
        callFun();
        ///Setting Action bar
        mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(NewOrder.this, BasicActvity.class);
            startActivity(intent);
        });
        mToolbar.findViewById(R.id.nav_bar_NO);
        mToolbar.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(NewOrder.this, OrderIssues.class);
            intent.putExtra("orderNo", or);
            startActivity(intent);
            return false;
        });
        mToolbar.setTitle(" # " + or);

        //Setting Totals
        totalFun();
        //Calling API for Order Details
        getOrderDetails(or);
        getNewBusinessOrders(or);
        //setting listeners
        clickListeners();
    }//oc


    public void clickListeners() {
        //Intent when order is Accepted
        mBtnAccept.setOnClickListener(v -> {
            mAccepted = "1";
            btnAccRejApi(or, mAccepted);
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
                    "Preparing",
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
                    mOrderItem.getStatus()
            );

            bgWork = new OneTimeWorkRequest.Builder(OrderWorker.class)
                    .build();
            WorkManager.getInstance(NewOrder.this).enqueue(bgWork);

            WorkManager.getInstance(NewOrder.this).getWorkInfoByIdLiveData(bgWork.getId())
                    .observe(this, info -> {
                        if (info != null && info.getState().isFinished()) {
                            Log.e(TAG, "clickListeners: " + Constants.ORDER_ITEM);
                            Intent intent = new Intent(NewOrder.this, BasicActvity.class);
                            intent.putExtra("AOR", "Accepted");
                            startActivity(intent);
                        } else {
                            Log.e(TAG, "onResponse: no info returning from live data");
                        }
                    });
        });

        // Intent when order is Rejected
        mBtnReject.setOnClickListener(v -> {
            mAccepted = "0";
            btnAccRejApi(or, mAccepted);
            Intent intent = new Intent(NewOrder.this, BasicActvity.class);
            intent.putExtra("AOR", "Rejected");
//            intent.putExtra("orderId", or);
            startActivity(intent);
        });
        //Setting Click listener and print intent
        mPrint.setOnClickListener(v -> {
            Intent intent = new Intent(NewOrder.this, Reciept.class);
            intent.putExtra("orderId", or);
            intent.putExtra("isTest", "0");
            startActivity(intent);
        });

        ////Ready order button to go to order activity
        mBtnReadyOrder.setOnClickListener(v -> {

            Log.e(TAG, "clickListeners: is pickup" + mIsPickUp);

            if (mIsPickUp == 0) {
                setOrderToReady(or);
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
                        "Ready",
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
                        mOrderItem.getStatus()
                );

                bgWork = new OneTimeWorkRequest.Builder(OrderWorker.class)
                        .build();
                WorkManager.getInstance(NewOrder.this).enqueue(bgWork);

                WorkManager.getInstance(NewOrder.this).getWorkInfoByIdLiveData(bgWork.getId())
                        .observe(this, info -> {
                            if (info != null && info.getState().isFinished()) {
                                Log.e(TAG, "clickListeners: " + Constants.ORDER_ITEM);
                                Intent intent = new Intent(NewOrder.this, BasicActvity.class);
                                intent.putExtra("AOR", "Ready");
                                startActivity(intent);
                            } else {
                                Log.e(TAG, "onResponse: no info returning from live data");
                            }
                        });
            }
            else {
                mBtnReadyOrder.setVisibility(View.GONE);
                mLLReady.setVisibility(View.GONE);
            }


        });
    }

    private void btnAccRejApi(String orderId, String actionKey) {

        //Creating Json Object to post Action API
        JsonObject obj = new JsonObject();
        obj.addProperty("id", orderId);
        obj.addProperty("action", actionKey);

        Call<ActionResponse> caall = RetrofitNetMan.getRestApiService().actionOfOrder(token, obj);
        caall.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(@NotNull Call<ActionResponse> caall, @NotNull Response<ActionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppManager.saveActionDetails(response.body());
                }
                else {
                    Toast.makeText(getApplicationContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ActionResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }//end order

    private void setOrderToReady(String orderId) {

        Call<SetToReadyResponse> caall = RetrofitNetMan.getRestApiService().setToReady(token, orderId);
        caall.enqueue(new Callback<SetToReadyResponse>() {
            @Override
            public void onResponse(@NotNull Call<SetToReadyResponse> caall, @NotNull Response<SetToReadyResponse> response) {

            }

            @Override
            public void onFailure(@NotNull Call<SetToReadyResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }//end order

    public void changingView() {

        String str = getIntent().getStringExtra("detailType");
        if (str != null) {
            if (str.trim().equals("processing")) {
                mBtnReject.setVisibility(View.GONE);
                mBtnAccept.setVisibility(View.GONE);
                mPrepareTime.setVisibility(View.VISIBLE);
                mDeliveryPartner.setVisibility(View.VISIBLE);
                mLLReady.setVisibility(View.VISIBLE);
                Log.e(TAG, "clickListeners: changing view eewe");
            } else if (str.trim().equals("ready")) {
                AppManager.intent(ReadyDetails.class);
            }
        } else {
            mBtnReject.setVisibility(View.VISIBLE);
            mBtnAccept.setVisibility(View.VISIBLE);
            mPrepareTime.setVisibility(View.GONE);
            mDeliveryPartner.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mOrderItem = getOrderFromDB(or);
        OrderTotalsItem mOrderTotalItem = getOrderTotalsFromDB(Integer.parseInt(or));
        List<OrderItemsItem> mOrderItemItem = getOrderItemsFromDB(or);

    }

    ///Getting order details
    private OrdersItem getOrderFromDB(String or) {
        return databaseRoom.mainDao().getOrderById(Integer.parseInt(or));
    }

    //Geting Order Totals
    private OrderTotalsItem getOrderTotalsFromDB(int or) {
        Log.e(TAG, "getOrderTotalsFromDB: " + databaseRoom.mainDao().getOrderTotalsById(or));
        return databaseRoom.mainDao().getOrderTotalsById(or);
    }

    //Getting order Items
    private List<OrderItemsItem> getOrderItemsFromDB(String or) {
        return databaseRoom.mainDao().getOrderItemsById(Integer.parseInt(or));
    }

    //Setting variables
    public void setVariable() {
        mPaymentStatusLayout = findViewById(R.id.payment_status_layout_NO);
        mTVDeliveryNote = findViewById(R.id.tv_delivery_note);
        mETDeliveryNote = findViewById(R.id.et_delivery_note);
        mCallCustomer = findViewById(R.id.tv_in_progress_call_customer);
        mPrint = findViewById(R.id.print);
        mProgressNO = findViewById(R.id.PBNewOrder);
        mPartner = findViewById(R.id.tv_in_progress_deliver_partner_name_PD);
        mNewOrderDetail = findViewById(R.id.rv_business_orders);
        mBtnAccept = findViewById(R.id.btn_accept_No);
        mBtnReject = findViewById(R.id.btn_reject_No);
        mDeliveryPartner = findViewById(R.id.layout_partner);
        mPrepareTime = findViewById(R.id.layout_prepare_time_tr);
        mNSVNewOrder = findViewById(R.id.nsv_newOrder);
        mBtnLayout = findViewById(R.id.ll_btns);
        mToolbar = findViewById(R.id.nav_bar_NO);
        edtBusinessNote = findViewById(R.id.edt_business_note);
        mCustName = findViewById(R.id.tv_in_progress_customer_name_PD);
        mPayStatus = findViewById(R.id.tv_pay_status);
        mTotalPayment = findViewById(R.id.tv_total_amount);
        mPrepareTimeTV = findViewById(R.id.time_prepare_tv);
        //Linear Layout
        mLLReady = findViewById(R.id.ll_ready_btns);
        mBtnReadyOrder = findViewById(R.id.btn_ready_order);
        ///RCV Totals
        mRVTotals = findViewById(R.id.rv_order_totals);
    }

    public void UpdateOrders() {
        //Setting Adapter
        AdapterBusinessOrders adapter;
        mNewOrderDetail.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterBusinessOrders(mOrders, NewOrder.this);
        mNewOrderDetail.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        mProgressNO.setVisibility(View.GONE);
        mNSVNewOrder.setVisibility(View.VISIBLE);
        mBtnLayout.setVisibility(View.VISIBLE);
    }

    private void getOrderDetails(String orderId) {
        Call<SubItems> call = RetrofitNetMan.getRestApiService().getOrderDetail(token, orderId);
        call.enqueue(new Callback<SubItems>() {
            @Override
            public void onResponse(@NotNull Call<SubItems> call, @NotNull Response<SubItems> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //List 1 (Orders Names)
                    mOrders = response.body().getData().subList(0, response.body().getData().size());

                    valueTotal = 0.0;
                    //Saving and calculating total amount of order
                    Log.e(TAG, "onResponse: Total Amount" + response.body().getData().size());
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        valueTotal = valueTotal + Double.parseDouble(response.body().getData().get(i).getItemPrice());
                    }
                    Log.e(TAG, "onResponse: Total Amount" + valueTotal);


                }
                else {
                    Toast.makeText(getApplicationContext(), "Token Expired", Toast.LENGTH_SHORT).show();
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

    //Setting Adapter
    @SuppressLint("SetTextI18n")
    private void UpdateUI(String bNote, String dNote
            , String fName, String lName, String pNo
            , String status, Double tPrice, String partner
            , String pickup, int isRider) {


        //Setting Name of Customer
        Log.e(TAG, "UpdateUI: " + fName + " " + lName );
        if (!fName.isEmpty() && !lName.isEmpty()) {
            String Name = fName + " " + lName;
            mCustName.setText(Name);
            Log.d("API", "Adding Data 2");

        }

        ////Checking for the order type
        if (isRider == 0) {
            mDeliveryPartner.setVisibility(View.GONE);
            mBtnReadyOrder.setText("Ready For Pickup");
        } else {
            if (mIsRiderAssigned == 0) {
                mDeliveryPartner.setVisibility(View.GONE);
            } else {
                mDeliveryPartner.setVisibility(View.VISIBLE);
            }
        }


        //Prepare Courier Time
        String[] minTime = pickup.split("T");
        Log.e(ContentValues.TAG, "onBindViewHolder: time 1 " + minTime[1]);
        Log.e(ContentValues.TAG, "onBindViewHolder: time 2 " + minTime[1].substring(0, minTime[1].length() - 8));

        Log.e(TAG, "UpdateUI: pick " + pickup);
        mPrepareTimeTV.setText(minTime[1].substring(0, minTime[1].length() - 8));

        ///Partner Name
        mPartner.setText(partner);

        ///Setting Phone Number
        if (!pNo.isEmpty()) {
            mCellNumber = pNo;
        }
        ///Setting Payment Status
        if (!status.isEmpty()) {
            if (status.trim().equals("1")) {
                mPayStatus.setText("Paid");
                mPayStatus.setBackground(getResources().getDrawable(R.drawable.paid_background));
            } else {
                mPayStatus.setText("Unpaid");
            }
        }
        //set business & delivery notes
        if (!bNote.isEmpty()) {
            edtBusinessNote.setText(bNote);
        } else {
            edtBusinessNote.setText("");
        }
        if (!dNote.isEmpty()) {

            if (dNote.trim().equals("Delivery")) {
//                mPaymentStatusLayout.setVisibility(View.GONE);
                if (dNote.trim().equals("Self-Delivery")) {
                    mETDeliveryNote.setVisibility(View.VISIBLE);
                    mTVDeliveryNote.setVisibility(View.VISIBLE);
                    mTVDeliveryNote.setText(dNote);
                }
            }
        } else {
            mTVDeliveryNote.setText("");
        }
        mTotalPayment.setText(Constants.CURRENCY_SIGN + " " + tPrice);

    }//UI

    public void totalFun() {
        mRVTotals.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        totalAdapter = new AdapterTotal(dummySubTotals(), NewOrder.this);
        mRVTotals.setAdapter(totalAdapter);
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


    ///Api link with id
    //method to get business orders from server
    private void getNewBusinessOrders(String orderId) {

        Call<NewOrderResponse> call = RetrofitNetMan.getRestApiService().getOrders(token, orderId);
        call.enqueue(new Callback<NewOrderResponse>() {
            @Override
            public void onResponse(@NotNull Call<NewOrderResponse> call, @NotNull Response<NewOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //collect data & update ui
                    try {
                        double orderTotal = 0;
                        int orderSize = response.body().getData().getOrderItems().size();
                        //Calculating Total Amount
                        for (int i = 0; i < orderSize; i++) {
                            orderTotal = orderTotal + Double.parseDouble(response.body().getData().getOrderItems().get(i).getItemPrice());
                        }

                        ///Inserting new order basic information data in database
                        Constants.ORDER_ITEM = new OrdersItem(
                                response.body().getData().getOrder().get(0).getPickuptime(),
                                String.valueOf(mRemainTime),
                                String.valueOf(currentTime),
                                "200",
                                response.body().getData().getOrder().get(0).getDateAdded(),
                                response.body().getData().getOrder().get(0).getMinPreTime(),
                                response.body().getData().getOrder().get(0).getMaxPreTime(),
                                response.body().getData().getOrder().get(0).getCourierNotes(),
                                response.body().getData().getOrder().get(0).getBusinessId(),
                                response.body().getData().getOrder().get(0).getId(),
                                "Pending",
                                response.body().getData().getOrder().get(0).getOrderType(),
                                response.body().getData().getOrder().get(0).getFirstName(),
                                response.body().getData().getOrder().get(0).getBusinessRevShare(),
                                response.body().getData().getOrder().get(0).getItemCount(),
                                response.body().getData().getOrder().get(0).getBusinessName(),
                                response.body().getData().getOrder().get(0).getBusinessNotes(),
                                response.body().getData().getOrder().get(0).getPaymentStatus(),
                                response.body().getData().getOrder().get(0).getLastName(),
                                response.body().getData().getOrder().get(0).getAction(),
                                response.body().getData().getOrder().get(0).getDateAdded(),
                                response.body().getData().getOrder().get(0).getPaymentType(),
                                response.body().getData().getOrder().get(0).getDelay(),
                                response.body().getData().getOrder().get(0).getDateModified(),
                                response.body().getData().getOrder().get(0).getPhoneNumber(),
                                response.body().getData().getOrder().get(0).getCustomerId(),
                                response.body().getData().getOrder().get(0).getBusinessId(),
                                response.body().getData().getOrder().get(0).getStatus()
                        );

                        mIsPickUp = response.body().getData().getOrder().get(0).getOrderType();

                        //Updating UI
                        UpdateUI(
                                response.body().getData().getOrder().get(0).getBusinessNotes(),
                                response.body().getData().getOrder().get(0).getCourierNotes(),
                                response.body().getData().getOrder().get(0).getFirstName(),
                                response.body().getData().getOrder().get(0).getLastName(),
                                response.body().getData().getOrder().get(0).getPhoneNumber(),
                                response.body().getData().getOrder().get(0).getStatus(),
                                orderTotal,
                                "Not Available",
                                response.body().getData().getOrder().get(0).getPickuptime(),
                                mIsPickUp
                        );

                    } catch (Exception ignored) {
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }//not null

            }

            @Override
            public void onFailure(@NotNull Call<NewOrderResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }//end get

    //Checking delivery
    public void checkStatusType(String type) {
        String name = "";
        switch (type) {
            case "0":
                name = "Pickup";
                break;
            case "1":
                name = "Delivery";
                break;
            case "2":
                name = "Delivery via Business";
                break;
            default: {
            }
        }

        Log.e("Status Value", type + " " + name + "" + str);
        if (!name.equals("Delivery")) {
            mPaymentStatusLayout.setVisibility(View.VISIBLE);
            if (name.equals("Delivery via Business")) {
                mTVDeliveryNote.setVisibility(View.VISIBLE);
                mETDeliveryNote.setVisibility(View.VISIBLE);
            }
        }
    }

    public String getOrderType() {
        return str;
    }

    //Setting Click listener to call customer usgin dial intent
    public void callFun() {
        mCallCustomer.setOnClickListener(v -> {
//            mCellNumber = "+923359010786";
            String uri = "tel:" + mCellNumber.trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        });
    }

}//end class

