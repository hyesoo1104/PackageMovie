package com.example.ohhye.packagemovie.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Created by ohhye on 2015-03-25.
 */
public class CustomVideoView extends VideoView {
    Context mContext;

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context,attrs);
        mContext = context;
    }
    protected void onMeasure(int width, int height){
        Display dis = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        setMeasuredDimension(dis.getWidth(),dis.getHeight());
    }
}
