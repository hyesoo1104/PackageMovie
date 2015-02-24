package com.example.ohhye.packagemovie;

import android.hardware.Camera;

/**
 * Created by ohhye on 2015-02-24.
 */
public class CameraManager {
    public static Camera getCameraInstance(Camera c) {
        c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    public static void releaseCamera(Camera c) {
        if (c != null) {
            c.release();
            c = null;
        }
    }
}
