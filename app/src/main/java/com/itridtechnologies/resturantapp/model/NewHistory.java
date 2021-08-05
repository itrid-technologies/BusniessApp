package com.itridtechnologies.resturantapp.model;

public class NewHistory {
    private String mOrderNumber;
    private String mCustomerName;
    private String mItemCount;
    private String mPrice;
    private String mTime;

    public NewHistory(String mOrderNumber, String mCustomerName, String mItemCount, String mPrice, String mTime) {
        this.mOrderNumber = mOrderNumber;
        this.mCustomerName = mCustomerName;
        this.mItemCount = mItemCount;
        this.mPrice = mPrice;
        this.mTime = mTime;
    }

    public String getmOrderNumber() {
        return mOrderNumber;
    }

    public String getmCustomerName() {
        return mCustomerName;
    }

    public String getmItemCount() {
        return mItemCount;
    }

    public String getmPrice() {
        return mPrice;
    }

    public String getmTime() {
        return mTime;
    }
}
