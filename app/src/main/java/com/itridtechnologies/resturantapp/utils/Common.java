package com.itridtechnologies.resturantapp.utils;

import android.content.Context;

import com.itridtechnologies.resturantapp.R;

import java.io.File;

public class Common {
    public static String getAppPath(Context context)
    {
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator
        );

        if(!dir.exists())
            dir.mkdir();
        return dir.getParent() + File.separator;
    }
}
