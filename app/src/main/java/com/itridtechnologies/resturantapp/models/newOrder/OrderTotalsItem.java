package com.itridtechnologies.resturantapp.models.newOrder;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;

@Entity(tableName = "order_total_details",foreignKeys = @ForeignKey(entity = OrdersItem.class, parentColumns = "id", childColumns = "orderId"))
public class OrderTotalsItem{

	public OrderTotalsItem(int id, String label, int sortBy, String type, int orderId, String value) {
		this.id = id;
		this.label = label;
		this.sortBy = sortBy;
		this.type = type;
		this.orderId = orderId;
		this.value = value;
	}

	@PrimaryKey
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