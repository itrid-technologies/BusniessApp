package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.Reciept;
import com.itridtechnologies.resturantapp.models.receiptOrder.OrderItemsItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecieptOrders extends RecyclerView.Adapter<AdapterRecieptOrders.detailHolder> {

    //Declaring Variables
    private final List<OrderItemsItem> mOrderList;
//
//    private List<List<com.itridtechnologies.resturantapp.models.receiptOrder.OrderAddonsItem>> mOrderAddonList;
    private List<com.itridtechnologies.resturantapp.models.receiptOrder.OrderAddonsItem> mOrderAddonList;
    private final Context mCtx;
    private final boolean haveSubItems = false;

    public AdapterRecieptOrders(List<OrderItemsItem> mOrderList, Context mCtx) {
        this.mOrderList = mOrderList;
        this.mCtx = mCtx;
    }

    @NonNull
    @NotNull
    @Override
    public detailHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order, parent, false);
        return new AdapterRecieptOrders.detailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull detailHolder holder, int position) {
        OrderItemsItem orderitem = mOrderList.get(position);
        holder.itemName.setText(orderitem.getItemName());
        holder.itemPrice.setText(orderitem.getItemPrice());
        holder.itemQty.setText(String.valueOf(orderitem.getItemQty()));

        mOrderAddonList = mOrderList.get(position).getOrderAddons();

        //Order Addons List
//        getOrderAddonsList(mOrderList);

        //if we have order sub-items
        //then we populate inner rv sub-items
        AdapterReciptOrderAddons adapterSubItems = new AdapterReciptOrderAddons(mOrderAddonList, mCtx);
        holder.rvSubItems.setHasFixedSize(true);
        holder.rvSubItems.setAdapter(adapterSubItems);


    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

//
//    private void getOrderAddonsList(List<com.itridtechnologies.resturantapp.models.receiptOrder.OrderItemsItem> orderItems) {
//
//        mOrderAddonList = new ArrayList<>();
//
//        for (com.itridtechnologies.resturantapp.models.receiptOrder.OrderItemsItem o : orderItems) {
//            mOrderAddonList.add(o.getOrderAddons());
//        }
//    }


    public static class detailHolder extends RecyclerView.ViewHolder {

        private final TextView itemQty;
        private final TextView itemName;
        private final TextView itemPrice;
        private final RecyclerView rvSubItems;
        private boolean isVisible = true;


        public detailHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //find views
            itemQty = itemView.findViewById(R.id.tv_order_item_qty);
            itemName = itemView.findViewById(R.id.tv_order_item_name);
            itemPrice = itemView.findViewById(R.id.tv_order_item_price);
            rvSubItems = itemView.findViewById(R.id.rv_order_sub_items);
            //rv visible
            rvSubItems.setEnabled(false);
            rvSubItems.setVisibility(View.VISIBLE);
        }
    }
}
