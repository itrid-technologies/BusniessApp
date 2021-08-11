package com.itridtechnologies.resturantapp.UiViews.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.UiModeManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.Setting.SettingResponse;
import com.itridtechnologies.resturantapp.models.UpdateSetting.UpdateSettingResponse;
import com.itridtechnologies.resturantapp.models.orderFeesSetting.FeeUpdateResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends AppCompatActivity {

    private TextView mAcceptOrder;
    private TextView mRejectOrder;
    private ImageButton mAddAcceptOrder;
    private ImageButton mAddRejectOrder;
    private ImageButton mSubAcceptOrder;
    private ImageButton mSubRejectOrder;
    private SwitchCompat mDarkmode;
    private SwitchCompat mDeliverOrders;
    private SwitchCompat mNewOrders;
    private SwitchCompat mPickupOrders;
    private TextView mBussinessName;
    private TextView mTestReciept;
    private TableRow mTRDarkMode;
    private ProgressBar mPBSetting;
    private NestedScrollView mNSVSetting;
    ///Variables for controlling Switches
    private int pickUpStatus;
    private int autoAcceptStatus;
    private int deliveryStatus;
    private int mAcceptNumber = 1;
    private int mRejectNumber = 0;
    ////Edit Texts in case of delivery type = 2
    private EditText delFee;
    private View delFeeView;
    private View minOrderView;
    private EditText minOrder;
    private ConstraintLayout saveBtn;
    /////
    private int deliveryType;
    private Toolbar mToolbar;
    private String key;             //key variable to pass in preference manager to store data
    private PreferencesManager pm;  //Shared Preference Manager for storing boolean value for not logging in again and again

    private static final String TAG = "Settings";

    ////Progress Bar in Save Button
    private TextView mSaveTv;
    private ProgressBar mPbSave;

    //Increment Degrement value
    private String mAcceptOrderCopies = "1";
    private String mRejectOrderCopies;

    ///Swipe Refresh Layout
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pm = new PreferencesManager(this);
        AppManager.hideStatusBar(Settings.this);
        setVariables();
        toolbar();
        darkMode();

        //getting value of increment decrement from preference
        mAcceptOrderCopies = pm.getMyDataString("acceptedCopies");
        mRejectOrderCopies = pm.getMyDataString("rejectedCopies");


        mRejectOrder.setText(mRejectOrderCopies);
        mAcceptOrder.setText(mAcceptOrderCopies);

        setting();
//        mAcceptNumber = Integer.parseInt(mAcceptOrder.getText().toString().trim());
//        mRejectNumber = Integer.parseInt(mRejectOrder.getText().toString().trim());
        incDecReciepts();
    }

    private void darkMode() {
        //getting and dark switch
        //dark on off
        boolean darkLight = pm.getMyDataBool("darkSwitch");

        if (darkLight) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            mDarkmode.setChecked(true);
        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mDarkmode.setChecked(false);
        }

        mDarkmode.setOnClickListener(v -> {
            if (mDarkmode.isChecked()) {
                Log.e(TAG, "onCheckedChanged: dark isChecked");
                mDarkmode.setChecked(true);
                pm.saveMyDataBool("darkSwitch", true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                Log.e(TAG, "onCheckedChanged: light + isNotChecked" );
                mDarkmode.setChecked(false);
                pm.saveMyDataBool("darkSwitch", false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        ///For printing testing
        testPrint();

        mDeliverOrders.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mDeliverOrders.isChecked()) {
                delFee.setVisibility(View.VISIBLE);
                delFeeView.setVisibility(View.VISIBLE);
                minOrderView.setVisibility(View.VISIBLE);
                minOrder.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
            } else {
                delFee.setVisibility(View.GONE);
                delFeeView.setVisibility(View.GONE);
                minOrderView.setVisibility(View.GONE);
                minOrder.setVisibility(View.GONE);
                saveBtn.setVisibility(View.GONE);
            }
        });

        mSwipeRefreshLayout.setProgressViewOffset(true, 10, 180);
        //Pull to Swipe
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            setting();
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                mSwipeRefreshLayout.setRefreshing(false);
            }, 2000);

        });
    }

    ///Test Receipt Print
    public void testPrint() {
        mTestReciept.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, Reciept.class);
            intent.putExtra("isTest", "1");
            startActivity(intent);
        });
    }

    ///Hitting Api to set Switches
    private void setting() {
        ///Retriving token
        String token = AppManager.getBusinessDetails().getData().getToken();


        //Hiding View And Showing Progress Bar
        mPBSetting.setVisibility(View.VISIBLE);
        mNSVSetting.setVisibility(View.GONE);

        ///Getting Data From Api
        Call<SettingResponse> call = RetrofitNetMan.getRestApiService().getSettingDetails(token);
        call.enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(@NotNull Call<SettingResponse> call, @NotNull Response<SettingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setSwitches(
                            response.body().getMessage().get(0).getDeliveryOrderMode(),
                            response.body().getMessage().get(0).getPickupOrderMode(),
                            response.body().getMessage().get(0).getAutoAcceptOrderMode(),
                            response.body().getMessage().get(0).getDeliveryFee(),
                            response.body().getMessage().get(0).getMinOrder()
                    );
                    deliveryType = response.body().getMessage().get(0).getDeliveryType();
                }


                if (deliveryType == 1 && mDeliverOrders.isChecked()) {
                    delFee.setVisibility(View.VISIBLE);
                    delFeeView.setVisibility(View.VISIBLE);
                    minOrderView.setVisibility(View.VISIBLE);
                    minOrder.setVisibility(View.VISIBLE);
                    saveBtn.setVisibility(View.VISIBLE);
                }

                //Saving Data On Server
                JsonObject delFeeObj = new JsonObject();


                // check Fields For Empty Values
                TextWatcher mTextWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        // check Fields For Empty Values
                        checkFieldsForEmptyValues();
                    }
                };

                // set listeners
                delFee.addTextChangedListener(mTextWatcher);
                minOrder.addTextChangedListener(mTextWatcher);
                checkFieldsForEmptyValues();
                saveBtn.setOnClickListener(v -> {

                    delFeeObj.addProperty("deliveryFee", delFee.getText().toString().trim());
                    delFeeObj.addProperty("minOrderPrice", minOrder.getText().toString().trim());

                    mSaveTv.setVisibility(View.GONE);
                    mPbSave.setVisibility(View.VISIBLE);
                    Call<FeeUpdateResponse> feeUpdate = RetrofitNetMan.getRestApiService().setFeeAndOrders(token, delFeeObj);
                    feeUpdate.enqueue(new Callback<FeeUpdateResponse>() {
                        @Override
                        public void onResponse(Call<FeeUpdateResponse> call12, Response<FeeUpdateResponse> response1) {
                            if (response.isSuccessful() && response.body() != null) {

                            } else if (response.code() == 400) {
                                Log.e("TAG", "onResponse: " + response.message());
                            }
                            mSaveTv.setVisibility(View.VISIBLE);
                            mPbSave.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<FeeUpdateResponse> call12, Throwable t) {
                            Log.e("TAG", "onResponse: " + t.getMessage());
                        }
                    });
                });

                //Showing View And Hiding Progress Bar
                mPBSetting.setVisibility(View.GONE);
                mNSVSetting.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<SettingResponse> call, @NotNull Throwable t) {
                AppManager.SnackBar(Settings.this, t.getMessage());
            }
        });

        ///// Setting (PUT) data on server
        JsonObject obj = new JsonObject();

        ///Delivery Orders
        mDeliverOrders.setOnClickListener(v -> {
            //Changing Value for Switches
            if (deliveryStatus == 0) {
                obj.addProperty("mode", "2");
                obj.addProperty("status", "1");
            } else {
                obj.addProperty("mode", "2");
                obj.addProperty("status", "0");
            }


            Call<UpdateSettingResponse> callUpdate = RetrofitNetMan.getRestApiService().setSetting(token, obj);
            callUpdate.enqueue(new Callback<UpdateSettingResponse>() {
                @Override
                public void onResponse(@NotNull Call<UpdateSettingResponse> call1, @NotNull Response<UpdateSettingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("TAG", "onResponse: " + response.message());
                    } else if (response.code() == 400) {
                        Log.e("TAG", "onResponse: " + response.message());
                    } else if (response.code() == 500) {
                        Log.e("TAG", "onResponse: b1" + response.message());
                        mDeliverOrders.setChecked(false);
                        AppManager.SnackBar(Settings.this, "Business mode can only be switched between business operation hours.");
                    }
                    //Showing View And Hiding Progress Bar
                    mPBSetting.setVisibility(View.GONE);
                    mNSVSetting.setVisibility(View.VISIBLE);
                }


                @Override
                public void onFailure(@NotNull Call<UpdateSettingResponse> call1, @NotNull Throwable t) {
                    Log.e("TAG", "onResponse: " + t.getMessage());
                }
            });
        });


        ///PickUp Orders
        mPickupOrders.setOnClickListener(v -> {
            //Changing Value for Switches
            if (pickUpStatus == 0) {
                obj.addProperty("mode", "3");
                obj.addProperty("status", "1");
            } else {
                obj.addProperty("mode", "3");
                obj.addProperty("status", "0");
            }


            Call<UpdateSettingResponse> callUpdate = RetrofitNetMan.getRestApiService().setSetting(token, obj);
            callUpdate.enqueue(new Callback<UpdateSettingResponse>() {
                @Override
                public void onResponse(@NotNull Call<UpdateSettingResponse> call1, @NotNull Response<UpdateSettingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("TAG", "onResponse: " + response.message());
                    } else if (response.code() == 400) {
                        Log.e("TAG", "onResponse: " + response.message());
                    } else if (response.code() == 500) {
                        Log.e("TAG", "onResponse: b2" + response.message());
                        mPickupOrders.setChecked(false);
                        AppManager.SnackBar(Settings.this, "Business mode can only be switched between business operation hours.");
                    }
                    //Showing View And Hiding Progress Bar
                    mPBSetting.setVisibility(View.GONE);
                    mNSVSetting.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(@NotNull Call<UpdateSettingResponse> call1, @NotNull Throwable t) {
                    Log.e("TAG", "onResponse: " + t.getMessage());
                }
            });
        });


        ///AUtoAccept Orders
        mNewOrders.setOnClickListener(v -> {
            //Changing Value for Switches
            if (autoAcceptStatus == 0) {
                obj.addProperty("mode", "4");
                obj.addProperty("status", "1");
            } else {
                obj.addProperty("mode", "4");
                obj.addProperty("status", "0");
            }


            Call<UpdateSettingResponse> callUpdate = RetrofitNetMan.getRestApiService().setSetting(token, obj);
            callUpdate.enqueue(new Callback<UpdateSettingResponse>() {
                @Override
                public void onResponse(@NotNull Call<UpdateSettingResponse> call1, @NotNull Response<UpdateSettingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("TAG", "onResponse: " + response.message());
                    } else if (response.code() == 400) {
                        Log.e("TAG", "onResponse: " + response.message());
                    } else if (response.code() == 500) {
                        Log.e("TAG", "onResponse: b3" + response.message());
                        mNewOrders.setChecked(false);
                        AppManager.SnackBar(Settings.this, "Business mode can only be switched between business operation hours.");
                    }
                    //Showing View And Hiding Progress Bar
                    mPBSetting.setVisibility(View.GONE);
                    mNSVSetting.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(@NotNull Call<UpdateSettingResponse> call1, @NotNull Throwable t) {
                    Log.e("TAG", "onResponse: " + t.getMessage());
                }
            });
        });

    }

    //Getting Data of Switches
    private void setSwitches(int delOrders, int pickupOrders, int newOrders, String deliveryFee, String minimunOrder) {

        deliveryStatus = delOrders;
        pickUpStatus = pickupOrders;
        autoAcceptStatus = newOrders;

        ///Setting delivery Switch
        if (deliveryStatus == 1) {
            mDeliverOrders.setChecked(true);
        } else if (deliveryStatus == 0) {
            mDeliverOrders.setChecked(false);
        }

        //Setting Pickup Switch
        if (pickUpStatus == 1) {
            mPickupOrders.setChecked(true);
        } else if (pickUpStatus == 0) {
            mPickupOrders.setChecked(false);
        }

        //Setting New Order Switch
        if (autoAcceptStatus == 1) {
            pm.saveMyDataBool("auto_accept", true);
            mNewOrders.setChecked(true);
        } else if (autoAcceptStatus == 0) {
            pm.saveMyDataBool("auto_accept", false);
            mNewOrders.setChecked(false);
        }

        //Setting Text
        Log.e("TAG", "setSwitches: " + minimunOrder);

        if (minimunOrder == null || deliveryFee.equals("null")) {
            minimunOrder = "150.00";
        }

        if (deliveryFee == null || deliveryFee.equals("null")) {
            minimunOrder = "150.00";
        }


        try {
            Double delprice = Double.valueOf(deliveryFee);
            Double minOrderprice = Double.valueOf(minimunOrder);
            DecimalFormat format = new DecimalFormat("0.00");

            delFee.setText(format.format(delprice));
            minOrder.setText(format.format(minOrderprice));
        } catch (Exception e) {
            Log.e("TAG", "setSwitches Exception: " + e.getMessage());
        }


    }

    ////setting buttons to increase and decrease no of recipts
    public void incDecReciepts() {

        //Setting Increasing accept button
        mAddAcceptOrder.setOnClickListener(v -> {
            mAcceptNumber = mAcceptNumber + 1;
            mAcceptOrder.setText(Integer.toString(mAcceptNumber));
            pm.saveMyData("acceptedCopies", Integer.toString(mAcceptNumber));
            mSubAcceptOrder.setImageResource(R.drawable.ic_remove);
            mSubAcceptOrder.setClickable(true);
        });
        //Setting Decreasing accept order button
        mSubAcceptOrder.setOnClickListener(v -> {
            mAcceptNumber = mAcceptNumber - 1;
            mAcceptOrder.setText(Integer.toString(mAcceptNumber));
            pm.saveMyData("acceptedCopies", Integer.toString(mAcceptNumber));
            if (mAcceptNumber > 1) {
                mSubAcceptOrder.setClickable(true);
                mSubAcceptOrder.setImageResource(R.drawable.ic_remove);
            } else if (mAcceptNumber < 2) {
                mSubAcceptOrder.setClickable(false);
                mSubAcceptOrder.setImageResource(R.drawable.ic_remove_gray);
            }
        });

        //Setting Increasing reject order button
        mAddRejectOrder.setOnClickListener(v -> {
            mRejectNumber = mRejectNumber + 1;
            mRejectOrder.setText(Integer.toString(mRejectNumber));
            pm.saveMyData("rejectedCopies", Integer.toString(mRejectNumber));
            mSubRejectOrder.setImageResource(R.drawable.ic_remove);
            mSubRejectOrder.setClickable(true);
        });

        //Setting Decreasing reject order button
        mSubRejectOrder.setOnClickListener(v -> {
            mRejectNumber = mRejectNumber - 1;
            pm.saveMyData("rejectedCopies", Integer.toString(mRejectNumber));
            pm.getMyDataString("rejectedCopies");
            if (mRejectNumber > 1) {
                mSubRejectOrder.setClickable(true);
                mSubRejectOrder.setImageResource(R.drawable.ic_remove);
            } else if (mRejectNumber < 2) {
                mSubRejectOrder.setClickable(false);
                mSubRejectOrder.setImageResource(R.drawable.ic_remove_gray);
            }
        });

    }

    //Check for text fields for data
    private void checkFieldsForEmptyValues() {

        String s1 = delFee.getText().toString();
        String s2 = minOrder.getText().toString();

        if (s1.equals("") || s2.equals("")) {
            saveBtn.setBackgroundColor(getResources().getColor(R.color.disable_grey));
            saveBtn.setEnabled(false);
        } else {
            saveBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));
            saveBtn.setEnabled(true);
        }
    }

    ///setting Variables
    public void setVariables() {
        mAcceptOrder = findViewById(R.id.no_of_recipts);
        mRejectOrder = findViewById(R.id.no_of_recipts_rej);
        mAddAcceptOrder = findViewById(R.id.plus_no_of_recipts);
        mAddRejectOrder = findViewById(R.id.plus_no_of_recipts_rej);
        mSubAcceptOrder = findViewById(R.id.minus_no_of_recipts);
        mSubRejectOrder = findViewById(R.id.minus_no_of_recipts_rej);
        mTestReciept = findViewById(R.id.print_test_recipt);
        mBussinessName = findViewById(R.id.cellDataId);
        mDarkmode = findViewById(R.id.switchDarkMode);
        mDeliverOrders = findViewById(R.id.delOrders);
        mPickupOrders = findViewById(R.id.pickOrder);
        mNewOrders = findViewById(R.id.newOrder);
        mToolbar = findViewById(R.id.nav_bar_setting);
        mSwipeRefreshLayout = findViewById(R.id.srl_seting);
        //Progress Bar in Button
        mSaveTv = findViewById(R.id.tvSave);
        mPbSave = findViewById(R.id.pbSaveBtn);


        //Progress Bar and nested view
        mPBSetting = findViewById(R.id.pbSetting);
        mNSVSetting = findViewById(R.id.scrollViewSetting);


        /////Table Row Set Theme Preference
        mTRDarkMode = findViewById(R.id.trDarkMode);

        delFee = findViewById(R.id.etDeliveryFee);
        delFeeView = findViewById(R.id.view8set);
        minOrderView = findViewById(R.id.view9set);
        minOrder = findViewById(R.id.etMinOrderPrice);
        saveBtn = findViewById(R.id.btnSave);

        //Getting & Setting Bussiness Name From API
        mBussinessName.setText(AppManager.getBusinessDetails().getData().getResults().getBusinessName()
                + " (" + AppManager.getBusinessDetails().getData().getResults().getId() + ")");

    }

    ////Toolbar
    public void toolbar() {
        mToolbar.setTitle("Setting");

        //Setting Toolbar Navigation Bar
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_availability: {
                    Intent intent = new Intent(Settings.this, Menu.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.settings: {
                    Intent intent = new Intent(Settings.this, Settings.class);
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
                case R.id.over_flow_log_out:{
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    pm.clearSharedPref();
                    pm.saveMyDataBool("login", false);
                    startActivity(intent);
                    return false;
                }
                default: {
                    Intent intent = new Intent(Settings.this, help.class);
                    startActivity(intent);
                    return false;
                }
            }
        });
        ///Back Listener
        mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, BasicActvity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Settings.this, BasicActvity.class);
        startActivity(intent);
    }//onBackPressed
}