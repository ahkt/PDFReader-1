package com.zhaapps.app.pdfview.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.zhaapps.app.pdfview.PermissionUtil;
import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.data.GlobalVariable;
import com.zhaapps.app.pdfview.data.Tools;

import java.util.TimerTask;

public class ActivitySplash extends AppCompatActivity {
    private GlobalVariable global;
    TimerTask task;
    // for permission android M (6.0)
    public static String[] ALL_REQUIRED_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        global = (GlobalVariable) getApplication();
        //coloringBackground();
        try {
            copyAssetPdfForSample();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!PermissionUtil.isAllPermissionGranted(ActivitySplash.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(ALL_REQUIRED_PERMISSION, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
            startActivity(i);
            finish();
        }
    }


    public void copyAssetPdfForSample() {
        if (global.getBooleanPref(GlobalVariable.B_KEY_FIRST_LAUNCH, true)) {
            Tools.copyAssets(getApplicationContext());
            //Toast.makeText(getApplicationContext(), "FIST", Toast.LENGTH_SHORT).show();
            global.setBooleanPref(GlobalVariable.B_KEY_FIRST_LAUNCH, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtil.isAllPermissionGranted(ActivitySplash.this)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    // go to the main activity
                    Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                    startActivity(i);
                    finish();
                }
            }, 3000);
        }
    }
}
