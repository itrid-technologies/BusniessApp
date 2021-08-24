package com.itridtechnologies.resturantapp.models.login;

import com.google.gson.annotations.SerializedName;

public class Results{

	@SerializedName("business_name")
	private String businessName;

	@SerializedName("is_phone_verified")
	private int isPhoneVerified;

	@SerializedName("cover_photo")
	private String coverPhoto;

	@SerializedName("profile_photo")
	private String profilePhoto;

	@SerializedName("business_address")
	private String businessAddress;

	@SerializedName("online_status")
	private int onlineStatus;

	@SerializedName("is_email_verified")
	private int isEmailVerified;

	@SerializedName("price_range")
	private String priceRange;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("courier_note")
	private String courierNote;

	@SerializedName("customer_note")
	private String customerNote;

	@SerializedName("uuid")
	private String uuid;

	@SerializedName("delivery_type")
	private int deliveryType;

	@SerializedName("business_type")
	private String businessType;

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

	public int getIsPhoneVerified(){
		return isPhoneVerified;
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

	public int getIsEmailVerified(){
		return isEmailVerified;
	}

	public String getPriceRange(){
		return priceRange;
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

	public String getUuid(){
		return uuid;
	}

	public int getDeliveryType(){
		return deliveryType;
	}

	public String getBusinessType(){
		return businessType;
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