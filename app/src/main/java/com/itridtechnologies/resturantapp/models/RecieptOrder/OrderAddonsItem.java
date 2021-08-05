package com.itridtechnologies.resturantapp.models.RecieptOrder;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderAddonsItem{

	@SerializedName("addon_name")
	private String addonName;

	@SerializedName("addonItems")
	private List<AddonItemsItem> addonItems;

	@SerializedName("addon_id")
	private int addonId;

	public void setAddonName(String addonName){
		this.addonName = addonName;
	}

	public String getAddonName(){
		return addonName;
	}

	public void setAddonItems(List<AddonItemsItem> addonItems){
		this.addonItems = addonItems;
	}

	public List<AddonItemsItem> getAddonItems(){
		return addonItems;
	}

	public void setAddonId(int addonId){
		this.addonId = addonId;
	}

	public int getAddonId(){
		return addonId;
	}

	@Override
 	public String toString(){
		return 
			"OrderAddonsItem{" + 
			"addon_name = '" + addonName + '\'' + 
			",addonItems = '" + addonItems + '\'' + 
			",addon_id = '" + addonId + '\'' + 
			"}";
		}
}