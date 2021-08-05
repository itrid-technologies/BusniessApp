package com.itridtechnologies.resturantapp.models.MenuCategories;

import com.google.gson.annotations.SerializedName;

public class ChildrenItem{

	@SerializedName("addon_available")
	private int addonAvailable;

	@SerializedName("description")
	private String description;

	@SerializedName("photo")
	private String photo;

	@SerializedName("discount_value")
	private Object discountValue;

	@SerializedName("tax")
	private String tax;

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

	@SerializedName("category_description")
	private String categoryDescription;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("date_modified")
	private String dateModified;

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

	@SerializedName("age_restriction")
	private int ageRestriction;

	@SerializedName("business_id")
	private int businessId;

	@SerializedName("status")
	private String status;

	public int getAddonAvailable(){
		return addonAvailable;
	}

	public String getDescription(){
		return description;
	}

	public String getPhoto(){
		return photo;
	}

	public Object getDiscountValue(){
		return discountValue;
	}

	public String getTax(){
		return tax;
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

	public String getCategoryDescription(){
		return categoryDescription;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public String getDateModified(){
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

	public int getAgeRestriction(){
		return ageRestriction;
	}

	public int getBusinessId(){
		return businessId;
	}

	public String getStatus(){
		return status;
	}
}