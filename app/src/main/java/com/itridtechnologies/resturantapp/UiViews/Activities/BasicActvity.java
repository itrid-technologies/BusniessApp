package com.itridtechnologies.resturantapp.UiViews.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itridtechnologies.resturantapp.UiViews.Activities.Frags.FragmentDashboard;
import com.itridtechnologies.resturantapp.UiViews.Activities.Frags.FragmentHistory;
import com.itridtechnologies.resturantapp.UiViews.Activities.Frags.FragmentProcessing;
import com.itridtechnologies.resturantapp.UiViews.Activities.Frags.FragmentReady;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

public class BasicActvity extends AppCompatActivity {

    private static final String TAG = "BasicActvity";
    private PreferencesManager pm;

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_actvity);

        String str = getIntent().getStringExtra("AOR");
        pm = new PreferencesManager(this);

        ///Saving Order Number on Shared Preference
        pm.saveMyData("orderNo","0");
        if (str != null) {

            if (str.equals("Rejected")) {

                Log.e(TAG, "onCreate: " + "rejected");
                String orderId = getIntent().getStringExtra("orderId");
                Bundle bundle = new Bundle();
                bundle.putString("OrderId",orderId);
                pm.saveMyDataBool("orders",true);
                FragmentHistory fragHist = new FragmentHistory();
                fragHist.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new FragmentHistory()).commit();
            }
            else if (str.equals("Accepted"))
            {
                Log.e(TAG, "onCreate: " + "accepted");
                String typeOrder = getIntent().getStringExtra("OrderTypeFromNO");
                Log.e(TAG, "onCreate" + typeOrder);
                Bundle bundle = new Bundle();
                bundle.putString("OrderTypeFromNOo", typeOrder);
                FragmentProcessing fragobj = new FragmentProcessing();
                fragobj.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragobj).commit();

            }
            else if (str.equals("Ready"))
            {
                Log.e(TAG, "onCreate: " + "accepted");
                String typeOrder = getIntent().getStringExtra("OrderTypeFromNO");
                Log.e(TAG, "onCreate" + typeOrder);
                Bundle bundle = new Bundle();
                bundle.putString("OrderTypeFromNOo", typeOrder);
                FragmentReady fragobj = new FragmentReady();
                fragobj.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragobj).commit();

            }
            else if (str.equals("History"))
            {
                Log.e(TAG, "onCreate: " + "accepted");
                String typeOrder = getIntent().getStringExtra("OrderTypeFromNO");
                Log.e(TAG, "onCreate" + typeOrder);
                Bundle bundle = new Bundle();
                bundle.putString("OrderTypeFromNOo", typeOrder);
                FragmentHistory fragobj = new FragmentHistory();
                fragobj.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragobj).commit();

            }
            else
                {
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

        //Getting if of bottom navigation
        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottom_nav);
//        mBottomNavigationView.setItemTextColor(getResources().getColorStateList(R.color.black));
//        mBottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.color.black));
//        mBottomNavigationView.setItemRippleColor(getResources().getColorStateList(R.color.theme_color));

        //setting click listener
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                //the dashboard
                case R.id.nav_home: {
                    selectedFragment = new FragmentDashboard();
                    item.setChecked(true);
                    break;
                }
                case R.id.nav_process: {

                    selectedFragment = new FragmentProcessing();
                    item.setChecked(true);
                    break;
                }
                case R.id.nav_ready: {
                    selectedFragment = new FragmentReady();
                    item.setChecked(true);
                    break;
                }
                case R.id.nav_history: {
                    selectedFragment = new FragmentHistory();
                    item.setChecked(true);
                    break;
                }
            }

            //replacing Fragment
            if (selectedFragment != null)//ctrl_alt_l
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, selectedFragment).commit();
            return true;
        });

    }//OC
}//end class