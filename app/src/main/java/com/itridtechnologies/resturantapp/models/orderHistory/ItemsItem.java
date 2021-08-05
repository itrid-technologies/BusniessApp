package com.itridtechnologies.resturantapp.models.orderHistory;

import com.google.gson.annotations.SerializedName;

public class ItemsItem{

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("item_price")
	private String itemPrice;

	@SerializedName("item_name")
	private String itemName;

	@SerializedName("item_total")
	private String itemTotal;

	@SerializedName("id")
	private int id;

	@SerializedName("item_tax")
	private String itemTax;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("item_qty")
	private int itemQty;

	public String getDateAdded(){
		return dateAdded;
	}

	public String getDateModified(){
		return dateModified;
	}

	public String getItemPrice(){
		return itemPrice;
	}

	public String getItemName(){
		return itemName;
	}

	public String getItemTotal(){
		return itemTotal;
	}

	public int getId(){
		return id;
	}

	public String getItemTax(){
		return itemTax;
	}

	public int getOrderId(){
		return orderId;
	}

	public int getItemQty(){
		return itemQty;
	}
}