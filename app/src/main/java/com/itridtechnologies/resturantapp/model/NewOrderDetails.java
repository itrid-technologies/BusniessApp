package com.itridtechnologies.resturantapp.model;

import android.widget.TextView;

public class NewOrderDetails {
    private String mOrderQuantity;
    private String mOrderName;
    private String mOrderPrice;

    public NewOrderDetails(String mOrderQuantity, String mOrderName, String mOrderPrice) {
        this.mOrderQuantity = mOrderQuantity;
        this.mOrderName = mOrderName;
        this.mOrderPrice = mOrderPrice;
    }

    public String getmOrderQuantity() {
        return mOrderQuantity;
    }

    public String getmOrderName() {
        return mOrderName;
    }

    public String getmOrderPrice() {
        return mOrderPrice;
    }
}
