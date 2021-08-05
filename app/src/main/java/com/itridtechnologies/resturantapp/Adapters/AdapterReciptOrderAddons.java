package com.itridtechnologies.resturantapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.Reciept;
import com.itridtechnologies.resturantapp.models.receiptOrder.AddonItemsItem;
import com.itridtechnologies.resturantapp.models.receiptOrder.OrderAddonsItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterReciptOrderAddons extends RecyclerView.Adapter<AdapterReciptOrderAddons.OrderSubItemsVH> {

    private final List<OrderAddonsItem> mAddonNames;
    //
//    private List<List<com.itridtechnologies.resturantapp.models.receiptOrder.AddonItemsItem>> mOrderAddonItemsList;
    private final Context mCtx;
    private final boolean haveSubAddonItems = false;

    public AdapterReciptOrderAddons(List<OrderAddonsItem> mAddonNames, Context mCtx) {
        this.mAddonNames = mAddonNames;
        this.mCtx = mCtx;
    }

    @NonNull
    @NotNull
    @Override
    public OrderSubItemsVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order_sub_items, parent, false);
        return new OrderSubItemsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderSubItemsVH holder, int position) {

//        List<OrderAddonsItem> orderAddonObjects = new ArrayList<>();
        OrderAddonsItem item = mAddonNames.get(position);

//        for (int i = 0; i < mAddonNames.size(); i++) {
//            orderAddonObjects.addAll(mAddonNames.get(i));
////        }
//        for (int i = 0; i < orderAddonObjects.size(); i++) {
//            Log.e("mAddonNames", "onBindViewHolder: " + orderAddonObjects.get(i).getAddonName());
//        }

//        OrderAddonsItem item = orderAddonObjects.get(position);

        //Set data
        holder.title.setText(item.getAddonName());
        List<AddonItemsItem> mOrderAddonItemsList = mAddonNames.get(position).getAddonItems();
//
//        //Order addon items List
//        getOrderAddonsItemList(mAddonNames);

        //if we have order sub-items
        //then we populate inner rv sub-items
        holder.rvAddOns.setLayoutManager(new LinearLayoutManager(mCtx));
        AdapterRecipetItems adapterAddonNames = new AdapterRecipetItems(mOrderAddonItemsList, mCtx);
        holder.rvAddOns.setHasFixedSize(true);
        holder.rvAddOns.setAdapter(adapterAddonNames);
    }
//
//    private void getOrderAddonsItemList(List<List<com.itridtechnologies.resturantapp.models.receiptOrder.OrderAddonsItem>> orderAddonItems) {
//
//        mOrderAddonItemsList = new ArrayList<>();
//
//        for (int i = 0; i < orderAddonItems.size(); i++) {
//
//            for (int j = 0; j < orderAddonItems.get(i).size(); j++) {
//                Log.e("TAG", "getOrderAddonsItemList: " + orderAddonItems.get(i).get(j).getAddonItems());
//                mOrderAddonItemsList.add(orderAddonItems.get(i).get(j).getAddonItems());
//            }
//
//        }//outer For
//    }

    @Override
    public int getItemCount() {
        return mAddonNames.size();
    }


    public interface ItemClickListenerOrder {
        void getSubItems(boolean hasSubItems, RecyclerView rvSubItems);
    }


    public class OrderSubItemsVH extends RecyclerView.ViewHolder {
        private final TextView title;
        private final RecyclerView rvAddOns;
        private boolean isVisible = true;

        public OrderSubItemsVH(@NonNull @NotNull View itemView) {
            super(itemView);

            //find views
            title = itemView.findViewById(R.id.tv_sub_item_title);
            rvAddOns = itemView.findViewById(R.id.rcv_item_addon);

        }
    }
}
