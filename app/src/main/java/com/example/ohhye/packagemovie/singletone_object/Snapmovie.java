package com.example.ohhye.packagemovie.singletone_object;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.HashMap;

/**
 * Created by ohhye on 2015-02-27.
 */
public class Snapmovie {

    private static Snapmovie mSnapmovie = null;

    //씬리스트 순서 정보
    private JSONArray scene_list = null;

    //자막 리스트
    private final static HashMap<Integer,String> subtitle= new HashMap<Integer,String>();
    private static int trans_num = 0;
    private static int bgm_num = 0;

    private final static int TRANS_EFFECT_BLACK = 1;
    private final static int TRANS_EFFECT_WHITE = 2;
    private final static int TRANS_EFFECT_CROSS = 3;

    //BGM
    private final static String bgm_path ="";

    public Snapmovie(){

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
    public void setSceneList(int index, int type, String path) throws JSONException {  // type  -->   1 = 동영상   //  2 = 자막
        JSONObject scene = null;
        scene.put("index",index);
        scene.put("type",type);
        scene.put("path",path);

        scene_list.add(scene);
    }


    //Get Path
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
