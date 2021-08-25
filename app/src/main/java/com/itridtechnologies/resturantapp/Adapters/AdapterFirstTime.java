package com.itridtechnologies.resturantapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItem;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class AdapterFirstTime extends RecyclerView.Adapter<AdapterFirstTime.detailHolder> {

    private final List<OrdersItem> prepareList;
    private final Context mCtx;
    private itemClickListener mListener;


    //Variables to pass data
    private String mCustomerName;
    private String mOrderNumber;
    private String mTotalItems;
    private String mTotalAmount;
    private String mType;
    private String mStatus;
    private SimpleDateFormat sdf;

    public AdapterFirstTime(List<OrdersItem> prepareList, Context mCtx) {
        this.prepareList = prepareList;
        this.mCtx = mCtx;
    }


    public void AddData(List<OrdersItem> list){

        prepareList.addAll(list);
        notifyDataSetChanged();

    }


    public void setOnItemClickListener(itemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @NotNull
    @Override
    public detailHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.processing_orders_container, parent, false);
        return new AdapterFirstTime.detailHolder(view, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull detailHolder holder, int position) {

        OrdersItem mOrderInfo = prepareList.get(position);

        int isRiderAssigned = 0;
        int orderType;
        orderType = mOrderInfo.getOrderType();

        if (orderType == 1) {
            if (isRiderAssigned == 1) {
                holder.mRider.setText("Bilal is Coming in 10 minutes");
            } else {
                holder.mRider.setText("Waiting for courier");
            }
        } else if (orderType == 0) {
            holder.mRider.setText(
                    "Waiting for " + mOrderInfo.getFirstName()
                            + " " + mOrderInfo.getLastName()
                            + " to Collect"
            );
        } else {
            holder.mRider.setText(
                    mOrderInfo.getItemCount() + " items (Rs. 140.00)"
            );
        }

        //Saving Data to pass
        mCustomerName = mOrderInfo.getFirstName() + " " + mOrderInfo.getLastName();
        mOrderNumber = String.valueOf(mOrderInfo.getId());
        mTotalItems = String.valueOf(mOrderInfo.getItemCount());
        mTotalAmount = "100.00";
        mType = String.valueOf(mOrderInfo.getOrderType());
        mStatus = String.valueOf(mOrderInfo.getPaymentStatus());

        String time = prepareList.get(position).getDateAdded();
        String[] minTime = time.split("T");
        Log.e(TAG, "onBindViewHolder: time 1 " + minTime[1]);
        Log.e(TAG, "onBindViewHolder: time 1 " + minTime[1]);

        holder.mOrderTime.setText(minTime[1].substring(0, minTime[1].length() - 8));

        //Payment Status
        if (mStatus.trim().equals("0")) {
            mStatus = "Unpaid";
        } else if (mStatus.trim().equals("1")) {
            mStatus = "Paid";
        }

        ///Setting the String name of Order Type 0,1,2
        if (mType.trim().equals("0")) {
            mType = "Self-Delivery";
        } else if (mType.trim().equals("1")) {
            mType = "Deliver with own courier";
        } else {
            mType = "Delivery";
        }

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));


        ///Seeting data in Textfields On screen
        holder.mOrderNumber.setText(mOrderNumber);
        holder.mStatus.setText(mStatus);
        holder.mCustomerName.setText(mCustomerName);
        holder.mType.setText(mType);


        //making invisible payment status if order type is delivery
        if (holder.mType.getText().toString().trim().equals("Delivery")) {
            holder.mStatus.setVisibility(View.INVISIBLE);
        }
        //Checking If its status is paid
        if (holder.mStatus.getText().toString().equals("Paid")) {
            holder.mStatus.setBackground(mCtx.getResources().getDrawable(R.drawable.paid_background));
        }

    }

    @Override
    public int getItemCount() {
        return prepareList.size();
    }

    //my listener interface
    public interface itemClickListener {
        void onItemClick(int position);
    }

    public class detailHolder extends RecyclerView.ViewHolder {
        private final TextView mOrderNumber;
        private final TextView mCustomerName;
        private final TextView mOrderTime;
        private final TextView mRider;
        private final TextView mType;
        private final TextView mStatus;

        public detailHolder(@NonNull @NotNull View itemView, final itemClickListener listener) {
            super(itemView);
            mOrderNumber = itemView.findViewById(R.id.order_number_PO);
            mCustomerName = itemView.findViewById(R.id.customer_name_PO);
            mOrderTime = itemView.findViewById(R.id.order_time_PO);
            mType = itemView.findViewById(R.id.delivery_PO);
            mStatus = itemView.findViewById(R.id.status_PO);
            mRider = itemView.findViewById(R.id.rider_coming_PO);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
