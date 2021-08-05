package com.itridtechnologies.resturantapp.model;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MenuModel {
    private String mItemName;
    private String mItemDescription;
    private String mPic=null;
    private int mYesNo;
    private int mMenuid;
    private String mAddOnAvailable;
    ArrayList<AddonModel> parentList = new ArrayList<>();
    private String mAvailabilityStatus;


    public MenuModel(String mItemName, String mItemDescription, String mPic, int mYesNo, int mMenuid, String mAddOnAvailable, ArrayList<AddonModel> parentList, String mAvailabilityStatus) {
        this.mItemName = mItemName;
        this.mItemDescription = mItemDescription;
        this.mPic = mPic;
        this.mYesNo = mYesNo;
        this.mMenuid = mMenuid;
        this.mAddOnAvailable = mAddOnAvailable;
        this.parentList = parentList;
        this.mAvailabilityStatus = mAvailabilityStatus;
    }


    public String getmItemName() {
        return mItemName;
    }

    public String getmItemDescription() {
        return mItemDescription;
    }

    public String getmPic() {
        return mPic;
    }

    public int getmYesNo() {
        return mYesNo;
    }

    public int getmMenuid() {
        return mMenuid;
    }

    public String getmAddOnAvailable() {
        return mAddOnAvailable;
    }

    public ArrayList<AddonModel> getParentList() {
        return parentList;
    }

    public String getmAvailabilityStatus() {
        return mAvailabilityStatus;
    }
}
