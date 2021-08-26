package com.itridtechnologies.resturantapp.models.OrderSubItems;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderAddonsItem{

	@SerializedName("addon_name")
	private String addonName;

	@SerializedName("addonItems")
	private List<AddonItemsItem> addonItems;

	@SerializedName("addon_id")
	private int addonId;

	public String getAddonName(){
		return addonName;
	}

	public List<AddonItemsItem> getAddonItems(){
		return addonItems;
	}

	public int getAddonId(){
		return addonId;
	}
}