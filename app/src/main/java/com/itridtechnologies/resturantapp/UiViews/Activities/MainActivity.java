package com.itridtechnologies.resturantapp.UiViews.Activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.login.LoginResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Constants;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;
import com.itridtechnologies.resturantapp.valid.validations;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Toolbar mToolbar; //setting name of toolbar
    private ConstraintLayout mLoginBtn;  //Login Button
    private TextView mLoginTV;
    private ProgressBar mPBLogin;
    private EditText mEmailId, mPasswordId; //Email and password edit text
    private TextView mEnterEmail;   //valid email textview
    private PreferencesManager pm;  //Shared Preference Manager for storing boolean value for not logging in again and again
    private TextWatcher mTextWatcherForValidation; //Text watcher for disable/enable button and showing email validity
    private ProgressBar mProgress;
    //fire base notification var
    private String mFCMToken;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = new PreferencesManager(this);
        //calling functions
        setVariables();
        ///Top bar Setting Name
        mToolbar.setTitle("Login");
        setSupportActionBar(mToolbar);
        //hide status bar
        AppManager.hideStatusBar(this);
        ///Handling Color of button if able and disable
        mLoginBtn.setBackgroundColor(getResources().getColor(R.color.disable_grey));
        mLoginBtn.setEnabled(false);
        ////Checking if its false
        /////Setting Values for testing purpose (temporary)
        mEmailId.setText("oho@oho.com");
        mPasswordId.setText("Aengagedb1");

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
        mEmailId.addTextChangedListener(mTextWatcher);
        mPasswordId.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
        /////Set on Click Listener
        mLoginBtn.setOnClickListener(v -> {
            if (validations.isEmailValid(mEmailId.getText().toString())) {
//                mProgress.setVisibility(View.VISIBLE);
                mLoginBtn.setEnabled(false);
                mLoginBtn.setBackgroundColor(getResources().getColor(R.color.disable_grey));
                mLoginTV.setVisibility(View.GONE);
                mPBLogin.setVisibility(View.VISIBLE);
                postDetails();
            } else {
                mEnterEmail.setText("Enter a Valid Email Address");
                mEnterEmail.setVisibility(View.VISIBLE);
                textWatcher();
                // set listeners
                mEmailId.addTextChangedListener(mTextWatcherForValidation);
            }
        });
    }//oc

    public void textWatcher() {
        mTextWatcherForValidation = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEnterEmail.setVisibility(View.INVISIBLE);
            }
        };
    }

    public void postDetails() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    mFCMToken = Constants.DEVICE_TOKEN;
                    Log.e("Main Activity ", "Fcm token 1: " + mFCMToken);
                });

        JsonObject obj = new JsonObject();
        obj.addProperty("email", mEmailId.getText().toString());
        obj.addProperty("password", mPasswordId.getText().toString());
        Log.e("Main Activity ", "Fcm token 2: " + Constants.DEVICE_TOKEN);
        obj.addProperty("token", Constants.DEVICE_TOKEN);


        Call<LoginResponse> call = RetrofitNetMan.getRestApiService().login(obj);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    pm.saveMyDataBool("login", true);
                    pm.saveMyDataBool("orders", false);
                    //save whole business object in preference
                    AppManager.saveBusinessDetails(response.body());

                    Intent intent = new Intent(MainActivity.this, BasicActvity.class);
                    startActivity(intent);

                } else {
                    mEnterEmail.setText("Email or Password Doesn't Match");
                    mEnterEmail.setVisibility(View.VISIBLE);
                    mLoginBtn.setEnabled(true);
                    mLoginBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));
                    textWatcher();
                    AppManager.SnackBar(MainActivity.this, "Email or Password Doesn't Match");
                    mPBLogin.setVisibility(View.GONE);
                    mLoginTV.setVisibility(View.VISIBLE);
                }
            }//res

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                AppManager.SnackBar(MainActivity.this, "Sorry :( Unable To Login!! We have Faced an Internet or Server Issue");
                mLoginTV.setVisibility(View.VISIBLE);
                mPBLogin.setVisibility(View.GONE);
//                ///Handling Color of button if able and disable
//                mLoginBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));
                mLoginBtn.setEnabled(true);
                mLoginBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));
                try {
                    mProgress.setVisibility(View.GONE);
                } catch (Exception ignored) {
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseMessaging.getInstance().subscribeToTopic("resturant")
                .addOnCompleteListener(task -> {

                    String msg = "done notifications";
                    if (!task.isSuccessful()) {
                        msg = "failed";
                    }
                    Log.d("TAG", msg);
                });
        //while activity is starting, we want to check if business is already logged in or not
//        try {
//            LoginResponse response = AppManager.getBusinessDetails();
//            if (response != null) {
//                //redirect to home screen
////                AppManager.intent(BasicActvity.class);
//            }
//        } catch (NullPointerException ex) {
//            Log.e(TAG, "onStart: business is not logged in !" + ex.getMessage());
//        }
    }//OS

    //Setting the variables Getting Ids and setting values
    private void setVariables() {
        mLoginBtn = findViewById(R.id.loginBtn);
        mEmailId = findViewById(R.id.email);
        mPasswordId = findViewById(R.id.password);
        mEnterEmail = findViewById(R.id.email_required);
        mToolbar = findViewById(R.id.action_bar);
        mProgress = findViewById(R.id.loginProgressBar);
        mPBLogin = findViewById(R.id.pbLogin);
        mLoginTV = findViewById(R.id.tvLogin);
    }

    //Check for text fields for data
    private void checkFieldsForEmptyValues() {

        String s1 = mEmailId.getText().toString();
        String s2 = mPasswordId.getText().toString();

        if (s1.equals("") || s2.equals("")) {
            mLoginBtn.setBackgroundColor(getResources().getColor(R.color.disable_grey));
            mLoginBtn.setEnabled(false);
        } else {
            mLoginBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));
            mLoginBtn.setEnabled(true);
        }
    }
}