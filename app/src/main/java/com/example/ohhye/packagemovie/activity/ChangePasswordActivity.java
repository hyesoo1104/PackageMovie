package com.example.ohhye.packagemovie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;

public class ChangePasswordActivity extends ActionBarActivity implements View.OnClickListener {


    SharedPreferences.Editor edit;
    EditText et_current_pwd;
    EditText et_change_pwd;
    EditText et_confirm_change_pwd;
    Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        et_current_pwd = (EditText)findViewById(R.id.settings_current_pwd);
        et_change_pwd = (EditText)findViewById(R.id.settings_change_pwd);
        et_confirm_change_pwd = (EditText)findViewById(R.id.settings_confirm_change_pwd);
        btn_change = (Button)findViewById(R.id.btn_change);

        btn_change.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_change)
        {

            String current_pwd = LoginActivity.mPref.getString("pwd","null");

            String pwd = et_current_pwd.getText().toString();
            String c_pwd=et_change_pwd.getText().toString();
            String c_re_pwd =et_confirm_change_pwd.getText().toString();


            Log.d("current_pwd.equals(c_pwd)",current_pwd.equals(c_pwd)+"       "+current_pwd+"         "+c_pwd);
            Log.d("c_pwd.equals(c_re_pwd)",c_pwd.equals(c_re_pwd)+"         "+c_pwd+"       "+c_re_pwd);


            if(current_pwd.equals(c_pwd))
            {
                Toast.makeText(this, "변경할 비밀번호가 현재 비밀번호와 같습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                if(current_pwd.equals(pwd)) {
                    if (c_pwd.equals(c_re_pwd)) {
                        edit = LoginActivity.mPref.edit();
                        edit.putString("pwd", et_change_pwd.getText().toString());
                        edit.remove("autoLogin");
                        edit.commit();
                        Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplication(), SettingsActivity.class));
                        overridePendingTransition(R.anim.fade, R.anim.hold);
                    } else {
                        Toast.makeText(this, "변경할 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(this, "현재 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
