package com.itridtechnologies.resturantapp.Adapters;

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
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.PreferencesManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class AdapterHistoryFragment extends RecyclerView.Adapter<AdapterHistoryFragment.detailHolder> implements AdapterHistoryFragmentt {

    private List<NewHistory> histList;
    private Context mCtx;
    //variable for time
    private int givenCurrentMinutes;
    private itemClickListener mListenerr;
    private int givenCurrentHours;
    private int totalGivenTime;
    private Calendar cal;
    private SimpleDateFormat sdf;
    private PreferencesManager pm;
    private static final String TAG = "AdapterHistoryFragment";


    private final List<NewHistory> searchList;


    public AdapterHistoryFragment(List<NewHistory> histList, Context mCtx) {
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

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull AdapterHistoryFragment.detailHolder holder, int position) {
        NewHistory mHistoryInfo = histList.get(position);
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long time = sdf.parse(mHistoryInfo.getmTime()).getTime();
            Log.e(TAG, "onBindViewHolder: " + time);
            long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            holder.mOrderTimeHistory.setText(ago);

            //split


        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mOrderNumberHistory.setText(mHistoryInfo.getmOrderNumber());
        holder.mCustomerNameHistory.setText(mHistoryInfo.getmCustomerName());
        holder.mItemQuantityHistory.setText(mHistoryInfo.getmItemCount());
        holder.mPriceHistory.setText(mHistoryInfo.getmPrice());
    }

    @Override
    public int getItemCount() {
        return histList.size();
    }


    ///Filtering
    public Filter getFilter(){return filterItem;}

    private Filter filterItem = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NewHistory> filteredList = new ArrayList<>();

            if (constraint == null) {
                filteredList.addAll(searchList);
                Log.e("TAG", "performFiltering: hey " );
            } else {
                String filterPattern = constraint.toString().trim();
                for (NewHistory item : searchList) {
                    if (item.getmTime().trim().contains(filterPattern)) {
                        filteredList.add(item);
                        Log.e("TAG", "performFiltering: "+item.getmTime() );
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


    public class detailHolder extends RecyclerView.ViewHolder {
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
