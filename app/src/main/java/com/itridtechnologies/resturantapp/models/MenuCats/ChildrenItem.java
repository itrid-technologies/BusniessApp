package com.itridtechnologies.resturantapp.models.MenuCats;

import com.google.gson.annotations.SerializedName;

public class ChildrenItem{

	@SerializedName("addon_available")
	private Object addonAvailable;

	@SerializedName("description")
	private Object description;

	@SerializedName("photo")
	private Object photo;

	@SerializedName("discount_value")
	private Object discountValue;

	@SerializedName("availability")
	private int availability;

	@SerializedName("menu_category_id")
	private int menuCategoryId;

	@SerializedName("discount_type")
	private int discountType;

	@SerializedName("categoryName")
	private String categoryName;

	@SerializedName("is_discount")
	private int isDiscount;

	@SerializedName("menu_item_availability")
	private int menuItemAvailability;

	@SerializedName("category_description")
	private Object categoryDescription;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("date_modified")
	private Object dateModified;

	@SerializedName("price")
	private String price;

	@SerializedName("categoryStatus")
	private String categoryStatus;

	@SerializedName("name")
	private String name;

	@SerializedName("order_by")
	private int orderBy;

	@SerializedName("id")
	private int id;

	@SerializedName("business_id")
	private int businessId;

	@SerializedName("age_restriction")
	private int ageRestriction;

	@SerializedName("cat_availability")
	private int catAvailability;

	@SerializedName("status")
	private String status;

	public Object getAddonAvailable(){
		return addonAvailable;
	}

	public Object getDescription(){
		return description;
	}

	public Object getPhoto(){
		return photo;
	}

	public Object getDiscountValue(){
		return discountValue;
	}

	public int getAvailability(){
		return availability;
	}

	public int getMenuCategoryId(){
		return menuCategoryId;
	}

	public int getDiscountType(){
		return discountType;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public int getIsDiscount(){
		return isDiscount;
	}

	public int getMenuItemAvailability(){
		return menuItemAvailability;
	}

	public Object getCategoryDescription(){
		return categoryDescription;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public Object getDateModified(){
		return dateModified;
	}

	public String getPrice(){
		return price;
	}

	public String getCategoryStatus(){
		return categoryStatus;
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

	public int getAgeRestriction(){
		return ageRestriction;
	}

	public int getCatAvailability(){
		return catAvailability;
	}

	public String getStatus(){
		return status;
	}
}