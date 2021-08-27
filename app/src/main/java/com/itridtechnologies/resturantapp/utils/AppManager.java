package com.itridtechnologies.resturantapp.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.UiViews.Activities.MainActivity;
import com.itridtechnologies.resturantapp.models.ActionOrder.ActionResponse;
import com.itridtechnologies.resturantapp.models.OrderSubItems.OrderAddonsItem;
import com.itridtechnologies.resturantapp.models.login.LoginResponse;

import java.io.File;
import java.io.FileOutputStream;

public final class AppManager {

    private static final String TAG = "AppManager";

    public static void toast(String message) {
        Context context = Restaurant.getAppContext();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    ///Taking ScreenShot returning bitmap
    public static Bitmap screenshot(View view) {
        //Get the dimensions of the view so we can re-layout the view at its current size
        //and create a bitmap of the same size
        int width = view.getWidth();
        int height = view.getHeight();
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        //Cause the view to re-layout
        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        //Create a bitmap backed Canvas to draw the view into
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        //Now that the view is laid out and we have a canvas, ask the view to draw itself into the canvas
        view.draw(c);
        return b;
    }

    ///Saving Image in Gallery
    public static Uri save(Activity activity, Bitmap bmImg) {
        File filename;
        Uri result = null;
        try {
            String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + activity.getString(R.string.app_name);
            //File file = new File(path1 + "/" + activity.getString(R.string.app_name));
            File file = new File(path1);
            if (!file.exists())
                file.mkdir();
            String DEFAULT_IMAGE_NAME = System.currentTimeMillis() + "_" + "bqc";
            filename = new File(file.getAbsolutePath() + "/" + DEFAULT_IMAGE_NAME + ".jpg");
            //Log.i("in save()", "after file");
            FileOutputStream out = new FileOutputStream(filename);
            //Log.i("in save()", "after outputstream");
            bmImg.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            ContentValues image = getImageContent(filename, DEFAULT_IMAGE_NAME, activity);
            result = activity.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }//save

    ///Getting Image Content
    public static ContentValues getImageContent(File parent, String imageName, Activity activity) {
        ContentValues image = new ContentValues();
        image.put(MediaStore.Images.Media.TITLE, activity.getString(R.string.app_name));
        image.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        image.put(MediaStore.Images.Media.DESCRIPTION, "Best Quotes Collection -favourite img");
        image.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        image.put(MediaStore.Images.Media.MIME_TYPE, getMimeType(parent.getAbsolutePath()));
        image.put(MediaStore.Images.Media.ORIENTATION, 0);
        image.put(MediaStore.Images.ImageColumns.BUCKET_ID, parent.toString()
                .toLowerCase().hashCode());
        image.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, parent.getName()
                .toLowerCase());
        image.put(MediaStore.Images.Media.SIZE, parent.length());
        image.put(MediaStore.Images.Media.DATA, parent.getAbsolutePath());
        return image;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void intent(final Class<? extends Activity> ActivityToOpen) {
        Context context = Restaurant.getAppContext();
        Intent intent = new Intent(context, ActivityToOpen);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //logout
    public static void logout()
    {
        PreferencesManager pm = new PreferencesManager(Restaurant.getAppContext());
        pm.saveMyDataBool("login", false);
        pm.clearSharedPref();
        AppManager.intent(MainActivity.class);
    }//logout

    //for snackBar
    public static void SnackBar(AppCompatActivity activity, String msg){

        Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(activity.getResources().getColor(R.color.theme_color))
                .setTextColor(activity.getResources().getColor(R.color.white))
                .show();

    }


    public static void saveAddonList(OrderAddonsItem addon) {
        TinyDB tinDB = new TinyDB(Restaurant.getAppContext());
        tinDB.putObject(
                "addon", addon
        );
        Log.e(TAG, "saveBusinessDetails: saved !");
    }

    public static void saveBusinessDetails(LoginResponse data) {

        TinyDB tinyDB = new TinyDB(Restaurant.getAppContext());

        tinyDB.putObject
                (
                        Constants.KEY_BUSINESS_DETAILS, data
                );
        Log.e(TAG, "saveBusinessDetails: saved !");
    }

    public static OrderAddonsItem getAddonList()
    {
        TinyDB tinydb = new TinyDB(Restaurant.getAppContext());
        return tinydb.getObject(
                "addon", OrderAddonsItem.class
        );
    }

    public static LoginResponse getBusinessDetails() {

        TinyDB tinyDB = new TinyDB(Restaurant.getAppContext());

        return tinyDB.getObject
                (
                        Constants.KEY_BUSINESS_DETAILS, LoginResponse.class
                );
    }

    public static void saveActionDetails(ActionResponse data) {

        TinyDB tinyDataBase = new TinyDB(Restaurant.getAppContext());

        tinyDataBase.putObject
                (
                        Constants.KEY_ACTION_DETAILS, data
                );
        Log.e(TAG, "saveBusinessDetails: saved !");
    }

    public static ActionResponse getActionDetails() {

        TinyDB tinyDataBase = new TinyDB(Restaurant.getAppContext());

        return tinyDataBase.getObject
                (
                        Constants.KEY_ACTION_DETAILS, ActionResponse.class
                );
    }

    public static void hideStatusBar(Activity activity) {
        //Making status Bar Invisible
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setStatus(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setStatus(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setStatus(Activity activity, final int bit, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bit;
        } else {
            winParams.flags &= bit;
        }
        win.setAttributes(winParams);
    }

}//end
