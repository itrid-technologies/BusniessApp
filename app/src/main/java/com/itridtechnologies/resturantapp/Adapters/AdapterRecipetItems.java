package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.receiptOrder.AddonItemsItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AdapterRecipetItems extends RecyclerView.Adapter<AdapterRecipetItems.detailHolderClass>{

    //declaring variable
    private List<AddonItemsItem> mAddonItems;
    private Context mCtx;

    public AdapterRecipetItems(List<AddonItemsItem> mAddonItems, Context mCtx) {
        this.mAddonItems = mAddonItems;
        this.mCtx = mCtx;
    }

    @NonNull
    @NotNull
    @Override
    public detailHolderClass onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_addon_item_container,parent,false);
        return new detailHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull detailHolderClass holder, int position) {

//        List<AddonItemsItem> addonsObjects = new ArrayList<>();

//        for (int i = 0; i < mAddonItems.size(); i++) {
//
//            addonsObjects.addAll(mAddonItems.get(i));
//
//        }
        
//        AddonItemsItem addonItems = addonsObjects.get(position);

        AddonItemsItem addonItems = mAddonItems.get(position);
        holder.mAddonName.setText(addonItems.getAddonItemName());
        holder.mAddonPrice.setText(addonItems.getAddonItemPrice());
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + mAddonItems.size());
        return mAddonItems.size();
    }

    public static class detailHolderClass extends RecyclerView.ViewHolder {
        private final TextView mAddonName;
        private final TextView mAddonPrice;
        public detailHolderClass(@NonNull @NotNull View itemView) {
            super(itemView);
            mAddonName = itemView.findViewById(R.id.tv_sub_item_name);
            mAddonPrice = itemView.findViewById(R.id.tv_sub_item_price);
        }
    }
}
