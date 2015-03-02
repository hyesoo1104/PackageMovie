package com.example.ohhye.packagemovie.object;

import java.util.HashMap;

/**
 * Created by ohhye on 2015-02-27.
 */
public class Snapmovie {
    //TODO: 편집 시 필요한 Snapmovie에 대한 정보
    private static Snapmovie mSnapmovie = null;
    //씬리스트 순서 정보

    //자막 리스트
    private final static HashMap<Integer,String> subtitle= new HashMap<Integer,String>();
    private static int trans = 0;
    private static int bgm = 0;

    private final static int TRANS_EFFECT_BLACK = 1;
    private final static int TRANS_EFFECT_WHITE = 2;
    private final static int TRANS_EFFECT_CROSS = 3;

    //BGM
    private final static String bgm_path ="";

    public Snapmovie(int _trans,int _bgm){
        trans = _trans;
        bgm = _bgm;
    }


    public static Snapmovie getSnapmovie() {
        if (mSnapmovie == null) {
            mSnapmovie = new Snapmovie(0,0);
        }
        return mSnapmovie;
    }

    public void resetSnapmovie(){
        trans = 0;
        bgm = 0;
    }

    public int getBGMType(){
        return bgm;
    }

    public void setBGMType(int type){
        bgm = type;
    }
}
