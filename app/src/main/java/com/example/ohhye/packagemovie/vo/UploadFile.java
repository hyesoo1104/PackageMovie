package com.example.ohhye.packagemovie.vo;

/**
 * Created by ohhye on 2015-03-06.
 */
public class UploadFile {
    String group_id = "";
    String path = "";
    String date = "";
    Integer size = 0;
    String running_time="";

    public UploadFile(String _id, String _path, String _date, Integer _size, String _running_time){
        group_id = _id;
        path = _path;
        date = _date;
        size = _size;
        running_time = _running_time;
    }

    public String getGroupId(){
        return group_id;
    }

    public String getPath(){
        return path;
    }

    public String getDate(){
        return date;
    }

    public Integer getSize(){
        return size;
    }

    public String getRunning_time(){
        return running_time;
    }
}
