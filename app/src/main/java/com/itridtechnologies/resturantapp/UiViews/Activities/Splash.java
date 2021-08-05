package com.itridtechnologies.resturantapp.UiViews.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    ImageView logo;
    Animation anim;
    private PreferencesManager pm;
    Boolean loggedIn;
    //dark on off
    private boolean darkLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pm = new PreferencesManager(Splash.this);
        //hide status bar
        AppManager.hideStatusBar(this);

        //dark light mode

        //getting and dark switch
        darkLight = pm.getMyDataBool("darkSwitch");

        if (darkLight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        loggedIn = pm.getMyDataBool("login");
        pm.saveMyDataBool("processingLoadedFirstTime", true);
        pm.saveMyDataBool("readyLoadedFirstTime", true);

        logo = findViewById(R.id.logo_splash);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(() -> {
                    /* Create an Intent that will start the Menu-Activity. */
                    if (loggedIn){
                        Intent intent;
                        intent = new Intent(Splash.this, BasicActvity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent;
                        intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);}

                    finish();
                }, SPLASH_DISPLAY_LENGTH);

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logo.startAnimation(anim);
    }
}