package com.itridtechnologies.resturantapp.model;

public class TotalModel {
    private String mTotalName;
    private String mTotalAmount;

    public TotalModel(String mTotalName, String mTotalAmount) {
        this.mTotalName = mTotalName;
        this.mTotalAmount = mTotalAmount;
    }

    public String getmTotalName() {
        return mTotalName;
    }

    public String getmTotalAmount() {
        return mTotalAmount;
    }
}
