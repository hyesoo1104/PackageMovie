package com.example.ohhye.packagemovie;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by ohhye on 2015-01-26.
 */
public class CameraActivity  extends ActionBarActivity implements View.OnClickListener {

    MediaPlayer player;
    MediaRecorder recorder;

    // 카메라 상태를 저장하고 있는 객체
    private Camera camera = null;

    SurfaceView surfaceView;
    SurfaceHolder holder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_camera);


        // SurfaceView 클래스 객체를 이용해서 카메라에 받은 녹화하고 재생하는데 쓰일것이다.
        surfaceView = (SurfaceView) findViewById(R.id.cameraView);
        // SurfaceView 클래스를 컨트롤하기위한 SurfaceHolder 생성
        holder = surfaceView.getHolder();
        holder.addCallback(surfaceListener);
        // 버퍼없음
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }


    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            camera.release();
            camera = null;
            Log.i("camera", "카메라 기능 해제");
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            camera = Camera.open();
            Log.i("camera", "카메라 미리보기 활성");

            try {
                camera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(width, height);
            camera.startPreview();
            Log.i("camera", "카메라 미리보기");

        }
    };


    @Override
    public void onClick(View v) {

    }
}