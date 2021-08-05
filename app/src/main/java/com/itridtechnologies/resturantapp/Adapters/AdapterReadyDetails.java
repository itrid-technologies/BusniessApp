package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.NewOrderDetails;

import java.util.List;

public class AdapterReadyDetails extends RecyclerView.Adapter<AdapterReadyDetails.detailHolder>{

    //declaring variable
    private List<NewOrderDetails> new_order_list;
    private Context context;
    private int flag = 0;

    //object of listener
//    private onItemClickListener mListener;
    public AdapterReadyDetails(List<NewOrderDetails> new_order_list, Context context) {
        this.new_order_list = new_order_list;
        this.context = context;
    }
//
//    public void setOnItemClickListener(onItemClickListener listener) {
//        mListener = listener;
//    }
    @NonNull
    @Override
    public detailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_container,parent,false);
        return new AdapterReadyDetails.detailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull detailHolder holder, int position) {
        NewOrderDetails mOrderInfoo = new_order_list.get(position);
        holder.mOrderQuantity.setText(mOrderInfoo.getmOrderQuantity());
        holder.mOrderPrice.setText(mOrderInfoo.getmOrderPrice());
        holder.mOrderName.setText(mOrderInfoo.getmOrderName());

        //srtting Click listerner to see addons
        holder.mOrderDetailContainer.setOnClickListener(v -> {
            if (flag == 0)
            {
                flag = 1;
                holder.mAddonDetails.setVisibility(View.VISIBLE);
                holder.mBottomView.setVisibility(View.INVISIBLE);
            }
            else if (flag == 1)
            {
                flag = 0;
                holder.mBottomView.setVisibility(View.VISIBLE);
                holder.mAddonDetails.setVisibility(View.GONE);
            }
                holder.mAddonDetails.setOnClickListener(v1 -> {
                holder.mAddonDetails.setVisibility(View.GONE);
                holder.mBottomView.setVisibility(View.VISIBLE);
            });
        });
    }


    @Override
    public int getItemCount() {
        return new_order_list.size();
    }

//
//    //my listener interface
//    public interface onItemClickListener {
//        void onItemClick(int position);
//    }


    public class detailHolder extends RecyclerView.ViewHolder {

        private final TextView mOrderQuantity;
        private final TextView mOrderName;
        private final TextView mOrderPrice;
        private ConstraintLayout mAddonDetails;
        private final View mBottomView;
        private ConstraintLayout mOrderDetailContainer;

        public detailHolder(@NonNull View itemView) {
            super(itemView);
            mOrderName = itemView.findViewById(R.id.order_name);
            mOrderQuantity = itemView.findViewById(R.id.order_quantity);
            mOrderPrice = itemView.findViewById(R.id.order_price);
            mAddonDetails = itemView.findViewById(R.id.layout_addons);
            mOrderDetailContainer = itemView.findViewById(R.id.inner_layout_order);
            mBottomView = itemView.findViewById(R.id.view_bottom);

//
//            itemView.setOnClickListener(v -> {
//                if (listener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        listener.onItemClick(position);
//                    }
//                }
//            });

        }
    }
}
