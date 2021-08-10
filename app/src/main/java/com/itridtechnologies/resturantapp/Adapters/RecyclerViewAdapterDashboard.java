package com.itridtechnologies.resturantapp.Adapters;

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

import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;
import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.GRAY;

public class RecyclerViewAdapterDashboard extends RecyclerView.Adapter<RecyclerViewAdapterDashboard.detailHolder> {
    private final List<OrdersItem> info_listtt;
    private final Context mCtx;
    private SimpleDateFormat sdf;
    private itemClickListener mListener;

    //Room Dataabase
    RoomDB databaseRoom;

    private static final String TAG = "RecyclerViewAdapterDash";


    //object of listener
//    private AdapterReadyDetails.onItemClickListener mListener;
    public RecyclerViewAdapterDashboard(Context mCtx, List<OrdersItem> info_listt) {
        this.mCtx = mCtx;
        this.info_listtt = info_listt;
        //Context for Room
        //initializing database
        databaseRoom = RoomDB.getInstance(mCtx);
    }


    public void setOnItemClickListener(itemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public detailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_container, parent, false);
        return new detailHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull detailHolder holder, int position) {

        Log.e(TAG, "onBindViewHolder: position________" + position);

        OrdersItem mOrderInfo = info_listtt.get(position);

        //Saving Data to pass
        //Variables to pass data
        String mCustomerName = mOrderInfo.getFirstName() + " " + mOrderInfo.getLastName();
        String mOrderNumber = String.valueOf(mOrderInfo.getId());
        String mTotalItems = String.valueOf(mOrderInfo.getItemCount());
        String mTotalAmount = "100.00";
        String mType = String.valueOf(mOrderInfo.getOrderType());
        String mStatus = String.valueOf(mOrderInfo.getPaymentStatus());


        //Payment Status
        if (mStatus.trim().equals("0")) {
            mStatus = "Unpaid";
        } else if (mStatus.trim().equals("1")) {
            mStatus = "Paid";
        }

        ///Order Type
        if (mType.trim().equals("0")) {
            mType = "Pickup";
        } else if (mType.trim().equals("1")) {
            mType = "Delivery";
        } else {
            mType = "Self-Delivery";
        }

        String time = info_listtt.get(position).getDateAdded();
        String[] minTime = time.split("T");
        Log.e(TAG, "onBindViewHolder: time 1 " + minTime[1] );
        Log.e(TAG, "onBindViewHolder: time 2 " + minTime[1].substring(0, minTime[1].length() - 8) );

        holder.mOrderTime.setText(minTime[1].substring(0, minTime[1].length() - 8));

        ///Setting data in Text fields On screen
        holder.mOrderNumber.setText(mOrderNumber);
        holder.mStatus.setText(mStatus);
        holder.mCustomerName.setText(mCustomerName);
        holder.mItemQuantity.setText(mTotalItems);
        holder.mPrice.setText(mTotalAmount);
        holder.mType.setText(mType);


        //making invisible payment status if order type is delivery
        if (holder.mType.getText().toString().trim().equals("Delivery")) {
            holder.mStatus.setVisibility(View.INVISIBLE);
        }
        //Checking If its status is paid
        if (holder.mStatus.getText().toString().equals("Paid")) {
            holder.mStatus.setBackground(mCtx.getResources().getDrawable(R.drawable.paid_background));
//            holder.mStatus.setBackgroundResource(R.drawable.paid_background);
        }

        holder.mRipplePulseLayout.startRippleAnimation();

    }



    @Override
    public int getItemCount() {
        return info_listtt.size();
    }

    //my listener interface
    public interface itemClickListener {
        void onItemClick(int position, String type);
    }

    public static class detailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mOrderNumber;
        private final TextView mCustomerName;
        private final TextView mOrderTime;
        private final TextView mItemQuantity;
        private final TextView mPrice;
        private final TextView mType;
        private final TextView mStatus;
        private final TextView mText;
        private final TextView mBracket;
        private final ConstraintLayout mLayout;
        private final ConstraintLayout mBottomLayout;
        private final RipplePulseLayout mRipplePulseLayout;

        public detailHolder(View itemView, final itemClickListener listener) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.recycler_container_dashboard);
            mOrderNumber = itemView.findViewById(R.id.order_number);
            mCustomerName = itemView.findViewById(R.id.customer_name);
            mOrderTime = itemView.findViewById(R.id.order_time);
            mItemQuantity = itemView.findViewById(R.id.item_quantity);
            mPrice = itemView.findViewById(R.id.price);
            mType = itemView.findViewById(R.id.delivery);
            mStatus = itemView.findViewById(R.id.status);
            mBottomLayout = itemView.findViewById(R.id.btm_layout_container_db);
            mText = itemView.findViewById(R.id.item_text);
            mBracket = itemView.findViewById(R.id.end_bracket);
            mRipplePulseLayout = itemView.findViewById(R.id.layout_ripplepulse);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, "item_click");
                    }

                    ///Changing Container View
                    mBottomLayout.setBackgroundResource(R.drawable.bottom_container);
                    mLayout.setBackgroundResource(R.drawable.bottom_container);
                    mOrderNumber.setTextColor(BLACK);
                    mType.setTextColor(BLACK);
                    mItemQuantity.setTextColor(GRAY);
                    mOrderTime.setTextColor(BLACK);
                    mPrice.setTextColor(Color.GRAY);
                    mCustomerName.setTextColor(Color.GRAY);
                    mText.setTextColor(Color.GRAY);
                    mBracket.setTextColor(Color.GRAY);
                    
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}

