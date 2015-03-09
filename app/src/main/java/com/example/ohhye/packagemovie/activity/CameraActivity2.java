package com.example.ohhye.packagemovie.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.util.manager.CameraManager;
import com.example.ohhye.packagemovie.util.manager.FileManager;
import com.example.ohhye.packagemovie.util.manager.MediaManager;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class CameraActivity2 extends ActionBarActivity implements View.OnClickListener, Animation.AnimationListener, SensorEventListener {

    private boolean isRecording = false;
    private boolean isFrontCamera = false;
    private boolean setting_menu_visible = false;
    private boolean s_isClose = false;
    private boolean s_isDark = false;
    private boolean isGrid  = false;
    private boolean isFlash = false;
    private boolean isMotion = false;
    private boolean isFilterMenu = false;
    private int timer_time = 0;
    private int mCameraFacing;       // 전면 or 후면 카메라 상태 저장

    private String video_path = "";

    private View settings_menu;
    private View filter_menu;
    private Animation aniShow, aniHide;

    private Camera mCamera = null;
    private Camera.Parameters mCameraParameter;
    private MediaRecorder mMediaRecorder = null;

    private ImageView captureButton;
    private ImageView btn_camera_capture;
    private ImageView btn_camera_change;
    private ImageView btn_camera_settings;
    private ImageView camera_grid;

    private Button btn_camera_settings_grid;
    private Button btn_camera_settings_flash;
    private Button btn_camera_settings_motion;
    private Button btn_camera_settings_timer;
    private Button btn_camera_settings_filter;

    //Sensor(Motion)
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private Sensor lightSensor;


    //Timer
    private TimerTask mTask;
    private Timer mTimer;

    // 카메라 상태를 저장하고 있는 객체
    private SurfaceView mPreview=null;
    private SurfaceHolder mHolder=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_activity2);

        mCameraFacing  = Camera.CameraInfo.CAMERA_FACING_BACK; // 최초 카메라 상태는 후면카메라로 설정

        init();

        Log.d("myTag","onCreate//getCameraInstance");
        // mCamera = CameraManager.getCameraInstance(mCamera);

        // SurfaceView 클래스 객체를 이용해서 카메라에 받은 녹화하고 재생하는데 쓰일것이다.
        mPreview = (SurfaceView) findViewById(R.id.camera_preview);
        // SurfaceView 클래스를 컨트롤하기위한 SurfaceHolder 생성
        mHolder = mPreview.getHolder();
        mHolder.addCallback(surfaceListener);
        // 버퍼없음
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //Sensor
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //Timer
        mTimer = new Timer();


    }

    @Override
    public void onStart() {
        super.onStart();
        if (proximitySensor != null)
            sensorManager.registerListener(this, proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        if (lightSensor != null)
            sensorManager.registerListener(this, lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_LIGHT:
                if(event.values[0]<20)
                {
                    s_isDark = true;
                }
                else s_isDark = false;
               // Log.d("Motion-Light","Light : "+event.values[0]);
                break;
            case Sensor.TYPE_PROXIMITY:
                if(event.values[0]==0)
                {
                    s_isClose = true;
                }
                else s_isClose = false;
                Log.d("Motion-Proximity","Proximity : "+event.values[0]);
                if(isMotion&&s_isClose)
                {
                    if(!isRecording) {
                        //촬영시작
                        record();
                        Log.d("Motion", "촬영시작!");
                    }
                    else if(isRecording)
                    {
                        //촬영종료
                        record();
                        Log.d("Motion","촬영종료!");
                    }
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void init(){
        settings_menu = findViewById(R.id.menu_camera_settings);
        filter_menu = findViewById(R.id.menu_camera_settings_filter_menu);

        captureButton = (ImageView) findViewById(R.id.btn_camera_capture);

        camera_grid = (ImageView)findViewById(R.id.camera_grid);
        btn_camera_capture = (ImageView)findViewById(R.id.btn_camera_capture);
        btn_camera_settings = (ImageView)findViewById(R.id.btn_camera_settings);
        btn_camera_change = (ImageView)findViewById(R.id.btn_camera_change);
        btn_camera_settings_grid = (Button)findViewById(R.id.btn_camera_settings_grid);
        btn_camera_settings_flash = (Button)findViewById(R.id.btn_camera_settings_flash);
        btn_camera_settings_motion = (Button)findViewById(R.id.btn_camera_settings_motion);
        btn_camera_settings_timer = (Button)findViewById(R.id.btn_camera_settings_timer);
        btn_camera_settings_filter = (Button)findViewById(R.id.btn_camera_settings_filter);

        btn_camera_capture.setOnClickListener(this);
        btn_camera_settings.setOnClickListener(this);
        btn_camera_change.setOnClickListener(this);
        btn_camera_settings_grid.setOnClickListener(this);
        btn_camera_settings_flash.setOnClickListener(this);
        btn_camera_settings_motion.setOnClickListener(this);
        btn_camera_settings_timer.setOnClickListener(this);
        btn_camera_settings_filter.setOnClickListener(this);


        camera_grid.setOnClickListener(this);
        camera_grid.setVisibility(View.GONE);
        settings_menu.setVisibility(View.GONE);
        filter_menu.setVisibility(View.GONE);


        //Animation
        aniShow = AnimationUtils.loadAnimation(this, R.anim.fade);
        aniShow.setAnimationListener(this);
        aniHide = AnimationUtils.loadAnimation(this, R.anim.finish_fade);
        aniHide.setAnimationListener(this);
    }


    private Camera openCamera(int facing) {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        Log.d("Camera","CameraCount : "+cameraCount);
        for (int cameraId = 0; cameraId < cameraCount; cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == facing) {
                try {
                    cam = Camera.open(cameraId);
                    break;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        return cam;
    }
    private void startCamera(int facing) {
        mCamera = openCamera(facing);
        if (mCamera == null) {
            mCamera = Camera.open();
            isFrontCamera = false;
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e){
            mCamera.release();
            mCamera = null;
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
    @Override
    protected void onPause() {
        Log.d("myTag","------------PAUSE------------");
        super.onPause();
        mTimer.cancel();
        if(isRecording==true) {
            record();
        }
    }

//    @Override
//    protected  void onResume(){
//        Log.d("myTag","------------RESUME------------");
//        super.onResume();
//        if(!mPreview.checkCamera())
//        {
//            mPreview.setCamera(mCamera);
//        }
//
//        //CameraManager.releaseCamera(mCamera);
//       // mCamera = CameraManager.getCameraInstance(mCamera);
//    }

    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            Log.i("camera", "Camera Release!");
            try {
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            Log.i("camera", "Camera Open");
            try {
                mCamera = Camera.open();
                mCameraParameter = mCamera.getParameters();
                mCamera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
            Log.i("camera", "Camera Preview");
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(width, height);
            mCamera.startPreview();
        }
    };

    /*
    *  -----------동영상녹화-----------
    */

    private  void record(){
        if (isRecording) {
            Log.d("myTag","Click");
            mMediaRecorder.stop();
            MediaManager.releaseMediaRecorder(mMediaRecorder, mCamera);

            mCamera.lock();

            captureButton
                    .setImageResource(R.drawable.device_access_camera);


            final File file = new File(video_path);

            //동영상제목 설정
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("동영상 제목");
            alert.setMessage("촬영한 영상의 제목을 입력해주세요");

            final EditText input = new EditText(this);
            input.setSingleLine();
            //확장자를 뺀 이름
            String tmp_filename = file.getName();
            int FileIdx = tmp_filename.lastIndexOf(".");
            input.setText(tmp_filename.substring(0,FileIdx));

            alert.setView(input);

            alert.setNegativeButton("저장 취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 파일 삭제.
                            file.delete();
                        }
                    });

            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    // Do something with value!
                    // 파일 이름 변경
                    File _mediaStorageDir = new File(
                            Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                            "PackageMovie");
                    File rename_file = new File(_mediaStorageDir.getPath() + File.separator+value+ ".mp4");
                    file.renameTo(rename_file);


                    FileManager.upload_record_file(rename_file.getPath(),value); //녹화파일 업로드
                    Log.d("Path",""+rename_file.getPath());
               }
            });

            alert.show();


            isRecording = false;


        } else {
            if (prepareVideoRecorder()) {
                mMediaRecorder.start();
                captureButton.setImageResource(R.drawable.av_stop);
                isRecording = true;
            } else {
                MediaManager.releaseMediaRecorder(mMediaRecorder,
                        mCamera);
            }
        }
    }


    private boolean prepareVideoRecorder() {

        File tmp = FileManager.getOutputMediaFile();

        CameraManager.releaseCamera(mCamera);

        mCamera = CameraManager.getCameraInstance(mCamera);
        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile
                .get(CamcorderProfile.QUALITY_720P));


        mMediaRecorder.setOutputFile(tmp.toString());

        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            MediaManager.releaseMediaRecorder(mMediaRecorder, mCamera);
            return false;
        } catch (IOException e) {
            MediaManager.releaseMediaRecorder(mMediaRecorder, mCamera);
            return false;
        }

        video_path = tmp.getPath();
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_camera_capture:
                filterMenuOff();
                if(setting_menu_visible==true)
                {
                    filterMenuOff();
                    settings_menu.startAnimation(aniHide);
                    settings_menu.setVisibility(View.GONE);
                    setting_menu_visible = false;
                }
                Log.d("Timer","capture"+timer_time*1000);

                //타이머 시간만큼 타이머 걸고 녹화시작
                if(timer_time!=0&&isRecording==false) {
                    startTimer();
                }
                else record();
                break;
            case R.id.btn_camera_change:
                //촬영중에는 버튼 비활성화
                if(isRecording ==true) break;

                closeCamera();
                isFrontCamera = !isFrontCamera;
                if (isFrontCamera) {
                    btn_camera_settings_flash.setBackgroundResource(R.drawable.flash_unable);
                    startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                } else {
                    btn_camera_settings_flash.setBackgroundResource(R.drawable.flash_off);
                    startCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                }
                mCamera.startPreview();
                break;
            case R.id.btn_camera_settings:
                //촬영중에는 버튼 비활성화
                if(isRecording ==true) break;

                //닫혀있을때
                if(setting_menu_visible==false)
                {
                    settings_menu.setVisibility(View.VISIBLE);
                    settings_menu.startAnimation(aniShow);
                    setting_menu_visible = true;
                }
                //열려있을때
                else if(setting_menu_visible==true)
                {
                    filterMenuOff();
                    settings_menu.startAnimation(aniHide);
                    settings_menu.setVisibility(View.GONE);
                    setting_menu_visible = false;
                }
                break;

            case R.id.btn_camera_settings_grid:
                filterMenuOff();
                if(isGrid==false)
                {
                   camera_grid.setVisibility(View.VISIBLE);
                   btn_camera_settings_grid.setBackgroundResource(R.drawable.grid_on);
                   isGrid=true;
                }
                else if(isGrid==true)
                {
                    camera_grid.setVisibility(View.GONE);
                    btn_camera_settings_grid.setBackgroundResource(R.drawable.grid_off);
                    isGrid=false;
                }
                break;

            case R.id.btn_camera_settings_flash:
                if(isFrontCamera) break;
                filterMenuOff();
                if(isFlash==false)
                {
                    Log.d("Flash","FlashON");
                    mCameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(mCameraParameter);
                    mCamera.startPreview();
                    btn_camera_settings_flash.setBackgroundResource(R.drawable.flash_on);
                    isFlash=true;
                }
                else if(isFlash==true)
                {
                    Log.d("Flash","FlashOFF");
                    mCameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(mCameraParameter);
                    mCamera.startPreview();
                    btn_camera_settings_flash.setBackgroundResource(R.drawable.flash_off);
                    isFlash=false;
                }
                break;

            case R.id.btn_camera_settings_motion:
                filterMenuOff();
                if(isMotion==false)
                {
                    btn_camera_settings_motion.setBackgroundResource(R.drawable.motion_on);
                    isMotion=true;
                }
                else if(isMotion==true)
                {
                    btn_camera_settings_motion.setBackgroundResource(R.drawable.motion_off);
                    isMotion=false;
                }
                break;

            case R.id.btn_camera_settings_timer:
                filterMenuOff();
                timer_setting(timer_time);
                break;

            case R.id.btn_camera_settings_filter:
                if(isFilterMenu==true){
                    filter_menu.startAnimation(aniHide);
                    filter_menu.setVisibility(View.GONE);
                    isFilterMenu = false;
                }
                else if(isFilterMenu==false){
                    filter_menu.setVisibility(View.VISIBLE);
                    filter_menu.startAnimation(aniShow);
                    isFilterMenu = true;
                }
                break;
        }
    }

    private void timer_setting(int _timer_time) {
        switch (_timer_time){
            case 0:
                timer_time = 3;
                btn_camera_settings_timer.setBackgroundResource(R.drawable.timer_3s);
                break;
            case 3:
                timer_time = 5;
                btn_camera_settings_timer.setBackgroundResource(R.drawable.timer_5s);
                break;
            case 5:
                timer_time = 10;
                btn_camera_settings_timer.setBackgroundResource(R.drawable.timer_10s);
                break;
            case 10:
                timer_time = 0;
                btn_camera_settings_timer.setBackgroundResource(R.drawable.timer_off);
                break;
        }
    }

    private void filterMenuOff(){
        if(isFilterMenu==true){
            filter_menu.startAnimation(aniHide);
            filter_menu.setVisibility(View.GONE);
            isFilterMenu = false;
        }
    }

    public void startTimer()
    {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable(){
                        public void run(){
                            //촬영시작
                            Log.d("Timer","Complete!!");
                            stopTimer();
                            record();
                        }
                    });
                }
            },timer_time*1000);
        Log.d("Timer","Start!!!");
    }

    public void stopTimer()
    {
        if(mTimer != null) {
            // 해당 타이머가 수행할 모든 행위들을 정지시킵니다.
            mTimer.cancel();
            // 대기중이던 취소된 행위가 있는 경우 모두 제거한다.
            mTimer.purge();
            mTimer = null;
        }
        Log.d("Timer", "Stop!!!");
    }
    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
