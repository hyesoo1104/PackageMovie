package com.example.ohhye.packagemovie.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.example.ohhye.packagemovie.util.Parser;
import com.example.ohhye.packagemovie.vo.UploadFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class UploadBackgroundService extends Service {

    public static Context mContext;

    public static boolean canUpload = true;

    public static String id ="";

    public static SharedPreferences mPref ;

    private static ArrayBlockingQueue<UploadFile> queue =null;

    public static UploadQueue uploadThread;

    public static ArrayBlockingQueue<UploadFile> getUploadQueue(){
        if (queue == null) {
            queue= new ArrayBlockingQueue<UploadFile>(50);
        }
        return queue;
    }


    public void onCreate(){
        Log.d("Service", "Service Start");
        //UploadQueue 동작
        mContext = getApplicationContext();
        mPref = getDefaultSharedPreferences(mContext);
        if(uploadThread==null){
            uploadThread = new UploadQueue();
        }
        uploadThread.execute(getUploadQueue(), null, null);

    }


    public int onStartCommand(Intent intent, int flags, int startId){

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }




    private static Boolean checkNetwordState() {
        if(mPref.getBoolean("WifiOption", false)==true) {
            ConnectivityManager connManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo state_wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(state_wifi.isConnected() ==false){
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }


    public static class UploadQueue extends AsyncTask<ArrayBlockingQueue<UploadFile>, Void, Void> {


        Parser parser = new Parser();


       // public static String id ="";


        String response ="";

        //String path = Environment.getExternalStorageDirectory()+"/Movies/PackageMovie/";
        String url = "http://210.118.74.131:8080/PackageMovie/uploadFile";
        public static Boolean isEnable=true;

        @Override
        protected Void doInBackground(ArrayBlockingQueue<UploadFile>... params) {
            /**
             * 소비자 클래스
             * @author falbb
             *
             */

            while(true) {
                Boolean isWifiOption = UploadBackgroundService.mPref.getBoolean("WifiOption", false);
                //Log.e("WifiOption",checkNetwordState().toString()+"");
                //Wifi일때만 업로드하는데 와이파이가 안켜져이써!!!! 그럼업로드를 못하겠지??????
                if(checkNetwordState()==false){
                    //Log.e("WifiOption",isWifiOption.toString()+"        isConnected-->>"+wifi.isConnected());
                }
                else{
                    try {
                        Log.d("Upload(BackgroundService)", "running");
                        //ArrayBlockingQueue<UploadFile> queue = getUploadQueue();
                        UploadFile file_info = queue.take();

                        if(checkNetwordState()==false){
                            queue.put(file_info);
                            Log.d("Upload(BackgroundService)", "Re enqueue");
                            continue;
                        }

                        Log.d("Upload(BackgroundService)", file_info.getName());

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

                        Log.d("Upload", "\ngroupid  :  " + file_info.getGroupId() + "\nvideo name  :  " + file_info.getName() + "\nrunning time  :  " + file_info.getRunning_time());

                        //전송
                        HttpClient client = AndroidHttpClient.newInstance("Android");
                        HttpPost post = new HttpPost(url); //전송할 URL
                        try {
                            post.setEntity(builder.build()); //builder.build() 메쏘드를 사용하여 httpEntity 객체를 얻는다.
                            HttpResponse httpRes;
                            httpRes = client.execute(post);
                            Log.d("Upload(BackgroundService)", "post request");
                            HttpEntity httpEntity = httpRes.getEntity();
                            if (httpEntity != null) {
                                response = EntityUtils.toString(httpEntity);
                                String result = parser.result_parser(response);
                                Log.d("result", response);
                                if (result.equals("200")) {
                                    Log.d("result", "file list refresh");
                                   // FileManagementActivity.refreshList();
                                }
                            }
                        } catch (Exception e) {
                            getUploadQueue().put(file_info);
                            e.printStackTrace();
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
