package com.example.ohhye.packagemovie.util.manager;

import android.annotation.SuppressLint;
import android.media.MediaMetadataRetriever;
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
    private String origin_path = "";

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



    public static void upload_record_file(String file_path,String file_name){
        String running_time = "";
        //재생시간 얻기
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(file_path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );
        long duration = timeInmillisec / 1000;
        running_time = Long.toString(duration);


        UploadFile temp = new UploadFile(LoginActivity.group_name,file_path,file_name,running_time);

        try {
            uploadQueue.put(temp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
