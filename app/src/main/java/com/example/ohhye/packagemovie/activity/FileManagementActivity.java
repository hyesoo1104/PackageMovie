package com.example.ohhye.packagemovie.activity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.singletone_object.UploadQueue;
import com.example.ohhye.packagemovie.vo.UploadFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ohhye on 2015-01-26.
 */
public class FileManagementActivity  extends ActionBarActivity implements View.OnClickListener {
    public static String id ="";

    File file;

    private DownloadManager mDownloadManager; //다운로드 매니저.
    private int mDownloadQueueId; //다운로드 큐 아이디..
    private String mFileName ; //파일다운로드 완료후...파일을 열기 위해 저장된 위치를 입력해둔다.

    private ProgressDialog progressDialog;
    public static final int progressbarType = 0;

    String server_ip = "";
    String upload_url = "";
    String download_url = "http://210.118.74.131:8080/PackageMovie/downloadFile/test1/test3"; //test1 = group_id, test3 = video_name
    String response ="";
    ArrayBlockingQueue<UploadFile> uploadQueue = null;

    private ImageView btn_file_add;
    private Button btn_file_upload;
    private Button btn_file_download;

    String path = Environment.getExternalStorageDirectory()+"/DCIM/Camera/20140614_045425.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_file);

        btn_file_add = (ImageView)findViewById(R.id.btn_file_add);
        btn_file_add.setOnClickListener(this);
        btn_file_upload = (Button)findViewById(R.id.btn_upload);
        btn_file_upload.setOnClickListener(this);
        btn_file_download = (Button)findViewById(R.id.btn_download);
        btn_file_download.setOnClickListener(this);
        server_ip = this.getText(R.string.server_ip).toString();
        upload_url = server_ip+"uploadFile";

        uploadQueue = UploadQueue.getUploadQueue();
        Log.d("myTag",upload_url);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progressbarType:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("파일을 다운로드 중입니다. 잠시만 기다려 주십시오...");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(true);
                progressDialog.show();
                return progressDialog;
            default:
                return null;
        }
    }

    protected void onProgressUpdate(String... progress) {
        progressDialog.setProgress(Integer.parseInt(progress[0]));
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
                UploadFile temp = new UploadFile(id,path,"name","running_time");
                try {
                    uploadQueue.put(temp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_download:
                //다운로드 구현
                download(download_url,this);
               break;
        }
    }


    public void download(String url,Context mContext) {
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        }

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request( uri );

        List<String> pathSegmentList = uri.getPathSegments();
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/PackageMovie").mkdirs();  //경로는 입맛에 따라...바꾸시면됩니다.
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/PackageMovie", pathSegmentList.get(pathSegmentList.size()-1) +".mp4");
        mFileName = pathSegmentList.get(pathSegmentList.size()-1);
        request.setTitle("PackageMovie");
        request.setDescription(mFileName+".mp4");

        mDownloadQueueId = (int) mDownloadManager.enqueue(request);
    }


    private BroadcastReceiver mCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                Toast.makeText(context, "Complete.", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setAction(android.content.Intent.ACTION_VIEW);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                String localUrl = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + "/temp/" + mFileName; //저장했던 경로..
                String extension = MimeTypeMap.getFileExtensionFromUrl(localUrl);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                File file = new File(localUrl);
                intent1.setDataAndType(Uri.fromFile(file), mimeType);
                try {
                    startActivity(intent1);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mCompleteReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mCompleteReceiver, completeFilter);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.finish_fade);
    }




}