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


    private final static int TRANS_EFFECT_BLACK = 1;
    private final static int TRANS_EFFECT_WHITE = 2;
    private final static int TRANS_EFFECT_CROSS = 3;

    //BGM
    private final static String bgm_path ="";

    private Snapmovie(){

    }


    public static Snapmovie getSnapmovie() {
        if (mSnapmovie == null) {
            mSnapmovie = new Snapmovie();
        }
        return mSnapmovie;
    }
}
