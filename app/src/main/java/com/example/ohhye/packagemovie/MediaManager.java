package com.example.ohhye.packagemovie;

import android.hardware.Camera;
import android.media.MediaRecorder;

/**
 * Created by ohhye on 2015-02-24.
 */
public class MediaManager {
    public static void releaseMediaRecorder(MediaRecorder mMediaRecorder,
                                            Camera c) {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            c.lock();
        }
    }

}
