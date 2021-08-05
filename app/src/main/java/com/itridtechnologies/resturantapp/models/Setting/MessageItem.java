package com.itridtechnologies.resturantapp.models.Setting;

import com.google.gson.annotations.SerializedName;

public class MessageItem{

	@SerializedName("delivery_fee")
	private String deliveryFee;

	@SerializedName("delivery_order_mode")
	private int deliveryOrderMode;

	@SerializedName("auto_accept_order_mode")
	private int autoAcceptOrderMode;

	@SerializedName("delivery_type")
	private int deliveryType;

	@SerializedName("busy_mode")
	private int busyMode;

	@SerializedName("pickup_order_mode")
	private int pickupOrderMode;

	@SerializedName("min_order")
	private String minOrder;

	public String getDeliveryFee(){
		return deliveryFee;
	}

	public int getDeliveryOrderMode(){
		return deliveryOrderMode;
	}

	public int getAutoAcceptOrderMode(){
		return autoAcceptOrderMode;
	}

	public int getDeliveryType(){
		return deliveryType;
	}

	public int getBusyMode(){
		return busyMode;
	}

	public int getPickupOrderMode(){
		return pickupOrderMode;
	}

	public String getMinOrder(){
		return minOrder;
	}
}