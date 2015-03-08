package com.example.ohhye.packagemovie.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.ohhye.packagemovie.R;

public class StreamingActivity extends ActionBarActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_streaming);


        videoView = (VideoView)findViewById(R.id.streaming_view);

        //비디오뷰 커스텀을 위한 미디어컨트롤러
        MediaController mediaController = new MediaController(this);

        //비디오뷰에 연결
//      mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);

        Uri uri = Uri.parse("rtsp://r8---sn-o097zuer.c.youtube.com/CiILENy73wIaGQnDY1tQZPRihBMYESARFEgGUgZ2aWRlb3MM/0/0/0/video.3gp");

        videoView.setVideoURI(uri);

        videoView.start();
    }




}
