package com.itridtechnologies.resturantapp.Work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.models.newOrder.OrderAddressItem;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllOrderAddress extends Worker {

    //Variables

    ///list for database
    RoomDB databaseRoom;


    public AllOrderAddress(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        //initializing database
        databaseRoom = RoomDB.getInstance(context);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        insertOrderAddress(Constants.ORDER_ADDRESS_ITEM);
        return Result.success();
    }


    ///Insert OrderItem
    private void insertOrderAddress(List<OrderAddressItem> order) {
        for (OrderAddressItem oAddress : order) {
            databaseRoom.mainDao().insertNewOrderAddress(oAddress);
        }
    }


}
