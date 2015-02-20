package com.example.ohhye.packagemovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    Button btn_login;
    Button btn_createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_createGroup = (Button)findViewById(R.id.btn_createGroup);

        btn_login.setOnClickListener(this);
        btn_createGroup.setOnClickListener(this);




    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                Intent i =  new Intent(this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i); // 로딩이 끝난후 이동할 Activity
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                finish();
                break;
            case R.id.btn_createGroup:
                startActivity(new Intent(getApplication(), SignUpActivity.class));
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
                break;
        }

    }
}
