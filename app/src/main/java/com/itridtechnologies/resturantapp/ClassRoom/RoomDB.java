package com.itridtechnologies.resturantapp.ClassRoom;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.itridtechnologies.resturantapp.models.Pagination.OrdersItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderAddressItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderItemsItem;
import com.itridtechnologies.resturantapp.models.newOrder.OrderTotalsItem;

////adding all the entities (Tables)
@Database(entities = {OrdersItem.class, OrderAddressItem.class, OrderItemsItem.class,
        OrderTotalsItem.class},
//        OrderTotalsItem.class, DataItem.class, OrderAddonsItem.class, AddonItemsItem.class},
        version = 5,
        exportSchema = false
)
public abstract class RoomDB extends RoomDatabase {
    ///Creating database instance
    private static RoomDB database;
    ///Define name of Database
    private static final String DATABASE_NAME = "ResturantDB";

    public synchronized static RoomDB getInstance(Context context)
    {
        ///Checking condition of null
        if(database == null)
        {
            ////If its null the initialize it
            database = Room.databaseBuilder(context.getApplicationContext()
            ,RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    ///Creating DAO (Database Access Object)
    public abstract MainDao mainDao();
}
