package com.itridtechnologies.resturantapp.UiViews.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.orderHistory.Order;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

public class OrderIssues extends AppCompatActivity {
    private AppCompatButton mCancelBtn;
    private AppCompatButton mDelayBtn;
    private Toolbar mToolbar;
    private String OrderNo;
    private static final String TAG = "OrderIssues";
    private PreferencesManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_issues);
        pm = new PreferencesManager(OrderIssues.this);
        checkVersions();
        setVariables();
        toolbarFun();
        setBtns();
        OrderNo = getIntent().getStringExtra("orderNo");
        Log.e(TAG, "onCreate: " + OrderNo);
        mToolbar.setTitle("Problem With #" + OrderNo);
    }


    //Setting Variables
    public void setVariables()
    {
        mToolbar = findViewById(R.id.nav_bar_OI);
        mCancelBtn = findViewById(R.id.cancel_order_btn_issues);
        mDelayBtn = findViewById(R.id.delay_order_btn);
    }

    ///Seeting buttons
    public void setBtns()
    {
        mCancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrderIssues.this,CancelOrder.class);
            intent.putExtra("orderNo",OrderNo);
            startActivity(intent);
        });

        mDelayBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrderIssues.this,DelayOrder.class);
            intent.putExtra("orderNo",OrderNo);
            startActivity(intent);
        });
    }

    //Setting Toolbar
    public void toolbarFun()
    {
        mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(OrderIssues.this,BasicActvity.class);
            intent.putExtra("orderId",OrderNo);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderIssues.this,NewOrder.class);
        intent.putExtra("orderId",OrderNo);
        startActivity(intent);
    }

    ///Checking version
    public void checkVersions()
    {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21)
        {
            setStatus(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21)
        {
            setStatus(this,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //Changing Color of Searchbar
    //status bar fun
    public void setStatus(Activity activity, final int bit, boolean on)
    {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if(on)
        {
            winParams.flags |= bit;
        }
        else
        {
            winParams.flags &= bit;
        }
        win.setAttributes(winParams);
    }

}