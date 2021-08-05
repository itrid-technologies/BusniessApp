package com.itridtechnologies.resturantapp.models.MenuAddOns;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("max_label")
	private int maxLabel;

	@SerializedName("addonType")
	private int addonType;

	@SerializedName("availability")
	private int availability;

	@SerializedName("addon_id")
	private int addonId;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("children")
	private List<ChildrenItem> children;

	@SerializedName("name")
	private String name;

	@SerializedName("order_by")
	private int orderBy;

	@SerializedName("id")
	private int id;

	@SerializedName("business_id")
	private int businessId;

	@SerializedName("max_items")
	private int maxItems;

	@SerializedName("max_single_item")
	private int maxSingleItem;

	@SerializedName("status")
	private String status;

	public int getMaxLabel(){
		return maxLabel;
	}

	public int getAddonType(){
		return addonType;
	}

	public int getAvailability(){
		return availability;
	}

	public int getAddonId(){
		return addonId;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public String getDateModified(){
		return dateModified;
	}

	public List<ChildrenItem> getChildren(){
		return children;
	}

	public String getName(){
		return name;
	}

	public int getOrderBy(){
		return orderBy;
	}

	public int getId(){
		return id;
	}

	public int getBusinessId(){
		return businessId;
	}

	public int getMaxItems(){
		return maxItems;
	}

	public int getMaxSingleItem(){
		return maxSingleItem;
	}

	public String getStatus(){
		return status;
	}
}