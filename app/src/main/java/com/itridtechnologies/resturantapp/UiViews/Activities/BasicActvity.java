package com.itridtechnologies.resturantapp.UiViews.Activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.Frags.FragmentDashboard;
import com.itridtechnologies.resturantapp.UiViews.Activities.Frags.FragmentHistory;
import com.itridtechnologies.resturantapp.UiViews.Activities.Frags.FragmentProcessing;
import com.itridtechnologies.resturantapp.UiViews.Activities.Frags.FragmentReady;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.LogoutViaNotification;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

public class BasicActvity extends AppCompatActivity {

    private static final String TAG = "BasicActvity";
    private PreferencesManager pm;
    //For notifications
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private BottomNavigationView mBottomNavigationView;

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_actvity);

        String str = getIntent().getStringExtra("AOR");
        pm = new PreferencesManager(this);


        //Getting if of bottom navigation
        mBottomNavigationView = findViewById(R.id.bottom_nav);

        LogoutViaNotification.logoutOnType();


        ///Saving Order Number on Shared Preference
        pm.saveMyData("orderNo", "0");

        try {
            if (str != null) {

                if (str.equals("Rejected")) {

                    Log.e(TAG, "onCreate: " + "rejected");
                    String orderId = getIntent().getStringExtra("orderId");
                    Bundle bundle = new Bundle();
                    bundle.putString("OrderId", orderId);
                    pm.saveMyDataBool("orders", true);
                    FragmentHistory fragHist = new FragmentHistory();
                    fragHist.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new FragmentHistory()).commit();

                } else if (str.equals("Accepted")) {

                    Log.e(TAG, "onCreate: " + "accepted");
                    String typeOrder = getIntent().getStringExtra("OrderTypeFromNO");
                    Log.e(TAG, "onCreate" + typeOrder);
                    Bundle bundle = new Bundle();
                    bundle.putString("OrderTypeFromNOo", typeOrder);
                    FragmentProcessing fragobj = new FragmentProcessing();
                    fragobj.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragobj).commit();

                } else if (str.equals("Ready")) {

                    Log.e(TAG, "onCreate: " + "accepted");
                    String typeOrder = getIntent().getStringExtra("OrderTypeFromNO");
                    Log.e(TAG, "onCreate" + typeOrder);
                    Bundle bundle = new Bundle();
                    bundle.putString("OrderTypeFromNOo", typeOrder);
                    FragmentReady fragobj = new FragmentReady();
                    fragobj.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragobj).commit();

                } else if (str.equals("History")) {

                    Log.e(TAG, "onCreate: " + "accepted");
                    String typeOrder = getIntent().getStringExtra("OrderTypeFromNO");
                    Log.e(TAG, "onCreate" + typeOrder);
                    Bundle bundle = new Bundle();
                    bundle.putString("OrderTypeFromNOo", typeOrder);
                    FragmentHistory fragobj = new FragmentHistory();
                    fragobj.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragobj).commit();


                } else {
                    //Loadin Dashboard fragment
                    Log.e(TAG, "onCreate: " + "Garbage");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new FragmentDashboard()).commit();
                }
            } else {
                if (savedInstanceState == null) {
                    //Loadin Dashboard fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new FragmentDashboard()).commit();
                }
            }

            //Making the status bar invisible
            AppManager.hideStatusBar(this);


            //setting click listener
            mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {

                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    //the dashboard
                    case R.id.nav_home: {

                        try {
                            selectedFragment = new FragmentDashboard();
                            item.setChecked(true);
                            break;

                        } catch (Exception ignored) {}

                    }
                    case R.id.nav_process: {
                        try {
                            selectedFragment = new FragmentProcessing();
                            item.setChecked(true);
                            break;

                        } catch (Exception ignored) {}

                    }
                    case R.id.nav_ready: {

                        try {
                            selectedFragment = new FragmentReady();
                            item.setChecked(true);
                            break;

                        } catch (Exception ignored) {}

                    }
                    case R.id.nav_history: {
                        try {
                            selectedFragment = new FragmentHistory();
                            item.setChecked(true);
                            break;

                        } catch (Exception ignored) {}
                    }
                }

                //replacing Fragment
                if (selectedFragment != null)//ctrl_alt_l
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, selectedFragment).commit();
                return true;
            });
        } catch (Exception ignored) {

        }


    }//OC

    @Override
    protected void onResume() {
        super.onResume();

        LogoutViaNotification.onResumeFun();

    }//Onresume


    @Override
    protected void onPause() {
        super.onPause();
        LogoutViaNotification.onPauseFun();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BasicActvity.this, BasicActvity.class));
    }
}//end class