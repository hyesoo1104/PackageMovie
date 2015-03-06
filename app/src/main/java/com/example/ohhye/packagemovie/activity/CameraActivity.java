package com.example.ohhye.packagemovie.activity;

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
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.util.CloseAnimation;
import com.example.ohhye.packagemovie.util.OpenAnimation;

/**
 * Created by ohhye on 2015-01-26.
 */
public class CameraActivity  extends ActionBarActivity implements View.OnClickListener {

    MediaPlayer player;
    MediaRecorder recorder;

    // 카메라 상태를 저장하고 있는 객체
    private Camera camera = null;

    private SurfaceView surfaceView;
    private SurfaceHolder holder;


    // slide menu
    private DisplayMetrics metrics;
    private LinearLayout ll_mainLayout;
    private LinearLayout ll_menuLayout;
    private FrameLayout.LayoutParams leftMenuLayoutPrams;
    private int leftMenuWidth;
    private static boolean isLeftExpanded;

    private Button btn_camera_settings;
    private Button bt_left, btn1, btn2, btn3, btn4;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_camera);


        initSildeMenu();
        
        // SurfaceView 클래스 객체를 이용해서 카메라에 받은 녹화하고 재생하는데 쓰일것이다.
      //  surfaceView = (SurfaceView) findViewById(R.id.cameraView);
        // SurfaceView 클래스를 컨트롤하기위한 SurfaceHolder 생성
     //   holder = surfaceView.getHolder();
      //  holder.addCallback(surfaceListener);
        // 버퍼없음
      //  holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    private void initSildeMenu() {

        // init left menu width
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        leftMenuWidth = (int) ((metrics.widthPixels) * 0.3);

        // init main view
        ll_mainLayout = (LinearLayout) findViewById(R.id.ll_mainlayout);

        // init left menu
        ll_menuLayout = (LinearLayout) findViewById(R.id.ll_menuLayout);
        leftMenuLayoutPrams = (FrameLayout.LayoutParams) ll_menuLayout
                .getLayoutParams();
        leftMenuLayoutPrams.width = leftMenuWidth;
        ll_menuLayout.setLayoutParams(leftMenuLayoutPrams);

        // init ui
        bt_left = (Button) findViewById(R.id.bt_left);
        bt_left.setOnClickListener(this);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    /*
    * left menu toggle
    */
    private void menuLeftSlideAnimationToggle() {

        if (!isLeftExpanded) {

            isLeftExpanded = true;

            // Expand
            new OpenAnimation(ll_mainLayout, leftMenuWidth,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.3f, 0, 0.0f, 0, 0.0f);

            // enable all of menu view
            FrameLayout viewGroup = (FrameLayout) findViewById(R.id.ll_menuLayout)
                    .getParent();
            enableDisableViewGroup(viewGroup, true);

            // enable empty view
            ((LinearLayout) findViewById(R.id.ll_empty))
                    .setVisibility(View.VISIBLE);

            findViewById(R.id.ll_empty).setEnabled(true);
            findViewById(R.id.ll_empty).setOnTouchListener(
                    new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View arg0, MotionEvent arg1) {
                            menuLeftSlideAnimationToggle();
                            return true;
                        }
                    });

        } else {
            isLeftExpanded = false;

            // Collapse
            new CloseAnimation(ll_mainLayout, leftMenuWidth,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.3f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);


            // enable all of menu view
            FrameLayout viewGroup = (FrameLayout) findViewById(R.id.ll_menuLayout)
                    .getParent();
            enableDisableViewGroup(viewGroup, false);

            // disable empty view
            ((LinearLayout) findViewById(R.id.ll_empty))
                    .setVisibility(View.GONE);
            findViewById(R.id.ll_empty).setEnabled(false);
            bt_left.setEnabled(true);

        }
    }


    public static void enableDisableViewGroup(ViewGroup viewGroup,
                                              boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {


            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);

            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_left:
                menuLeftSlideAnimationToggle();
                break;
            case R.id.btn1:
                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.btn2:
                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.btn3:
                Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.btn4:
                Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT)
                        .show();
                break;

        }

    }


    /*private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {

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
    };*/


}