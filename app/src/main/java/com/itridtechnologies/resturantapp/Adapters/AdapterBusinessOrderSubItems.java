package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.OrderSubItems.AddonItemsItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.OrderAddonsItem;


import java.util.List;

public class AdapterBusinessOrderSubItems extends RecyclerView.Adapter<AdapterBusinessOrderSubItems.OrderSubItemsVH> {

    private final List<OrderAddonsItem> mSubItemsList;
    private ItemClickListenerOrder mListener;
    private final Context mCtx;
    private final boolean haveSubAddonItems = false;

    public AdapterBusinessOrderSubItems(List<OrderAddonsItem> mSubItemsList, Context mCtx) {
        this.mSubItemsList = mSubItemsList;
        this.mCtx = mCtx;
    }

    public void setOnItemClickListener(ItemClickListenerOrder listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public OrderSubItemsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order_sub_items, parent, false);
        return new OrderSubItemsVH(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSubItemsVH holder, int position) {
        OrderAddonsItem item = mSubItemsList.get(position);
        //Set data
        holder.title.setText(item.getAddonName());

        //Giving list
        //Addons details
        List<AddonItemsItem> mOrderAddonDetails = mSubItemsList.get(position).getAddonItems();
        //Adapter
//        if (haveSubAddonItems) {
            //if we have order sub-items
            //then we populate inner rv sub-items
            holder.rvAddOns.setLayoutManager(new LinearLayoutManager(mCtx));
            AdapterOrderAddOns adapterAddonNames = new AdapterOrderAddOns(mOrderAddonDetails, mCtx);
            holder.rvAddOns.setHasFixedSize(true);
            holder.rvAddOns.setAdapter(adapterAddonNames);
//        }
    }

    @Override
    public int getItemCount() {
        return mSubItemsList.size();
    }


    public interface ItemClickListenerOrder {
        void getSubItems(boolean hasSubItems, RecyclerView rvSubItems);
    }


    public static class OrderSubItemsVH extends RecyclerView.ViewHolder {
        private final TextView title;
        private final RecyclerView rvAddOns;
        private boolean isVisible = true;

        public OrderSubItemsVH(@NonNull View itemView, ItemClickListenerOrder listenerr) {
            super(itemView);
            //find views
            title = itemView.findViewById(R.id.tv_sub_item_title);
            rvAddOns = itemView.findViewById(R.id.rcv_item_addon);

//            listenerr.getSubItems(true, rvAddOns);
//            //listener
//            itemView.setOnClickListener(v -> {
//                //handle rv visibility
//                if (!isVisible) {
//                    //rv visible
//                    rvAddOns.setVisibility(View.VISIBLE);
//                    isVisible = true;
//                    listenerr.getSubItems(true, rvAddOns);
//                } else {
//                    //rv gone
//                    rvAddOns.setVisibility(View.GONE);
//                    isVisible = false;
//                }
//            });

        }//const
    }//vh
}//end class
