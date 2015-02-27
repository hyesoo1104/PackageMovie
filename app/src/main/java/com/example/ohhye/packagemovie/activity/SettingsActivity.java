package com.example.ohhye.packagemovie.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ohhye.packagemovie.R;

import java.util.ArrayList;

/**
 * Created by ohhye on 2015-01-26.
 */
public class SettingsActivity extends ActionBarActivity implements View.OnClickListener{

    private TextView btn_wifiUploadOnOff;
    private int wifiUploadOnOff = 0; //OFF

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        btn_wifiUploadOnOff=(TextView)findViewById(R.id.wifiUploadOnOff);
        btn_wifiUploadOnOff.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifiUploadOnOff:
                //On = FFFFD15B       Off = ff959595
                //OFF일때
                if(wifiUploadOnOff==0) {
                    wifiUploadOnOff=1;
                    btn_wifiUploadOnOff.setText("ON");
                    String onColor = "#FFFFD15B";
                    btn_wifiUploadOnOff.setTextColor(Color.parseColor(onColor));
                }
                //ON일때
                else if(wifiUploadOnOff==1){
                    wifiUploadOnOff=0;
                    btn_wifiUploadOnOff.setText("OFF");
                    String offColor = "#ff959595";
                    btn_wifiUploadOnOff.setTextColor(Color.parseColor(offColor));
                }
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.finish_fade);
    }
}