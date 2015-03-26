package com.example.ohhye.packagemovie.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ohhye on 2015-03-26.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_PACKAGE_ADDED)){
            // 앱이 설치되었을 때
            Log.e("BootReceiver","Service Start  :  "+intent.getAction());
            Intent i = new Intent(".service.UploadBackgroundService");
            context.startService(i);

        } else if(action.equals(Intent.ACTION_PACKAGE_REMOVED)){
            Log.e("BootReceiver","Service Start  :  "+intent.getAction());
            // 앱이 삭제되었을 때
        } else if(action.equals(Intent.ACTION_PACKAGE_REPLACED)){
               // 앱이 업데이트 되었을 때
            Log.e("BootReceiver","Service Start  :  "+intent.getAction());
            Intent i = new Intent(".service.UploadBackgroundService");
            context.startService(i);
        }

        if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.e("BootReceiver","Service Start  :  "+intent.getAction());
            Intent i = new Intent(".service.UploadBackgroundService");
            context.startService(i);
        }
    }
}
