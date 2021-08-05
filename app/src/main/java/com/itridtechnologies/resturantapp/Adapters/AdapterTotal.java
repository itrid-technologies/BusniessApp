package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.TotalModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterTotal extends RecyclerView.Adapter<AdapterTotal.detailHolderr>{

    private List<TotalModel> totalModelList = new ArrayList<>();
    private final Context mCtx;

    public AdapterTotal(List<TotalModel> totalModelList, Context mCtx) {
        this.totalModelList = totalModelList;
        this.mCtx = mCtx;
    }

    @NonNull
    @NotNull
    @Override
    public detailHolderr onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_container,parent,false);
        return new AdapterTotal.detailHolderr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull detailHolderr holder, int position) {
        TotalModel totalModel = totalModelList.get(position);
        holder.mSubTotalName.setText(totalModel.getmTotalName());
        holder.mSubTotalAmount.setText(totalModel.getmTotalAmount());

    }

    @Override
    public int getItemCount() {
        return totalModelList.size();
    }

    public class detailHolderr extends RecyclerView.ViewHolder {

        private TextView mSubTotalName;
        private TextView mSubTotalAmount;

        public detailHolderr(@NonNull @NotNull View itemView) {
            super(itemView);
            mSubTotalName = itemView.findViewById(R.id.tv_subtotal_name);
            mSubTotalAmount = itemView.findViewById(R.id.tv_subtotal_amount);
        }
    }
}
