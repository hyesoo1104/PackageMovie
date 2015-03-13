package com.example.ohhye.packagemovie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.example.ohhye.packagemovie.R;

/**
 * Created by ohhye on 2015-01-26.
 */
public class SettingsActivity extends ActionBarActivity implements View.OnClickListener{

    private TextView btn_wifiUploadOnOff;
    private static TextView settings_id;
    private TextView btn_logout;
    private TextView btn_pwd_change;
    private int wifiUploadOnOff = 0; //OFF

    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        edit = LoginActivity.mPref.edit();

        settings_id = (TextView)findViewById(R.id.settings_id);
        settings_id.setText(LoginActivity.group_name+"\n");

        btn_logout = (TextView)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);

        btn_pwd_change = (TextView)findViewById(R.id.btn_pwd_change);
        btn_pwd_change.setOnClickListener(this);

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

            case R.id.btn_pwd_change:
                startActivity(new Intent(getApplication(), ChangePasswordActivity.class));
                overridePendingTransition(R.anim.fade,R.anim.hold);


                break;

            case R.id.btn_logout:
                edit.remove("autoLogin");
                edit.remove("id");
                edit.remove("pwd");
                edit.commit();
                Intent i = new Intent(getApplication(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.fade,R.anim.hold);

                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.finish_fade);
    }
}