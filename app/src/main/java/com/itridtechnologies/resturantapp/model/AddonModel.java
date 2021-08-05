package com.itridtechnologies.resturantapp.model;

import com.itridtechnologies.resturantapp.models.MenuAddOns.ChildrenItem;

import java.util.ArrayList;
import java.util.List;

public class AddonModel {
    private String mAddonName;
    private int mAvailibility;
    List<ChildrenItem> addOnParent = new ArrayList<>();

    public AddonModel(String mAddonName, int mAvailibility, List<ChildrenItem> addOnParent) {
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

    public List<ChildrenItem> getAddOnParent() {
        return addOnParent;
    }
}

