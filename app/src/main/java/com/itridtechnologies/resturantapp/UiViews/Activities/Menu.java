package com.itridtechnologies.resturantapp.UiViews.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.itridtechnologies.resturantapp.Adapters.AdapterMenu;
import com.itridtechnologies.resturantapp.Adapters.AdapterMenuContainer;
import com.itridtechnologies.resturantapp.Adapters.AdapterModifer;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.AddonModel;
import com.itridtechnologies.resturantapp.model.MenuModel;
import com.itridtechnologies.resturantapp.model.ModiferModel;
import com.itridtechnologies.resturantapp.models.MenuAddOns.MenuAddOnResponse;
import com.itridtechnologies.resturantapp.models.MenuCategories.ChildrenItem;
import com.itridtechnologies.resturantapp.models.MenuCategories.DataItem;
import com.itridtechnologies.resturantapp.models.MenuCategories.MenuCategoriesResponse;
import com.itridtechnologies.resturantapp.models.MenuCats.MenuCatsResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.Helper;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Menu extends AppCompatActivity {

    private static final String TAG = "Menu";

    //ui views
    private TabLayout mTabLayout;
    //private ProgressBar mPBMenu;
    private final ArrayList<MenuModel> menuItemList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private int tabPos = 0;
    private ProgressBar mPbCenter;
    private TextView mNotFound;
    private ImageView mImageNotFound;
    private LinearLayout mFullLayout;
    private CardView mTabCard;
    //setting name of toolbar
    Toolbar mToolbar;
    //Swipe Refresh Layout
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String id;
    private final ArrayList<AddonModel> mMenuAddonsList = new ArrayList<>();
    private final List<ModiferModel> mModifiersList = new ArrayList<>();
    private final List<String> cats = new ArrayList<>();
    private PreferencesManager pm;
    String token = AppManager.getBusinessDetails().getData().getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        pm = new PreferencesManager(Menu.this);
        //find views
        setVariables();
        getCategoriesApi();
        toolbar();
        ///Categories Items
        getCatItems(tabPos);
        mSwipeRefreshLayout.setProgressViewOffset(true, 10, 250);
        //Pull to Swipe
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            finish();
            startActivity(getIntent());
        });
        //hide status bar
        AppManager.hideStatusBar(this);
    }//oc

    ////Toolbar
    public void toolbar() {
        mToolbar.setTitle("Menu");


        //Setting Toolbar Navigation Bar

        //Setting Toolbar Navigation Bar
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_availability: {
                    Intent intent = new Intent(Menu.this, Menu.class);
                    startActivity(intent);
                    return false;
                }
                case R.id.settings: {
                    Intent intent = new Intent(Menu.this, Settings.class);
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
                    Intent intent = new Intent(Menu.this, MainActivity.class);
                    pm.clearSharedPref();
                    pm.saveMyDataBool("login", false);
                    startActivity(intent);
                    return false;
                }
                default: {
                    Intent intent = new Intent(Menu.this, help.class);
                    startActivity(intent);
                    return false;
                }
            }
        });

        ///Back Listener
        mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, BasicActvity.class);
            startActivity(intent);
        });

    }

    public void setVariables() {
        mTabLayout = findViewById(R.id.tabLayout);
        mRecyclerView = findViewById(R.id.rv_business_menu);
        mNotFound = findViewById(R.id.tv_no_menu);
        mFullLayout = findViewById(R.id.full_layout);
        mImageNotFound = findViewById(R.id.ic_noMenu);
        mPbCenter = findViewById(R.id.PBMenuCenter);
        mSwipeRefreshLayout = findViewById(R.id.srl);
        mTabCard = findViewById(R.id.TabCardMenu);
        mToolbar = findViewById(R.id.action_bar_menu);
    }

    public void setTabs() {

        //Checking if no categories then Show illustrates
        //No Menu Available
        if (cats.size() == 0) {
            mTabCard.setVisibility(View.GONE);
            mNotFound.setVisibility(View.VISIBLE);
            mImageNotFound.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < cats.size(); i++) {
                //assign titles to tab Layout
                //Setting categories one by one
                mTabLayout.addTab(mTabLayout.newTab().setText(cats.get(i)));
                mTabLayout.setTabGravity(TabLayout.GRAVITY_START);
                Log.e("tag", "fun com 2");
            }//for
            //loading different views on click on tabs
            tabListener();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cats.clear();
    }

    public void tabListener() {
        ///selected tab
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Getting tab position to hit api
                tabPos = tab.getPosition();
                ///Categories Items
//                if (tabPos != 0) {
                //Hitting api to get load the preview
                getCatItems(tabPos);
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                menuItemList.clear();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    //Hitting Api to get data
    public void getCategoriesApi() {
        menuItemList.clear();
        cats.clear();
        mTabLayout.removeAllTabs();
        Call<MenuCatsResponse> call = RetrofitNetMan.getRestApiService().getCats(token);

        try {
            call.enqueue(new Callback<MenuCatsResponse>() {

                @Override
                public void onResponse(@NotNull Call<MenuCatsResponse> call, @NotNull Response<MenuCatsResponse> response) {
                    Log.e(TAG, "onResponse: sucess 1");
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e(TAG, "onResponse: sucess");
                        for (int i = 0; i < response.body().getData().size(); i++) {

                                ///Adding all categories with Active status in a string list
                                cats.add(response.body().getData().get(i).getName());

//                            if (response.body().getData().get(i).getCategoryStatus().equals("Active")) {
//                                ///Adding all categories with Active status in a string list
//                                cats.add(response.body().getData().get(i).getName());
//                            }
                        }
                        ///Calling set Tabs Function to add all strings
                        setTabs();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Token Expired " + response.message(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }

                    Log.e(TAG, "onResponse:succcess");
                }

                @Override
                public void onFailure(@NotNull Call<MenuCatsResponse> call, @NotNull Throwable t) {
                    mNotFound.setVisibility(View.VISIBLE);
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    mFullLayout.setVisibility(View.GONE);
                    mImageNotFound.setVisibility(View.VISIBLE);
                    mPbCenter.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "getCategoriesApi: " + e.getMessage());
        }


//        call.enqueue(new Callback<MenuCategoriesResponse>() {
//            @Override
//            public void onResponse(Call<MenuCategoriesResponse> call, Response<MenuCategoriesResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Log.e(TAG, "onResponse: sucess");
//                    for (int i = 0; i < response.body().getData().size(); i++) {
//                        if (response.body().getData().get(i).getCategoryStatus().equals("Active")) {
//                            ///Adding all categories with Active status in a string list
//                            cats.add(response.body().getData().get(i).getName());
//                        }
//                    }
//                    ///Calling set Tabs Function to add all strings
//                    setTabs();
//                }
//
//                Log.e(TAG, "onResponse:succcess");
//            }
//
//            @Override
//            public void onFailure(Call<MenuCategoriesResponse> call,  Throwable t) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                mNotFound.setVisibility(View.VISIBLE);
//                Log.e(TAG, "onResponse: Failure " + t.getMessage());
//                mFullLayout.setVisibility(View.GONE);
//                mImageNotFound.setVisibility(View.VISIBLE);
//                mPbCenter.setVisibility(View.GONE);
//            }
//        });
    }

    ///Getting Category Items from server
    public void getCatItems(int pos) {
        Call<MenuCatsResponse> call = RetrofitNetMan.getRestApiService().getCats(token);
        call.enqueue(new Callback<MenuCatsResponse>() {
            @Override
            public void onResponse(@NotNull Call<MenuCatsResponse> call, @NotNull Response<MenuCatsResponse> response) {
                try {
                    Log.e("Pos", "" + pos);

                    //variables for handling null values
                    String description = "";

                    //checking size of children
                    Log.e(TAG, "onResponse: getting chlidren of tab items " + response.body().getData().get(pos).getChildren().get(0).getAddonAvailable());
                    id = String.valueOf(response.body().getData().get(pos).getChildren().get(0).getAddonAvailable());
                    for (int i = 0; i < response.body().getData().get(pos).getChildren().size(); i++) {


                        if (response.body().getData().get(pos).getChildren().get(i).getDescription() == null) {
                            Log.e(TAG, "onResponse: value is null");
                            description = "";
                        }

                        Log.e(TAG, "onResponse: Item available" + response.body().getData().get(pos).getChildren().get(i).getAvailability());


                            //Adding data in the list
                            menuItemList.add(new MenuModel(
                                    response.body().getData().get(pos).getChildren().get(i).getName(),
                                    description,
                                    Helper.IMAGEURL + response.body().getData().get(pos).getChildren().get(i).getPhoto(),
                                    response.body().getData().get(pos).getChildren().get(i).getAvailability(),
                                    response.body().getData().get(pos).getChildren().get(i).getId(),
                                    String.valueOf(response.body().getData().get(pos).getChildren().get(i).getAddonAvailable()),
                                    mMenuAddonsList,
                                    response.body().getData().get(pos).getChildren().get(i).getStatus()
                            ));


//                        if (response.body().getData().get(pos).getChildren().get(i).getStatus().equals("Active")) {
//                            //Adding data in the list
//                            menuItemList.add(new MenuModel(
//                                    response.body().getData().get(pos).getChildren().get(i).getName(),
//                                    description,
//                                    Helper.IMAGEURL + response.body().getData().get(pos).getChildren().get(i).getPhoto(),
//                                    response.body().getData().get(pos).getChildren().get(i).getAvailability(),
//                                    response.body().getData().get(pos).getChildren().get(i).getId(),
//                                    String.valueOf(response.body().getData().get(pos).getChildren().get(i).getAddonAvailable()),
//                                    mMenuAddonsList,
//                                    response.body().getData().get(pos).getChildren().get(i).getStatus()
//                            ));
//                        }


                    }
                    //Adapter to show the addons
                    adapter();
                } catch (Exception e) {
                    Log.e("TAG", "onResponse: Exception" + e.getMessage());
                    mNotFound.setVisibility(View.VISIBLE);
                    mFullLayout.setVisibility(View.GONE);
                    mImageNotFound.setVisibility(View.VISIBLE);
                    mPbCenter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<MenuCatsResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    //Setting Adapter
    public void adapter() {
        AdapterMenu menuAdapter;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        menuAdapter = new AdapterMenu(menuItemList, getApplicationContext());
        mRecyclerView.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();

        //Handling Views
        mFullLayout.setVisibility(View.VISIBLE);
        mPbCenter.setVisibility(View.GONE);

        ///Again hitting api for menu addons on onClick
        menuAdapter.setOnItemClickListener(((hasSubItems, mParent1RV) -> {
            mMenuAddonsList.clear();
            mModifiersList.clear();
            mTabLayout.setEnabled(false);
            //mModifiersList.clear();
            Log.e("Id", "id is " + id);
            Call<MenuAddOnResponse> call = RetrofitNetMan.getRestApiService().getAddons(token, id);
            call.enqueue(new Callback<MenuAddOnResponse>() {
                @Override
                public void onResponse(@NotNull Call<MenuAddOnResponse> call, @NotNull Response<MenuAddOnResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            mMenuAddonsList.add(new AddonModel(
                                    response.body().getData().get(i).getName(),
                                    response.body().getData().get(i).getAvailability(),
                                    response.body().getData().get(i).getChildren()
                            ));
                            for (int j = 0; j < response.body().getData().get(i).getChildren().size(); j++) {
                                mModifiersList.add(new ModiferModel(
                                        response.body().getData().get(i).getChildren().get(j).getName()
                                ));
                            }
                            pm.saveMyData("itemId", String.valueOf(response.body().getData().get(i).getId()));
                        }

                    }
                    mTabLayout.setEnabled(true);

                    if (hasSubItems) {
                        ////if we have data then we will show it
                        AdapterMenuContainer adapMenuContainer = new AdapterMenuContainer(mMenuAddonsList, getApplicationContext());
                        mParent1RV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mParent1RV.setHasFixedSize(true);
                        mParent1RV.setAdapter(adapMenuContainer);

                        adapMenuContainer.setOnItemClickListener(((hasSubItems1, mRVAddon) -> {
                            AdapterModifer adapterModifer = new AdapterModifer(mModifiersList, getApplicationContext());
                            mRVAddon.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mRVAddon.setHasFixedSize(true);
                            mRVAddon.setAdapter(adapterModifer);
                        }));
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MenuAddOnResponse> call, @NotNull Throwable t) {
                    Log.e("TAG", "onFailure: " + t.getMessage());
                }
            });
        }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.intent(BasicActvity.class);
    }
}//end class