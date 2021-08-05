package com.itridtechnologies.resturantapp.models.login;

import com.google.gson.annotations.SerializedName;

public class Results{

	@SerializedName("business_name")
	private String businessName;

	@SerializedName("cover_photo")
	private String coverPhoto;

	@SerializedName("profile_photo")
	private String profilePhoto;

	@SerializedName("business_address")
	private String businessAddress;

	@SerializedName("online_status")
	private int onlineStatus;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("courier_note")
	private String courierNote;

	@SerializedName("customer_note")
	private String customerNote;

	@SerializedName("business_description")
	private String businessDescription;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("id")
	private int id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	public String getBusinessName(){
		return businessName;
	}

	public String getCoverPhoto(){
		return coverPhoto;
	}

	public String getProfilePhoto(){
		return profilePhoto;
	}

	public String getBusinessAddress(){
		return businessAddress;
	}

	public int getOnlineStatus(){
		return onlineStatus;
	}

	public String getLastName(){
		return lastName;
	}

	public String getCourierNote(){
		return courierNote;
	}

	public String getCustomerNote(){
		return customerNote;
	}

	public String getBusinessDescription(){
		return businessDescription;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public int getId(){
		return id;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getEmail(){
		return email;
	}

	public String getStatus(){
		return status;
	}
}