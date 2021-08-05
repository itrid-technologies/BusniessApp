package com.itridtechnologies.resturantapp.models.orderHistory;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("orders")
	private List<OrdersItem> orders;

	@SerializedName("totalCount")
	private int totalCount;

	public List<OrdersItem> getOrders(){
		return orders;
	}

	public int getTotalCount(){
		return totalCount;
	}
}