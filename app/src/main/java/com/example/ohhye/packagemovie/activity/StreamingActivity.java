package com.example.ohhye.packagemovie.activity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.util.CustomVideoView;
import com.yixia.camera.util.DeviceUtils;

import java.io.File;
import java.util.List;

public class StreamingActivity extends ActionBarActivity implements View.OnClickListener {

    private DownloadManager mDownloadManager; //다운로드 매니저.
    private int mDownloadQueueId; //다운로드 큐 아이디..
    private String mFileName ; //파일다운로드 완료후...파일을 열기 위해 저장된 위치를 입력해둔다.

    private ProgressDialog progressDialog;
    public static final int progressbarType = 0;

    ImageView btn_streaming_download;
    CustomVideoView videoView;
    String streaming_path;
    String video_path;

    MediaController mediaController;
    Uri uri;

    private int mWindowWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_streaming);

        mWindowWidth = DeviceUtils.getScreenWidth(this);


        //videoView = new CustomVideoView(this,);

        Intent intent = getIntent();
        streaming_path = intent.getStringExtra("streaming_path");
        video_path = intent.getStringExtra("video_path");
        uri = Uri.parse(streaming_path);

        //비디오뷰 커스텀을 위한 미디어컨트롤러
        mediaController = new MediaController(this);

        btn_streaming_download = (ImageView)findViewById(R.id.btn_streaming_download);
        btn_streaming_download.setOnClickListener(this);

        videoView = (CustomVideoView)findViewById(R.id.streaming_view);

       /* videoView.getLayoutParams().width = mWindowWidth;
        videoView.getLayoutParams().height = mWindowWidth;*/


        //비디오뷰에 연결
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        // 동영상 재생이 완료된걸 알수있는 리스너
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            // 동영상 재생이 완료된후 호출되는 메서드
            public void onCompletion(MediaPlayer player) {
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_streaming_download:
                download(video_path,this);
                break;
        }
    }






     /*---------------------------------------------------------------------------
     *      File Download
     *      usage : download(download_url,this);
     ---------------------------------------------------------------------------*/

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
}
