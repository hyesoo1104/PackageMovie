package com.example.ohhye.packagemovie.singletone_object;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ohhye.packagemovie.vo.UploadFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ohhye on 2015-03-06.
 */
public class UploadQueue extends AsyncTask<ArrayBlockingQueue<UploadFile>, Void, Void> {

    private static ArrayBlockingQueue<UploadFile> queue =null;

    public static ArrayBlockingQueue<UploadFile> getUploadQueue(){
        if (queue == null) {
            queue= new ArrayBlockingQueue<UploadFile>(50);
        }
        return queue;
    }

    public static String id ="";


    String response ="";

    //String path = Environment.getExternalStorageDirectory()+"/Movies/PackageMovie/";
    String url = "http://210.118.74.131:8080/PackageMovie/uploadFile";

    @Override
    protected Void doInBackground(ArrayBlockingQueue<UploadFile>... params) {


        /**
         * 소비자 클래스
         * @author falbb
         *
         */
        while(true) {
            try {
                Log.d("Thread","running");
                ArrayBlockingQueue<UploadFile> queue = getUploadQueue();
                UploadFile file_info = queue.take();


                File file = new File(file_info.getPath());


                //Multipart 객체를 선언한다.
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                //문자열 데이터 추가
                builder.addTextBody("group_id", file_info.getGroupId(), ContentType.create("Multipart/related", "UTF-8"));
                builder.addTextBody("video_name", file_info.getName(), ContentType.create("Multipart/related", "UTF-8"));
                builder.addTextBody("running_time", file_info.getRunning_time(), ContentType.create("Multipart/related", "UTF-8"));
                //파일 데이터 추가
                builder.addPart("multipart_file", new FileBody(file)); //빌더에 FileBody 객체에 인자로 File 객체를 넣어준다.

                //전송
                HttpClient client = AndroidHttpClient.newInstance("Android");

                HttpPost post = new HttpPost(url); //전송할 URL
                try {
                    post.setEntity(builder.build()); //builder.build() 메쏘드를 사용하여 httpEntity 객체를 얻는다.
                    HttpResponse httpRes;
                    httpRes = client.execute(post);
                    HttpEntity httpEntity = httpRes.getEntity();
                    if (httpEntity != null) {
                        response = EntityUtils.toString(httpEntity);
                        Log.d("result", response);
                    }
                } catch (UnsupportedEncodingException e) {
                } catch (ClientProtocolException e1) {
                } catch (IOException e1) {
                } catch (ParseException e) {
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }




    }

}