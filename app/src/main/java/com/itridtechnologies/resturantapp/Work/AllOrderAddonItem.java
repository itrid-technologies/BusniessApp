package com.itridtechnologies.resturantapp.Work;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.itridtechnologies.resturantapp.ClassRoom.RoomDB;
import com.itridtechnologies.resturantapp.models.OrderSubItems.AddonItemsItem;
import com.itridtechnologies.resturantapp.models.OrderSubItems.OrderAddonsItem;
import com.itridtechnologies.resturantapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class AllOrderAddonItem extends Worker {

    //Variables
    ///list for database
    RoomDB databaseRoom;

    public AllOrderAddonItem(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        //initializing database
        databaseRoom = RoomDB.getInstance(context);

    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        insertNewOrderAddonItems(Constants.ORDER_ADDON_ITEMS);
        return Result.success();
    }


    ///Insert OrderAddon Names
    private void insertNewOrderAddonItems(List<AddonItemsItem> order) {
        for (AddonItemsItem o : order) {
            Log.e(TAG, "insertAllOrders: Working" );
//            databaseRoom.mainDao().insertNewOrderAddonsItems(o);
        }
    }
}
