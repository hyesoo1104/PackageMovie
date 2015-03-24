package com.example.ohhye.packagemovie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.util.Network;

import java.util.regex.Pattern;


public class SignUpActivity extends ActionBarActivity implements View.OnClickListener {

    Network net;

    public static Context mContext;

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

        mContext = this;
        net = new Network(mContext);

        btn_create = (Button)findViewById(R.id.btn_signup_create);
        btn_create.setOnClickListener(this);


        int maxLength = 15;
        InputFilter filterArray = new InputFilter.LengthFilter(maxLength);


        signup_group = (EditText)findViewById(R.id.signup_groupid);
        signup_group.setFilters(new InputFilter[]{filterArray,filterAlphaNum});


        signup_pwd = (EditText)findViewById(R.id.signup_password);
        signup_pwd.setFilters(new InputFilter[]{filterArray,filterAlphaNum});

        signup_repwd = (EditText)findViewById(R.id.signup_repassword);
        signup_repwd.setFilters(new InputFilter[]{filterArray,filterAlphaNum});
    }


    // 영문만 허용 (숫자 포함)
    protected InputFilter filterAlphaNum = new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

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

                //아이디나 비밀번호가 입력이 안됬을때
                if(group_name.equals("")||password.equals(""))
                {
                    Toast.makeText(mContext,"아이디와 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (password.equals(repassword)) {
                        net.createGroup(group_name, password);
                    } else {
                        Toast.makeText(mContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public void toast(String result){
        if(result.equals("600"))
            Toast.makeText(mContext,"이미 존재하는 그룹 아이디입니다.",Toast.LENGTH_SHORT).show();
        else if(result.equals("601"))
            Toast.makeText(mContext,"존재하지 않은 그룹 아이디입니다.",Toast.LENGTH_SHORT).show();
        else if(result.equals("602"))
            Toast.makeText(mContext,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
        else if(result.equals("200"))
            Toast.makeText(mContext,"그룹이 정상적으로 만들어졌습니다.\n 이제 스냅무비를 만들어보세요!",Toast.LENGTH_SHORT).show();
    }
}
