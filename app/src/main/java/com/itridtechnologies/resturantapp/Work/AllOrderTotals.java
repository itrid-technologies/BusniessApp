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

import java.util.List;

public class AllOrderTotals extends Worker {


    //Variables

    ///list for database
    RoomDB databaseRoom;

    public AllOrderTotals(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        //initializing database
        databaseRoom = RoomDB.getInstance(context);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        insertAllOrderTotals(Constants.ORDER_TOTALS);
        return Result.success();
    }


    ///Insert OrderItem
    private void insertAllOrderTotals(List<OrderTotalsItem> order) {
        for (OrderTotalsItem orderTotals : order) {
            databaseRoom.mainDao().insertOrderTotals(orderTotals);
        }
    }

}
