package com.example.ohhye.packagemovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpActivity extends ActionBarActivity implements View.OnClickListener {

    Network net;

    Context mContext;

    Button btn_create;

    EditText signup_group;
    EditText signup_pwd;
    EditText signup_repwd;

    String group_name = "";
    String password = "";
    String repassword = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mContext = getApplicationContext();
        net = new Network(mContext);

        btn_create = (Button)findViewById(R.id.btn_signup_create);
        btn_create.setOnClickListener(this);

        signup_group = (EditText)findViewById(R.id.signup_groupid);
        signup_pwd = (EditText)findViewById(R.id.signup_password);
        signup_repwd = (EditText)findViewById(R.id.signup_repassword);
    }



    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_signup_create:
                //TODO : 길이제한걸기
                group_name = signup_group.getText().toString();
                password = signup_pwd.getText().toString();
                repassword = signup_repwd.getText().toString();

                if(password.equals(repassword))
                {
                    net.createGroup(group_name,password);
                    onBackPressed();
                }
                else
                {
                    Toast.makeText(mContext,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
