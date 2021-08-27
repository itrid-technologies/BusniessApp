package com.itridtechnologies.resturantapp.UiViews.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.itridtechnologies.resturantapp.Adapters.AdapterAddonNames;
import com.itridtechnologies.resturantapp.Adapters.AdapterHistoryOrders;
import com.itridtechnologies.resturantapp.Adapters.AdapterHistorySubItems;
import com.itridtechnologies.resturantapp.Adapters.AdapterTotal;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.TotalModel;
import com.itridtechnologies.resturantapp.models.FeedbackReviewGiven.FeedbackReviewGivenResponse;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.AddonItemsItem;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.DataItem;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.HistOrderDetailResponse;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.OrderAddonsItem;
import com.itridtechnologies.resturantapp.models.historyNew.NewHistoryWithTotals;
import com.itridtechnologies.resturantapp.models.historyagain.HistResponse;
import com.itridtechnologies.resturantapp.models.newOrder.NewOrderResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Constants;
import com.itridtechnologies.resturantapp.utils.LogoutViaNotification;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryDetails extends AppCompatActivity {

    private static final String TAG = "HistoryDetails";

    //Declaring Variables
    private TextView mCustomerName;
    private TextView mAmmount;
    private TextView mOrderStatus;
    private ProgressBar mProgressHistDetails;
    private RecyclerView mRVHistoryDetail;
    private RecyclerView mRVTotals;
    private PreferencesManager pm;

    private double orderTotal = 0;
    //Reviews
    private TextView mCustomerReview;
    private TextView mCourierReview;

    private String pos;

    //Working Variables
    private Double mTotalValue = 0.00;
    private Double mTotalAmount = 0.00;

    //Order Details in Order History
    private List<DataItem> mOrderDetails = new ArrayList<>();

    private NestedScrollView mNSVHistDetails;
    private String or;
    private final String token = AppManager.getBusinessDetails().getData().getToken();
    private Toolbar mToolbar;

    List<TotalModel> list = new ArrayList<>();

    //Views For Reviews
    private View mTopView;
    private View mMiddleView;
    private View mBottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        pm = new PreferencesManager(this);
        mToolbar = findViewById(R.id.nav_bar_HD);
        //Hiding Status Bar with help of App Manager
        AppManager.hideStatusBar(this);

        LogoutViaNotification.logoutOnType();

    }

    @Override
    protected void onStart() {
        super.onStart();

        or = getIntent().getStringExtra("orderIdHis");
        Log.e(TAG, "onStart: id # " + or);
        pos = getIntent().getStringExtra("orderHisPos");

        toolbarFun();
        setVariables();

        Log.e(TAG, "onStart: order id history" + or);
        Log.e(TAG, "onStart: order id Position" + pos);

        getHistDetails(or);

        //setting listeners on Review Fields
        reviewListeners();

        //checking if feedback is given or not
        checkFeedbackGiven(or);

    }

    private void checkFeedbackGiven(String orderId) {
        Call<FeedbackReviewGivenResponse> call = RetrofitNetMan.getRestApiService().getFeedbackReviewGiven(token, orderId);
        call.enqueue(new Callback<FeedbackReviewGivenResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeedbackReviewGivenResponse> call, @NotNull Response<FeedbackReviewGivenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().getData().isCustomerReview()) {
                        mTopView.setVisibility(View.VISIBLE);
                        mMiddleView.setVisibility(View.VISIBLE);
                        mBottomView.setVisibility(View.VISIBLE);
                        mCustomerReview.setVisibility(View.VISIBLE);
                    }

                    if (!response.body().getData().isPartnerReview()) {
                        mTopView.setVisibility(View.VISIBLE);
                        mMiddleView.setVisibility(View.VISIBLE);
                        mBottomView.setVisibility(View.VISIBLE);
                        mCourierReview.setVisibility(View.VISIBLE);
                    }
                } else {
                    AppManager.SnackBar(HistoryDetails.this, response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeedbackReviewGivenResponse> call, @NotNull Throwable t) {
                AppManager.SnackBar(HistoryDetails.this, t.getMessage());
            }
        });
    }//checkFeedbackGiven

    private void reviewListeners() {
        mCustomerReview.setOnClickListener(v -> {
            list.clear();
            Intent intent = new Intent(HistoryDetails.this, FeedbackActivity.class);
            intent.putExtra("ORDER_ID", or);
            startActivity(intent);
        });

        mCourierReview.setOnClickListener(v -> {
            list.clear();
            Intent intent = new Intent(HistoryDetails.this, FeedbackActivity.class);
            intent.putExtra("ORDER_ID", or);
            startActivity(intent);
        });
    }//reviewListeners

    //Setting Variables
    public void setVariables() {
        mCustomerName = findViewById(R.id.tv_custname_hist);
        mAmmount = findViewById(R.id.tv_price_hist);
        mOrderStatus = findViewById(R.id.status_hist);
        mNSVHistDetails = findViewById(R.id.nsvHistoryDetails);
        mRVHistoryDetail = findViewById(R.id.rv_hist);
        mProgressHistDetails = findViewById(R.id.pb_hist_details);
        mRVTotals = findViewById(R.id.rv_totals_history);
        mCourierReview = findViewById(R.id.tv_review_for_courier);
        mCustomerReview = findViewById(R.id.tv_review_for_customer);

        //Views For Reviews
        mTopView = findViewById(R.id.viewTopReview);
        mMiddleView = findViewById(R.id.viewMiddleReview);
        mBottomView = findViewById(R.id.viewBottomReview);

    }


    @Override
    protected void onPause() {
        super.onPause();
        LogoutViaNotification.onPauseFun();
    }

    @SuppressLint("SetTextI18n")
    public void totalFun() {
        mRVTotals.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AdapterTotal totalAdapter = new AdapterTotal(list, HistoryDetails.this);
        mRVTotals.setAdapter(totalAdapter);
        totalAdapter.notifyDataSetChanged();
    }//end total function



    ///Api link with id
    //method to get business orders from server
    private void getNewBusinessOrders(String orderId) {

        Log.e(TAG, "onCreate: Order id inside API " + orderId);
        Call<NewOrderResponse> call = RetrofitNetMan.getRestApiService().getOrders(token, orderId);
        call.enqueue(new Callback<NewOrderResponse>() {
            @Override
            public void onResponse(@NotNull Call<NewOrderResponse> call, @NotNull Response<NewOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    //collect data & update ui
                    try {
                        Log.e(TAG, "onResponse: first and last name: " + response.body().getData().getOrder().get(0).getFirstName() +
                                response.body().getData().getOrder().get(0).getLastName());

                        if (response.body().getData().getOrderTotals().size() > 0)
                        {
                            //Calculating Total Amount
                            for (int i = 0; i < response.body().getData().getOrderTotals().size(); i++) {
                                list.add(new TotalModel(
                                        response.body().getData().getOrderTotals().get(i).getLabel(),
                                        response.body().getData().getOrderTotals().get(i).getValue()
                                ));
                                orderTotal = Double.parseDouble(response.body().getData().getOrderTotals().get(i).getValue());
                                Log.e(TAG, "onResponse: order total " + orderTotal);
                            }
                        }


                        totalFun();

                            //Updating History User interface
                            UpdateHistUI(
                                    response.body().getData().getOrder().get(0).getFirstName(),
                                    response.body().getData().getOrder().get(0).getLastName(),
                                    String.valueOf(orderTotal),
                                    response.body().getData().getOrder().get(0).getStatus()
                            );

                        ///Inserting new order basic information data in database
                        Log.e(TAG, "onResponse: Data inserting");
//                        Constants.ORDER_ITEM = new OrdersItem(
//                                response.body().getData().getOrder().get(0).getPickuptime(),
////                                String.valueOf(mRemainTime),
////                                String.valueOf(currentTime),
//                                "200",
//                                response.body().getData().getOrder().get(0).getDateAdded(),
//                                response.body().getData().getOrder().get(0).getMinPreTime(),
//                                response.body().getData().getOrder().get(0).getMaxPreTime(),
//                                response.body().getData().getOrder().get(0).getCourierNotes(),
//                                response.body().getData().getOrder().get(0).getBusinessId(),
//                                response.body().getData().getOrder().get(0).getId(),
//                                "Pending",
//                                response.body().getData().getOrder().get(0).getOrderType(),
//                                response.body().getData().getOrder().get(0).getFirstName(),
//                                response.body().getData().getOrder().get(0).getBusinessRevShare(),
//                                response.body().getData().getOrder().get(0).getItemCount(),
//                                response.body().getData().getOrder().get(0).getBusinessName(),
//                                response.body().getData().getOrder().get(0).getBusinessNotes(),
//                                response.body().getData().getOrder().get(0).getPaymentStatus(),
//                                response.body().getData().getOrder().get(0).getLastName(),
//                                response.body().getData().getOrder().get(0).getAction(),
//                                response.body().getData().getOrder().get(0).getDateAdded(),
//                                response.body().getData().getOrder().get(0).getPaymentType(),
//                                response.body().getData().getOrder().get(0).getDelay(),
//                                response.body().getData().getOrder().get(0).getDateModified(),
//                                response.body().getData().getOrder().get(0).getPhoneNumber(),
//                                response.body().getData().getOrder().get(0).getCustomerId(),
//                                response.body().getData().getOrder().get(0).getBusinessId(),
//                                response.body().getData().getOrder().get(0).getStatus()
//                        );
//

                    } catch (Exception ignred) {
                        Log.e(TAG, "onResponse: " + ignred.getMessage());
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }//not null

            }

            @Override
            public void onFailure(@NotNull Call<NewOrderResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }//end get

    ////Recieving Data From Server order details of history with addons etc
    public void getHistDetails(String id) {
        Call<HistOrderDetailResponse> call = RetrofitNetMan.getRestApiService().getHistOrderDetails(token, id);
        call.enqueue(new Callback<HistOrderDetailResponse>() {
            @Override
            public void onResponse(@NotNull Call<HistOrderDetailResponse> call, @NotNull Response<HistOrderDetailResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            mTotalAmount = mTotalAmount + Double.parseDouble(response.body().getData().get(i).getItemPrice());
                        }


                        Log.e(TAG, "onResponse: " + mTotalAmount);

                        mOrderDetails = response.body().getData().subList(0, response.body().getData().size());
                        UpdateItemNameList();
                        mProgressHistDetails.setVisibility(View.GONE);
                        mNSVHistDetails.setVisibility(View.VISIBLE);
                    }

                    getNewBusinessOrders(or);
//                    getDetails(pos);
                } else {
                    AppManager.SnackBar(HistoryDetails.this, response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<HistOrderDetailResponse> call, @NotNull Throwable t) {
                AppManager.toast("Server Error");
            }
        });
    }

    public void UpdateItemNameList() {
        //Setting Adapter
        AdapterHistoryOrders adapter;
        mRVHistoryDetail.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterHistoryOrders(mOrderDetails, HistoryDetails.this);
        mRVHistoryDetail.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Updating UI
    @SuppressLint("SetTextI18n")
    private void UpdateHistUI(String fCustName, String lCustName, String orderAmount, String status) {

        ///Name of Customer
        if (fCustName != null && lCustName != null) {
            String customerName = fCustName + " " + lCustName;
            mCustomerName.setText(customerName);
        }
        //Amount
        if (orderAmount != null) {
            Log.e(TAG, "UpdateHistUI: " + orderAmount);
            DecimalFormat format = new DecimalFormat("0.00");
            mAmmount.setText(Constants.CURRENCY_SIGN + " " + format.format(Double.parseDouble(orderAmount)));
        }
        //Status
        if (status != null) {
            mOrderStatus.setText(status);
        }
    }

    //Setting Toolbar
    public void toolbarFun() {

        mToolbar.setTitle(" # " + or);
        mToolbar.setNavigationOnClickListener(v -> AppManager.intent(BasicActvity.class));

        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_availability: {
                    Intent intent = new Intent(HistoryDetails.this, Menu.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.settings: {
                    Intent intent = new Intent(HistoryDetails.this, Settings.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.call_support: {
                    String mCellNumber = "+923359010786";
                    String uri = "tel:" + mCellNumber.trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                    return false;
                }
                case R.id.over_flow_log_out: {
                    Intent intent = new Intent(HistoryDetails.this, MainActivity.class);
                    pm.clearSharedPref();
                    pm.saveMyDataBool("login", false);
                    startActivity(intent);
                    return false;
                }
                default: {
                    Intent intent = new Intent(HistoryDetails.this, help.class);
                    startActivity(intent);
                    return false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrderDetails.clear();
        LogoutViaNotification.onResumeFun();
    }

}