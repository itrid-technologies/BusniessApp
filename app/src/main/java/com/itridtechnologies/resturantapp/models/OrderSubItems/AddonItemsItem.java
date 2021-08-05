package com.itridtechnologies.resturantapp.models.OrderSubItems;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

//@Entity(tableName = "order_addon_items")
public class AddonItemsItem{

	@SerializedName("addon_item_price")
	private String addonItemPrice;

	@SerializedName("addon_item_name")
	private String addonItemName;

	@PrimaryKey
	private int id;

	@SerializedName("qty")
	private int qty;

	public void setAddonItemPrice(String addonItemPrice) {
		this.addonItemPrice = addonItemPrice;
	}

	public void setAddonItemName(String addonItemName) {
		this.addonItemName = addonItemName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getId() {
		return id;
	}

	public String getAddonItemPrice(){
		return addonItemPrice;
	}

	public String getAddonItemName(){
		return addonItemName;
	}

	public int getQty(){
		return qty;
	}

	@Override
	public String toString() {
		return "AddonItemsItem{" +
				"addonItemPrice='" + addonItemPrice + '\'' +
				", addonItemName='" + addonItemName + '\'' +
				", id=" + id +
				", qty=" + qty +
				'}';
	}
}