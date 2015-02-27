package com.example.ohhye.packagemovie.util;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by ohhye on 2015-02-24.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder = null;
        private Camera mCamera = null;

        @SuppressWarnings("deprecation")
        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

    public boolean checkCamera(){
        if(mCamera==null) return false;
        else return true;
    }

    public void setCamera(Camera camera){
        mCamera= camera;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("myTag","Preview Surface Created!");
        try {
            mCamera.open();
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("myTag","Preview Surface Destroyed!");
        mCamera.release();
        mCamera=null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.d("myTag","Preview Surface Changed!");
        if (mHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
        }
    }
}