package com.itridtechnologies.resturantapp.models.newOrder;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;

@Entity(tableName = "order_address_details", foreignKeys = @ForeignKey(entity = OrdersItem.class, parentColumns = "id", childColumns = "orderId"))
public class OrderAddressItem{

	@SerializedName("address_line_1")
	private String addressLine1;//fine

	@SerializedName("address_line_2")
	private String addressLine2;//fine

	@SerializedName("city")
	private String city;//fine

	@SerializedName("post_code")
	private String postCode;//fine

	@SerializedName("state")
	private String state;//fine

	@SerializedName("country")
	private String country;//fine

	@SerializedName("date_added")
	private String dateAdded;//fine


	@SerializedName("date_modified")
	private String dateModified;//fine

	@SerializedName("latitude")
	private String latitude;//fine

	@PrimaryKey
	@SerializedName("id")
	private int id; //fine

	@SerializedName("type")
	private int type;//fine

	@SerializedName("order_id")
	private int orderId;//fine

	@SerializedName("longitude")
	private String longitude;//fine

	public OrderAddressItem(String addressLine1, String addressLine2, String city, String postCode, String state, String country, String dateAdded, String dateModified, String latitude, int id, int type, int orderId, String longitude) {
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.postCode = postCode;
		this.state = state;
		this.country = country;
		this.dateAdded = dateAdded;
		this.dateModified = dateModified;
		this.latitude = latitude;
		this.id = id;
		this.type = type;
		this.orderId = orderId;
		this.longitude = longitude;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getDateModified() {
		return dateModified;
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}