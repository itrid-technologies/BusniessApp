package com.itridtechnologies.resturantapp.model;

public class AddOnItemModel {
    private String mAddonItemName;
    private String mAddonItemPrice;

    public AddOnItemModel(String mAddonItemName, String mAddonItemPrice) {
        this.mAddonItemName = mAddonItemName;
        this.mAddonItemPrice = mAddonItemPrice;
    }

    public String getmAddonItemName() {
        return mAddonItemName;
    }

    public String getmAddonItemPrice() {
        return mAddonItemPrice;
    }
}
