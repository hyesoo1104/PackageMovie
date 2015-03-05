package com.example.ohhye.packagemovie.util;

import android.util.Log;

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

    public String list_parser(String jsonString) {
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
}
