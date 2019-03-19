package com.zhaapps.app.pdfview;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.zhaapps.app.pdfview.activity.ActivitySplash;


public abstract class PermissionUtil {

    public static boolean isAllPermissionGranted(Activity activity) {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            String[] permission = ActivitySplash.ALL_REQUIRED_PERMISSION;
            if (permission.length == 0) return false;
            for (String s : permission) {
                if (ActivityCompat.checkSelfPermission(activity, s) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
