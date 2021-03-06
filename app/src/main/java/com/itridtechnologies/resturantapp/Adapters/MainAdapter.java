package com.itridtechnologies.resturantapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.detailHolder> {

    private final List<OrdersItem> prepareList;
    private final Context mCtx;
    private itemClickListener mListener;
    private PreferencesManager pm;

    public MainAdapter(List<OrdersItem> prepareList, Context mCtx) {
        this.prepareList = prepareList;
        this.mCtx = mCtx;
        pm = new PreferencesManager(mCtx);
    }

    public void setOnItemClickListener(itemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @NotNull
    @Override
    public detailHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.processing_orders_container, parent, false);
        return new MainAdapter.detailHolder(view, mListener);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull @NotNull detailHolder holder, int position) {

        OrdersItem mOrderInfo = prepareList.get(position);

        int isRiderAssigned = 0;
        int orderType;
        orderType = mOrderInfo.getOrderType();

        String customerName = prepareList.get(position).getFirstName() + " " + prepareList.get(position).getLastName();
        String orderNumber = String.valueOf(mOrderInfo.getId());
        int paymentStatus = mOrderInfo.getPaymentStatus();
        String itemTotal = String.valueOf(mOrderInfo.getItemCount());

        ///Setting data in Textfields On screen
        holder.mOrderNumber.setText("#" + orderNumber);
        holder.mCustomerName.setText(customerName);
        holder.mRider.setText(itemTotal + " items (Rs. N/A)");

        if (mOrderInfo.getOrderType() == 1) {
            holder.mStatus.setVisibility(View.GONE);
            holder.mType.setText("Delivery");

        }//end if (orderType == 1)

        else if (mOrderInfo.getOrderType() == 0 || mOrderInfo.getOrderType() == 2) {

            //Checking If its status is paid
            holder.mType.setText("Pickup");
            holder.mStatus.setVisibility(View.VISIBLE);

            //Payment Status 200 OK
            if (paymentStatus == 0) {
                holder.mStatus.setText("Unpaid");
            } else if (paymentStatus == 1) {
                holder.mStatus.setText("Paid");
                holder.mStatus.setBackground(mCtx.getResources().getDrawable(R.drawable.paid_background));
            }

            if (mOrderInfo.getOrderType() == 2)  {
                holder.mType.setText("Deliver with own rider");
            }
        }

        String time = prepareList.get(position).getPickuptime();
        String[] minTime = time.split("T");
        Log.e(TAG, "onBindViewHolder: time 1 " + minTime[1]);
        Log.e(TAG, "onBindViewHolder: time 1 " + minTime[1]);

        holder.mOrderTime.setText(minTime[1].substring(0, minTime[1].length() - 8));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

    }

    @Override
    public int getItemCount() {
        return prepareList.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void AddData(List<OrdersItem> list) {

        prepareList.addAll(list);
        notifyDataSetChanged();

    }

    //my listener interface
    public interface itemClickListener {
        void onItemClick(int position);
    }

    public static class detailHolder extends RecyclerView.ViewHolder {
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
