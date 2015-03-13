package com.example.ohhye.packagemovie.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.activity.LoginActivity;
import com.example.ohhye.packagemovie.activity.SignUpActivity;
import com.example.ohhye.packagemovie.singletone_object.VolleySingleton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ohhye on 2015-02-27.
 */
public class Network{
    private Gson gson;

    private Context context;

    private Map<String, String> mParams = null;

    private static String uri;

    public String result="";
    private String server_ip;


    Parser parser = new Parser();

    public Network(Context context) {
        this.context = context;
        server_ip = context.getString(R.string.server_ip);
    }




    /* ------------------------
    *  회원가입
    ---------------------------*/
    public void createGroup(final String Groupname, final String Password){
        //URI 설정
        uri = server_ip+"makeGroup";

        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                // 응답 JSON Parsing
                result = parser.result_parser(response);
                Log.d("result", result);

                if(result.equals("200")) {
                    ((SignUpActivity)context).onBackPressed();
                }
                ((SignUpActivity)context).toast(result);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                // Log.d("Error.Response", response);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                mParams = new HashMap<String, String>();
                mParams.put("group_id", Groupname);
                mParams.put("group_password", Password);

                return mParams;
            }
        };
        VolleySingleton.getInstance(context).getRequestQueue().add(postRequest);
    }

    /* ------------------------
    *  로그인
    ---------------------------*/
    public void login(final String Groupname, final String Password){
        uri = server_ip+"login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                result = parser.result_parser(response);
                Log.d("result", result);

                //임시!! 항상 로그인
                result ="200";

                if(result.equals("200")) {
                    ((LoginActivity)context).login(Groupname,Password);
                }else {
                    ((LoginActivity)context).toast(result);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                // Log.d("Error.Response", response);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                mParams = new HashMap<String, String>();
                mParams.put("group_id", Groupname);
                mParams.put("group_password", Password);

                return mParams;
            }
        };
        VolleySingleton.getInstance(context).getRequestQueue().add(postRequest);
    }



    /* ------------------------
    *  비밀번호 변경
    ---------------------------*/
    public void user_update(final String Groupname, final String Password, final String new_password){
        uri = server_ip+"login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                result = parser.result_parser(response);
                Log.d("result", result);

                //임시!! 항상 로그인
                result ="200";

                if(result.equals("200")) {
                    ((LoginActivity)context).login(Groupname,Password);
                }else {
                    ((LoginActivity)context).toast(result);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                // Log.d("Error.Response", response);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                mParams = new HashMap<String, String>();
                mParams.put("group_id", Groupname);
                mParams.put("group_password", Password);

                return mParams;
            }
        };
        VolleySingleton.getInstance(context).getRequestQueue().add(postRequest);
    }



    /* ------------------------
    *  파일 리스트 불러오기
    ---------------------------*/
    public void load_file_list(){
        uri = server_ip+"login";

        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                result = parser.result_parser(response);
                Log.d("result", result);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                // Log.d("Error.Response", response);
            }
        })/* {
            @Override
            protected Map<String, String> getParams() {
                mParams = new HashMap<String, String>();

                return mParams;
            }
        }*/;
        VolleySingleton.getInstance(context).getRequestQueue().add(postRequest);
    }


}
