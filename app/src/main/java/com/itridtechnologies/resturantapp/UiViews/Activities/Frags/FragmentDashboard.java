package com.itridtechnologies.resturantapp.UiViews.Activities.Frags;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.itridtechnologies.resturantapp.Adapters.RecyclerViewAdapterDashboard;
import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.MainActivity;
import com.itridtechnologies.resturantapp.UiViews.Activities.Menu;
import com.itridtechnologies.resturantapp.UiViews.Activities.NewOrder;
import com.itridtechnologies.resturantapp.UiViews.Activities.Settings;
import com.itridtechnologies.resturantapp.UiViews.Activities.help;
import com.itridtechnologies.resturantapp.Work.AllOrderWorker;
import com.itridtechnologies.resturantapp.Work.OrderWorker;
import com.itridtechnologies.resturantapp.models.ActionOrder.ActionResponse;
import com.itridtechnologies.resturantapp.models.BussinessHours.BusinessHours;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.Pagination.PaginationResponse;
import com.itridtechnologies.resturantapp.models.Setting.SettingResponse;
import com.itridtechnologies.resturantapp.models.UpdateSetting.UpdateSettingResponse;
import com.itridtechnologies.resturantapp.models.newOrder.NewOrderResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.notification.FCM;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Config;
import com.itridtechnologies.resturantapp.utils.Constants;
import com.itridtechnologies.resturantapp.utils.Internet;
import com.itridtechnologies.resturantapp.utils.NotificationsUtils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDashboard extends Fragment {

    //Variables here
    private RecyclerView mOrderRecyclerView; //Main RecyclerView
    private String mOrderId;
    private SwitchCompat mBusyMode;
    private ProgressBar mPBFull;
    private NestedScrollView mNSVDash;
    private LinearLayout mBusyNotify;
    private int busyStatus = 0;
    private TextView noOrders;
    private ImageView imgNoOrder;
    private int i;

    //Animation for busy card
    private Animation anim_in;
    private Animation anim_out;

    //Vibration
    private Vibrator mVibration;
    //Sound
    private MediaPlayer mMediaPlayer;

    private int internetButNoDatasbase = 0;
    //Adapter
    private RecyclerViewAdapterDashboard adapter;
    //autoaccept
    private int mAutoAccept;


    ///Variables for Opening and closing days and time
    private int mIsClosed = 0;
    private String mOpenTime = "N/A";
    private String mCloseTime = "N/A";
    private String mOpenDay = "N/A";
    private int mCurrentDay = 0;

    //Calneder instance
    Calendar calendar = Calendar.getInstance();

    //countdown time for order to stay alive
    private int mCountDown = 50000;
    private double mRemainTime = 50000.00;
    private String mSavingTime = "00:00:00";

    //work for background
    OneTimeWorkRequest bgWork;

    ///Room database
    RoomDB databaseRoom;
    //Order item
    private OrdersItem mOrderItem = null;

    //Declare timer
    CountDownTimer cTimer = null;

    ///list
    private List<OrdersItem> mNewPageOrderItemList = new ArrayList<>();

    //Receiving Broadcast for notifications
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private SwipeRefreshLayout mSwipeDash;
    //retrieve token from pref
    String token = AppManager.getBusinessDetails().getData().getToken();

    //List for Getting data from API (Pagination)
//    private List<OrdersItem> mOrders = new ArrayList<>();


    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: i m created");

        anim_in = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
        anim_out = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // myHelper = new MyHelper(requireContext());
        mOrderRecyclerView = root.findViewById(R.id.recycler_view_dashboard);
        mBusyMode = root.findViewById(R.id.busy_mode_switch_dashboard);
        //setting name of toolbar
        Toolbar mToolbar;
        mPBFull = root.findViewById(R.id.pb_dash_center);
        mToolbar = root.findViewById(R.id.action_bar_dashboard);
        mNSVDash = root.findViewById(R.id.nsv_dashboard);

        //Busy Notification
        mBusyNotify = root.findViewById(R.id.busy_notification);

        ///Header Name
        mToolbar.setTitle("New Orders");

        Log.e(TAG, "onCreateView:  i m onCreateView");

        //Context for Room
        //initializing database
        databaseRoom = RoomDB.getInstance(requireContext());

        //getting current time from system
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        mSavingTime = mdformat.format(calendar.getTime());

        Log.e(TAG, "onCreateView: time" + mSavingTime);

        ///Checking AutoAccept
        autoAcceptApi();


        //Broadcasted order id Recieving from notification

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    Log.e(TAG, "onCreate: " + FCM.deviceToken.getValue());

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    mOrderId = intent.getStringExtra("idByNotification");
                    Log.e(TAG, "onReceive: Order id " + mOrderId);

                    //Split
                    String[] range = mOrderId.split("=");
                    String initialId = range[1];
                    //creating a constructor of StringBuffer class
                    StringBuffer id = new StringBuffer(initialId);
                    //invoking the method
                    id.deleteCharAt(id.length() - 1);
                    //prints the string after deleting the character
                    Log.e(TAG, "onReceive: id final " + id);

                    getNewBusinessOrders(String.valueOf(id));
                }
            }
        };

        //Swipe Layout
        mSwipeDash = root.findViewById(R.id.srl_dash);
        mNSVDash.setVisibility(View.GONE);

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
                default: {
                    Intent intent = new Intent(getContext(), help.class);
                    startActivity(intent);
                    return false;
                }
            }
        });

        mOrderRecyclerView.setVisibility(View.VISIBLE);
        //returning root
        return root;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //vibration context
        mVibration = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);

        i = 0;
        ///Variables when no orders
        ///When No Orders
        noOrders = view.findViewById(R.id.tv_no_order);
        imgNoOrder = view.findViewById(R.id.ic_noOrder);

        ////Setting no orders
        openCloseFun();

        //Sound
        mMediaPlayer = MediaPlayer.create(requireContext(), R.raw.tune);

        mSwipeDash.setProgressViewOffset(true, 10, 180);
        //Pull to Swipe
        mSwipeDash.setOnRefreshListener(() -> {
            final Handler handler = new Handler();
            handler.postDelayed(() -> mSwipeDash.setRefreshing(false), 2000);
            getFragmentManager().beginTransaction().detach(FragmentDashboard.this)
                    .attach(FragmentDashboard.this).commit();
        });
        getBusyMode();

        ///PickUp Orders
        mBusyMode.setOnClickListener(v -> {
            busyMode();
        });

        ///First Time (Internet Available No Database)
        if (Internet.isAvailable(requireContext())) {
            internetButNoDatasbase = 1;
            Log.e(TAG, "onResponse: internet but No database available");
            //net available but no database
            getOrdersViaState(token);
            mNSVDash.setVisibility(View.VISIBLE);
            mPBFull.setVisibility(View.GONE);
        }
        //(Internet Available With Database)
        else if (Internet.isAvailable(requireContext()) && !databaseRoom.mainDao().getAll().isEmpty()) {
            Log.e(TAG, "onResponse: internet with database available");
            ///get data from DB
            //if work is succeceded then we get data from DB
            final List<OrdersItem> orders = databaseRoom.mainDao().getAll();
            if (!orders.isEmpty()) {
                Log.e(TAG, "onResponse: List in database");
                setUpRecView(orders);
            } else {
                Log.e(TAG, "onResponse: No list in Database");
            }
            mPBFull.setVisibility(View.GONE);
            mNSVDash.setVisibility(View.VISIBLE);
            noOrders.setVisibility(View.GONE);
            imgNoOrder.setVisibility(View.GONE);
        }
        ///(No Internet Available)
        else if (!Internet.isAvailable(requireContext())) {
            Log.e(TAG, "onResponse: No internet No database available");

            ////no net no database
            ////no net no daadpttabase
            noOrders.setText("Internet is not available");
            noOrders.setVisibility(View.VISIBLE);
            imgNoOrder.setVisibility(View.VISIBLE);
            mPBFull.setVisibility(View.GONE);
            mNSVDash.setVisibility(View.VISIBLE);

        }
//        /(No Internet Available But Database Available)
        else if (!Internet.isAvailable(requireContext()) && !databaseRoom.mainDao().getAll().isEmpty()) {
            ///get data from DB
            Log.e(TAG, "onResponse: No internet but database available");
            //if work is succeceded then we get data from DB
            final List<OrdersItem> orders = databaseRoom.mainDao().getAll();
            if (!orders.isEmpty()) {
                Log.e(TAG, "onResponse: List in database");
                setUpRecView(orders);
            } else {
                Log.e(TAG, "onResponse: No list in Database");
            }
            mPBFull.setVisibility(View.GONE);
            noOrders.setVisibility(View.GONE);
            imgNoOrder.setVisibility(View.GONE);
            mNSVDash.setVisibility(View.VISIBLE);
        }

        subscribeToTest();
    }


    private void subscribeToTest() {
        FirebaseMessaging.getInstance().subscribeToTopic("test")
                .addOnCompleteListener(task -> {
                });
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    ///Checking current day
    public int currentDay() {

        //current day
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            //monday
            case Calendar.MONDAY: {
                mCurrentDay = 0;
                break;
            }
            //tuesday
            case Calendar.TUESDAY: {
                mCurrentDay = 1;
                break;
            }
            //wednesday
            case Calendar.WEDNESDAY: {
                mCurrentDay = 2;
                break;
            }
            //thursday
            case Calendar.THURSDAY: {
                mCurrentDay = 3;
                break;
            }
            //friday
            case Calendar.FRIDAY: {
                mCurrentDay = 4;
                break;
            }
            //saturday
            case Calendar.SATURDAY: {
                mCurrentDay = 5;
                break;
            }
            //sunday
            case Calendar.SUNDAY: {
                mCurrentDay = 6;
                break;
            }
        }
        Log.e(TAG, "currentDay: " + mCurrentDay);
        return mCurrentDay;
    }

    //If auto-accept is on send in processing directly
    private void autoAcceptApi() {
        Call<SettingResponse> call = RetrofitNetMan.getRestApiService().getSettingDetails(token);
        call.enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(@NotNull Call<SettingResponse> call, @NotNull Response<SettingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mAutoAccept = response.body().getMessage().get(0).getAutoAcceptOrderMode();
                    Log.e(TAG, "onResponse: " + mAutoAccept);
                }
            }

            @Override
            public void onFailure(@NotNull Call<SettingResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    //If No order is on screen
    private void openCloseFun() {
        Call<BusinessHours> call = RetrofitNetMan.getRestApiService().getHours(token);
        call.enqueue(new Callback<BusinessHours>() {
            @Override
            public void onResponse(@NotNull Call<BusinessHours> call, @NotNull Response<BusinessHours> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int nextday = currentDay();
                    nextday = nextday + 1;
                    if (nextday > 6) {
                        nextday = 0;
                    }
                    Log.e(TAG, "onResponse: Next day " + nextday);
                    try {
                        mIsClosed = response.body().getData().getBusinessHours().get(currentDay()).getIsClosed();
                        mOpenTime = response.body().getData().getBusinessHours().get(nextday).getOpeningTime();
                        mCloseTime = response.body().getData().getBusinessHours().get(currentDay()).getClosingTime();
                        mOpenDay = response.body().getData().getBusinessHours().get(nextday).getDay();
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.getMessage());
                    }

                    Log.e(TAG, "onResponse: " + mIsClosed + mOpenTime + mCloseTime);

                    //Passing values to function and setting layout
                    setTimeLayout(mIsClosed, mOpenTime, mCloseTime, mOpenDay);

                } else {
                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(@NotNull Call<BusinessHours> call, @NotNull Throwable t) {
                AppManager.toast(t.getMessage());
            }
        });
    }

    private void setTimeLayout(int isClosed, String openTimeWithSecond, String closeTimeWithSecond, String day) {

        String openTime = openTimeWithSecond;
        String closeTime = closeTimeWithSecond;

        if (closeTime.equals("N/A") && openTime.equals("N/A")) {
            String msg = "Business Hours are not Assigned";
            noOrders.setText(msg);
        } else {
            Log.e(TAG, "setTimeLayout: " + openTime.substring(0, openTime.length() - 3) + " close " + closeTime);

            String closeMsg = "Closed - Opening " + day + " " + openTime.substring(0, openTime.length() - 3) + " AM";
            String openMsg = "Accepting Orders till " + closeTime.substring(0, closeTime.length() - 3) + " PM";

            if (isClosed == 0) {
                noOrders.setText(closeMsg);
            } else {
                noOrders.setText(openMsg);
            }
        }

    }

    ////Send to preparing order if autoaccept is on
    private void autoPreparing(String orderId) {

        //Creating Json Object to post Action API
        JsonObject obj = new JsonObject();
        obj.addProperty("id", orderId);
        obj.addProperty("action", "1");

        Call<ActionResponse> caall = RetrofitNetMan.getRestApiService().actionOfOrder(token, obj);
        caall.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(@NotNull Call<ActionResponse> caall, @NotNull Response<ActionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppManager.saveActionDetails(response.body());
                } else {
                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ActionResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }//end order

    ///Api link with id
    //method to get business orders from server
    private void getNewBusinessOrders(String orderId) {

        Call<NewOrderResponse> call = RetrofitNetMan.getRestApiService().getOrders(token, orderId);
        call.enqueue(new Callback<NewOrderResponse>() {
            @Override
            public void onResponse(@NotNull Call<NewOrderResponse> call, @NotNull Response<NewOrderResponse> response) {

                try {
                    mNewPageOrderItemList.add(new OrdersItem(
                            response.body().getData().getOrder().get(0).getPickuptime(),
                            String.valueOf(mRemainTime),
                            String.valueOf(mSavingTime),
                            "200",
                            response.body().getData().getOrder().get(0).getDateAdded(),
                            response.body().getData().getOrder().get(0).getMinPreTime(),
                            response.body().getData().getOrder().get(0).getMaxPreTime(),
                            response.body().getData().getOrder().get(0).getCourierNotes(),
                            response.body().getData().getOrder().get(0).getBusinessId(),
                            response.body().getData().getOrder().get(0).getId(),
                            "Active",
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
                    ));


                    bgWork = new OneTimeWorkRequest.Builder(OrderWorker.class)
                            .build();
                    WorkManager.getInstance(requireContext()).enqueue(bgWork);

                    WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(bgWork.getId())
                            .observe(getViewLifecycleOwner(), info -> {
                                if (info != null && info.getState().isFinished()) {
                                    Log.e(Oscillator.TAG, "onResponse: Information is returning on live data");
                                    //if work is succeceded then we get data from DB
                                    final List<OrdersItem> orders = databaseRoom.mainDao().getAll();
                                    if (!orders.isEmpty()) {
                                        Log.e(Oscillator.TAG, "onResponse: List in database");
                                        setUpRecView(mNewPageOrderItemList);
                                    } else {
                                        Log.e(Oscillator.TAG, "onResponse: No list in Database");
                                        noOrders.setVisibility(View.VISIBLE);
                                        imgNoOrder.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Log.e(Oscillator.TAG, "onResponse: no info returning from live data");
                                }
                            });

                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }

                Log.e(TAG, "onResponse: get order from notifiication" + response.message());

            }

            @Override
            public void onFailure(@NotNull Call<NewOrderResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        if (mAutoAccept == 0) {
            noOrders.setVisibility(View.GONE);
            imgNoOrder.setVisibility(View.GONE);
            getOrdersViaState(token);
        } else if (mAutoAccept == 1) {
            autoPreparing(orderId);
        }
    }//end get

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push id receiver
        // by doing this, the activity will be notified each time a new id arrives
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationsUtils.clearNotifications(requireContext());

    }

    private void getOrdersViaState(String token) {
        Call<PaginationResponse> call = RetrofitNetMan.getRestApiService().getPaginationPendingOrders(token);
        call.enqueue(new Callback<PaginationResponse>() {
            @Override
            public void onResponse(@NotNull Call<PaginationResponse> call, @NotNull Response<PaginationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Log.e(TAG, "onResponse: " + response.body().getMessage());
                    //collect data & update ui
                    mNewPageOrderItemList = response.body().getData().getOrders();
                    try {
                        if (mNewPageOrderItemList.isEmpty() || response.body().getMessage().equals("No records found")) {
                            Log.e(TAG, "onResponse: here 1");
                            noOrders.setVisibility(View.VISIBLE);
                            imgNoOrder.setVisibility(View.VISIBLE);

                        } else {
                            Log.e(TAG, "onResponse: here");

                            ///Inserting data in database
                            Constants.ORDER_PAGE_LIST = response.body().getData().getOrders();
                            Log.e(Oscillator.TAG, "onResponse: Processing" + Constants.ORDER_PAGE_LIST);

                            ///Saving in database
                            //////For Order Items (Saving in Database---Will execute one time)
                            bgWork = new OneTimeWorkRequest.Builder(AllOrderWorker.class)
                                    .build();
                            WorkManager.getInstance(requireContext()).enqueue(bgWork);

                            ///Getting live data from database
                            WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(bgWork.getId())
                                    .observe(getViewLifecycleOwner(), info -> {
                                        if (info != null && info.getState().isFinished()) {

                                            Log.e(Oscillator.TAG, "onResponse: Information is returning on live data");
                                            //if work is succeceded then we get data from DB
                                            final List<OrdersItem> orders = databaseRoom.mainDao().getAll();
                                            if (!orders.isEmpty()) {
                                                Log.e(Oscillator.TAG, "onResponse: List in database");
                                                setUpRecView(mNewPageOrderItemList);
                                            } else {
                                                Log.e(Oscillator.TAG, "onResponse: No list in Database");
                                                noOrders.setVisibility(View.VISIBLE);
                                                imgNoOrder.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            Log.e(Oscillator.TAG, "onResponse: no info returning from live data");
                                        }
                                    });

                            setUpRecView(mNewPageOrderItemList);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: iii" + e.getMessage());
                        noOrders.setVisibility(View.VISIBLE);
                        imgNoOrder.setVisibility(View.VISIBLE);
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

    //Recycler View
    private void setUpRecView(List<OrdersItem> paginationOrders) {

        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext()));
        adapter = new RecyclerViewAdapterDashboard(requireContext().getApplicationContext(), paginationOrders);
        mOrderRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, type) -> {
            if (type.equals("item_click")) {
                mMediaPlayer.stop();
                Intent intent = new Intent(requireContext(), NewOrder.class);
                Log.e(TAG, "setUpRecView: " + paginationOrders.get(position).getId());
                intent.putExtra("orderId", String.valueOf(paginationOrders.get(position).getId()));
                intent.putExtra("detailType", "newOrder");
                startActivity(intent);
            }
        });

        if (internetButNoDatasbase == 0) {


            //getting current time from system
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            String currentTime = mdformat.format(calendar.getTime());

            Log.e(TAG, "onCreateView: time 222" + currentTime);

            String savingHours;
            String savingMinutes;
            String savingSeconds;
            double savingTimeInSeconds;

            String currentHours;
            String currentMinutes;
            String currentSeconds;
            double currentTimeInSeconds;


            //Split
            String[] currentRange = currentTime.split(":");

            Log.e(TAG, "setUpRecView: currentrange Size" + currentRange.length);

            currentHours = currentRange[0];
            currentMinutes = currentRange[1];
            currentSeconds = currentRange[2];

            currentTimeInSeconds = Double.parseDouble(currentHours) * 60 * 60; //hours converted in seconds
            currentTimeInSeconds = currentTimeInSeconds + Double.parseDouble(currentMinutes) * 60; //Minutes converted in seconds
            currentTimeInSeconds = currentTimeInSeconds + Double.parseDouble(currentSeconds); //full time converted in seconds

            Log.e(TAG, "setUpRecView: current time in seconds " + currentTimeInSeconds);


            String remainingSeconds = "0";

            //loop to check and get remaining time
            Log.e(TAG, "setUpRecView: pagination (order list) " + paginationOrders.size());
            for (int i = 0; i <= paginationOrders.size(); i++) {
                remainingSeconds = paginationOrders.get(i).getRemainingtime();
                Log.e(TAG, "setUpRecView: Remainging Seconds " + remainingSeconds);
                Log.e(TAG, "setUpRecView: Remainging Seconds " + paginationOrders.get(i).getSavingtime());

                //Split
                String[] range = paginationOrders.get(i).getSavingtime().split(":");
                savingHours = range[0];
                savingMinutes = range[1];
                savingSeconds = range[2];

                savingTimeInSeconds = Double.parseDouble(savingHours) * 60 * 60; //hours converted in seconds
                savingTimeInSeconds = savingTimeInSeconds + Double.parseDouble(savingMinutes) * 60; //Minutes converted in seconds
                savingTimeInSeconds = savingTimeInSeconds + Double.parseDouble(savingSeconds); //full time converted in seconds

                Log.e(TAG, "setUpRecView: savingTimeInSeconds time in seconds " + savingTimeInSeconds);

                int remain = Integer.parseInt(String.valueOf(currentTimeInSeconds)) - Integer.parseInt(String.valueOf(savingTimeInSeconds));

                Log.e(TAG, "setUpRecView: current time - savong time " + remain);
                if (remain > 0) {
                    mCountDown = remain;
                    timer();
                } else {
                    if (mNewPageOrderItemList != null) {
                        Log.e(TAG, "onconditioncheck: " + mNewPageOrderItemList);
                        for (OrdersItem o : mNewPageOrderItemList) {
                            //update data in database
                            updateOrderStateOnServer(o);
                        }//For Each Loop
                    }//if (!mNewPageOrderItemList.isEmpty())
                    else {
                        Log.e(TAG, "onconditioncheck: Face changed with no list");
                    }
                }

            }
        }//internetButNoDatasbase
        else {
            timer();
        }

    }//end setup

    public void getBusyMode() {
        ///Getting Data From Server
        Call<SettingResponse> call = RetrofitNetMan.getRestApiService().getSettingDetails(token);
        call.enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(@NotNull Call<SettingResponse> call, @NotNull Response<SettingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setBusy(response.body().getMessage().get(0).getBusyMode());
                } else {
                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(@NotNull Call<SettingResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void busyMode() {

        ////Setting (PUT) Data on Server
        JsonObject obj = new JsonObject();

        //Changing Value for Switches
        if (busyStatus == 0) {
            obj.addProperty("mode", "1");
            obj.addProperty("status", "1");
        } else {
            obj.addProperty("mode", "1");
            obj.addProperty("status", "0");
        }


        Call<UpdateSettingResponse> callUpdate = RetrofitNetMan.getRestApiService().setSetting(token, obj);
        callUpdate.enqueue(new Callback<UpdateSettingResponse>() {
            @Override
            public void onResponse(@NotNull Call<UpdateSettingResponse> call1, @NotNull Response<UpdateSettingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {


                    if (mBusyMode.isChecked()) {


                        anim_in.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mBusyNotify.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        mBusyNotify.startAnimation(anim_in);

                    } else if (!mBusyMode.isChecked()) {


                        anim_out.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mBusyNotify.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        mBusyNotify.startAnimation(anim_out);

                    }
                    Log.e(TAG, "onResponse: " + response.message());


                } else if (response.code() == 400) {
                    if (mBusyMode.isChecked()) {



                        anim_in.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mBusyNotify.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        mBusyNotify.startAnimation(anim_in);



                    } else if (!mBusyMode.isChecked()) {



                        anim_out.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mBusyNotify.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        mBusyNotify.startAnimation(anim_out);
                    }
                    AppManager.toast(response.message());
                    Log.e(TAG, "onResponse: " + response.message());
                } else if (!response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                } else {
                    AppManager.toast("Delivery or Pickup order mode must be enable. " + response.message());
                    mBusyMode.setChecked(false);

                    anim_out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mBusyNotify.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    mBusyNotify.startAnimation(anim_out);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UpdateSettingResponse> call1, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                AppManager.toast(t.getMessage());
            }
        });
    }

    //Function to Set Busy Mode
    private void setBusy(int busy) {
        busyStatus = busy;
        ///Setting delivery Switch
        if (busyStatus == 1) {
            mBusyMode.setChecked(true);



            anim_in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBusyNotify.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            mBusyNotify.startAnimation(anim_in);

        } else if (busyStatus == 0) {
            mBusyMode.setChecked(false);

            anim_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBusyNotify.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            mBusyNotify.startAnimation(anim_out);
        }

    }

    private void timer() {

        if (!mNewPageOrderItemList.isEmpty()) {
            for (OrdersItem o : mNewPageOrderItemList) {
                Log.e(TAG, "timer: i for loop" + i);
                //Timer to Make Status Expire
                cTimer = new CountDownTimer(mCountDown, 1000) {
                    public void onTick(long millisUntilFinished) {
                        mRemainTime = millisUntilFinished;
                        Log.e(TAG, "onTick: Remaining Time " + mRemainTime);
                        //Virbration Starts

                        // Vibration
                        // Checking version
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mVibration.vibrate(VibrationEffect.createOneShot(millisUntilFinished, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26 (Before Oreo)
                            mVibration.vibrate(millisUntilFinished);
                        }
                        //Starting tune
                        //Sound Starts
                        mMediaPlayer.start();
                        Log.e(TAG, "onTick: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        Log.e(TAG, "onFinish: Done");
//                    mListener.onItemClick(position,"time_out");
                        Log.e(TAG, "timer: onfinish i " + i);
                        //Vibration and Sound Stops
                        mVibration.cancel();
                        mMediaPlayer.stop();
                        //Hit Expires Order API
                        updateOrderStateOnServer(o);
                        ///Object for database
                        mOrderItem = getOrderFromDB(String.valueOf(o.getId()));
                    }
                }.start();
            }//For Each Loop
        }//if (!mNewPageOrderItemList.isEmpty())
    }//Timer

    ///Getting order details
    private OrdersItem getOrderFromDB(String or) {
        return databaseRoom.mainDao().getOrderById(Integer.parseInt(or));
    }

    private void updateOrderStateOnServer(OrdersItem o) {

        JsonObject obj = new JsonObject();
        String id = String.valueOf(o.getId());
        obj.addProperty("id", id);
        obj.addProperty("action", "2");

        Call<ActionResponse> caall = RetrofitNetMan.getRestApiService().actionOfOrder(token, obj);
        caall.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(@NotNull Call<ActionResponse> caall, @NotNull Response<ActionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        mNewPageOrderItemList.remove(o);
                        adapter.notifyDataSetChanged();

                        try {
                            //Update Database
                            ///Inserting new order basic information data in database
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
                                    "Expired",
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
                            WorkManager.getInstance(requireContext()).enqueue(bgWork);

                            WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(bgWork.getId())
                                    .observe(getViewLifecycleOwner(), info -> {
                                        if (info != null && info.getState().isFinished()) {
                                            Log.e(Oscillator.TAG, "onResponse: Information is returning on live data");
                                            //if work is succeceded then we get data from DB
                                            final List<OrdersItem> orders = databaseRoom.mainDao().getAll();
                                            if (!orders.isEmpty()) {
                                                Log.e(Oscillator.TAG, "onResponse: List in database");
                                                setUpRecView(mNewPageOrderItemList);
                                            } else {
                                                Log.e(Oscillator.TAG, "onResponse: No list in Database");
                                                noOrders.setVisibility(View.VISIBLE);
                                                imgNoOrder.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            Log.e(Oscillator.TAG, "onResponse: no info returning from live data");
                                        }
                                    });
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
            public void onFailure(@NotNull Call<ActionResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }//end update

    //saving in room database with state active
    private void saveActiveOrders() {

        try {
            //Update Database
            ///Inserting new order basic information data in database
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
                    "Active",
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
            WorkManager.getInstance(requireContext()).enqueue(bgWork);

            WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(bgWork.getId())
                    .observe(getViewLifecycleOwner(), info -> {
                        if (info != null && info.getState().isFinished()) {
                            Log.e(Oscillator.TAG, "onResponse: Information is returning on live data");
                            //if work is succeceded then we get data from DB
                            final List<OrdersItem> orders = databaseRoom.mainDao().getAll();
                            if (!orders.isEmpty()) {
                                Log.e(Oscillator.TAG, "onResponse: List in database");
                                setUpRecView(mNewPageOrderItemList);
                            } else {
                                Log.e(Oscillator.TAG, "onResponse: No list in Database");
                                noOrders.setVisibility(View.VISIBLE);
                                imgNoOrder.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e(Oscillator.TAG, "onResponse: no info returning from live data");
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "onResponse: " + e.getMessage());
        }
    }//saveActiveOrders

    @Override
    public void onDetach() {
        super.onDetach();
        //Vibration and Sound Stops
        mVibration.cancel();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Vibration and Sound Stops
        mVibration.cancel();
        Log.e(TAG, "onStop: i m stopped");
        if (mNewPageOrderItemList != null) {
            Log.e(TAG, "onStop: " + mNewPageOrderItemList);
            for (OrdersItem o : mNewPageOrderItemList) {
                //update data in database
                saveActiveOrders();
            }//For Each Loop
        }//if (!mNewPageOrderItemList.isEmpty())
        else {
            Log.e(TAG, "onStop: Face changed with no list");
        }
    }//super.onStop();

}