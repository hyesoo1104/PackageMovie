package com.example.ohhye.packagemovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class SignUpActivity extends ActionBarActivity implements View.OnClickListener {

    Button btn_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        btn_create = (Button)findViewById(R.id.btn_signup_create);
        btn_create.setOnClickListener(this);
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
                onBackPressed();
                break;
        }
    }
}
