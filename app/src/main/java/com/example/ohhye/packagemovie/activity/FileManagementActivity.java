package com.example.ohhye.packagemovie.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by ohhye on 2015-01-26.
 */
public class FileManagementActivity  extends ActionBarActivity{

    private final int SELECT_MOVIE = 2;
    File file;
    public static String id;
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

        String id = LoginActivity.getID();
        fileList = (ListView)findViewById(R.id.main_file_list);
        mAdapter = new FileListAdapter(mContext, R.layout.item_file_list, dataArr);


        fileList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        fileList.setAdapter(mAdapter);
        //fileList.setOnItemClickListener(mItemClickListener);
        fileList.setOnItemLongClickListener(mItemLongClickListener);


        btn_file_add = (ImageView)findViewById(R.id.btn_file_add);
        btn_file_add.setOnClickListener(btnClickListener);

        server_ip = this.getText(R.string.server_ip).toString();
        upload_url = server_ip+"uploadFile";

        uploadQueue = UploadBackgroundService.getUploadQueue();
        Log.d("myTag",upload_url);

        refreshList();


    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.file_list_item_thumbnail:
                    String s_path = mAdapter.getItem((Integer)v.getTag()).streaming_path;
                    String v_path = mAdapter.getItem((Integer)v.getTag()).path;

                    Intent i = new Intent(getApplication(), StreamingActivity.class);
                    i.putExtra("streaming_path",s_path);
                    i.putExtra("video_path",v_path);
                    startActivity(i);

                    break;

                case R.id.btn_file_add:
                    selectFile();
                    break;


                case R.id.btn_file_download:
                    int position = (Integer)v.getTag();
                    String file_url =  "http://210.118.74.131:8080/PackageMovie/downloadFile/"+LoginActivity.getID()+"/";

                    String e_name = "";

                    try {
                        e_name = URLEncoder.encode(mAdapter.fileDataArr.get(position).name, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        e_name = mAdapter.fileDataArr.get(position).name;
                    }

                    file_url = file_url+e_name;

                    String fileChk = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)+"/PackageMovie/"+mAdapter.fileDataArr.get(position).name+".mp4";
                    File file = new File(fileChk);
                    Log.e("1",fileChk);


                    if(file.exists())
                        break;

                    download(file_url,mContext);
                    Log.d("Download",file_url+"             //////down!!!");
                    break;
            }
        }
    };


/*    *//*---------------------------------------------------------------------------------------------------------------
        *   Click
    ---------------------------------------------------------------------------------------------------------------*//*
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            String s_path = mAdapter.getItem(position).streaming_path;
            String v_path = mAdapter.getItem(position).path;

            Intent i = new Intent(getApplication(), StreamingActivity.class);
            i.putExtra("streaming_path",s_path);
            i.putExtra("video_path",v_path);
            startActivity(i);
        }
    };*/

    /*---------------------------------------------------------------------------------------------------------------
        *   LongClick
    ---------------------------------------------------------------------------------------------------------------*/
    private AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener() {


        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long l_position) {
            Log.d("LongClick","LongClicksw!!!");
            //LongClick
            final String file_name = dataArr.get(position).name;

            //동영상제목 설정
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

            alert.setMessage("파일 삭제하시겠습니까?");


            alert.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String e_name = "";

                    try {
                        e_name = URLEncoder.encode(mAdapter.fileDataArr.get(position).name, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        e_name = mAdapter.fileDataArr.get(position).name;
                    }
                    Log.d("Name",e_name);
                    net.file_delete(id, e_name, position);
                }
            });

            alert.show();
            return false;
        }
    };




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
                String fileExtend = getExtension(path);

                Log.d("FileSelect","확장자 = "+fileExtend);

                if(fileExtend.equals("mp4")) {
                    //재생시간 얻기
                    running_time = getRunningTime(path,mContext);

                    UploadFile temp = new UploadFile(id, path, name, running_time);

                    try {
                        uploadQueue.put(temp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("FileSelect", "실제경로 : " + path + "\n파일명 : " + name + "\n재생시간 : " + running_time);
                }
                else
                {
                    //TODO: mp4파일만올릴수 있다고 다이얼로그 띄워주기기
                    AlertDialog.Builder ab = new AlertDialog.Builder(FileManagementActivity.this);
                    ab.setMessage("MP4 형식의 동영상만 업로드할 수 있습니다.");
                    ab.setPositiveButton("확인", null);
                    ab.show();

                }
            }

        }
    }

    public static void refreshList(){
        if(dataArr != null)
        {
        dataArr.clear();
        net.load_file_list();
        }
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
        Log.e("Download Uri",uri.toString());
        DownloadManager.Request request = new DownloadManager.Request( uri );

        List<String> pathSegmentList = uri.getPathSegments();
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES + "/PackageMovie").mkdirs();  //경로는 입맛에 따라...바꾸시면됩니다.
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES + "/PackageMovie", pathSegmentList.get(pathSegmentList.size()-1) +".mp4");
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
    public static String getRunningTime(String path,Context context)
    {
        MediaPlayer mp = MediaPlayer.create(context, Uri.parse(path));
        int duration = mp.getDuration();
        mp.release();

        long time = TimeUnit.MILLISECONDS.toSeconds(duration);

        return Long.toString(time);

       /* String rt="";
        //재생시간 얻기
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );

        rt = Long.toString(timeInmillisec);
        return rt;*/
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
        String streaming_path;

        FileData(String _path,Bitmap _Img, String _name, String _duration, String _streaming_path){
            path = _path;
            thumbnail = _Img;
            name = _name;
            duration = _duration;
            streaming_path = _streaming_path;
        }
    }


    /*---------------------------------------------------------------------------------------------------------------
   *   AddItem
   ---------------------------------------------------------------------------------------------------------------*/
    public static void addItem(String path,Bitmap thumbnail, String name, String duration, String streaming_path){
        dataArr.add(new FileData(path,thumbnail,name,duration,streaming_path));
        mAdapter.notifyDataSetChanged();
    }

    /*---------------------------------------------------------------------------------------------------------------
    *   RemoveItem
    ---------------------------------------------------------------------------------------------------------------*/
    public static void removeItem(int position){
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
        public FileData getItem(int position) {
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
            itemThumbnail.setTag(position);
            itemThumbnail.setImageBitmap(fileDataArr.get(position).thumbnail);
            itemThumbnail.setOnClickListener(btnClickListener);

            TextView sceneName = (TextView)convertView.findViewById(R.id.file_list_item_name);
            sceneName.setText(fileDataArr.get(position).name);

            TextView sceneDesciption = (TextView)convertView.findViewById(R.id.file_list_item_duration);
            sceneDesciption.setText(fileDataArr.get(position).duration);

            Button btn_file_download = (Button)convertView.findViewById(R.id.btn_file_download);


            //태그를 이름으로 해놓고 Download 함수에서 isexist 확인한다음에 fasle일때만 name으로 url만들어서 다운로드
            btn_file_download.setTag(position);
            btn_file_download.setOnClickListener(btnClickListener);

            //아니면 리소스를 확인해서 다운로드 할지 안할지 결정정

            String fileChk = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)+"/PackageMovie/"+fileDataArr.get(position).name+".mp4";
            File file = new File(fileChk);


            if(file.exists()) //파일이 존재할때
            {
                btn_file_download.setBackgroundResource(R.drawable.file_exist);
            }
            else { //파일이 존재하지 않을때
                btn_file_download.setBackgroundResource(R.drawable.btn_file_download);
            }

            return convertView;
        }

    }

    public void toast(String result){
        if(result.equals("200"))
            Toast.makeText(mContext, "파일이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        else if(result.equals("601"))
            Toast.makeText(mContext,"존재하지 않은 그룹 아이디입니다.",Toast.LENGTH_SHORT).show();
        else if(result.equals("602"))
            Toast.makeText(mContext,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
    }


    /**
     * 파일의 확장자 조회
     *
     * @param fileStr
     * @return
     */
    public static String getExtension(String fileStr) {
        return fileStr.substring(fileStr.lastIndexOf(".") + 1, fileStr.length());
    }
}