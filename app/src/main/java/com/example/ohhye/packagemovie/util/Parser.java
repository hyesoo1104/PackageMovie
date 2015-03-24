package com.example.ohhye.packagemovie.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.fragment.Edit_SceneListFragment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by ohhye on 2015-03-05.
 */
public class Parser {

    public String result_parser(String jsonString) {
        String result="";
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            result = jsonObject.get("results").toString();

            Log.d("resultTag", result);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void list_parser(String jsonString) {
        JSONArray fileList;

        JSONObject item;
        String video_name;
        String video_path;
        String thumbnail_path;
        String running_time;

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            fileList = (JSONArray)jsonObject.get("fileList");

            //TODO:파싱해서 Edit_ListFragment.addItem()호출----> 리스트에 아이템 추가


            for(int i = 0; i<fileList.size(); i++)
            {
                item = (JSONObject)fileList.get(i);

                video_name = item.get("video_name").toString();
                video_path = item.get("video_path").toString();
                thumbnail_path = item.get("thumbnail_path").toString();
                running_time = item.get("running_time").toString();

                Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.edit_text_icon);
                Edit_SceneListFragment.addItem(video_path, bm, video_name, running_time);
            }

            Log.d("fileList : " , fileList.toString());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
