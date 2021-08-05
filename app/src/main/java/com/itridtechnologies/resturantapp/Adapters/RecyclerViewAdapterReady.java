package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.ReadyDetails;
import com.itridtechnologies.resturantapp.model.NewReady;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class RecyclerViewAdapterReady extends RecyclerView.Adapter<RecyclerViewAdapterReady.detailHolder> {

    //Declaring Variables
    private final List<NewReady> readyOrdersList;
    private final Context context;
    //variable for time
    private final int givenCurrentMinutes;
    private final int givenCurrentHours;
    private int value;
    private final int totalGivenTime;
    private final Calendar cal;
    /////Variables for Rider is assigned or not
    private int mRiderAssigned = 0;

    public RecyclerViewAdapterReady(List<NewReady> readyOrdersList, Context context) {
        this.readyOrdersList = readyOrdersList;
        this.context = context;
        //getting time zone to get time
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+05"));
        //taking given time on container
        givenCurrentMinutes = cal.get(Calendar.MINUTE);
        givenCurrentHours = cal.get(Calendar.HOUR);
        int givenCurrentSeconds = cal.get(Calendar.SECOND);
        totalGivenTime = ( givenCurrentHours * 3600 ) + ( givenCurrentMinutes * 60 ) + givenCurrentSeconds;
    }



    @NonNull
    @Override
    public RecyclerViewAdapterReady.detailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ready_container,parent,false);
        return new RecyclerViewAdapterReady.detailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReady.detailHolder holder, int position) {

        //
        String mDayTime = "AM";
       if (cal.get(Calendar.AM_PM) == Calendar.AM)
        {
            mDayTime = "AM";
        }
        else if (cal.get(Calendar.AM_PM) == Calendar.PM)
        {
            mDayTime = "PM";
        }
        String mOrderTime = givenCurrentHours + " : " + givenCurrentMinutes + " " + mDayTime;

        NewReady mReadyOrderInfo = readyOrdersList.get(position);
        holder.mOrderNumber.setText(mReadyOrderInfo.getmOrderNumber());
        holder.mTimeOfOrder.setText(mOrderTime);
        holder.mCustomerName.setText(mReadyOrderInfo.getmCustomerName());
        holder.mArrivingTime.setText(mReadyOrderInfo.getmArrivingTime());
        holder.mCustNameToCollect.setText(mReadyOrderInfo.getmCustNameToCollect());
        holder.mType.setText(mReadyOrderInfo.getmType());
        holder.mItems.setText(mReadyOrderInfo.getmItems());
        holder.mRiderName.setText(mReadyOrderInfo.getmRiderName());
        holder.mStatus.setText(mReadyOrderInfo.getmStatus());
        holder.mPrice.setText(mReadyOrderInfo.getPrice());


        //Making visible and invisble Paid/Unpaid box
        if (holder.mType.getText().toString().trim().equals("Delivery"))
        {
            holder.mStatus.setVisibility(View.INVISIBLE);
        }

        ///Creating intent ot go to details of ready orders
        holder.mContraintLayoutReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadyDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        //////set who is coming
        if (holder.mType.getText().toString().trim().equals("Pickup")) {
            holder.mLinearLayoutCustComing.setVisibility(View.VISIBLE);
            holder.mSearching.setVisibility(View.GONE);
        }
        else if (holder.mType.getText().toString().trim().equals("Self-Delivery"))
        {
            holder.mLinearLayoutSelfDelivery.setVisibility(View.VISIBLE);
            holder.mSearching.setVisibility(View.GONE);
        }
        else if (holder.mType.getText().toString().trim().equals("Delivery"))
        {
            if (mRiderAssigned == 0)
            {

            }
            else
            {
                holder.mLinearLayoutRiderComing.setVisibility(View.VISIBLE);
                holder.mSearching.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return readyOrdersList.size();
    }

    public class detailHolder extends RecyclerView.ViewHolder {
        private final TextView mOrderNumber;
        private final TextView mTimeOfOrder;
        private final TextView mCustomerName;
        private final TextView mType;
        private final TextView mStatus;
        private final TextView mRiderName;
        private final TextView mArrivingTime;
        private final TextView mCustNameToCollect;
        private final TextView mItems;
        private final TextView mPrice;
        private final TextView mSearching;
        private final ConstraintLayout mContraintLayoutReady;
        private final LinearLayout mLinearLayoutRiderComing;
        private final LinearLayout mLinearLayoutCustComing;
        private final LinearLayout mLinearLayoutSelfDelivery;

        public detailHolder(@NonNull View itemView) {
            super(itemView);
            mOrderNumber = itemView.findViewById(R.id.ready_order_number);
            mTimeOfOrder = itemView.findViewById(R.id.order_time_ready);
            mCustomerName = itemView.findViewById(R.id.customer_name_ready_TV);
            mType = itemView.findViewById(R.id.delivery_ready);
            mStatus = itemView.findViewById(R.id.status_ready);
            mRiderName = itemView.findViewById(R.id.rider_name);
            mArrivingTime = itemView.findViewById(R.id.coming_time_ready);
            mCustNameToCollect = itemView.findViewById(R.id.cust_name_ready_to_collect);
            mItems = itemView.findViewById(R.id.item_quantity_ready);
            mPrice = itemView.findViewById(R.id.price_ready);
            mSearching = itemView.findViewById(R.id.searching_rider);
            mContraintLayoutReady = itemView.findViewById(R.id.inner_layout_ready);
            mLinearLayoutRiderComing = itemView.findViewById(R.id.rider_coming_ready);
            mLinearLayoutCustComing = itemView.findViewById(R.id.waiting_customer);
            mLinearLayoutSelfDelivery = itemView.findViewById(R.id.items_price_ready);
        }
    }
}
