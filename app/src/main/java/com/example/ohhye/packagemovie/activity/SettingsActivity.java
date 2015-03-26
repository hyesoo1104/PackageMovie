package com.example.ohhye.packagemovie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.util.Network;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by ohhye on 2015-01-26.
 */
public class SettingsActivity extends ActionBarActivity implements View.OnClickListener{

    private TextView btn_wifiUploadOnOff;

    Network net;

    ImageView settings_pwd_bg;
    TextView txt_current_pwd;
    EditText settings_current_pwd;
    TextView txt_new_pwd;
    EditText settings_new_pwd;
    TextView txt_re_new_pwd;
    EditText settings_re_new_pwd;
    Button btn_pwd_change;
    Button btn_pwd_cancel;

    private static TextView settings_id;
    private TextView btn_logout;
    private TextView btn_settings_pwd_change;
    private int wifiUploadOnOff = 0; //OFF

    SharedPreferences mPref;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        net = new Network(this);

        mPref = getDefaultSharedPreferences(this.getBaseContext());


        settings_id = (TextView)findViewById(R.id.settings_id);
        settings_pwd_bg = (ImageView)findViewById(R.id.settings_bg2);
        txt_current_pwd = (TextView)findViewById(R.id.txt_current_pwd);
        settings_current_pwd = (EditText)findViewById(R.id.settings_current_pwd);
        txt_new_pwd = (TextView)findViewById(R.id.txt_new_pwd);
        settings_new_pwd = (EditText)findViewById(R.id.settings_new_pwd);
        txt_re_new_pwd = (TextView)findViewById(R.id.txt_re_new_pwd);
        settings_re_new_pwd = (EditText)findViewById(R.id.settings_re_new_pwd);

        btn_pwd_change = (Button)findViewById(R.id.btn_pwd_change);
        btn_pwd_cancel = (Button)findViewById(R.id.btn_pwd_cancel);

        btn_pwd_cancel.setOnClickListener(this);
        btn_pwd_change.setOnClickListener(this);




        btn_logout = (TextView)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);


        btn_settings_pwd_change = (TextView)findViewById(R.id.btn_settings_pwd_change);
        btn_settings_pwd_change.setOnClickListener(this);


        btn_wifiUploadOnOff=(TextView)findViewById(R.id.wifiUploadOnOff);
        btn_wifiUploadOnOff.setOnClickListener(this);

        Boolean isWifi = mPref.getBoolean("WifiOption",false);
        if(mPref.getBoolean("WifiOption",false)==true) {

            Log.d("WifiOption",isWifi.toString());
            btn_wifiUploadOnOff.setText("ON");
            btn_wifiUploadOnOff.setTextColor(Color.parseColor("#FFFFD15B"));
        }
        else if(mPref.getBoolean("WifiOption",false)==false) {
            Log.d("WifiOption",isWifi.toString());
            btn_wifiUploadOnOff.setText("OFF");
            btn_wifiUploadOnOff.setTextColor(Color.parseColor("#ff959595"));
        }

        changePwdLayout(false);
    }


    void changePwdLayout(boolean isVisible){

        if(isVisible == false)
        {/*
            settings_pwd_bg = (ImageView)findViewById(R.id.settings_bg2);
            txt_current_pwd = (TextView)findViewById(R.id.txt_current_pwd);
            settings_current_pwd = (EditText)findViewById(R.id.settings_current_pwd);
            txt_new_pwd = (TextView)findViewById(R.id.txt_new_pwd);
            settings_new_pwd = (EditText)findViewById(R.id.settings_new_pwd);
            txt_re_new_pwd = (TextView)findViewById(R.id.txt_re_new_pwd);
            settings_re_new_pwd = (EditText)findViewById(R.id.settings_re_new_pwd);

            btn_pwd_change = (Button)findViewById(R.id.btn_pwd_change);
            btn_pwd_cancel = (Button)findViewById(R.id.btn_pwd_cancel);*/


            settings_id.setText(LoginActivity.group_name+"");
            settings_id.setVisibility(View.VISIBLE);

            Log.d("id",settings_id.getText().toString());
            settings_pwd_bg.setVisibility(View.GONE);
            txt_current_pwd.setVisibility(View.GONE);
            settings_current_pwd.setVisibility(View.GONE);
            txt_new_pwd.setVisibility(View.GONE);
            settings_new_pwd.setVisibility(View.GONE);
            txt_re_new_pwd.setVisibility(View.GONE);
            settings_re_new_pwd.setVisibility(View.GONE);
            btn_pwd_change.setVisibility(View.GONE);
            btn_pwd_cancel.setVisibility(View.GONE);
        }
        else if(isVisible == true)
        {
            /*settings_pwd_bg = (ImageView)findViewById(R.id.settings_bg2);
            txt_current_pwd = (TextView)findViewById(R.id.txt_current_pwd);
            settings_current_pwd = (EditText)findViewById(R.id.settings_current_pwd);
            txt_new_pwd = (TextView)findViewById(R.id.txt_new_pwd);
            settings_new_pwd = (EditText)findViewById(R.id.settings_new_pwd);
            txt_re_new_pwd = (TextView)findViewById(R.id.txt_re_new_pwd);
            settings_re_new_pwd = (EditText)findViewById(R.id.settings_re_new_pwd);

            btn_pwd_change = (Button)findViewById(R.id.btn_pwd_change);
            btn_pwd_cancel = (Button)findViewById(R.id.btn_pwd_cancel);*/

            settings_id.setVisibility(View.GONE);

            settings_pwd_bg.setVisibility(View.VISIBLE);
            txt_current_pwd.setVisibility(View.VISIBLE);
            settings_current_pwd.setVisibility(View.VISIBLE);
            txt_new_pwd.setVisibility(View.VISIBLE);
            settings_new_pwd.setVisibility(View.VISIBLE);
            txt_re_new_pwd.setVisibility(View.VISIBLE);
            settings_re_new_pwd.setVisibility(View.VISIBLE);
            btn_pwd_change.setVisibility(View.VISIBLE);
            btn_pwd_cancel.setVisibility(View.VISIBLE);

            settings_current_pwd.setText("");
            settings_new_pwd.setText("");
            settings_re_new_pwd.setText("");
/*
            btn_pwd_change.setOnClickListener(this);
            btn_pwd_cancel.setOnClickListener(this);*/
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifiUploadOnOff:
                //On = FFFFD15B       Off = ff959595
                //OFF일때  (ON으로 설정)
                if(wifiUploadOnOff==0) {

                    wifiUploadOnOff=1;
                    btn_wifiUploadOnOff.setText("ON");
                    String onColor = "#FFFFD15B";
                    btn_wifiUploadOnOff.setTextColor(Color.parseColor(onColor));
                    edit = mPref.edit();
                    edit.putBoolean("WifiOption", true);
                    edit.commit();
                    Boolean isWifi = mPref.getBoolean("WifiOption",false);
                    Log.d("WifiOption",isWifi.toString());
                }
                //ON일때  (OFF로 설정)
                else if(wifiUploadOnOff==1){

                    wifiUploadOnOff=0;
                    btn_wifiUploadOnOff.setText("OFF");
                    String offColor = "#ff959595";
                    btn_wifiUploadOnOff.setTextColor(Color.parseColor(offColor));
                    edit = mPref.edit();
                    edit.putBoolean("WifiOption",false);
                    edit.commit();
                    Boolean isWifi = mPref.getBoolean("WifiOption",false);
                    Log.d("WifiOption",isWifi.toString());
                }
                break;

            case R.id.btn_logout:
                edit = mPref.edit();
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

            case R.id.btn_settings_pwd_change:
                changePwdLayout(true);
                break;

            case R.id.btn_pwd_change:
                String current_pwd = LoginActivity.mPref.getString("pwd","null");

                String pwd = settings_current_pwd.getText().toString();
                String new_pwd=settings_new_pwd.getText().toString();
                String new_re_pwd =settings_re_new_pwd.getText().toString();


                Log.d("current_pwd.equals(c_pwd)", current_pwd.equals(new_pwd) + "       " + current_pwd + "         " + new_pwd);
                Log.d("c_pwd.equals(c_re_pwd)",new_pwd.equals(new_re_pwd)+"         "+new_pwd+"       "+new_re_pwd);

                if (!new_pwd.equals(new_re_pwd)) {
                    Toast.makeText(this, "변경할 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

                if(current_pwd.equals(new_pwd))
                {
                    Toast.makeText(this, "변경할 비밀번호가 현재 비밀번호와 같습니다.", Toast.LENGTH_SHORT).show();
                }

                net.pwd_change(pwd,new_pwd,new_re_pwd);


                break;

            case R.id.btn_pwd_cancel:
                changePwdLayout(false);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.finish_fade);
    }

    public void toast(String result){
        if(result.equals("200")) {
            edit = LoginActivity.mPref.edit();
            edit.putString("pwd", settings_new_pwd.getText().toString());
            edit.remove("autoLogin");
            edit.commit();
            Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            changePwdLayout(false);
        }
        else if(result.equals("602"))
            Toast.makeText(this,"현재 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
    }
}