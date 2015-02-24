package com.example.ohhye.packagemovie;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;


public class CameraActivity2 extends ActionBarActivity {

    private boolean isRecording = false;

    private Camera mCamera = null;
    private MediaRecorder mMediaRecorder = null;

    private ImageView captureButton = null;

    // 카메라 상태를 저장하고 있는 객체
    private SurfaceView mPreview=null;
    private SurfaceHolder mHolder=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_activity2);

        Log.d("myTag","onCreate//getCameraInstance");
       // mCamera = CameraManager.getCameraInstance(mCamera);

        // SurfaceView 클래스 객체를 이용해서 카메라에 받은 녹화하고 재생하는데 쓰일것이다.
        mPreview = (SurfaceView) findViewById(R.id.camera_preview);
        // SurfaceView 클래스를 컨트롤하기위한 SurfaceHolder 생성
        mHolder = mPreview.getHolder();
        mHolder.addCallback(surfaceListener);
        // 버퍼없음
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



        captureButton = (ImageView) findViewById(R.id.record);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    Log.d("myTag","Click");
                    mMediaRecorder.stop();
                    MediaManager.releaseMediaRecorder(mMediaRecorder, mCamera);

                    mCamera.lock();

                    captureButton
                            .setImageResource(R.drawable.device_access_camera);

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
        });
    }


    @Override
    protected void onPause() {
        Log.d("myTag","------------PAUSE------------");
        super.onPause();
        MediaManager.releaseMediaRecorder(mMediaRecorder, mCamera);
        CameraManager.releaseCamera(mCamera);
        captureButton
                .setImageResource(R.drawable.device_access_camera);
        isRecording = false;
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

    private boolean prepareVideoRecorder() {

        CameraManager.releaseCamera(mCamera);

        mCamera = CameraManager.getCameraInstance(mCamera);
        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile
                .get(CamcorderProfile.QUALITY_720P));

        mMediaRecorder.setOutputFile(FileManager.getOutputMediaFile()
                .toString());

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
        return true;
    }


}
