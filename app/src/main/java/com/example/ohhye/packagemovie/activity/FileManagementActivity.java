package com.example.ohhye.packagemovie.activity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.service.UploadBackgroundService;
import com.example.ohhye.packagemovie.util.Network;
import com.example.ohhye.packagemovie.vo.UploadFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ohhye on 2015-01-26.
 */
public class FileManagementActivity  extends ActionBarActivity {
    public static String id = LoginActivity.getID();
    private final int SELECT_MOVIE = 2;
    File file;

    private DownloadManager mDownloadManager; //다운로드 매니저.
    private int mDownloadQueueId; //다운로드 큐 아이디..
    private String mFileName ; //파일다운로드 완료후...파일을 열기 위해 저장된 위치를 입력해둔다.

    private ProgressDialog progressDialog;
    public static final int progressbarType = 0;

    String server_ip = "";
    String upload_url = "";
    String download_url = "http://210.118.74.131:8080/PackageMovie/downloadFile/test1/test3"; //test1 = group_id, test3 = video_name
    String response ="";
    ArrayBlockingQueue<UploadFile> uploadQueue = null;

    private ImageView btn_file_add;
    private Button btn_file_upload;
    private Button btn_file_download;

    private Context mContext;

    static Network net;
    //List View
    static ArrayList<FileData> dataArr = new ArrayList<FileData>();
    static FileListAdapter mAdapter;
    ListView fileList;

    String path = Environment.getExternalStorageDirectory()+"/Movies/PackageMovie/20150309_034953.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_file);

        mContext = this;
        net = new Network(mContext);

        fileList = (ListView)findViewById(R.id.main_file_list);
        mAdapter = new FileListAdapter(mContext, R.layout.item_file_list, dataArr);
        fileList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        fileList.setAdapter(mAdapter);

        fileList.setOnItemLongClickListener(new ListViewItemLongClickListener());


        btn_file_add = (ImageView)findViewById(R.id.btn_file_add);
        btn_file_add.setOnClickListener(btnClickListener);

        server_ip = this.getText(R.string.server_ip).toString();
        upload_url = server_ip+"uploadFile";

        uploadQueue = UploadBackgroundService.getUploadQueue();
        Log.d("myTag",upload_url);

        dataArr.clear();
        net.load_file_list();
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_file_add:
                    selectFile();
                    break;


                case R.id.btn_file_download:

                    String file_url = "http://";
                    file_url = file_url+(String)v.getTag();
                    download(file_url,mContext);
                    Log.d("Download",file_url+"//////down!!!");
                    break;
            }
        }
    };

    /*---------------------------------------------------------------------------------------------------------------
    *   LongClick
    ---------------------------------------------------------------------------------------------------------------*/

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            //LongClick
            return false;
        }

    }



    /*---------------------------------------------------------------------------
     *      Select File & Upload
     *      usage : Just selectFile() Call!
     ---------------------------------------------------------------------------*/

    private void selectFile(){
        //갤러리에서 선택해서 가져오기
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("video/*");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try
        {
            startActivityForResult(i, SELECT_MOVIE);
        } catch (android.content.ActivityNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode,resultCode,intent);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == SELECT_MOVIE)
            {
                Uri uri = intent.getData();
                String path = getPath(uri);
                String name = getName(uri);


                String running_time = "";

                //재생시간 얻기
                running_time = getRunningTime(path);

                UploadFile temp = new UploadFile(id, path, name, running_time);

                try {
                    uploadQueue.put(temp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("FileSelect", "실제경로 : " + path + "\n파일명 : " + name + "\n재생시간 : " + running_time );
            }

        }
    }

    public static void refreshList(){
        dataArr.clear();
        net.load_file_list();
    }

    /*---------------------------------------------------------------------------
     *      File Download
     *      usage : download(download_url,this);
     ---------------------------------------------------------------------------*/

    public void download(String url,Context mContext) {
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        }

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request( uri );

        List<String> pathSegmentList = uri.getPathSegments();
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/PackageMovie").mkdirs();  //경로는 입맛에 따라...바꾸시면됩니다.
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/PackageMovie", pathSegmentList.get(pathSegmentList.size()-1) +".mp4");
        mFileName = pathSegmentList.get(pathSegmentList.size()-1);
        request.setTitle("PackageMovie");
        request.setDescription(mFileName+".mp4");

        mDownloadQueueId = (int) mDownloadManager.enqueue(request);
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progressbarType:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("파일을 다운로드 중입니다. 잠시만 기다려 주십시오...");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(true);
                progressDialog.show();
                return progressDialog;
            default:
                return null;
        }
    }

    protected void onProgressUpdate(String... progress) {
        progressDialog.setProgress(Integer.parseInt(progress[0]));
    }




    private BroadcastReceiver mCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                Toast.makeText(context, "Complete.", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setAction(android.content.Intent.ACTION_VIEW);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                String localUrl = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + "/temp/" + mFileName; //저장했던 경로..
                String extension = MimeTypeMap.getFileExtensionFromUrl(localUrl);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                File file = new File(localUrl);
                intent1.setDataAndType(Uri.fromFile(file), mimeType);
                try {
                    startActivity(intent1);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mCompleteReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mCompleteReceiver, completeFilter);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.finish_fade);
    }





    /*--------------------------------------------
     *  Get File Infomation Function
     --------------------------------------------*/

    //재생시간 얻기
    public static String getRunningTime(String path)
    {
        String rt="";
        //재생시간 얻기
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );

        rt = Long.toString(timeInmillisec);
        return rt;
    }


    // 실제 경로 찾기
    private String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // 파일명 찾기
    private String getName(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.TITLE };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }




    /*---------------------------------------------------------------------------------------------------------------
    *   FileData
   --------------------------------------------------------------------------------------------------------------- */
    static class FileData{
        String path;        //null이면 자막
        Bitmap thumbnail;
        String name;
        String duration;

        FileData(String _path,Bitmap _Img, String _name, String _duration){
            path = _path;
            thumbnail = _Img;
            name = _name;
            duration = _duration;
        }
    }


    /*---------------------------------------------------------------------------------------------------------------
   *   AddItem
   ---------------------------------------------------------------------------------------------------------------*/
    public static void addItem(String path,Bitmap thumbnail, String name, String duration){
        dataArr.add(new FileData(path,thumbnail,name,duration));
        mAdapter.notifyDataSetChanged();
    }

    /*---------------------------------------------------------------------------------------------------------------
    *   RemoveItem
    ---------------------------------------------------------------------------------------------------------------*/
    public void removeItem(int position){
        dataArr.remove(position);
        mAdapter.notifyDataSetChanged();
    }



    /* ---------------------------------------------------------------------------------------------------------------
    *   Adapter
    ---------------------------------------------------------------------------------------------------------------*/

    class FileListAdapter extends BaseAdapter {
        Context context;
        int layoutId;
        ArrayList<FileData> fileDataArr;
        LayoutInflater Inflater;


        FileListAdapter(Context _context, int _layoutId, ArrayList<FileData> _fileDataArr) {
            context = _context;
            layoutId = _layoutId;
            fileDataArr = _fileDataArr;
            Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return fileDataArr.size();
        }

        @Override
        public Object getItem(int position) {
            return fileDataArr.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            // 1번 구역
            if (convertView == null) {
                convertView = Inflater.inflate(layoutId, parent, false);
            }

            ImageView itemThumbnail = (ImageView)convertView.findViewById(R.id.file_list_item_thumbnail);
            itemThumbnail.setImageBitmap(fileDataArr.get(position).thumbnail);

            TextView sceneName = (TextView)convertView.findViewById(R.id.file_list_item_name);
            sceneName.setText(fileDataArr.get(position).name);

            TextView sceneDesciption = (TextView)convertView.findViewById(R.id.file_list_item_duration);
            sceneDesciption.setText(fileDataArr.get(position).duration);

            Button btn_file_download = (Button)convertView.findViewById(R.id.btn_file_download);
            btn_file_download.setTag(fileDataArr.get(position).path);
            btn_file_download.setOnClickListener(btnClickListener);

            return convertView;
        }

    }

}