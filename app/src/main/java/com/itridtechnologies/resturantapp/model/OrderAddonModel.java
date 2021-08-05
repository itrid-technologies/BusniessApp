package com.itridtechnologies.resturantapp.model;

public class OrderAddonModel {
    private String mAddOnTitle;
    private String mAddOnName;
    private String mAddOnPrice;

    public OrderAddonModel(String mAddOnTitle, String mAddOnName, String mAddOnPrice) {
        this.mAddOnTitle = mAddOnTitle;
        this.mAddOnName = mAddOnName;
        this.mAddOnPrice = mAddOnPrice;
    }

    public String getmAddOnTitle() {
        return mAddOnTitle;
    }

    public String getmAddOnName() {
        return mAddOnName;
    }

    public String getmAddOnPrice() {
        return mAddOnPrice;
    }
}
