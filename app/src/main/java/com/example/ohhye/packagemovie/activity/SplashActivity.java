package com.example.ohhye.packagemovie.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.WindowManager;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.service.UploadBackgroundService;


public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if(isServiceRunning(".service.UploadBackgroundService")==false){
            Log.d("isServiceRunning","Not Running.. Now start");
            Intent serviceIntent = new Intent(this,UploadBackgroundService.class);
            startService(serviceIntent);

        }

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler() , 2000); // 3초 후에 hd Handler 실행
    }

    private class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            overridePendingTransition(R.anim.fade,R.anim.hold);
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }

    public Boolean isServiceRunning(String serviceName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
