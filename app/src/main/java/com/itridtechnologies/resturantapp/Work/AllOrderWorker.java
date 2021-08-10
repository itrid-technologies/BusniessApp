package com.itridtechnologies.resturantapp.Work;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderAddressItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItem;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllOrderWorker extends Worker {

    //Variables
    ///list for database
    RoomDB databaseRoom;
    private static final String TAG = "AllOrderWorker";

    public AllOrderWorker(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        //initializing database
        databaseRoom = RoomDB.getInstance(context);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        insertAllOrders(Constants.ORDER_PAGE_LIST);
        return Result.success();
    }

    ///Insert OrderItem
    private void insertAllOrders(List<OrdersItem> order) {
        for (OrdersItem o : order) {
            Log.e(TAG, "insertAllOrders: Working" );
            databaseRoom.mainDao().insertNewOrder(o);
        }
    }

}
