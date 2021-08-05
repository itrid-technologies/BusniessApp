package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.ModiferModel;
import com.itridtechnologies.resturantapp.models.MenuAddOns.ChildrenItem;

import java.util.List;

public class AdapterModifer extends RecyclerView.Adapter<AdapterModifer.detailHolderr>{
    //declaring variable
    private final List<ModiferModel> modifierItems;
    private final Context mCtx;

    public AdapterModifer(List<ModiferModel> modifierItems, Context mCtx) {
        this.modifierItems = modifierItems;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public AdapterModifer.detailHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modifier_layout,parent,false);
        return new AdapterModifer.detailHolderr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterModifer.detailHolderr holder, int position) {
        ModiferModel mModifierItems = modifierItems.get(position);
        holder.mModifierName.setText(mModifierItems.getModifier());
    }

    @Override
    public int getItemCount() {
        return modifierItems.size();
    }

    public static class detailHolderr extends RecyclerView.ViewHolder {
        private final TextView mModifierName;
        public detailHolderr(@NonNull View itemView) {
            super(itemView);
            mModifierName = itemView.findViewById(R.id.modifier);
        }
    }
}
