package com.itridtechnologies.resturantapp.Work;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.models.OrderSubItems.OrderAddonsItem;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class AllOrdersAddOnNames extends Worker {

    //Variables
    ///list for database
    RoomDB databaseRoom;
    private static final String TAG = "AllOrdersAddOnNames";

    public AllOrdersAddOnNames(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        //initializing database
        databaseRoom = RoomDB.getInstance(context);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        insertNewOrderAddonNames(Constants.ORDER_ADDON_NAME);
        return Result.success();
    }


    ///Insert OrderAddon Names
    private void insertNewOrderAddonNames(List<OrderAddonsItem> order) {
        for (OrderAddonsItem o : order) {
            Log.e(TAG, "insertAllOrders: Working" );
//            databaseRoom.mainDao().insertNewOrderAddons(o);
        }
    }


}
