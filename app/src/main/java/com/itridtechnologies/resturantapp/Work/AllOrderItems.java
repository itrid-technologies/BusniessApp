package com.itridtechnologies.resturantapp.Work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItemsItem;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllOrderItems extends Worker {


    //Variables

    ///list for database
    RoomDB databaseRoom;

    public AllOrderItems(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        //initializing database
        databaseRoom = RoomDB.getInstance(context);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        insertAllOrderItems(Constants.ORDER_ITEM_ITEM);
        return Result.success();
    }


    ///Insert OrderItem
    private void insertAllOrderItems(List<OrderItemsItem> order) {
        for (OrderItemsItem orderItem : order) {
            databaseRoom.mainDao().insertNewOrderItemItem(orderItem);
        }
    }


}
