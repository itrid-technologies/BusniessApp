package com.itridtechnologies.resturantapp.model;

public class ProcessingOrder {
    private String mOrderNumber;
    private String mCustomerName;
    private String mItemCount;
    private String mPrice;
    private String mType;
    private String mStatus;
    private String mRiderName;
    private String mEstimatedTime;
    private int mRiderAllocated;

    public ProcessingOrder(String mOrderNumber, String mCustomerName, String mItemCount, String mPrice, String mType, String mStatus, String mRiderName, String mEstimatedTime, int mRiderAllocated) {
        this.mOrderNumber = mOrderNumber;
        this.mCustomerName = mCustomerName;
        this.mItemCount = mItemCount;
        this.mPrice = mPrice;
        this.mType = mType;
        this.mStatus = mStatus;
        this.mRiderName = mRiderName;
        this.mEstimatedTime = mEstimatedTime;
        this.mRiderAllocated = mRiderAllocated;
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

    public String getmRiderName() {
        return mRiderName;
    }

    public String getmEstimatedTime() {
        return mEstimatedTime;
    }

    public int getmRiderAllocated() {
        return mRiderAllocated;
    }

}
