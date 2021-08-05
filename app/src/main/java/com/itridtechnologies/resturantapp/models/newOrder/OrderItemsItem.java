package com.itridtechnologies.resturantapp.models.newOrder;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;

@Entity(tableName = "order_item_details",foreignKeys = @ForeignKey(entity = OrdersItem.class, parentColumns = "id", childColumns = "orderId"))
public class OrderItemsItem{


	public OrderItemsItem(String dateAdded, String dateModified, String itemPrice, String itemName, String itemTotal, int id, String itemTax, int orderId, int itemQty) {
		this.dateAdded = dateAdded;
		this.dateModified = dateModified;
		this.itemPrice = itemPrice;
		this.itemName = itemName;
		this.itemTotal = itemTotal;
		this.id = id;
		this.itemTax = itemTax;
		this.orderId = orderId;
		this.itemQty = itemQty;
	}

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

	@PrimaryKey
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