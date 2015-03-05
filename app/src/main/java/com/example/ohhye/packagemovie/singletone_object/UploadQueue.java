package com.example.ohhye.packagemovie.singletone_object;

import com.example.ohhye.packagemovie.vo.UploadFile;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ohhye on 2015-03-06.
 */
public class UploadQueue{

    /**
     * 생산자 클래스
     * @author falbb
     *
     */
    private static ArrayBlockingQueue<UploadFile> queue =null;

    public static ArrayBlockingQueue<UploadFile> getUploadQueue(){
        if (queue == null) {
            queue= new ArrayBlockingQueue<UploadFile>(50);
        }
        return queue;
    }




}
