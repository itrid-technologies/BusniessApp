package com.itridtechnologies.resturantapp.models.newOrder;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;

@Entity(tableName = "order_address_details", foreignKeys = @ForeignKey(entity = OrdersItem.class, parentColumns = "id", childColumns = "orderId"))
public class OrderAddressItem{

	public OrderAddressItem(String dateAdded, String address, String dateModified, String latitude, int id, int type, int orderId, String longitude) {
		this.dateAdded = dateAdded;
		this.address = address;
		this.dateModified = dateModified;
		this.latitude = latitude;
		this.id = id;
		this.type = type;
		this.orderId = orderId;
		this.longitude = longitude;
	}

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("address")
	private String address;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("latitude")
	private String latitude;

	@PrimaryKey
	@SerializedName("id")
	private int id;

	@SerializedName("type")
	private int type;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("longitude")
	private String longitude;

	public String getDateAdded(){
		return dateAdded;
	}

	public String getAddress(){
		return address;
	}

	public String getDateModified(){
		return dateModified;
	}

	public String getLatitude(){
		return latitude;
	}

	public int getId(){
		return id;
	}

	public int getType(){
		return type;
	}

	public int getOrderId(){
		return orderId;
	}

	public String getLongitude(){
		return longitude;
	}
}