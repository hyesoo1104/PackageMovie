package com.example.ohhye.packagemovie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ohhye on 2015-01-26.
 */
public class FileManagementActivity  extends ActionBarActivity implements View.OnClickListener {

    private ImageView btn_file_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_file);

        btn_file_add = (ImageView)findViewById(R.id.btn_file_add);
        btn_file_add.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_file_add:
            break;

        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.finish_fade);
    }

}