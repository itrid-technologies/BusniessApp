package com.itridtechnologies.resturantapp.model;

public class OrderDetailModel {
    private String mQty;
    private String mOrderNamel;
    private String mPrice;

    public OrderDetailModel(String mQty, String mOrderNamel, String mPrice) {
        this.mQty = mQty;
        this.mOrderNamel = mOrderNamel;
        this.mPrice = mPrice;
    }

    public String getmQty() {
        return mQty;
    }

    public String getmOrderNamel() {
        return mOrderNamel;
    }

    public String getmPrice() {
        return mPrice;
    }
}
