package com.example.ohhye.packagemovie.vo;

/**
 * Created by ohhye on 2015-03-06.
 */
public class UploadFile {
    String group_id = "";
    String path = "";
    String name = "";
    String running_time="";

    public UploadFile(String _id, String _path, String _name, String _running_time){
        group_id = _id;
        path = _path;
        name = _name;
        running_time = _running_time;
    }

    public String getGroupId(){
        return group_id;
    }

    public String getPath(){
        return path;
    }

    public String getName(){
        return name;
    }

    public String getRunning_time(){
        return running_time;
    }
}
