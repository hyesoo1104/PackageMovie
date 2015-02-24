package com.example.ohhye.packagemovie;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button btn_camera;
    Button btn_edit;
    Button btn_file;
    Button btn_settings;

    private BackPressCloseHandler backPressCloseHandler; //Back Key 더블 터치해서 앱 종료하는 핸들러

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);

        btn_camera=(Button)findViewById(R.id.btn_main_camera);
        btn_edit=(Button)findViewById(R.id.btn_main_edit);
        btn_file=(Button)findViewById(R.id.btn_main_file);
        btn_settings=(Button)findViewById(R.id.btn_main_settings);

        btn_camera.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_file.setOnClickListener(this);
        btn_settings.setOnClickListener(this);

    }

    //앱 종료
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_camera:
                startActivity(new Intent(getApplication(), CameraActivity2.class));
                overridePendingTransition(R.anim.fade,R.anim.hold);
                break;
            case R.id.btn_main_edit:
                startActivity(new Intent(getApplication(), EditActivity.class));
                overridePendingTransition(R.anim.fade,R.anim.hold);
                break;
            case R.id.btn_main_file:
                startActivity(new Intent(getApplication(), FileManagementActivity.class));
                overridePendingTransition(R.anim.fade,R.anim.hold);
                break;
            case R.id.btn_main_settings:
                startActivity(new Intent(getApplication(), SettingsActivity.class));
                overridePendingTransition(R.anim.fade,R.anim.hold);
                break;
        }
    }
}
