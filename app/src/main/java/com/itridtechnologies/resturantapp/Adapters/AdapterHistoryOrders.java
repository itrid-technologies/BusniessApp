package com.itridtechnologies.resturantapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.HistoryDetails;
import com.itridtechnologies.resturantapp.model.OrderDetailModel;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.DataItem;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.OrderAddonsItem;
import com.itridtechnologies.resturantapp.models.orderHistory.AddonsItem;
import com.itridtechnologies.resturantapp.models.orderHistory.ItemsItem;
import com.itridtechnologies.resturantapp.models.orderHistory.OrdersItem;
import com.itridtechnologies.resturantapp.utils.Constants;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterHistoryOrders extends RecyclerView.Adapter<AdapterHistoryOrders.detailHolder>{

    //declaring variable
    private final List<DataItem> mHistoryOrders;
    private boolean haveSubItems = false;
    //Addons Item Names
    private List<OrderAddonsItem> mAddonItemName;
    private final Context mCtx;

    public AdapterHistoryOrders(List<DataItem> mHistoryOrders, Context mCtx) {
        this.mHistoryOrders = mHistoryOrders;
        this.mCtx = mCtx;
    }

    public void AddData(List<DataItem> list){
        mHistoryOrders.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public detailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order, parent, false);
        return new AdapterHistoryOrders.detailHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull detailHolder holder, int position) {
        DataItem orderItem = mHistoryOrders.get(position);
        //set data

        DecimalFormat format = new DecimalFormat("0.00");

        holder.itemName.setText(orderItem.getItemName());
        holder.itemQty.setText(String.valueOf(orderItem.getItemQty()));
        holder.itemPrice.setText(Constants.CURRENCY_SIGN + " " + format.format(Double.parseDouble(orderItem.getItemPrice())));

        //Assigning List
        mAddonItemName = mHistoryOrders.get(position).getOrderAddons();
        //Setting Adapter

        //if we have order sub-items
        //then we populate inner rv sub-items
        AdapterHistorySubItems adapterSubItems = new AdapterHistorySubItems(mAddonItemName, mCtx);
        holder.rvSubItems.setHasFixedSize(true);
        holder.rvSubItems.setAdapter(adapterSubItems);
    }

    @Override
    public int getItemCount() {
        return mHistoryOrders.size();
    }


    public static class detailHolder extends RecyclerView.ViewHolder {


        private final TextView itemQty;
        private final TextView itemName;
        private final TextView itemPrice;
        private final RecyclerView rvSubItems;
        private boolean isVisible = false;

        public detailHolder(@NonNull View itemView) {
            super(itemView);

            //find views
            itemQty = itemView.findViewById(R.id.tv_order_item_qty);
            itemName = itemView.findViewById(R.id.tv_order_item_name);
            itemPrice = itemView.findViewById(R.id.tv_order_item_price);
            rvSubItems = itemView.findViewById(R.id.rv_order_sub_items);
            //rv visible
            rvSubItems.setVisibility(View.VISIBLE);
            isVisible = true;

        }//const
    }//vh
}//end adapter
