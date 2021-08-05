package com.itridtechnologies.resturantapp.models.OrderSubItems;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import kotlin.jvm.JvmStatic;

//@Entity(tableName = "order_items")
public class DataItem{

	@SerializedName("orderAddons")
	private List<OrderAddonsItem> orderAddons;

	@ColumnInfo(name = "dateAdded")
	@SerializedName("date_added")
	private String dateAdded;

	@ColumnInfo(name = "dateModified")
	@SerializedName("date_modified")
	private String dateModified;

	@PrimaryKey
	@SerializedName("item_id")
	private int itemId;

	@ColumnInfo(name = "itemDiscountType")
	@SerializedName("item_discount_type")
	private int itemDiscountType;

	@ColumnInfo(name = "itemPrice")
	@SerializedName("item_price")
	private String itemPrice;

	@ColumnInfo(name = "itemName")
	@SerializedName("item_name")
	private String itemName;

	@ColumnInfo(name = "itemDiscountValue")
	@SerializedName("item_discount_value")
	private String itemDiscountValue;

	@ColumnInfo(name = "orderId")
	@SerializedName("order_id")
	private int orderId;

	@ColumnInfo(name = "itemQty")
	@SerializedName("item_qty")
	private int itemQty;

	public void setOrderAddons(List<OrderAddonsItem> orderAddons) {
		this.orderAddons = orderAddons;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setItemDiscountType(int itemDiscountType) {
		this.itemDiscountType = itemDiscountType;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setItemDiscountValue(String itemDiscountValue) {
		this.itemDiscountValue = itemDiscountValue;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}

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

	public String getItemDiscountValue(){
		return itemDiscountValue;
	}

	public int getOrderId(){
		return orderId;
	}

	public int getItemQty(){
		return itemQty;
	}

	@Override
	public String toString() {
		return "DataItem{" +
				"orderAddons=" + orderAddons +
				", dateAdded='" + dateAdded + '\'' +
				", dateModified='" + dateModified + '\'' +
				", itemId=" + itemId +
				", itemDiscountType=" + itemDiscountType +
				", itemPrice='" + itemPrice + '\'' +
				", itemName='" + itemName + '\'' +
				", itemDiscountValue='" + itemDiscountValue + '\'' +
				", orderId=" + orderId +
				", itemQty=" + itemQty +
				'}';
	}
}