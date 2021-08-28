package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItem;

import java.util.List;

public class RecyclerViewAdapterProcessingOrders extends RecyclerView.Adapter<RecyclerViewAdapterProcessingOrders.detailHolder> {
    private final List<OrderItem> mProcessingOrderList;
    private final Context mCtx;
    private itemClickListener mListener;

    public RecyclerViewAdapterProcessingOrders(List<OrderItem> mProcessingOrderList, Context mCtx) {
        this.mProcessingOrderList = mProcessingOrderList;
        this.mCtx = mCtx;
    }


    public void setOnItemClickListener(itemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public detailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.processing_orders_container, parent, false);
        return new RecyclerViewAdapterProcessingOrders.detailHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull detailHolder holder, int position) {

        OrderItem mOrderInfo = mProcessingOrderList.get(position);

        holder.mOrderNumberPO.setText(String.valueOf(mOrderInfo.getId()));
        holder.mStatusPO.setText(String.valueOf(mOrderInfo.getPaymentStatus()));
        holder.mCustomerNamePO.setText(mOrderInfo.getFirstName() + " " + mOrderInfo.getLastName());
        holder.mItemQuantityPO.setText(String.valueOf(mOrderInfo.getItemCount()));
        holder.mPricePO.setText(mOrderInfo.getOrderTotalPrice());
        holder.mTypePO.setText(String.valueOf(mOrderInfo.getOrderType()));
        holder.mRiderName.setText(mOrderInfo.getFirstName());
        holder.mEstimatedTime.setText(String.valueOf(mOrderInfo.getMaxPreTime()));


        //making invisible payment status if order type is delivery
        if (holder.mTypePO.getText().toString().trim().equals("1")) {
            holder.mTypePO.setText("Delivery");
            holder.mStatusPO.setVisibility(View.INVISIBLE);
            holder.mTypePO.setVisibility(View.VISIBLE);
        } else if (holder.mTypePO.getText().toString().trim().equals("2")) {
            holder.mTypePO.setText("Pickup");
        } else {
            holder.mTypePO.setText("Self Service");
        }

        if (mOrderInfo.getOrderType() == 1) {
            holder.mStatusPO.setVisibility(View.GONE);

        } else if (mOrderInfo.getOrderType() == 0 || mOrderInfo.getOrderType() == 2) {

            //Checking If its status is paid
            holder.mStatusPO.setVisibility(View.VISIBLE);
            if (holder.mStatusPO.getText().toString().equals("1")) {
                holder.mStatusPO.setText("Paid");
                holder.mStatusPO.setBackgroundResource(R.drawable.paid_background);
            } else {
                holder.mStatusPO.setText("UnPaid");
            }
        }

    }

    @Override
    public int getItemCount() {
        return mProcessingOrderList.size();
    }

    //my listener interface
    public interface itemClickListener {
        void onItemClick(int position);
    }


    public static class detailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mOrderNumberPO;
        private final TextView mCustomerNamePO;
        private final TextView mOrderTimePO;
        private final TextView mItemQuantityPO;
        private final TextView mPricePO;
        private final TextView mTypePO;
        private final TextView mStatusPO;
        private final TextView mRiderName;
        private final TextView mEstimatedTime;

        public detailHolder(@NonNull View itemView, final itemClickListener listener) {
            super(itemView);
            mOrderNumberPO = itemView.findViewById(R.id.order_number_PO);
            mCustomerNamePO = itemView.findViewById(R.id.customer_name_PO);
            mOrderTimePO = itemView.findViewById(R.id.order_time_PO);
            mItemQuantityPO = itemView.findViewById(R.id.item_quantity_PO);
            mPricePO = itemView.findViewById(R.id.price_PO);
            mTypePO = itemView.findViewById(R.id.delivery_PO);
            mStatusPO = itemView.findViewById(R.id.status_PO);
            mRiderName = itemView.findViewById(R.id.rider_name_PO);
            mEstimatedTime = itemView.findViewById(R.id.estimated_time_PO);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
