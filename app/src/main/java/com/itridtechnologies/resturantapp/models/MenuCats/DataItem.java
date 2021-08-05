package com.itridtechnologies.resturantapp.models.MenuCats;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("children")
	private List<ChildrenItem> children;

	@SerializedName("categoryStatus")
	private String categoryStatus;

	@SerializedName("name")
	private String name;

	@SerializedName("cat_availability")
	private int catAvailability;

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

	public int getCatAvailability(){
		return catAvailability;
	}

	public Object getCategoryDescription(){
		return categoryDescription;
	}
}