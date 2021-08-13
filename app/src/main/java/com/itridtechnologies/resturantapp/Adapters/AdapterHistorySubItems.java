package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.HistoryDetails;
import com.itridtechnologies.resturantapp.model.HistoryAddonDetailsItemNameModel;
import com.itridtechnologies.resturantapp.model.OrderDetailModel;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.AddonItemsItem;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.OrderAddonsItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterHistorySubItems extends RecyclerView.Adapter<AdapterHistorySubItems.HistorySubItemsVH> {

    private final List<OrderAddonsItem> mHistoryItemListt;
    private Context mCtx;

    //Addons Items
    private List<AddonItemsItem> mAddonItemListt;
    private final boolean haveSubAddonItems = false;

    public AdapterHistorySubItems(List<OrderAddonsItem> mHistoryItemListt, Context mCtx) {
        this.mHistoryItemListt = mHistoryItemListt;
        this.mCtx = mCtx;
    }


    @NonNull
    @Override
    public AdapterHistorySubItems.HistorySubItemsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order_sub_items, parent, false);
        return new AdapterHistorySubItems.HistorySubItemsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorySubItemsVH holder, int position) {
        OrderAddonsItem orderitem = mHistoryItemListt.get(position);
        holder.itemName.setText(orderitem.getAddonName());
        mAddonItemListt = mHistoryItemListt.get(position).getAddonItems();

        //Setting Adapter
        holder.rvAddOns.setLayoutManager(new LinearLayoutManager(mCtx));
        AdapterAddonNames adapterAddonNames = new AdapterAddonNames(mAddonItemListt, mCtx);
        holder.rvAddOns.setHasFixedSize(true);
        holder.rvAddOns.setAdapter(adapterAddonNames);
    }

    @Override
    public int getItemCount() {
        return mHistoryItemListt.size();
    }


    public static class HistorySubItemsVH extends RecyclerView.ViewHolder {

        private final TextView itemName;
        private final RecyclerView rvAddOns;
        private boolean isVisible = true;

        public HistorySubItemsVH(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_sub_item_title);
            rvAddOns = itemView.findViewById(R.id.rcv_item_addon);
            //rv visible
            rvAddOns.setVisibility(View.VISIBLE);
            isVisible = true;

        }
    }
}
