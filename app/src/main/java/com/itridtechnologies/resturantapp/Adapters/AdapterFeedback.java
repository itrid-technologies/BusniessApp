package com.itridtechnologies.resturantapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.models.feedbacktags.DataItem;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterFeedback extends RecyclerView.Adapter<AdapterFeedback.viewHolder> {

    private static final String TAG = "Cannot invoke method length() on null object";
    private final Context mCtx;
    private final List<DataItem> mFeedbackItems;
    private itemClickListener mListener;

    int selecteditem = -1;
    int lastItemSelected = -1;

    public AdapterFeedback(Context mCtx, List<DataItem> mFeedbackItems) {
        this.mCtx = mCtx;
        this.mFeedbackItems = mFeedbackItems;
    }

    public void setOnItemClickListener(itemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feedback, parent, false);
        return new viewHolder(view, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {

        DataItem feedback = mFeedbackItems.get(position);

        //setting text
        holder.mFeedback.setText(feedback.getDisplayName());

        if(position == selecteditem)
        {
            holder.mFeedback.setBackgroundResource(R.drawable.bg_selected_feedback);
        }
        else
        {
            holder.mFeedback.setBackgroundResource(R.drawable.bg_book_now_fields);
        }


    }//onBindViewHolder


    @Override
    public int getItemCount() {
        return mFeedbackItems.size();
    }//getItemCount

    //my listener interface
    public interface itemClickListener {
        void onItemClick(int position, String type);
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mFeedback;

        public viewHolder(@NonNull @NotNull View itemView, final itemClickListener listener) {
            super(itemView);

            mFeedback = itemView.findViewById(R.id.feedback);

            itemView.setOnClickListener(v -> {

                if (listener != null) {
                    int position = getAdapterPosition();
                    selecteditem = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, "item_click");

                        if(lastItemSelected == -1)
                            lastItemSelected = selecteditem;

                        else {
                            notifyItemChanged(lastItemSelected);
                            lastItemSelected = selecteditem;
                        }
                        notifyItemChanged(selecteditem);

                    }

                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
