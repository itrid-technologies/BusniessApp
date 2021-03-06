package com.itridtechnologies.resturantapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.TotalModel;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull detailHolderr holder, int position) {
        TotalModel totalModel = totalModelList.get(position);
        DecimalFormat format = new DecimalFormat("0.00");

        if (position == totalModelList.size()-1)
        {
            holder.mSubTotalAmount.setTypeface(null, Typeface.BOLD);
            holder.mSubTotalName.setTypeface(null, Typeface.BOLD);
        }

        holder.mSubTotalName.setText(totalModel.getmTotalName());
        holder.mSubTotalAmount.setText(Constants.CURRENCY_SIGN + " " + format.format(Double.parseDouble(totalModel.getmTotalAmount())));

    }

    @Override
    public int getItemCount() {
        return totalModelList.size();
    }

    public static class detailHolderr extends RecyclerView.ViewHolder {

        private final TextView mSubTotalName;
        private final TextView mSubTotalAmount;

        public detailHolderr(@NonNull @NotNull View itemView) {
            super(itemView);
            mSubTotalName = itemView.findViewById(R.id.tv_subtotal_name);
            mSubTotalAmount = itemView.findViewById(R.id.tv_subtotal_amount);
        }
    }
}
