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

public class AdapterPreparingDetails extends RecyclerView.Adapter<AdapterPreparingDetails.detailHolder>{

    //declaring variable
    private List<NewOrderDetails> new_order_list;
    private Context context;
    private int flag = 0;

    public AdapterPreparingDetails(List<NewOrderDetails> new_order_list, Context context) {
        this.new_order_list = new_order_list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterPreparingDetails.detailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_container,parent,false);
        return new AdapterPreparingDetails.detailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPreparingDetails.detailHolder holderr, int position) {
        NewOrderDetails mOrderInfoo = new_order_list.get(position);
        holderr.mOrderQuantity.setText(mOrderInfoo.getmOrderQuantity());
        holderr.mOrderPrice.setText(mOrderInfoo.getmOrderPrice());
        holderr.mOrderName.setText(mOrderInfoo.getmOrderName());

        //srtting Click listerner to see addons
        holderr.mOrderDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0)
                {
                    flag = 1;
                    holderr.mAddonDetails.setVisibility(View.VISIBLE);
                    holderr.mBottomView.setVisibility(View.INVISIBLE);
                }
                else if (flag == 1)
                {
                    flag = 0;
                    holderr.mBottomView.setVisibility(View.VISIBLE);
                    holderr.mAddonDetails.setVisibility(View.GONE);
                }
                holderr.mAddonDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holderr.mAddonDetails.setVisibility(View.GONE);
                        holderr.mBottomView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return new_order_list.size();
    }

    public class detailHolder extends RecyclerView.ViewHolder {

        private final TextView mOrderQuantity;
        private final TextView mOrderName;
        private final TextView mOrderPrice;
        private final ConstraintLayout mAddonDetails;
        private final View mBottomView;
        private final ConstraintLayout mOrderDetailContainer;

        public detailHolder(@NonNull View itemView) {
            super(itemView);
            mOrderName = itemView.findViewById(R.id.order_name);
            mOrderQuantity = itemView.findViewById(R.id.order_quantity);
            mOrderPrice = itemView.findViewById(R.id.order_price);
            mAddonDetails = itemView.findViewById(R.id.layout_addons);
            mOrderDetailContainer = itemView.findViewById(R.id.inner_layout_order);
            mBottomView = itemView.findViewById(R.id.view_bottom);
        }
    }
}
