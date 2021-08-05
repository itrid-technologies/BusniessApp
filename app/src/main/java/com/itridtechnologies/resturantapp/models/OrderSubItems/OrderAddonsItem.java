package com.itridtechnologies.resturantapp.models.OrderSubItems;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import com.google.gson.annotations.SerializedName;

//@Entity(tableName = "order_addon_names")
public class OrderAddonsItem{

	@SerializedName("addon_name")
	private String addonName;

	@SerializedName("addonItems")
	private List<AddonItemsItem> addonItems;

	@PrimaryKey
	@SerializedName("addon_id")
	private int addonId;

	public void setAddonName(String addonName) {
		this.addonName = addonName;
	}

	public void setAddonItems(List<AddonItemsItem> addonItems) {
		this.addonItems = addonItems;
	}

	public void setAddonId(int addonId) {
		this.addonId = addonId;
	}

	public String getAddonName(){
		return addonName;
	}

	public List<AddonItemsItem> getAddonItems(){
		return addonItems;
	}

	public int getAddonId(){
		return addonId;
	}

	@Override
	public String toString() {
		return "OrderAddonsItem{" +
				"addonName='" + addonName + '\'' +
				", addonItems=" + addonItems +
				", addonId=" + addonId +
				'}';
	}
}