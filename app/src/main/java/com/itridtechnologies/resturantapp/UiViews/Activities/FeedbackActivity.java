package com.itridtechnologies.resturantapp.UiViews.Activities;

import static android.content.ContentValues.TAG;
import static com.itridtechnologies.resturantapp.utils.AppManager.logout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itridtechnologies.resturantapp.Adapters.AdapterFeedback;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.databinding.ActivityFeedback2Binding;
import com.itridtechnologies.resturantapp.models.Feedback.FeedbackResponse;
import com.itridtechnologies.resturantapp.models.feedbacktags.DataItem;
import com.itridtechnologies.resturantapp.models.feedbacktags.FeedbackTagsResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.LogoutViaNotification;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    private static final String TAG = "FeedbackActivity";

    private ActivityFeedback2Binding binding;
    private Toolbar mToolbar;
    private AppCompatButton mFeedbackBtn;

    //Adding tags in array
    private ArrayList<String> tags = new ArrayList<>();
    //getting tags from server and adding here
    private ArrayList<String> mTagsFromServer = new ArrayList<>();

    //rating
    private String mOrderId;
    private int mRating = 1;

    private RecyclerView mRCV;

    //Customer name
    private String mName;

    //RCV Variables
    private AdapterFeedback mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback2);

        AppManager.hideStatusBar(FeedbackActivity.this);

        mFeedbackBtn = findViewById(R.id.btn_delivered);
        mRCV = findViewById(R.id.frequent_options);
        mOrderId = String.valueOf(getIntent().getStringExtra("ORDER_ID"));
        mName = getIntent().getStringExtra("NAME");
        Log.e(TAG, "onCreate: mOrderId = " + mOrderId);

        toolbarfun();

        LogoutViaNotification.logoutOnType();

    }//onCreate

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

    //Setting Toolbar
    public void toolbarfun() {
        mToolbar = findViewById(R.id.nav_bar_HD);
        ///Header Name
        mToolbar.setTitle("Leave Feedback");
        mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(FeedbackActivity.this, BasicActvity.class);
            startActivity(intent);
        });

        //Setting Toolbar Navigation Bar
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_availability: {
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.settings: {
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
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
                    Intent intent = new Intent(getApplicationContext(), help.class);
                    startActivity(intent);
                    return false;
                }
            }
        });
    }//toolbarfun

    //onStart Method
    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        //setting cutomer name
        binding.yourExpereinceTv.setText("How was your overall experience with " + mName);

        //setting chips
        setFeedback();
//        getFromChips();
        //setting listener on button
        listeners();
    }//onStart

    private void addInChipGroup(List<DataItem> feedbackList) {

        Log.e(TAG, "setRCV: + rcv ");
//        binding.frequentOptions.setLayoutManager(new GridLayoutManager(this, 2));
//        mAdapter = new AdapterFeedback(FeedbackActivity.this, feedbackList);
//        binding.frequentOptions.setAdapter(mAdapter);
//
//        mAdapter.setOnItemClickListener((position, type) -> {
//            tags.add(feedbackList.get(position).getDisplayName());
//        });


//        for (String tag : mTagsFromServer) {
//            Chip chip = new Chip(FeedbackActivity.this);
//            Log.e(TAG, "addInChipGroup: " + tag);
//            chip.setText(tag);
//            binding.frequentOptions.addView(chip);
//        }

//        if (binding.frequentOptions.getChildCount() == 0) {
//            int i = 0;
//            for (String tag : mTagsFromServer) {
//                Chip resultChip = new Chip(FeedbackActivity.this);
//                resultChip.setId(i++);
//                resultChip.setText(tag);
//                binding.frequentOptions.addView(resultChip);
//            }
//        }

//        binding.frequentOptions.setOnCheckedChangeListener((group, checkedId) -> {
//            Chip chip = group.findViewById(checkedId);
//            if (chip != null) {
//                tags.add(chip.getText().toString());
//                Toast.makeText(getApplicationContext(), chip.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

        binding.nsvFeedback.setVisibility(View.VISIBLE);
        binding.pbFeedback.setVisibility(View.GONE);

    }//addInChipGroup

    //click listeners
    @SuppressLint("SetTextI18n")
    private void listeners() {

        mFeedbackBtn.setOnClickListener(v -> {
            //hit api
            //post data to server
            postFeedback();

        });

        //updating thumbs up and thumbs down and updating rating value
        if (binding.thumbsUp != null) {
            binding.thumbsUp.setOnClickListener(v -> {

                binding.ratingTv.setText("Excellent rating. What did you enjoy? ");

                binding.thumbsUp.setImageResource(R.drawable.ic_like_tint);
                binding.thumbsDown.setImageResource(R.drawable.ic_dislike_untint);
                binding.thumbsDown.setBackgroundResource(R.drawable.unselected_thumb_bg);
                binding.thumbsUp.setBackgroundResource(R.drawable.selected_thumb_bg);
                mRating = 5;
                Log.e(TAG, "listeners: mRating " + mRating);

            });
        }

        if (binding.thumbsDown != null) {
            binding.thumbsDown.setOnClickListener(v -> {


                binding.ratingTv.setText("What was wrong with Bilal's Order? ");

                binding.thumbsUp.setImageResource(R.drawable.ic_like_untint);
                binding.thumbsDown.setImageResource(R.drawable.ic_dislike_tint);
                binding.thumbsDown.setBackgroundResource(R.drawable.selected_thumb_down_bg);
                binding.thumbsUp.setBackgroundResource(R.drawable.unselected_thumb_bg);
                mRating = 1;
                Log.e(TAG, "listeners: mRating " + mRating);
            });
        }
    }//listeners()

    private void postFeedback() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("orderId", mOrderId);
        jsonObject.addProperty("forType", "2");
        jsonObject.addProperty("rating", mRating);
        jsonObject.addProperty("comment", binding.feebackEt.getText().toString());

        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i <= tags.size() - 1; i++) {
            jsonArray.add(tags.get(i));
        }

        jsonObject.add("tag", jsonArray);
        Log.e(TAG, "postFeedback: json object " + jsonObject.toString());


        Call<FeedbackResponse> call = RetrofitNetMan.getRestApiService().postFeedback(
                AppManager.getBusinessDetails().getData().getToken(),
                jsonObject
        );

        call.enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeedbackResponse> call, @NotNull Response<FeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FeedbackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FeedbackActivity.this, BasicActvity.class);
                    startActivity(intent);
                } else if (response.code() == 401) {
                    logout();
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeedbackResponse> call, @NotNull Throwable t) {
                AppManager.SnackBar(FeedbackActivity.this, "Something went wrong " + t.getMessage());
            }
        });

    }//postFeedback

    //setting chips
    private void setFeedback() {
        Call<FeedbackTagsResponse> call = RetrofitNetMan.getRestApiService().getTags();
        call.enqueue(new Callback<FeedbackTagsResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeedbackTagsResponse> call, @NotNull Response<FeedbackTagsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    mAdapter = new AdapterFeedback(FeedbackActivity.this, response.body().getData());
                    mRCV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    mRCV.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener((position, type) -> {
                        tags.add(response.body().getData().get(position).getDisplayName());
                    });

                    binding.nsvFeedback.setVisibility(View.VISIBLE);
                    binding.pbFeedback.setVisibility(View.GONE);

//                    addInChipGroup(response.body().getData());

                } else if (response.code() == 401) {
                    logout();
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeedbackTagsResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }//setChips

//    //getting data from chip and setting in feedback edit text
//    private void getFromChips() {
//        binding.chip1.setOnClickListener(v -> {
//            if (binding.chip1.isChecked()) {
//                tags.add(binding.chip1.getText().toString());
//                index1 = tags.size() - 1;
//                Log.e(TAG, "getFromChips: " + String.valueOf(tags) + index1);
//            } else {
//                tags.remove(tags.size() - 1);
//                Log.e(TAG, "getFromChips: " + String.valueOf(tags) + index6);
//            }
//        });
//
//        binding.chip2.setOnClickListener(v -> {
//
//            if (binding.chip2.isChecked()) {
//                tags.add(binding.chip2.getText().toString());
//                index2 = tags.size() - 1;
//                Log.e(TAG, "getFromChips: " + tags + index2);
//            } else {
//                tags.remove(tags.size() - 1);
//                Log.e(TAG, "getFromChips: " + tags + index6);
//            }
//
//        });
//
//        binding.chip3.setOnClickListener(v -> {
//            if (binding.chip3.isChecked()) {
//                tags.add(binding.chip3.getText().toString());
//                index3 = tags.size() - 1;
//                Log.e(TAG, "getFromChips: " + tags + index3);
//            } else {
//                tags.remove(tags.size() - 1);
//                Log.e(TAG, "getFromChips: " + tags + index6);
//            }
//        });
//
//        binding.chip4.setOnClickListener(v -> {
//            if (binding.chip4.isChecked()) {
//                tags.add(binding.chip4.getText().toString());
//                index4 = tags.size() - 1;
//                Log.e(TAG, "getFromChips: " + tags + index4);
//            } else {
//                tags.remove(tags.size() - 1);
//                Log.e(TAG, "getFromChips: " + tags + index6);
//            }
//        });
//
//        binding.chip5.setOnClickListener(v -> {
//            if (binding.chip5.isChecked()) {
//                tags.add(binding.chip5.getText().toString());
//                index5 = tags.size() - 1;
//                Log.e(TAG, "getFromChips: " + tags + index5);
//            } else {
//                tags.remove(tags.size() - 1);
//                Log.e(TAG, "getFromChips: " + tags + index6);
//            }
//        });
//
//        binding.chip6.setOnClickListener(v -> {
//            if (binding.chip6.isChecked()) {
//                tags.add(binding.chip6.getText().toString());
//                index6 = tags.size() - 1;
//                Log.e(TAG, "getFromChips: " + tags + index6);
//            } else {
//                tags.remove(tags.size() - 1);
//                Log.e(TAG, "getFromChips: " + tags + index6);
//            }
//
//        });
//    }//getFromChips

}