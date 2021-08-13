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
import com.itridtechnologies.resturantapp.models.OrderSubItems.DataItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.OrderAddonsItem;
import com.itridtechnologies.resturantapp.utils.Constants;


import java.text.DecimalFormat;
import java.util.List;
public class AdapterBusinessOrders extends RecyclerView.Adapter<AdapterBusinessOrders.detailHolder> {

    //declaring variable
    private final List<DataItem> mOrderItemListt;
    private final Context mCtx;
    //Addon Name of Order
    private List<OrderAddonsItem> mOrderAddons;
    private final boolean haveSubItems = false;

    public AdapterBusinessOrders(List<DataItem> mOrderItemListt, Context mCtx) {
        this.mOrderItemListt = mOrderItemListt;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public detailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order, parent, false);
        return new detailHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull detailHolder holder, int position) {
        DataItem orderitem = mOrderItemListt.get(position);

        DecimalFormat format = new DecimalFormat("0.00");

        mOrderAddons = mOrderItemListt.get(position).getOrderAddons();
        //if we have order sub-items
        //then we populate inner rv sub-items
        AdapterBusinessOrderSubItems adapterSubItems = new AdapterBusinessOrderSubItems(mOrderAddons, mCtx);
        holder.rvSubItems.setHasFixedSize(true);
        holder.rvSubItems.setAdapter(adapterSubItems);

        holder.itemName.setText(orderitem.getItemName());
        holder.itemPrice.setText(Constants.CURRENCY_SIGN + " " + format.format(Double.parseDouble(orderitem.getItemPrice())));
        holder.itemQty.setText(String.valueOf(orderitem.getItemQty()));

    }

    @Override
    public int getItemCount() {
        return mOrderItemListt.size();
    }

    public static class detailHolder extends RecyclerView.ViewHolder {

        private final TextView itemQty;
        private final TextView itemName;
        private final TextView itemPrice;
        private final View itemViewAddon;
        private final RecyclerView rvSubItems;
//        private boolean isVisible = true;

        public detailHolder(@NonNull View itemView) {
            super(itemView);

            //find views
            itemQty = itemView.findViewById(R.id.tv_order_item_qty);
            itemName = itemView.findViewById(R.id.tv_order_item_name);
            itemPrice = itemView.findViewById(R.id.tv_order_item_price);
            itemViewAddon = itemView.findViewById(R.id.viewAddon);
            rvSubItems = itemView.findViewById(R.id.rv_order_sub_items);
            rvSubItems.setVisibility(View.VISIBLE);
//            listener.getSubItems(true, rvSubItems);
            //listener
//            itemView.setOnClickListener(v -> {
//                //handle rv visibility
//                if (!isVisible) {
//                    //rv visible
//
//                    isVisible = true;
////                    listener.getSubItems(true, rvSubItems);
//                } else {
//                    //rv gone
//                    rvSubItems.setVisibility(View.GONE);
//                    isVisible = false;
//                }
//            });
        }//const
    }//vh
}//end adapter
