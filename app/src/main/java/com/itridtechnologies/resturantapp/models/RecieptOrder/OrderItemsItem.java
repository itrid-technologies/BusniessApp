package com.itridtechnologies.resturantapp.models.RecieptOrder;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderItemsItem{

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

	public void setOrderAddons(List<OrderAddonsItem> orderAddons){
		this.orderAddons = orderAddons;
	}

	public List<OrderAddonsItem> getOrderAddons(){
		return orderAddons;
	}

	public void setDateAdded(String dateAdded){
		this.dateAdded = dateAdded;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public void setDateModified(String dateModified){
		this.dateModified = dateModified;
	}

	public String getDateModified(){
		return dateModified;
	}

	public void setItemId(int itemId){
		this.itemId = itemId;
	}

	public int getItemId(){
		return itemId;
	}

	public void setItemDiscountType(int itemDiscountType){
		this.itemDiscountType = itemDiscountType;
	}

	public int getItemDiscountType(){
		return itemDiscountType;
	}

	public void setItemPrice(String itemPrice){
		this.itemPrice = itemPrice;
	}

	public String getItemPrice(){
		return itemPrice;
	}

	public void setItemName(String itemName){
		this.itemName = itemName;
	}

	public String getItemName(){
		return itemName;
	}

	public void setItemDiscountValue(Object itemDiscountValue){
		this.itemDiscountValue = itemDiscountValue;
	}

	public Object getItemDiscountValue(){
		return itemDiscountValue;
	}

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return orderId;
	}

	public void setItemQty(int itemQty){
		this.itemQty = itemQty;
	}

	public int getItemQty(){
		return itemQty;
	}

	@Override
 	public String toString(){
		return 
			"OrderItemsItem{" + 
			"orderAddons = '" + orderAddons + '\'' + 
			",date_added = '" + dateAdded + '\'' + 
			",date_modified = '" + dateModified + '\'' + 
			",item_id = '" + itemId + '\'' + 
			",item_discount_type = '" + itemDiscountType + '\'' + 
			",item_price = '" + itemPrice + '\'' + 
			",item_name = '" + itemName + '\'' + 
			",item_discount_value = '" + itemDiscountValue + '\'' + 
			",order_id = '" + orderId + '\'' + 
			",item_qty = '" + itemQty + '\'' + 
			"}";
		}
}