package com.itridtechnologies.resturantapp.model;

import java.util.ArrayList;
import java.util.List;

public class AddonModel {
    private String mAddonName;
    private int mAvailibility;
    List<com.itridtechnologies.resturantapp.models.MenuAddOns.AddonItem> addOnParent = new ArrayList<>();

    public AddonModel(String mAddonName, int mAvailibility, List<com.itridtechnologies.resturantapp.models.MenuAddOns.AddonItem> addOnParent) {
        this.mAddonName = mAddonName;
        this.mAvailibility = mAvailibility;
        this.addOnParent = addOnParent;
    }

    public String getmAddonName() {
        return mAddonName;
    }

    public int getmAvailibility() {
        return mAvailibility;
    }

    public List<com.itridtechnologies.resturantapp.models.MenuAddOns.AddonItem> getAddOnParent() {
        return addOnParent;
    }
}

