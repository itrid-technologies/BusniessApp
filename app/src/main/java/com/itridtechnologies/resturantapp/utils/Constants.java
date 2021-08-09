package com.itridtechnologies.resturantapp.utils;

import com.itridtechnologies.resturantapp.models.OrderSubItems.AddonItemsItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.DataItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.OrderAddonsItem;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderAddressItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItemsItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderTotalsItem;

import java.util.ArrayList;
import java.util.List;

public final class Constants {
    public static final String BASE_URL="http://ec2-18-222-200-202.us-east-2.compute.amazonaws.com:3000/api/v1/";
    public static final String KEY_BUSINESS_DETAILS = "key_business";
    public static final String KEY_ACTION_DETAILS = "key_action";
    //List for new Orders
    //Pending Order from pagination API
    public static List<OrdersItem> ORDER_LIST = new ArrayList<>();
    //List for Order Address
    public static List<OrderAddressItem> ORDER_ADDRESS_ITEM = new ArrayList<>();
    //List for Order Totals
    public static List<OrderTotalsItem> ORDER_TOTALS = new ArrayList<>();
    //List for Order Items
    public static List<OrderItemsItem> ORDER_ITEM_ITEM = new ArrayList<>();
    //List of Pagination Orders
    public static List<OrdersItem> ORDER_PAGE_LIST = new ArrayList<>();


    ///List for storing Orders
    public static List<DataItem> ORDER_NAMES = new ArrayList<>();
    //List for storing Addons
    public static List<OrderAddonsItem> ORDER_ADDON_NAME = new ArrayList<>();
    //List for storing Addon Names
    public static List<AddonItemsItem> ORDER_ADDON_ITEMS = new ArrayList<>();

    //Orderitem object
    //Pagination API
    public static OrdersItem ORDER_ITEM = null;
    //OrderitemTotal object
    public static OrderTotalsItem ORDER_TOTAL_ITEM = null;
    //OrderitemAddress object
    public static OrderAddressItem ORDER_ADDRES_ITEM = null;

    ///Order Id From Notification
    public static String orderIdFromNotification;

    public static String CURRENCY_SIGN = "Rs.";

    public static String DEVICE_TOKEN = null;
}
