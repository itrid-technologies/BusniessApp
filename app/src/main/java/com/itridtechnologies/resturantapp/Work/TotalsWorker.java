package com.itridtechnologies.resturantapp.Work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderTotalsItem;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class TotalsWorker extends Worker {
    //Variables

    ///list for database
    RoomDB databaseRoom;

    public TotalsWorker(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        //initializing database
        databaseRoom = RoomDB.getInstance(context);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        insertOrders(Constants.ORDER_TOTAL_ITEM);
        return Result.success();
    }


    private void insertOrders(OrderTotalsItem order) {
        databaseRoom.mainDao().insertOrderTotals(order);
    }
}
