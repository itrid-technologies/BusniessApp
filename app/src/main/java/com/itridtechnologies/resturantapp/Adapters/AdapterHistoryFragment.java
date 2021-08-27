package com.itridtechnologies.resturantapp.Adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.NewHistory;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.historyNew.NewHistoryWithTotals;
import com.itridtechnologies.resturantapp.models.historyNew.ResultsItem;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class AdapterHistoryFragment extends RecyclerView.Adapter<AdapterHistoryFragment.detailHolder> implements AdapterHistoryFragmentt {

    private final List<ResultsItem> histList;
    private final Context mCtx;
    //variable for time
    private int givenCurrentMinutes;
    private itemClickListener mListenerr;
    private int givenCurrentHours;
    private int totalGivenTime;
    private Calendar cal;
    private SimpleDateFormat sdf;
    private PreferencesManager pm;
    private static final String TAG = "AdapterHistoryFragment";


    private final List<ResultsItem> searchList;


    public AdapterHistoryFragment(List<ResultsItem> histList, Context mCtx) {
        this.histList = histList;
        this.mCtx = mCtx;

        pm = new PreferencesManager(mCtx);

        searchList = new ArrayList<>(histList);
//        getting time zone to get time
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+05"));
        //taking given time on container
        givenCurrentMinutes = cal.get(Calendar.MINUTE);
        givenCurrentHours = cal.get(Calendar.HOUR);
        int givenCurrentSeconds = cal.get(Calendar.SECOND);
        totalGivenTime = (givenCurrentHours * 3600) + (givenCurrentMinutes * 60) + givenCurrentSeconds;

    }


    public void setOnItemClickListener(itemClickListener listener) {
        mListenerr = listener;
    }

    @NonNull
    @Override
    public AdapterHistoryFragment.detailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_container, parent, false);
        return new AdapterHistoryFragment.detailHolder(view, mListenerr);
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull AdapterHistoryFragment.detailHolder holder, int position) {
        ResultsItem mHistoryInfo = histList.get(position);
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        try {
//            Date time = sdf.parse(mHistoryInfo.getPickuptime());
//            Log.e(TAG, "onBindViewHolder: " + time);
//            long now = System.currentTimeMillis();
////            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
//            holder.mOrderTimeHistory.setText(String.valueOf(now));

//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        //Working Variables
        double mTotalValue = 0.00;

        //setting format for total price

        DecimalFormat format = new DecimalFormat("0.00");

        if (mHistoryInfo.getOrderTotal().size() > 0)
        {
            //CALCULATING TOTAL
            for (int j = 0; j < mHistoryInfo.getOrderTotal().size(); j++) {
                mTotalValue = Double.parseDouble(mHistoryInfo.getOrderTotal().get(j).getValue());
            }
            Log.e(TAG, "onResponse: " + mTotalValue);

        }

        String name = mHistoryInfo.getFirstName() + " " +
                mHistoryInfo.getLastName();

        Log.e(TAG, "onBindViewHolder: sdfg" + mHistoryInfo.getId() + name + mHistoryInfo.getItemCount());

        holder.mOrderNumberHistory.setText("#" + mHistoryInfo.getId() + "");
        holder.mCustomerNameHistory.setText(name);
        holder.mItemQuantityHistory.setText(mHistoryInfo.getItemCount() + "");
        holder.mPriceHistory.setText(format.format(mTotalValue));

    }

    @Override
    public int getItemCount() {
        return histList.size();
    }

    public void AddData(List<ResultsItem> list){

        histList.addAll(list);
        notifyDataSetChanged();

    }

    ///Filtering
    public Filter getFilter(){return filterItem;}

    private Filter filterItem = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ResultsItem> filteredList = new ArrayList<>();

            if (constraint == null) {
                filteredList.addAll(searchList);
                Log.e("TAG", "performFiltering: hey " );
            } else {
                String filterPattern = constraint.toString().trim();
                for (ResultsItem item : searchList) {
                    if (item.getPickuptime().trim().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            Log.e("TAG", "performFiltering: i m here");
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            histList.clear();
            histList.addAll((List) results.values);

            if (histList.isEmpty() || histList.size() == 0)
            {
                AppManager.toast("No Record Found");
                pm.saveMyDataBool("recordFound",false);
            }

            notifyDataSetChanged();
        }
    };


//my listener interface
    public interface itemClickListener {
        void onItemClick(int position);
    }


    public static class detailHolder extends RecyclerView.ViewHolder {
        private final TextView mOrderNumberHistory;
        private final TextView mCustomerNameHistory;
        private final TextView mOrderTimeHistory;
        private final TextView mItemQuantityHistory;
        private final TextView mPriceHistory;
        private final TextView mTextHistory;
        private final TextView mBracketHistory;
        private final ConstraintLayout mLayoutHistory;

        public detailHolder(@NonNull View itemView, final itemClickListener listener) {
            super(itemView);
            mOrderNumberHistory = itemView.findViewById(R.id.order_number_history);
            mCustomerNameHistory = itemView.findViewById(R.id.customer_name_history);
            mOrderTimeHistory = itemView.findViewById(R.id.order_time_history);
            mItemQuantityHistory = itemView.findViewById(R.id.item_quantity_history);
            mPriceHistory = itemView.findViewById(R.id.price_history);
            mTextHistory = itemView.findViewById(R.id.item_text_history);
            mBracketHistory = itemView.findViewById(R.id.end_bracket_history);
            mLayoutHistory = itemView.findViewById(R.id.inner_layout_order_history);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
