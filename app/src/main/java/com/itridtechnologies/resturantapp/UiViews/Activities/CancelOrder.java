package com.itridtechnologies.resturantapp.UiViews.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
//import com.itridtechnologies.resturantapp.model.cancelOrder.CancelOrderResponse;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.CancelOrder.CancelResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Helper;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelOrder extends AppCompatActivity {

    private static final String TAG = "CancelOrder";

    private AppCompatButton mCancelOrder;
    private EditText mCancellationMessage;
    private TextView mErrorMsg;
    private String mOrderNo;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);

        AppManager.hideStatusBar(this);
        setVariables();
        toolbarFun();
        TextWatchFun();
        mOrderNo = getIntent().getStringExtra("orderNo");

        mToolbar.setTitle("Cancel Order #"+mOrderNo);
        mCancelOrder.setBackgroundColor(getResources().getColor(R.color.disable_grey));
        mCancelOrder.setEnabled(false);
        mErrorMsg.setVisibility(View.INVISIBLE);


        mCancelOrder.setOnClickListener(v -> {
            if (Helper.BUSINESS_ID != -1)
                cancelBusinessOrder
                        (
                                mOrderNo,
                                mCancellationMessage.getText().toString().trim()
                        );

            Intent intent = new Intent(CancelOrder.this,OrderIssues.class);
            intent.putExtra("orderNo",mOrderNo);
            startActivity(intent);

        });

    }//OC

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CancelOrder.this,OrderIssues.class);
        intent.putExtra("orderNo",mOrderNo);
        startActivity(intent);
    }

    ///setting textwatcher to check the cacelaation message
    public void TextWatchFun() {
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
        mCancellationMessage.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();

    }

    //Setting Variables
    public void setVariables() {
        mToolbar = findViewById(R.id.nav_bar_CO);
        mCancelOrder = findViewById(R.id.cancel_btn_CO);
        mCancellationMessage = findViewById(R.id.cancel_msg_ET);
        mErrorMsg = findViewById(R.id.error_message_cancel);
    }

    //Setting Toolbar
    public void toolbarFun() {
        mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(CancelOrder.this, OrderIssues.class);
            intent.putExtra("orderNo",mOrderNo);
            startActivity(intent);
        });
    }

    //Check for text fields for data
    private void checkFieldsForEmptyValues() {

        String s1 = mCancellationMessage.getText().toString();

        if (s1.equals("")) {
            mCancelOrder.setBackgroundColor(getResources().getColor(R.color.disable_grey));
            mCancelOrder.setEnabled(false);
            mErrorMsg.setVisibility(View.VISIBLE);
        } else {
            mCancelOrder.setBackgroundColor(getResources().getColor(R.color.red));
            mCancelOrder.setEnabled(true);
            mErrorMsg.setVisibility(View.INVISIBLE);
        }
    }

    private void cancelBusinessOrder(String orderId, String reason) {
        Log.d(TAG, "cancelBusinessOrder: preparing request...");

        JsonObject body = new JsonObject();
        body.addProperty("id", orderId);
        body.addProperty("reason", reason);
//
        Call<CancelResponse> call = RetrofitNetMan.getRestApiService().cancelOrder
                (
                        AppManager.getBusinessDetails().getData().getToken(),
                        body
                );
        call.enqueue(new Callback<CancelResponse>() {
            @Override
            public void onResponse(@NotNull Call<CancelResponse> call, @NotNull Response<CancelResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppManager.intent(BasicActvity.class);

                } else if (response.code() == 400) {

                    Toast.makeText(CancelOrder.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }//res

            @Override
            public void onFailure(@NotNull Call<CancelResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }//end order

}//end class