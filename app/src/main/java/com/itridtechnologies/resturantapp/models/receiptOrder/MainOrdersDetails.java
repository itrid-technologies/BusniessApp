package com.itridtechnologies.resturantapp.models.receiptOrder;

import com.google.gson.annotations.SerializedName;

public class MainOrdersDetails {

	@SerializedName("pickuptime")
	private String pickuptime;

	@SerializedName("business_tax")
	private String businessTax;

	@SerializedName("action_date")
	private Object actionDate;

	@SerializedName("min_pre_time")
	private int minPreTime;

	@SerializedName("max_pre_time")
	private int maxPreTime;

	@SerializedName("courier_notes")
	private String courierNotes;

	@SerializedName("action")
	private Object action;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("order_type")
	private int orderType;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("business_rev_share")
	private String businessRevShare;

	@SerializedName("item_count")
	private int itemCount;

	@SerializedName("business_name")
	private String businessName;

	@SerializedName("business_notes")
	private String businessNotes;

	@SerializedName("payment_status")
	private int paymentStatus;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("otp")
	private int otp;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("payment_type")
	private int paymentType;

	@SerializedName("delay")
	private int delay;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("customer_id")
	private int customerId;

	@SerializedName("business_id")
	private int businessId;

	@SerializedName("status")
	private String status;

	public String getPickuptime(){
		return pickuptime;
	}

	public String getBusinessTax(){
		return businessTax;
	}

	public Object getActionDate(){
		return actionDate;
	}

	public int getMinPreTime(){
		return minPreTime;
	}

	public int getMaxPreTime(){
		return maxPreTime;
	}

	public String getCourierNotes(){
		return courierNotes;
	}

	public Object getAction(){
		return action;
	}

	public int getId(){
		return id;
	}

	public String getState(){
		return state;
	}

	public int getOrderType(){
		return orderType;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getBusinessRevShare(){
		return businessRevShare;
	}

	public int getItemCount(){
		return itemCount;
	}

	public String getBusinessName(){
		return businessName;
	}

	public String getBusinessNotes(){
		return businessNotes;
	}

	public int getPaymentStatus(){
		return paymentStatus;
	}

	public String getLastName(){
		return lastName;
	}

	public int getOtp(){
		return otp;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public int getPaymentType(){
		return paymentType;
	}

	public int getDelay(){
		return delay;
	}

	public String getDateModified(){
		return dateModified;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public int getCustomerId(){
		return customerId;
	}

	public int getBusinessId(){
		return businessId;
	}

	public String getStatus(){
		return status;
	}
}