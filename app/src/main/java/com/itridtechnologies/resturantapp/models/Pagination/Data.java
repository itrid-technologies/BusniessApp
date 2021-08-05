package com.itridtechnologies.resturantapp.models.Pagination;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("orders")
	private List<OrdersItem> orders;

	public List<OrdersItem> getOrders(){
		return orders;
	}
}