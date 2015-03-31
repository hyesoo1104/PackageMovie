package com.example.ohhye.packagemovie.singletone_object;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.io.File;

/**
 * Created by ohhye on 2015-02-27.
 */
public class Snapmovie {

    private static Snapmovie mSnapmovie = null;

    //씬리스트 순서 정보
    private JSONArray scene_list = null;

    //자막 리스트
    private int trans_num = 0;



    //BGM
    private String bgm_path ="";
    private static int bgm_num = 0;

    public Snapmovie(){
        scene_list = new JSONArray();
    }


    public static Snapmovie getSnapmovie() {
        if (mSnapmovie == null) {
            mSnapmovie = new Snapmovie();
        }
        return mSnapmovie;
    }

    public void resetSnapmovie(){
        trans_num = 0;
        bgm_num = 0;
    }




    /*--------------------------------------------------------------------
     *  SceneList
     --------------------------------------------------------------------*/
    //Set Path --> JSONArray 안에 넣는거
    public void setSceneList(int index, String path) throws JSONException {  // type  -->   1 = 동영상   //  2 = 자막
        JSONObject scene = new JSONObject();
        scene.put("index",index);
        scene.put("path",path);

        File tmp = new File(path);
        String fileName = tmp.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
        log(ext);

        //확장자로 자막인지 영상인지 확인한다음 type넣어줌

        if(ext.equals("png")){
            scene.put("type","text");
        }
        else{
            scene.put("type","video");
        }

        scene_list.add(scene);
    }

    //Clear SceneList Data
    public void clearSceneListData(){
        scene_list.clear();
    }


    //Get SceneList Data
    public JSONArray getSceneList() {
        return scene_list;
    }


    //Get Count
    public int getListCount() {
        return scene_list.size();
    }


    /*--------------------------------------------------------------------
     *  BGM
     --------------------------------------------------------------------*/
    public int getBGMType(){
        return bgm_num;
    }

    public void setBGMType(int type){
        bgm_num = type;
    }

    public void setBGMPath(String path){
        bgm_path = path;
    }

    public String getBGMPath(){
        return bgm_path;
    }


    /*--------------------------------------------------------------------
     *  TransEffect
     --------------------------------------------------------------------*/
    public int getTransEffectType() {
        return trans_num;
    }

    public void setTransEffectType(int type){
        trans_num = type;
    }



    /*--------------------------------------------------------------------
     *  Log
     --------------------------------------------------------------------*/
    public void log(String msg){
        Log.d("SnapMoive", msg);
    }

}


