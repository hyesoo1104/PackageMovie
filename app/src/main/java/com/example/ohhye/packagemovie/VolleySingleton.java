package com.example.ohhye.packagemovie;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by ohhye on 2015-02-27.
 */
public class VolleySingleton {
    public final static String IMG_URL = "http://210.118.74.131:8080/myapp/";
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(this.mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache mCache = new LruCache(10);

                    public void putBitmap(String url, Bitmap bitmap) {
                        mCache.put(url, bitmap);
                    }

                    public Bitmap getBitmap(String url) {
                        return (Bitmap) mCache.get(url);
                    }
                });
    }

    public static VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public static ImageLoader getImageLoader() {
        return mImageLoader;
    }
}