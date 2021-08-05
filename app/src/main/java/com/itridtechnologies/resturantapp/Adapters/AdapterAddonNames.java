package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.AddOnItemModel;
import com.itridtechnologies.resturantapp.model.AddonModel;
import com.itridtechnologies.resturantapp.model.ModiferModel;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.AddonItemsItem;

import java.util.List;

public class AdapterAddonNames extends RecyclerView.Adapter<AdapterAddonNames.detailHolderClass>{

    //declaring variable
    private List<AddonItemsItem> addOnModelList;
    private Context mCtx;

    public AdapterAddonNames(List<AddonItemsItem> addOnModelList, Context mCtx) {
        this.addOnModelList = addOnModelList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public detailHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_addon_item_container,parent,false);
        return new AdapterAddonNames.detailHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull detailHolderClass holder, int pos) {
        AddonItemsItem mAddonItems = addOnModelList.get(pos);
        holder.mAddonName.setText(mAddonItems.getAddonItemName());
        holder.mAddonPrice.setText(mAddonItems.getAddonItemPrice());
    }

    @Override
    public int getItemCount() {
        return addOnModelList.size();
    }

    public static class detailHolderClass extends RecyclerView.ViewHolder {
        private final TextView mAddonName;
        private final TextView mAddonPrice;
        public detailHolderClass(@NonNull View itemView) {
            super(itemView);
            mAddonName = itemView.findViewById(R.id.tv_sub_item_name);
            mAddonPrice = itemView.findViewById(R.id.tv_sub_item_price);
        }
    }
}
