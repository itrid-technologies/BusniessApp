package com.itridtechnologies.resturantapp.UiViews.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.utils.AppManager;

import zendesk.logger.Logger;
import zendesk.messaging.android.Messaging;

public class help extends AppCompatActivity {

    //setting name of toolbar
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mToolbar = findViewById(R.id.action_bar_help);
        AppManager.hideStatusBar(help.this);
        toolbar();
        Messaging.instance().showMessaging(help.this);
        Logger.setLoggable(true);
        Logger.isLoggable();
    }

    ////Toolbar
    public void toolbar()
    {
        //Setting Toolbar Navigation Bar
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_availability: {
                    Intent intent = new Intent(help.this, Menu.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.settings: {
                    Intent intent = new Intent(help.this, Settings.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.call_support: {
                    String mCellNumber = AppManager.getBusinessDetails().getData().getResults().getPhoneNumber();;
                    String uri = "tel:" + mCellNumber.trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                    return false;
                }
                default: {
                    Intent intent = new Intent(help.this, help.class);
                    startActivity(intent);
                    return false;
                }
            }
        });

        ///Back Listener
        mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(help.this, BasicActvity.class);
            startActivity(intent);
        });
    }

}