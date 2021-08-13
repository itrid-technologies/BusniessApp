package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.AddonModel;
import com.itridtechnologies.resturantapp.model.ModiferModel;
import com.itridtechnologies.resturantapp.models.MenuItemAvailable.MenuItemAvailableResponse;
import com.itridtechnologies.resturantapp.models.OrderSubItems.AddonItemsItem;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMenuContainer extends RecyclerView.Adapter<AdapterMenuContainer.detailHolderr> {

    //declaring variable
    private final List<AddonModel> addonItems;
    private ItemClickListenerContainer mListener;
    private final boolean haveSubItems = false;
    private final Context mCtx;
    private String token;

    public AdapterMenuContainer(List<AddonModel> addonItems, Context mCtx) {
        this.addonItems = addonItems;
        this.mCtx = mCtx;
        token = AppManager.getBusinessDetails().getData().getToken();
    }

    public void setOnItemClickListener(ItemClickListenerContainer listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public detailHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_container_addon, parent, false);
        return new AdapterMenuContainer.detailHolderr(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull detailHolderr holder, int position) {

        AddonModel mAddonItem = addonItems.get(position);
        holder.mAddOnTitle.setText(mAddonItem.getmAddonName());

        Log.e("TAG", "onBindViewHolder: mAddonItem.getAddOnParent().get(0).getAddonId()" + mAddonItem.getmAvailibility());

        List<ModiferModel> mOrderModifier = new ArrayList<>();

        for (int i=0;i<mAddonItem.getAddOnParent().size();i++)
        {
            mOrderModifier.add(new ModiferModel(
                    mAddonItem.getAddOnParent().get(i).getName()
            ));
        }

        holder.mRVAddon.setLayoutManager(new LinearLayoutManager(mCtx));
        AdapterModifer adapterModifier = new AdapterModifer(mOrderModifier, mCtx);
        holder.mRVAddon.setHasFixedSize(true);
        holder.mRVAddon.setAdapter(adapterModifier);

        holder.mSwitch.setChecked(mAddonItem.getmAvailibility() == 1);

        ///Hitting Put Api
        holder.mSwitch.setOnClickListener(v -> {
            JsonObject obj = new JsonObject();
            holder.mSwitch.setEnabled(false);
            if (holder.mSwitch.isChecked()) {
                obj.addProperty("action", "0");
                obj.addProperty("actionType", "item");
            } else {
                obj.addProperty("action", "1");
                obj.addProperty("actionType", "item");
            }

            Call<MenuItemAvailableResponse> call = RetrofitNetMan.getRestApiService().itemAvailable(token, String.valueOf(mAddonItem.getAddOnParent().get(0).getAddonId()), obj);
            call.enqueue(new Callback<MenuItemAvailableResponse>() {
                @Override
                public void onResponse(@NotNull Call<MenuItemAvailableResponse> call, @NotNull Response<MenuItemAvailableResponse> response) {
//                    Log.e("Id Number", pm.getMyDataString("itemId"));
                    if (response.isSuccessful()) {
                        Log.e("TAG", "onResponse: " + response.message() + response.code());
                        AppManager.toast(response.message());
                    } else if (response.code() == 400) {
                        Log.e("TAG", "onResponse:hs " + response.message() + response.code());
                        AppManager.toast(response.message());
                        holder.mSwitch.setChecked(false);
                    }
                    holder.mSwitch.setEnabled(true);
                }

                @Override
                public void onFailure(@NotNull Call<MenuItemAvailableResponse> call, @NotNull Throwable t) {
                    AppManager.toast("No Internet Connection");
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return addonItems.size();
    }


    public interface ItemClickListenerContainer {
//        void getMenuAddons(boolean hasSubItems, RecyclerView mRVAddon);
    }


    public static class detailHolderr extends RecyclerView.ViewHolder {
        private final TextView mAddOnTitle;
        private final RecyclerView mRVAddon;
        private SwitchCompat mSwitch;
//        private boolean isVisible = false;
//        private ImageView mShowDet;
//        private ImageView mHideDet;

        public detailHolderr(@NonNull View itemView, ItemClickListenerContainer listener) {
            super(itemView);
            mAddOnTitle = itemView.findViewById(R.id.addon);
            mRVAddon = itemView.findViewById(R.id.rv_addon);
            mSwitch = itemView.findViewById(R.id.switch_rv3_addon);
            mRVAddon.setVisibility(View.VISIBLE);
//            listener.getMenuAddons(true, mRVAddon);
//            mShowDet = itemView.findViewById(R.id.iv_open_details_addon);
//            mHideDet = itemView.findViewById(R.id.iv_close_details_addon);
//
//            itemView.setOnClickListener(v -> {
//                //handle rv visibility
//                if (!isVisible) {
//                    //rv visible
//                    mRVAddon.setVisibility(View.VISIBLE);
//                    isVisible = true;
//                    listener.getMenuAddons(true, mRVAddon);
//                } else {
//                    //rv gone
//                    mRVAddon.setVisibility(View.GONE);
//                    isVisible = false;
////                    mShowDet.setVisibility(View.VISIBLE);
////                    mHideDet.setVisibility(View.INVISIBLE);
//                }
//            });

        }
    }
}
