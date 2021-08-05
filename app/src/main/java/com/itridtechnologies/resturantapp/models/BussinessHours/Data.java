package com.itridtechnologies.resturantapp.models.BussinessHours;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("businessHours")
	private List<BusinessHoursItem> businessHours;

	public List<BusinessHoursItem> getBusinessHours(){
		return businessHours;
	}
}