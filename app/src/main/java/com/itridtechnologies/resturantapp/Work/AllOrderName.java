package com.itridtechnologies.resturantapp.Work;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.models.OrderSubItems.DataItem;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class AllOrderName extends Worker {
    //Variables
    ///list for database
    RoomDB databaseRoom;

    public AllOrderName(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        //initializing database
        databaseRoom = RoomDB.getInstance(context);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        insertAllOrdersNames(Constants.ORDER_NAMES);
        return Result.success();
    }

    ///Insert OrderItem
    private void insertAllOrdersNames(List<DataItem> order) {
        for (DataItem o : order) {
            Log.e(TAG, "insertAllOrders: Working" );
//            databaseRoom.mainDao().insertNewOrderItems(o);
        }
    }


}
