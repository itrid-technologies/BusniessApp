package com.itridtechnologies.resturantapp.models.orderHistory;

import com.google.gson.annotations.SerializedName;

public class AddonsItem{

	@SerializedName("addon_item_price")
	private String addonItemPrice;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("addon_name")
	private String addonName;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("addon_item_name")
	private String addonItemName;

	@SerializedName("order_items_addon_id")
	private int orderItemsAddonId;

	@SerializedName("id")
	private int id;

	public String getAddonItemPrice(){
		return addonItemPrice;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public String getAddonName(){
		return addonName;
	}

	public int getQuantity(){
		return quantity;
	}

	public String getDateModified(){
		return dateModified;
	}

	public String getAddonItemName(){
		return addonItemName;
	}

	public int getOrderItemsAddonId(){
		return orderItemsAddonId;
	}

	public int getId(){
		return id;
	}
}