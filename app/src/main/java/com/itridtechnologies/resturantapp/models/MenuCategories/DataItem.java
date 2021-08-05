package com.itridtechnologies.resturantapp.models.MenuCategories;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("children")
	private List<ChildrenItem> children;

	@SerializedName("categoryStatus")
	private String categoryStatus;

	@SerializedName("name")
	private String name;

	@SerializedName("availability")
	private int availability;

	@SerializedName("categoryDescription")
	private Object categoryDescription;

	public List<ChildrenItem> getChildren(){
		return children;
	}

	public String getCategoryStatus(){
		return categoryStatus;
	}

	public String getName(){
		return name;
	}

	public int getAvailability(){
		return availability;
	}

	public Object getCategoryDescription(){
		return categoryDescription;
	}
}