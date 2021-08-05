package com.itridtechnologies.resturantapp.models.newOrder;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

//@Entity(tableName = "order_details")
public class OrderItem {

    @SerializedName("pickuptime")
    private String pickuptime;

    @SerializedName("action_date")
    private String actionDate;

    @SerializedName("min_pre_time")
    private int minPreTime;

    @SerializedName("max_pre_time")
    private int maxPreTime;

    @SerializedName("courier_notes")
    private String courierNotes;

    @SerializedName("action")
    private int action;

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "order_total_price")
    @SerializedName("order_total_price")
    private String orderTotalPrice;

    @ColumnInfo(name = "order_type")
    @SerializedName("order_type")
    private int orderType;

    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    private String firstName;

    @SerializedName("business_rev_share")
    private String businessRevShare;

    @ColumnInfo(name = "item_count")
    @SerializedName("item_count")
    private int itemCount;

    @SerializedName("business_name")
    private String businessName;

    @SerializedName("business_notes")
    private String businessNotes;

    @ColumnInfo(name = "payment_status")
    @SerializedName("payment_status")
    private int paymentStatus;

    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    private String lastName;

    @SerializedName("otp")
    private int otp;

    @SerializedName("date_added")
    private String dateAdded;

    @ColumnInfo(name = "payment_type")
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

    @ColumnInfo(name = "status")
    @SerializedName("status")
    private String status;

    public String getPickuptime() {
        return pickuptime;
    }

    public int getMinPreTime() {
        return minPreTime;
    }

    public int getMaxPreTime() {
        return maxPreTime;
    }

    public String getCourierNotes() {
        return courierNotes;
    }

    public int getId() {
        return id;
    }

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public int getOrderType() {
        return orderType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getBusinessRevShare() {
        return businessRevShare;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessNotes() {
        return businessNotes;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public String getLastName() {
        return lastName;
    }

    public int getOtp() {
        return otp;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public int getDelay() {
        return delay;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public String getStatus() {
        return status;
    }

    public String getActionDate() {
        return actionDate;
    }

    public int getAction() {
        return action;
    }

    public OrderItem(String pickuptime, String actionDate, int minPreTime, int maxPreTime, String courierNotes, int action, int id, String orderTotalPrice, int orderType, String firstName, String businessRevShare, int itemCount, String businessName, String businessNotes, int paymentStatus, String lastName, int otp, String dateAdded, int paymentType, int delay, String dateModified, String phoneNumber, int customerId, int businessId, String status) {
        this.pickuptime = pickuptime;
        this.actionDate = actionDate;
        this.minPreTime = minPreTime;
        this.maxPreTime = maxPreTime;
        this.courierNotes = courierNotes;
        this.action = action;
        this.id = id;
        this.orderTotalPrice = orderTotalPrice;
        this.orderType = orderType;
        this.firstName = firstName;
        this.businessRevShare = businessRevShare;
        this.itemCount = itemCount;
        this.businessName = businessName;
        this.businessNotes = businessNotes;
        this.paymentStatus = paymentStatus;
        this.lastName = lastName;
        this.otp = otp;
        this.dateAdded = dateAdded;
        this.paymentType = paymentType;
        this.delay = delay;
        this.dateModified = dateModified;
        this.phoneNumber = phoneNumber;
        this.customerId = customerId;
        this.businessId = businessId;
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "pickuptime='" + pickuptime + '\'' +
                ", actionDate='" + actionDate + '\'' +
                ", minPreTime=" + minPreTime +
                ", maxPreTime=" + maxPreTime +
                ", courierNotes='" + courierNotes + '\'' +
                ", action=" + action +
                ", id=" + id +
                ", orderTotalPrice='" + orderTotalPrice + '\'' +
                ", orderType=" + orderType +
                ", firstName='" + firstName + '\'' +
                ", businessRevShare='" + businessRevShare + '\'' +
                ", itemCount=" + itemCount +
                ", businessName='" + businessName + '\'' +
                ", businessNotes='" + businessNotes + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", lastName='" + lastName + '\'' +
                ", otp=" + otp +
                ", dateAdded='" + dateAdded + '\'' +
                ", paymentType=" + paymentType +
                ", delay=" + delay +
                ", dateModified='" + dateModified + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", customerId=" + customerId +
                ", businessId=" + businessId +
                ", status='" + status + '\'' +
                '}';
    }
}