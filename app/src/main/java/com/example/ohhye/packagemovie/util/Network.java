package com.example.ohhye.packagemovie.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.activity.FileManagementActivity;
import com.example.ohhye.packagemovie.activity.LoginActivity;
import com.example.ohhye.packagemovie.activity.SettingsActivity;
import com.example.ohhye.packagemovie.activity.SignUpActivity;
import com.example.ohhye.packagemovie.fragment.Edit_SceneListFragment;
import com.example.ohhye.packagemovie.singletone_object.VolleySingleton;
import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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




    /* ------------------------------------------------------------------------------------------------
    *  회원가입
    ---------------------------------------------------------------------------------------------------*/
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




    /* ------------------------------------------------------------------------------------------------
    *  로그인
    ---------------------------------------------------------------------------------------------------*/
    public void login(final String Groupname, final String Password){
        uri = server_ip+"login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                result = parser.result_parser(response);
                Log.d("result", result);


                if(result.equals("200")) {
                    Log.d("Network","GroupName  :  "+Groupname+"     Password  :  "+Password);
                    ((LoginActivity)context).login(Groupname, Password);
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



    /* ------------------------------------------------------------------------------------------------
    *  비밀번호 변경
   ------------------------------------------------------------------------ ---------------------------*/
    public void pwd_change(final String Groupname, final String Password, final String NewPassword){
        uri = server_ip+"editGroup";
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                result = parser.result_parser(response);
                Log.d("result", result);


                ((SettingsActivity)context).toast(result);

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
                mParams.put("new_password", NewPassword);

                return mParams;
            }
        };
        VolleySingleton.getInstance(context).getRequestQueue().add(postRequest);
    }




  /* ------------------------------------------------------------------------------------------------
  *  편집화면 동영상 리스트 불러오기
  ---------------------------------------------------------------------------------------------------*/
    public void load_scene_list(){
        uri = server_ip+"fileListView";
        Log.d("Load File","load file list");
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                result = parser.result_parser(response);
                Log.d("result", result);

                //리스트 데이터가 성공적으로 왔을때
                if(result.equals("200"))
                {
                    //리스트 아이템을 하나하나 파싱
                    JSONArray fileList;

                    JSONObject item;
                    String video_name;
                    String video_path;
                    String streaming_path;
                    String thumbnail_path;
                    String running_time;

                    try {
                        JSONParser jsonParser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
                        fileList = (JSONArray)jsonObject.get("fileList");

                        //TODO:파싱해서 Edit_ListFragment.addItem()호출----> 리스트에 아이템 추가


                        for(int i = 0; i<fileList.size(); i++)
                        {
                            item = (JSONObject)fileList.get(i);

                            video_name = item.get("video_name").toString();
                            video_path = item.get("video_path").toString();
                            streaming_path = item.get("streaming_path").toString();
                            thumbnail_path = item.get("thumbnail_path").toString();
                            running_time = item.get("running_time").toString();

                            getSceneListThumbnail(video_name,video_path,streaming_path,running_time,thumbnail_path);
                            Log.d("ListItemParsing",video_name+"///"+video_path+"///"+running_time+"///");

                        }

                        Log.d("fileList : " , fileList.toString());
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
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
                mParams.put("group_id",LoginActivity.getID());
                return mParams;
            }
        };
        VolleySingleton.getInstance(context).getRequestQueue().add(postRequest);
    }

    public void getSceneListThumbnail(final String video_name, final String video_path,final String streaming_path, final String running_time, String image_url){
        //uri = "http://210.118.74.131:8080/PackageMovie/viewImage/test1/test";
        String e_name="";

        try {
            e_name = URLEncoder.encode(video_name,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            e_name = video_name;
        }

        uri = "http://210.118.74.131:8080/PackageMovie/viewImage/"+LoginActivity.getID()+"/"+e_name;

        Log.d("getThumbnail",""+uri);

        ImageRequest imageRequest = new ImageRequest(uri, new Response.Listener<Bitmap>(){
            @Override
            public void onResponse(Bitmap response) {
                Log.d("getThumbnail Response","Response!!!");
                Edit_SceneListFragment.addItem(video_path, response, video_name, convertTime(running_time));
            }
        }, /*maxWidth*/0, /*maxHeight*/ 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        VolleySingleton.getInstance(context).getRequestQueue().add(imageRequest);
    }






    /* ------------------------------------------------------------------------------------------------
    *  파일 리스트 불러오기
   ------------------------------------------------------------------------ ---------------------------*/
    public void load_file_list(){
        uri = server_ip+"fileListView";
        Log.d("Load File","load file list");
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                result = parser.result_parser(response);
                Log.d("result", result);

                //리스트 데이터가 성공적으로 왔을때
                if(result.equals("200"))
                {
                    //리스트 아이템을 하나하나 파싱
                    JSONArray fileList;

                    JSONObject item;
                    String video_name;
                    String video_path;
                    String streaming_path;
                    String thumbnail_path;
                    String running_time;

                    try {
                        JSONParser jsonParser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
                        fileList = (JSONArray)jsonObject.get("fileList");

                        //TODO:파싱해서 Edit_ListFragment.addItem()호출----> 리스트에 아이템 추가


                        for(int i = 0; i<fileList.size(); i++)
                        {
                            item = (JSONObject)fileList.get(i);

                            video_name = item.get("video_name").toString();
                            video_path = item.get("video_path").toString();
                            streaming_path = item.get("streaming_path").toString();
                            thumbnail_path = item.get("thumbnail_path").toString();
                            running_time = item.get("running_time").toString();

                            getFileListThumbnail(video_name,video_path,streaming_path,running_time,thumbnail_path);
                            Log.d("ListItemParsing",video_name+"///"+video_path+"///"+running_time+"///");

                        }

                        Log.d("fileList : " , fileList.toString());
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
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
                mParams.put("group_id",LoginActivity.getID());
                return mParams;
            }
        };
        VolleySingleton.getInstance(context).getRequestQueue().add(postRequest);
    }


    public void getFileListThumbnail(final String video_name, final String video_path,final String streaming_path, final String running_time, String image_url){
        String e_name="";

        try {
            e_name = URLEncoder.encode(video_name,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            e_name = video_name;
        }

        uri = "http://210.118.74.131:8080/PackageMovie/viewImage/"+LoginActivity.getID()+"/"+e_name;

        Log.d("getThumbnail",""+uri);

        ImageRequest imageRequest = new ImageRequest(uri, new Response.Listener<Bitmap>(){
            @Override
            public void onResponse(Bitmap response) {
                Log.d("getThumbnail Response","Response!!!");
                FileManagementActivity.addItem(video_path, response, video_name, convertTime(running_time),streaming_path);
            }
        }, /*maxWidth*/0, /*maxHeight*/ 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        VolleySingleton.getInstance(context).getRequestQueue().add(imageRequest);
    }



    private String convertTime(String time){
        String convert_time = "";
        long duration = Long.parseLong(time);
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);

        if(hours!=0) {
            convert_time = hours+":";
        }
        convert_time = minutes+":"+seconds;

        Log.d("Running Time",convert_time);
        return convert_time;
    }


    /* ------------------------------------------------------------------------------------------------
    *  파일삭제
   ------------------------------------------------------------------------ ---------------------------*/
    public void file_delete(final String Groupname, final String File_name, final int Position){
        uri = server_ip+"deleteFile/"+Groupname+"/"+File_name;
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                result = parser.result_parser(response);
                Log.d("result", result);

                if(result.equals("200")) {
                    FileManagementActivity.removeItem(Position);
                    ((FileManagementActivity)context).toast(result);
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

                return mParams;
            }
        };
        VolleySingleton.getInstance(context).getRequestQueue().add(postRequest);
    }


}
