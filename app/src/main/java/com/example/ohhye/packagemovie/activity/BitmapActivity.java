package com.example.ohhye.packagemovie.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.ohhye.packagemovie.R;

public class BitmapActivity extends ActionBarActivity {

    Typeface mfont;

    ImageView img;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        Display dis = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        img = (ImageView)findViewById(R.id.imageView);
        img.getLayoutParams().width = dis.getWidth();
        img.getLayoutParams().height = dis.getWidth();


        Bitmap text_img = createText("Package Movie / 패키지무비");
        img.setImageBitmap(text_img);
    }

    public Bitmap createText(String text){
        Typeface font = Typeface.createFromAsset(getAssets(),"NanumBarunpenR.ttf");

        Bitmap bm = Bitmap.createBitmap(480, 480, Bitmap.Config.ARGB_8888);
        Paint pnt = new Paint();
        Canvas c = new Canvas(bm);
        int centerPos = (c.getWidth() / 2);



        c.drawColor(Color.BLACK);
        pnt.reset();
        pnt.setColor(Color.WHITE);
        pnt.setTextAlign(Paint.Align.CENTER);
        pnt.setAntiAlias(true);
        pnt.setTextSize(25);
        pnt.setTypeface(font);
        c.drawText(text,centerPos,centerPos,pnt);

        return bm;
    }

}
