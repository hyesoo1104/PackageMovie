package com.example.ohhye.packagemovie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.singletone_object.UploadQueue;
import com.example.ohhye.packagemovie.util.Network;
import com.example.ohhye.packagemovie.vo.UploadFile;

import java.util.concurrent.ArrayBlockingQueue;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {


    Network net;
    Context mContext;

    EditText login_group_id;
    EditText login_pwd;
    CheckBox auto_login;

    public static String group_name = "";
    String password = "";

    Button btn_login;
    Button btn_createGroup;

    public static SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);

        net = new Network(mContext);

        auto_login = (CheckBox)findViewById(R.id.checkbox_autologin);
        auto_login.setChecked(mPref.getBoolean("autoLogin",false));

        login_group_id = (EditText)findViewById(R.id.login_group_id);
        login_pwd = (EditText)findViewById(R.id.login_pwd);

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_createGroup = (Button)findViewById(R.id.btn_createGroup);

        btn_login.setOnClickListener(this);
        btn_createGroup.setOnClickListener(this);


        Log.d("AutoLogin",mPref.getBoolean("autoLogin",false)+"");
        if(mPref.getBoolean("autoLogin",false)==true)
        {
            String pref_id = mPref.getString("id",null);
            String pref_pwd = mPref.getString("pwd",null);
           // net.login(pref_id,pref_pwd);
            login(pref_id,pref_pwd);
        }


    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                String pwd = login_pwd.getText().toString();
                group_name = login_group_id.getText().toString();
                //net.login(group_name,pwd);
                login(group_name,pwd);
                break;
            case R.id.btn_createGroup:
                signUp();
                break;
        }

    }


    public void login(String id, String pwd){
        String group_id = id;
        String password = pwd;
        UploadQueue.id = group_id;
        FileManagementActivity.id = group_id;

        //UploadQueue 동작
        ArrayBlockingQueue<UploadFile> q = UploadQueue.getUploadQueue();
        new UploadQueue().execute(q, null, null);


        //아이디 비번 저장, 자동로그인 저장
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("id",group_id);
        editor.putString("pwd",password);
        editor.putBoolean("autoLogin",auto_login.isChecked());
        editor.commit();

        Log.d("Login","ID : "+id+"     PWD : "+pwd+"        AutoLogin : "+mPref.getBoolean("autoLogin",false));

        Intent i =  new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i); // 로딩이 끝난후 이동할 Activity
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        finish();
    }

    public void signUp(){
        startActivity(new Intent(getApplication(), SignUpActivity.class));
        overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
    }

    public void toast(String result){
        if(result.equals("600"))
            Toast.makeText(mContext, "이미 존재하는 그룹 아이디입니다.", Toast.LENGTH_SHORT).show();
        else if(result.equals("601"))
            Toast.makeText(mContext,"존재하지 않은 그룹 아이디입니다.",Toast.LENGTH_SHORT).show();
        else if(result.equals("602"))
            Toast.makeText(mContext,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
    }

}
