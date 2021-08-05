package com.itridtechnologies.resturantapp.model;

public class NewReady {
    private String mOrderNumber;
    private String mCustomerName;
    private String mType;
    private String mStatus;
    private String mRiderName;
    private String mArrivingTime;
    private String mCustNameToCollect;
    private String mItems;
    private String Price;

    public NewReady(String mOrderNumber, String mCustomerName, String mType, String mStatus, String mRiderName, String mArrivingTime, String mCustNameToCollect, String mItems, String price) {
        this.mOrderNumber = mOrderNumber;
        this.mCustomerName = mCustomerName;
        this.mType = mType;
        this.mStatus = mStatus;
        this.mRiderName = mRiderName;
        this.mArrivingTime = mArrivingTime;
        this.mCustNameToCollect = mCustNameToCollect;
        this.mItems = mItems;
        Price = price;
    }

    public String getmOrderNumber() {
        return mOrderNumber;
    }

    public String getmCustomerName() {
        return mCustomerName;
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

    public String getmArrivingTime() {
        return mArrivingTime;
    }

    public String getmCustNameToCollect() {
        return mCustNameToCollect;
    }

    public String getmItems() {
        return mItems;
    }

    public String getPrice() {
        return Price;
    }

}
