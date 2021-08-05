package com.itridtechnologies.resturantapp.models.HistoryOrderDetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("orderAddons")
	private List<OrderAddonsItem> orderAddons;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("item_id")
	private int itemId;

	@SerializedName("item_discount_type")
	private int itemDiscountType;

	@SerializedName("item_price")
	private String itemPrice;

	@SerializedName("item_name")
	private String itemName;

	@SerializedName("item_discount_value")
	private Object itemDiscountValue;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("item_qty")
	private int itemQty;

	public List<OrderAddonsItem> getOrderAddons(){
		return orderAddons;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public String getDateModified(){
		return dateModified;
	}

	public int getItemId(){
		return itemId;
	}

	public int getItemDiscountType(){
		return itemDiscountType;
	}

	public String getItemPrice(){
		return itemPrice;
	}

	public String getItemName(){
		return itemName;
	}

	public Object getItemDiscountValue(){
		return itemDiscountValue;
	}

	public int getOrderId(){
		return orderId;
	}

	public int getItemQty(){
		return itemQty;
	}
}