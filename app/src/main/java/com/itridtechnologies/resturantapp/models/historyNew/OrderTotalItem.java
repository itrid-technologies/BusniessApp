package com.itridtechnologies.resturantapp.models.historyNew;

import com.google.gson.annotations.SerializedName;

public class OrderTotalItem{

	@SerializedName("id")
	private int id;

	@SerializedName("label")
	private String label;

	@SerializedName("sort_by")
	private int sortBy;

	@SerializedName("type")
	private String type;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("value")
	private String value;

	public int getId(){
		return id;
	}

	public String getLabel(){
		return label;
	}

	public int getSortBy(){
		return sortBy;
	}

	public String getType(){
		return type;
	}

	public int getOrderId(){
		return orderId;
	}

	public String getValue(){
		return value;
	}
}