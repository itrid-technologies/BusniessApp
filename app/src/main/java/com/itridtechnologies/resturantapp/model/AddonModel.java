package com.itridtechnologies.resturantapp.model;

import com.itridtechnologies.resturantapp.models.MenuAddOns.AddonItem;

import java.util.ArrayList;
import java.util.List;

public class AddonModel {
    private int id;
    private String mAddonName;
    private int mAvailibility;
    List<com.itridtechnologies.resturantapp.models.MenuAddOns.AddonItem> addOnParent = new ArrayList<>();

    public AddonModel(int id, String mAddonName, int mAvailibility, List<AddonItem> addOnParent) {
        this.id = id;
        this.mAddonName = mAddonName;
        this.mAvailibility = mAvailibility;
        this.addOnParent = addOnParent;
    }

    public int getId() {
        return id;
    }

    public String getmAddonName() {
        return mAddonName;
    }

    public int getmAvailibility() {
        return mAvailibility;
    }

    public List<AddonItem> getAddOnParent() {
        return addOnParent;
    }

    public void setmAvailibility(int mAvailibility) {
        this.mAvailibility = mAvailibility;
    }
}

