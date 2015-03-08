package com.example.ohhye.packagemovie.util.manager;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.example.ohhye.packagemovie.activity.LoginActivity;
import com.example.ohhye.packagemovie.singletone_object.UploadQueue;
import com.example.ohhye.packagemovie.vo.UploadFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ohhye on 2015-02-24.
 */
public class FileManager {
    @SuppressLint("SimpleDateFormat")
    private static ArrayBlockingQueue<UploadFile> uploadQueue = UploadQueue.getUploadQueue();

    public static File getOutputMediaFile() {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                "PackageMovie");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                 + timeStamp + ".mp4");

        return mediaFile;
    }

    public static void upload_record_file(String file_path){
        UploadFile temp = new UploadFile(LoginActivity.group_name,file_path,"20150306",3,"running_time");
        try {
            uploadQueue.put(temp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
