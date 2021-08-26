package com.itridtechnologies.resturantapp.ClassRoom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderAddressItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItemsItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderTotalsItem;

import java.util.List;

@Dao
public interface MainDao {
    //Insertion
    //Order Class
    //Pagination API
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewOrder(OrdersItem orderDao);

    //Insertion
    //Order Class
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewOrderAddress(OrderAddressItem orderDao);

    ///Insertion
    //Order Class
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrderTotals(OrderTotalsItem orderDao);

    ///Insertion
    //Order Class
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewOrderItemItem(OrderItemsItem orderDao);

//    ///Insertion
//    ///Order Names (Orders List 1)
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertNewOrderItems(DataItem orderItemDao);
//
//    //Insertion
//    ///Order Addon Names (Orders List 2 (Addon Names))
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertNewOrderAddons(OrderAddonsItem orderAddonNameDao);
//
//    //Insertion
//    ///Order Addon Items (Orders List 3 (Addon Items))
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertNewOrderAddonsItems(AddonItemsItem orderAddonNameDao);


    ////retrieving datat from id
    ///Data Pagination
    @Query("SELECT * FROM order_details WHERE id = :id")
    public OrdersItem getOrderById(int id);

    ////retrieving Address from id
    @Query("SELECT * FROM order_address_details WHERE orderId = :id")
    public OrderAddressItem getOrderAddressById(int id);

    ////retrieving Totals from id
    @Query("SELECT * FROM order_total_details WHERE orderId = :totalId AND type='sub_total'")
    public OrderTotalsItem getOrderTotalsById(int totalId);

    ////retrieving Totals from id
    @Query("SELECT * FROM order_item_details WHERE orderId = :orderId")
    List<OrderItemsItem> getOrderItemsById(int orderId);


    ////For pagination API
    @Query("SELECT * FROM order_details WHERE state!='Expired' AND state!='Preparing' AND state!='Ready' AND state!='Delivered'")
    List<OrdersItem> getAll();

//    ///List of 2nd API
//    @Query("SELECT * FROM order_details WHERE status!='Accepted' AND status!='Ready'")
//    List<OrdersItem> getAllPagination();
//

    @Query("SELECT * FROM order_details WHERE state='Preparing'")
    List<OrdersItem> getProcessOrders();


    @Query("SELECT * FROM order_details WHERE state='Ready'")
    List<OrdersItem> getReadyOrders();

}
