package com.itridtechnologies.resturantapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.Menu;
import com.itridtechnologies.resturantapp.model.AddonModel;
import com.itridtechnologies.resturantapp.model.MenuModel;
import com.itridtechnologies.resturantapp.model.ModiferModel;
import com.itridtechnologies.resturantapp.models.MenuAddOns.MenuAddOnResponse;
import com.itridtechnologies.resturantapp.models.MenuItemAvailable.MenuItemAvailableResponse;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.detailHolder> {

    //declaring variable
    private final List<MenuModel> menuItems;
    private final Context mCtx;
    private final boolean haveSubItems = false;
    private PreferencesManager pm;
    private final String token = AppManager.getBusinessDetails().getData().getToken();
    private ItemClickListenerMenu mListenerMenu;
    private boolean isVisibleFlag = false;

    public AdapterMenu(List<MenuModel> menuItems, Context mCtx) {
        this.menuItems = menuItems;
        this.mCtx = mCtx;
        pm = new PreferencesManager(mCtx);
    }

    public void setOnItemClickListener(ItemClickListenerMenu ListenerMenu) {
        mListenerMenu = ListenerMenu;
    }

    @NonNull
    @Override
    public detailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_container, parent, false);
        return new AdapterMenu.detailHolder(view, mListenerMenu, menuItems);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull detailHolder holder, int position) {

        MenuModel mMenuItem = menuItems.get(position);
        holder.mItemName.setText(mMenuItem.getmItemName());
        List<AddonModel> mOrderAddon = new ArrayList<>();

        Log.e(TAG, "onBindViewHolder: yes or no " + mMenuItem.getmYesNo());

        if (mMenuItem.getmYesNo() == 1) {
            holder.mSwitchItem.setChecked(true);
        }

        if (mMenuItem.getId() != null) {
            //Setting on click listener
            holder.itemView.setOnClickListener(v -> {
                mOrderAddon.clear();

                if (!mMenuItem.getmAddOnAvailable().equals("null")) {
                    //handle rv visibility
                    if (!isVisibleFlag) {
                        //rv visible
                        //Changing the
                        //Call<MenuAddOnResponse> call = RetrofitNetMan.getRestApiService().getAddons(token, menuItemList.get(position).getmAddOnAvailable());
                        Call<MenuAddOnResponse> call = RetrofitNetMan.getRestApiService().getAddons(token, mMenuItem.getId());
                        call.enqueue(new Callback<MenuAddOnResponse>() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onResponse(@NotNull Call<MenuAddOnResponse> call, @NotNull Response<MenuAddOnResponse> response) {
                                Log.e(TAG, "onResponse called for addons ");
                                if (response.isSuccessful() && response.body() != null) {
                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                        Log.e(TAG, "onResponse: addon respomse" + response.body().getData().size());
                                        mOrderAddon.add(new AddonModel(
                                                response.body().getData().get(i).getName(),
                                                response.body().getData().get(i).getAvailability(),
                                                response.body().getData().get(i).getAddon()
                                        ));
                                        pm.saveMyData("itemId", String.valueOf(response.body().getData().get(i).getId()));
                                    }//for

                                    //setting adapter
                                    holder.mParent1RV.setLayoutManager(new LinearLayoutManager(mCtx));
                                    AdapterMenuContainer adapterMenuContainer = new AdapterMenuContainer(mOrderAddon, mCtx);
                                    holder.mParent1RV.setHasFixedSize(true);
                                    holder.mParent1RV.setAdapter(adapterMenuContainer);

                                } else if (!response.isSuccessful()) {
                                    Log.e(TAG, "onResponse: not success " + response.message());
                                    Snackbar.make(holder.view, "No Data Found", 2000)
                                            .setBackgroundTint(mCtx.getResources().getColor(R.color.theme_color))
                                            .show();
                                }
//                        if (hasSubItems) {
//                            Log.e(TAG, "onResponse: i have data now ");
//                            ////if we have data then we will show it
//                            AdapterMenuContainer adapMenuContainer = new AdapterMenuContainer(mMenuAddonsList, getApplicationContext());
//                            mParent1RV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                            mParent1RV.setHasFixedSize(true);
//                            mParent1RV.setAdapter(adapMenuContainer);
//
//                            adapMenuContainer.setOnItemClickListener(((hasSubItems1, mRVAddon) -> {
////                                AdapterModifer adapterModifer = new AdapterModifer(mModifiersList, getApplicationContext());
////                                mRVAddon.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
////                                mRVAddon.setHasFixedSize(true);
////                                mRVAddon.setAdapter(adapterModifer);
//                            }));
//                        }
                            }

                            @Override
                            public void onFailure(@NotNull Call<MenuAddOnResponse> call, @NotNull Throwable t) {
                                Log.e("TAG", "onFailure: i m failed" + t.getMessage());
                            }
                        });

                        holder.mAddons.setVisibility(View.VISIBLE);
                        isVisibleFlag = true;
                        holder.mShowDetails.setVisibility(View.INVISIBLE);
                        holder.view.setVisibility(View.VISIBLE);
                        holder.mHideDetails.setVisibility(View.VISIBLE);
                    } else {
                        //rv gone
                        holder.mAddons.setVisibility(View.GONE);
                        holder.view.setVisibility(View.GONE);
                        holder.mShowDetails.setVisibility(View.VISIBLE);
                        holder.mHideDetails.setVisibility(View.GONE);
                        isVisibleFlag = false;
                    }
                } else {
                    Log.e(TAG, "detailHolder: i m f***android:stateListAnimator=\"@null\"ed ");
                }

            });
        } else {
            Snackbar.make(holder.view, "No Data Found", 2000)
                    .setBackgroundTint(mCtx.getResources().getColor(R.color.theme_color))
                    .show();
        }

        try {

            //validatyin chevron
            if (!mMenuItem.getmAddOnAvailable().equals("null")) {
                holder.mShowDetails.setVisibility(View.VISIBLE);
            } else {
                holder.mShowDetails.setVisibility(View.GONE);
            }

            ////Validating Description
            if (!mMenuItem.getmItemDescription().equals("")) {
                holder.mItemDescription.setText(mMenuItem.getmItemDescription());
                holder.mItemDescription.setVisibility(View.VISIBLE);
            } else {
                holder.mItemDescription.setVisibility(View.GONE);
            }
        } catch (Exception ignored) {
            Log.e(TAG, "onBindViewHolder: Menu add on ");
        }

        Log.e(TAG, "onBindViewHolder: Menu availablitiy status" + mMenuItem.getmYesNo());

        if (mMenuItem.getmYesNo() == 0) {
            holder.itemView.setEnabled(true);
            holder.mSwitchItem.setChecked(false);
        } else {
            holder.itemView.setEnabled(true);
            holder.mSwitchItem.setChecked(true);
//            holder.mShowDetails.setVisibility(View.GONE);
        }

        /////Hiding If Not Available
//        String avalibilityStatus = mMenuItem.getmAvailabilityStatus();
//        if (!avalibilityStatus.equals("Active")) {
//            holder.itemView.setVisibility(View.GONE);
//        }

        ////Loading Image
        Glide.with(mCtx).load(mMenuItem.getmPic()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e("Error", "error");
                holder.mPhoto.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.mPhoto.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(holder.mPhoto);

//        Hitting PUT API
//        Setting Switch
        holder.mSwitchItem.setOnClickListener(v -> {

            JsonObject obj = new JsonObject();
            holder.mSwitchItem.setEnabled(false);
            if (mMenuItem.getmYesNo() == 0) {
                obj.addProperty("action", "1");
            } else {
                obj.addProperty("action", "0");
            }

            obj.addProperty("actionType", "item");
            Log.e(TAG, "onBindViewHolder: menu " + mMenuItem.getmMenuid());
            Call<MenuItemAvailableResponse> call = RetrofitNetMan.getRestApiService().itemAvailable(token, String.valueOf(mMenuItem.getmMenuid()), obj);
            call.enqueue(new Callback<MenuItemAvailableResponse>() {
                @Override
                public void onResponse(@NotNull Call<MenuItemAvailableResponse> call, @NotNull Response<MenuItemAvailableResponse> response) {
                    Log.e("Id Number", pm.getMyDataString("itemId"));

                    if (response.isSuccessful() && response.body() != null) {
                        Snackbar.make(holder.view, " " + response.body().getMessage(), 2000)
                                .setBackgroundTint(mCtx.getResources().getColor(R.color.theme_color))
                                .show();
                        Log.e(TAG, "onResponse: " + response.message());
                    } else {
                        Snackbar.make(holder.view, response.message(), 2000).show();
                    }
                    holder.mSwitchItem.setEnabled(true);
                }

                @Override
                public void onFailure(@NotNull Call<MenuItemAvailableResponse> call, @NotNull Throwable t) {
                    Snackbar.make(holder.view, Objects.requireNonNull(t.getMessage()), 2000)
                            .setBackgroundTint(mCtx.getResources().getColor(R.color.theme_color))
                            .show();
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }


    public interface ItemClickListenerMenu {
        void getMenuItems(boolean hasSubItems, RecyclerView mParent1RV, int position);
    }

    private void adapter() {

    }//adapter

    public static class detailHolder extends RecyclerView.ViewHolder {
        private final TextView mItemName;
        private final TextView mItemDescription;
        private final ImageView mPhoto;
        private final ImageView mShowDetails;
        private final ImageView mHideDetails;
        private final LinearLayout mAddons;
        private final RecyclerView mParent1RV;
        private final SwitchCompat mSwitchItem;
        private final View view;
        private Context ctx;
        private final List<MenuModel> menu = new ArrayList<>();
        private final ArrayList<AddonModel> mMenuAddonsList = new ArrayList<>();
        private boolean isVisible = false;
        private String token = AppManager.getBusinessDetails().getData().getToken();


        public detailHolder(@NonNull View itemView, ItemClickListenerMenu listenerMenu, List<MenuModel> menuItems) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.tv_itemName);
            mItemDescription = itemView.findViewById(R.id.tv_description);
            mAddons = itemView.findViewById(R.id.lin_lay);
            mParent1RV = itemView.findViewById(R.id.rv_menu_container);
            mPhoto = itemView.findViewById(R.id.iv_menu_item);
            mSwitchItem = itemView.findViewById(R.id.switch_rv);
            mShowDetails = itemView.findViewById(R.id.iv_open_details);
            mHideDetails = itemView.findViewById(R.id.iv_close_details);
            view = itemView.findViewById(R.id.view);

            //listener
//            itemView.setOnClickListener(v -> {
//                MenuModel mMenuItem = menuItems.get(getAdapterPosition());
//                Log.e(TAG, "detailHolder: menu id " + mMenuItem.getmAddOnAvailable());
//                if (!mMenuItem.getmAddOnAvailable().equals("null")) {
//                    //handle rv visibility
//                    if (!isVisible) {
//                        //rv visible
//                        mAddons.setVisibility(View.VISIBLE);
//                        isVisible = true;
//                        mShowDetails.setVisibility(View.INVISIBLE);
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listenerMenu.getMenuItems(true, mParent1RV, position);
//                        }
//                        view.setVisibility(View.VISIBLE);
//                        mHideDetails.setVisibility(View.VISIBLE);
//                    } else {
//                        //rv gone
//                        mAddons.setVisibility(View.GONE);
//                        view.setVisibility(View.GONE);
//                        mShowDetails.setVisibility(View.VISIBLE);
//                        mHideDetails.setVisibility(View.GONE);
//                        isVisible = false;
//                    }
//                } else {
//                    Log.e(TAG, "detailHolder: i m f***android:stateListAnimator=\"@null\"ed ");
//                }
//            });
        }
    }
}
