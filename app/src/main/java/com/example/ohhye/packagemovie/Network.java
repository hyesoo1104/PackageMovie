package com.example.ohhye.packagemovie;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

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

    private String result="";
    private String server_ip;

    public Network(Context context) {
        this.context = context;
        server_ip = context.getString(R.string.server_ip);
    }


    public void createGroup(final String Groupname, final String Password){
        uri = server_ip+"makeGroup";
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                result = response;
                Log.d("result", result);
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
                mParams.put("Groupname", Groupname);
                mParams.put("Password", Password);

                return mParams;
            }
        };
        VolleySingleton.getInstance(context).getRequestQueue().add(postRequest);
    }
}
