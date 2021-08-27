package com.itridtechnologies.resturantapp.UiViews.Activities;

import static com.itridtechnologies.resturantapp.utils.AppManager.logout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.DelayOrder.DelayResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.LogoutViaNotification;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelayOrder extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView m10min;
    private TextView m15min;
    private TextView m20min;
    private TextView m30min;
    private String mDelayedTime;
    private String mOrderNo;
    private AppCompatButton mUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay_order);
        checkVersions();
        setVariables();
        toolbarFun();
        mOrderNo = getIntent().getStringExtra("orderNo");
        mToolbar.setTitle("Delay Order #"+mOrderNo);
        setClickListener();
        LogoutViaNotification.logoutOnType();
    }

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

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void setClickListener() {

        m10min.setOnClickListener(v -> {
            mDelayedTime = "10";

            //Updating Color
            m10min.setTextColor(getResources().getColor(R.color.white));
            m10min.setBackground(getResources().getDrawable(R.drawable.delay_selected_background));
            m15min.setTextColor(getResources().getColor(R.color.black));
            m15min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));
            m20min.setTextColor(getResources().getColor(R.color.black));
            m20min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));
            m30min.setTextColor(getResources().getColor(R.color.black));
            m30min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));

            mUpdateTime.setEnabled(true);
            mUpdateTime.setBackgroundColor(getResources().getColor(R.color.green));
        });

        m15min.setOnClickListener(v -> {

            mDelayedTime = "15";

            //Updating Color
            m10min.setTextColor(getResources().getColor(R.color.black));
            m10min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));
            m15min.setTextColor(getResources().getColor(R.color.white));
            m15min.setBackground(getResources().getDrawable(R.drawable.delay_selected_background));
            m20min.setTextColor(getResources().getColor(R.color.black));
            m20min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));
            m30min.setTextColor(getResources().getColor(R.color.black));
            m30min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));

            mUpdateTime.setEnabled(true);
            mUpdateTime.setBackgroundColor(getResources().getColor(R.color.green));
        });

        m20min.setOnClickListener(v -> {

            mDelayedTime = "20";


            //Updating Color
            m10min.setTextColor(getResources().getColor(R.color.black));
            m10min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));
            m15min.setTextColor(getResources().getColor(R.color.black));
            m15min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));
            m20min.setTextColor(getResources().getColor(R.color.white));
            m20min.setBackground(getResources().getDrawable(R.drawable.delay_selected_background));
            m30min.setTextColor(getResources().getColor(R.color.black));
            m30min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));

            //Button Color Change on enable
            mUpdateTime.setBackgroundColor(getResources().getColor(R.color.green));
            mUpdateTime.setEnabled(true);
        });

        m30min.setOnClickListener(v -> {

            //Updating Color
            m10min.setTextColor(getResources().getColor(R.color.black));
            m10min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));
            m15min.setTextColor(getResources().getColor(R.color.black));
            m15min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));
            m20min.setTextColor(getResources().getColor(R.color.black));
            m20min.setBackground(getResources().getDrawable(R.drawable.cancel_order_background));
            m30min.setTextColor(getResources().getColor(R.color.white));
            m30min.setBackground(getResources().getDrawable(R.drawable.delay_selected_background));



            mUpdateTime.setBackgroundColor(getResources().getColor(R.color.green));
            mDelayedTime = "30";
            mUpdateTime.setEnabled(true);
        });



        mUpdateTime.setOnClickListener(v -> {
            delayBusinessOrder(mDelayedTime);

            Intent intent = new Intent(DelayOrder.this, OrderIssues.class);
            intent.putExtra("orderNo",mOrderNo);
            startActivity(intent);
        });
    }

    //Setting Variables
    public void setVariables() {
        mToolbar = findViewById(R.id.nav_bar_DO);
        m10min = findViewById(R.id.ten_min);
        m15min = findViewById(R.id.fifteen_min);
        m20min = findViewById(R.id.twenty_min);
        m30min = findViewById(R.id.thirty_min);
        mUpdateTime = findViewById(R.id.update_btn_DO);
    }


    //Setting Toolbar
    public void toolbarFun() {
        mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(DelayOrder.this, OrderIssues.class);
            intent.putExtra("orderNo",mOrderNo);
            startActivity(intent);
        });
    }

    public void delayBusinessOrder(String time) {
        JsonObject obj = new JsonObject();
        obj.addProperty("time", time);

        Call<DelayResponse> call = RetrofitNetMan.getRestApiService().delayOrder(
                AppManager.getBusinessDetails().getData().getToken(),
                obj,
                mOrderNo
        );

        call.enqueue(new Callback<DelayResponse>() {
            @Override
            public void onResponse(@NotNull Call<DelayResponse> call, @NotNull Response<DelayResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppManager.toast(response.body().getMessage());
                    AppManager.toast(time + " increased");
//                    AppManager.intent(BasicActvity.class);

                }
                else if (response.code() == 401)
                {
                    logout();
                } else {

                    if (response.body() != null) {
                        AppManager.SnackBar(DelayOrder.this,response.body().getMessage());
                    }
                    else {
                        AppManager.SnackBar(DelayOrder.this,response.message());
                    }
                }
            } //res

            @Override
            public void onFailure(@NotNull Call<DelayResponse> call, @NotNull Throwable t) {
                AppManager.toast("Internet Not Available");
            }
        });
    }

    ///Checking version
    public void checkVersions() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setStatus(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setStatus(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //Changing Color of Searchbar
    //status bar fun
    public void setStatus(Activity activity, final int bit, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bit;
        } else {
            winParams.flags &= bit;
        }
        win.setAttributes(winParams);
    }

}