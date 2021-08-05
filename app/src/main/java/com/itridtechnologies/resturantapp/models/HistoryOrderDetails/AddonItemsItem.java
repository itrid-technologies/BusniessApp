package com.itridtechnologies.resturantapp.models.HistoryOrderDetails;

import com.google.gson.annotations.SerializedName;

public class AddonItemsItem{

	@SerializedName("addon_item_price")
	private String addonItemPrice;

	@SerializedName("addon_item_name")
	private String addonItemName;

	@SerializedName("qty")
	private int qty;

	public String getAddonItemPrice(){
		return addonItemPrice;
	}

	public String getAddonItemName(){
		return addonItemName;
	}

	public int getQty(){
		return qty;
	}
}