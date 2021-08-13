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
import com.itridtechnologies.resturantapp.models.OrderSubItems.AddonItemsItem;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterOrderAddOns extends RecyclerView.Adapter<AdapterOrderAddOns.detailHolderClass>{

    //declaring variable
    private List<AddonItemsItem> addOnName;
    private Context mCtx;

    public AdapterOrderAddOns(List<AddonItemsItem> addOnName, Context mCtx) {
        this.addOnName = addOnName;
        this.mCtx = mCtx;
    }

    @NonNull
    @NotNull
    @Override
    public detailHolderClass onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_addon_item_container,parent,false);
        return new detailHolderClass(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull detailHolderClass holder, int position) {
        AddonItemsItem mAddonItems = addOnName.get(position);
        DecimalFormat format = new DecimalFormat("0.00");

        holder.mAddonName.setText(mAddonItems.getAddonItemName());
        holder.mAddonPrice.setText("(" + Constants.CURRENCY_SIGN + "." + format.format(Double.parseDouble(mAddonItems.getAddonItemPrice())) + ")");

    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return addOnName.size();
    }

    public static class detailHolderClass extends RecyclerView.ViewHolder {
        private final TextView mAddonName;
        private final TextView mAddonPrice;
        public detailHolderClass(@NonNull @NotNull View itemView) {
            super(itemView);
            mAddonName = itemView.findViewById(R.id.tv_sub_item_name);
            mAddonPrice = itemView.findViewById(R.id.tv_sub_item_price);
        }
    }//detailHolderClass
}
