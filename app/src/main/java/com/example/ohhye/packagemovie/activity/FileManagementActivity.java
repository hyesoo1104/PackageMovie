package com.example.ohhye.packagemovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.singletone_object.UploadQueue;
import com.example.ohhye.packagemovie.vo.UploadFile;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ohhye on 2015-01-26.
 */
public class FileManagementActivity  extends ActionBarActivity implements View.OnClickListener {
    public static String id ="";

    String url = "";
    String response ="";
    ArrayBlockingQueue<UploadFile> uploadQueue = null;

    private ImageView btn_file_add;
    private Button btn_file_upload;

    String path = Environment.getExternalStorageDirectory()+"/DCIM/Camera/20140614_045425.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_file);

        btn_file_add = (ImageView)findViewById(R.id.btn_file_add);
        btn_file_add.setOnClickListener(this);
        btn_file_upload = (Button)findViewById(R.id.btn_upload);
        btn_file_upload.setOnClickListener(this);
        url = this.getText(R.string.server_ip).toString();
        url = url+"uploadFile";

        uploadQueue = UploadQueue.getUploadQueue();
        Log.d("myTag",url);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_file_add:
                startActivity(new Intent(getApplication(), StreamingActivity.class));
            break;

            case R.id.btn_upload:
                //임시 Upload객체
                //String _id, String _path, String _date, Integer _size, String _running_time
                UploadFile temp = new UploadFile(id,path,"20150306",3,"running_time");
                try {
                    uploadQueue.put(temp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }




    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.finish_fade);
    }



}