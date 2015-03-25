package com.example.ohhye.packagemovie.service;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.ohhye.packagemovie.activity.LoginActivity;
import com.example.ohhye.packagemovie.util.Parser;
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

public class UploadBackgroundService extends Service {


    public static String id ="";

    private static ArrayBlockingQueue<UploadFile> queue =null;

    public static ArrayBlockingQueue<UploadFile> getUploadQueue(){
        if (queue == null) {
            queue= new ArrayBlockingQueue<UploadFile>(50);
        }
        return queue;
    }


    public void onCreate(){
        Toast.makeText(getApplicationContext(),"Service Start",Toast.LENGTH_SHORT).show();

    }


    public int onStartCommand(Intent intent, int flags, int startId){
        //UploadQueue 동작
        ArrayBlockingQueue<UploadFile> q = getUploadQueue();
        new UploadQueue(getApplicationContext()).execute(q, null, null);

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    public static class UploadQueue extends AsyncTask<ArrayBlockingQueue<UploadFile>, Void, Void> {

        private Context mContext;
        ProgressDialog mDialog ;

        public UploadQueue(Context context) {
            mContext = context;
            mDialog = new ProgressDialog(mContext);

        }

        Parser parser = new Parser();


       // public static String id ="";


        String response ="";

        //String path = Environment.getExternalStorageDirectory()+"/Movies/PackageMovie/";
        String url = "http://210.118.74.131:8080/PackageMovie/uploadFile";



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setCancelable(true);
            mDialog.setOnCancelListener(cancelListener);
            mDialog.setMessage("업로드중입니다.");
            mDialog.show();
        }

        @Override
        protected Void doInBackground(ArrayBlockingQueue<UploadFile>... params) {
            /**
             * 소비자 클래스
             * @author falbb
             *
             */
            while(true) {
                try {

                    if(LoginActivity.mPref.getBoolean("WifiOption",false)==false)
                    {
                        Log.d("Upload(BackgroundService)", "running");
                        //ArrayBlockingQueue<UploadFile> queue = getUploadQueue();
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

                        Log.d("Upload","\ngroupid  :  " +file_info.getGroupId()+"\nvideo name  :  "+file_info.getName()+"\nrunning time  :  "+file_info.getRunning_time() );

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
                                String result = parser.result_parser(response);
                                Log.d("result", response);
                                if(result.equals("200")){
                                    Log.d("result", "file list refresh");
                                    //FileManagementActivity.refreshList();
                                }
                            }
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }
                        catch (ClientProtocolException e1)
                        {
                            e1.printStackTrace();
                        }
                        catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            if (progress[0].equals("progress")) {
                mDialog.setProgress(Integer.parseInt(progress[1]));
                mDialog.setMessage(progress[2]);
            } else if (progress[0].equals("max")) {
                mDialog.setMax(Integer.parseInt(progress[1]));
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String unused) {
            mDialog.dismiss();
            //Toast.makeText(mContext, Integer.toString(result) + " total sum",
            //Toast.LENGTH_SHORT).show();
        }

        DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }
        };


    }
}
