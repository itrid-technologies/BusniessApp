package com.itridtechnologies.resturantapp.models.BussinessHours;

import com.google.gson.annotations.SerializedName;

public class BusinessHoursItem{

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("closing_time")
	private String closingTime;

	@SerializedName("opening_time")
	private String openingTime;

	@SerializedName("id")
	private int id;

	@SerializedName("business_id")
	private int businessId;

	@SerializedName("day")
	private String day;

	@SerializedName("is_closed")
	private int isClosed;

	public String getDateAdded(){
		return dateAdded;
	}

	public String getDateModified(){
		return dateModified;
	}

	public String getClosingTime(){
		return closingTime;
	}

	public String getOpeningTime(){
		return openingTime;
	}

	public int getId(){
		return id;
	}

	public int getBusinessId(){
		return businessId;
	}

	public String getDay(){
		return day;
	}

	public int getIsClosed(){
		return isClosed;
	}
}