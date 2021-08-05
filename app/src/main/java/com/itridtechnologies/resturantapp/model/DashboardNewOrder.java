package com.itridtechnologies.resturantapp.model;

public class DashboardNewOrder {
    private String mOrderNumber;
    private String mCustomerName;
    private String mItemCount;
    private String mPrice;
    private String mType;
    private String mStatus;
    private String mTime;


    public DashboardNewOrder(String mOrderNumber, String mCustomerName, String mItemCount, String mPrice, String mType, String mStatus, String mTime) {
        this.mOrderNumber = mOrderNumber;
        this.mCustomerName = mCustomerName;
        this.mItemCount = mItemCount;
        this.mPrice = mPrice;
        this.mType = mType;
        this.mStatus = mStatus;
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

    public String getmType() {
        return mType;
    }

    public String getmStatus() {
        return mStatus;
    }

    public String getmTime() {
        return mTime;
    }
}
