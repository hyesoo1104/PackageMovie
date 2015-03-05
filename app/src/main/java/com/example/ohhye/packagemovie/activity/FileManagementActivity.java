package com.example.ohhye.packagemovie.activity;

import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ohhye.packagemovie.R;

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

/**
 * Created by ohhye on 2015-01-26.
 */
public class FileManagementActivity  extends ActionBarActivity implements View.OnClickListener {
    public static String id ="";

    String url = "";
    String response ="";

    private ImageView btn_file_add;
    private Button btn_file_upload;

    String path = Environment.getExternalStorageDirectory()+"/DCIM/Camera/20140614_045425.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_file);

        btn_file_add = (ImageView)findViewById(R.id.btn_file_add);
        btn_file_add.setOnClickListener(this);
        btn_file_upload = (Button)findViewById(R.id.btn_upload);
        btn_file_upload.setOnClickListener(this);
        url = this.getText(R.string.server_ip).toString();
        url = url+"uploadFile";
        Log.d("myTag",url);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_file_add:

            break;

            case R.id.btn_upload:
                send_file(path);
            break;
        }
    }


    public void send_file(String _path) {
        File file = new File(_path);
        Log.d("filename", file.toString());
        //Multipart 객체를 선언한다.
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //문자열 데이터 추가
        builder.addTextBody("group_id", "sadf", ContentType.create("Multipart/related", "UTF-8")); //스트링 데이터..
        builder.addPart("multipartFile", new FileBody(file)); //빌더에 FileBody 객체에 인자로 File 객체를 넣어준다.

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
            }
        } catch (UnsupportedEncodingException e) {
        } catch (ClientProtocolException e1) {
        } catch (IOException e1) {
        } catch (ParseException e) {
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.finish_fade);
    }

}