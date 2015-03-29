package com.example.ohhye.packagemovie.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.fragment.Edit_BgmFragment;
import com.example.ohhye.packagemovie.fragment.Edit_SceneListFragment;
import com.example.ohhye.packagemovie.fragment.Edit_TransFrgment;
import com.example.ohhye.packagemovie.singletone_object.Snapmovie;
import com.example.ohhye.packagemovie.util.Network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by ohhye on 2015-01-26.
 */
public class EditActivity  extends Activity implements View.OnClickListener {

    private final int SELECT_MOVIE = 2;

    Network net;
    //Button
    Button btn_edit_add;
    Button btn_edit_list;
    Button btn_edit_text;
    Button btn_edit_trans;
    Button btn_edit_bgm;
    Button btn_edit_complete;

    //Fragment
    private int FRAGMENT_FLAG = 1; //1 : 리스트, 2 : 전환효과, 3 : BGM


    @Override
    public void onBackPressed() {
        //TODO :팝업창띄워서 메인으로 돌아가면 편집하던 내용을 다시 복구할 수 없다고 창띄워주기.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("편집 종료")
                .setMessage("편집한 내용은 저장되지 않습니다.\n 메인 메뉴로 돌아가시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_edit);

        Snapmovie.getSnapmovie().resetSnapmovie();

        net = new Network(getApplicationContext());

        btn_edit_add = (Button)findViewById(R.id.btn_edit_add);
        btn_edit_list = (Button)findViewById(R.id.btn_edit_list);
        btn_edit_text = (Button)findViewById(R.id.btn_edit_text);
        btn_edit_trans = (Button)findViewById(R.id.btn_edit_trans);
        btn_edit_bgm = (Button)findViewById(R.id.btn_edit_bgm);
        btn_edit_complete = (Button)findViewById(R.id.btn_edit_complete);

        btn_edit_add.setOnClickListener(this);
        btn_edit_list.setOnClickListener(this);
        btn_edit_text.setOnClickListener(this);
        btn_edit_trans.setOnClickListener(this);
        btn_edit_bgm.setOnClickListener(this);
        btn_edit_complete.setOnClickListener(this);



        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.edit_fragment, new Edit_SceneListFragment());
        fragmentTransaction.commit();


        Log.d("EditActivity","onCreate");
        //서버로부터 리스트 로딩
        Edit_SceneListFragment.clearSceneList();
        net.load_scene_list();

        /* dataArr.add(new BGMData(R.drawable.icon_bgm_no, "Original",  "녹화된 동영상의 소리가 재생됩니다.", null) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_bikerides, "Bike Rides",  "genre1", "android.resource://" + mContext.getPackageName() + "/"+R.raw.bike_rides) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_blueskies, "Blue Skies",  "genre2", "android.resource://" + mContext.getPackageName() + "/"+R.raw.blue_skies) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_dixeoutlandish, "Dixie Outlandish",  "genre3","android.resource://" + mContext.getPackageName() + "/"+R.raw.dixie_outlandish) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_grassyhill, "Grassy Hill",  "genre4","android.resource://" + mContext.getPackageName() + "/"+R.raw.grassy_hill) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_ifihadachiken, "If I Had A Chicken",  "genre5","android.resource://" + mContext.getPackageName() + "/"+R.raw.if_i_had_a_chicken) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_jackinthebox, "Jack In The Box",  "genre6","android.resource://" + mContext.getPackageName() + "/"+R.raw.jack_in_the_box) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_moringscroll, "Morining Scroll",  "genre7","android.resource://" + mContext.getPackageName() + "/"+R.raw.morning_stroll) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_mrpink, "Mr Pink",  "genre8", "android.resource://" + mContext.getPackageName() + "/"+R.raw.mr_pink) );*/

        Edit_BgmFragment.clearBGMList();
        Edit_BgmFragment.addDefaultBGM(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.finish_fade);
    }

    //선택된 것들을 하나의 객체로 담아두기


    private void setFragment(Fragment fr){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.edit_fragment, fr);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View view) {
        Fragment fr = null;

        switch (view.getId()){
            case R.id.btn_edit_add:
                //리스트
                if(FRAGMENT_FLAG==1){
                    Log.d("MyTag","Add Scene Click");
                    addScene();
                }
                //BGM
                else if(FRAGMENT_FLAG==3){
                    Log.d("MyTag","Add BGM Click");
                    addBGM();
                }
                break;

            case R.id.btn_edit_list:
                Log.d("MyTag","list btn click");
                FRAGMENT_FLAG = 1;
                btn_edit_add.setBackgroundResource(R.drawable.btn_add_video);
                btn_edit_list.setBackgroundResource(R.drawable.btn_scenelist_selected);
                btn_edit_text.setBackgroundResource(R.drawable.btn_add_subtitle);
                btn_edit_trans.setBackgroundResource(R.drawable.btn_trasition);
                btn_edit_bgm.setBackgroundResource(R.drawable.btn_bgm);
                fr = new Edit_SceneListFragment();
                //net.load_scene_list();
                setFragment(fr);
                break;

            case R.id.btn_edit_text:
                if(FRAGMENT_FLAG==1) addText();
                break;

            case R.id.btn_edit_trans:
                FRAGMENT_FLAG = 2;
                btn_edit_add.setBackgroundResource(R.drawable.btn_edit_unable);
                btn_edit_list.setBackgroundResource(R.drawable.btn_scenelist);
                btn_edit_text.setBackgroundResource(R.drawable.btn_edit_unable);
                btn_edit_trans.setBackgroundResource(R.drawable.btn_trasition_selected);
                btn_edit_bgm.setBackgroundResource(R.drawable.btn_bgm);
                Log.d("MyTag","trans btn click");
                fr = new Edit_TransFrgment();
                setFragment(fr);
                break;

            case R.id.btn_edit_bgm:
                Log.d("MyTag","bgm btn click");
                FRAGMENT_FLAG = 3;
                btn_edit_add.setBackgroundResource(R.drawable.btn_add_bgm);
                btn_edit_list.setBackgroundResource(R.drawable.btn_scenelist);
                btn_edit_text.setBackgroundResource(R.drawable.btn_edit_unable);
                btn_edit_trans.setBackgroundResource(R.drawable.btn_trasition);
                btn_edit_bgm.setBackgroundResource(R.drawable.btn_bgm_selected);
                fr = new Edit_BgmFragment();
                setFragment(fr);
                break;

            case R.id.btn_edit_complete:
                Log.e("SnapMovie Count", Snapmovie.getSnapmovie().getListCount()+"");

                Log.d("SnapMovie SceneList", Snapmovie.getSnapmovie().getSceneList().toString());

                Log.d("SnapMovie BGMPath",Snapmovie.getSnapmovie().getBGMPath().toString());
                break;
        }


    }






    //--------------------------------------------자막추가----------------------------------------------------------------------
    private void addText(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("자막 추가");
        alert.setMessage("추가할 자막의 내용을 입력하세요. (20자 이내)");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(20);
        input.setFilters(FilterArray);

        alert.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Do something with value!
                Toast.makeText(EditActivity.this, value,Toast.LENGTH_SHORT).show();
                Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.edit_text_icon);
                String subtitle_path = createText(value);
                Edit_SceneListFragment.addItem(subtitle_path, bm, value, "0:3");

            }
        });

        alert.show();
    }


    public String createText(String text){
        Typeface font = Typeface.createFromAsset(getAssets(),"NanumBarunpenR.ttf");

        String path = "";

        Bitmap bm = Bitmap.createBitmap(480, 480, Bitmap.Config.ARGB_8888);
        Paint pnt = new Paint();
        Canvas c = new Canvas(bm);
        int centerPos = (c.getWidth() / 2);

        c.drawColor(Color.BLACK);
        pnt.reset();
        pnt.setColor(Color.WHITE);
        pnt.setTextAlign(Paint.Align.CENTER);
        pnt.setAntiAlias(true);
        pnt.setTextSize(25);
        pnt.setTypeface(font);
        c.drawText(text,centerPos,centerPos,pnt);

        // imageview.setImageBitmap(bm);
        Date d = new Date();
        //CharSequence s  = DateFormat.format("EEEE, MMMM d, yyyy ", d.getTime());

        File file = new File("/storage/emulated/0/" + Environment.DIRECTORY_MOVIES + "/PackageMovie/"+text+ ".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.PNG, 0, fos);

        path = file.getAbsolutePath();

        return path;
    }


    //--------------------------------------------음악추가----------------------------------------------------------------------
    private void addBGM(){
        //폰에 있는 MP3파일 중 고르기
        Intent i =  new Intent(this,Edit_AddBGM.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i); // 로딩이 끝난후 이동할 Activity
    }





    //--------------------------------------------영상추가----------------------------------------------------------------------
    private void addScene(){
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
                String id = getId(uri);         //ID는 썸네일 추출시 필요
                String fileExtend = getExtension(path);

                Log.d("FileSelect","확장자 = "+fileExtend);

                if(fileExtend.equals("mp4"))
                {
                    String running_time = "";

                    //재생시간 얻기
                    running_time = getRunningTime(path);

                    long video_id = Long.parseLong(id);

                    //썸네일 얻기
                    Bitmap bm = mGetVideoThumnailImg(video_id);

                    Edit_SceneListFragment.addItem(path,bm,name,running_time);
                    Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\n재생시간 : " + running_time + "\nID : " + id );
                }
                else
                {
                    AlertDialog.Builder ab = new AlertDialog.Builder(EditActivity.this);
                    ab.setMessage("MP4 형식의 동영상만 편집할 수 있습니다.");
                    ab.setPositiveButton("확인", null);
                    ab.show();

               }
            }

        }
    }

    //재생시간 얻기
    public static String getRunningTime(String path)
    {
        String rt="";
        //재생시간 얻기
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );
        long duration = timeInmillisec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);

        if(hours!=0)
        {
            rt = rt + hours+":"+minutes + ":" + seconds;
        }
        else
        {
            rt = minutes + ":" + seconds;
        }
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

    private String getId(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns._ID };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    //썸네일 만들기
    public Bitmap mGetVideoThumnailImg(long id)
    {
        ContentResolver mCrThumb 			=   this.getContentResolver();
        BitmapFactory.Options options		=	new BitmapFactory.Options();
        options.inSampleSize 						= 1;

        //MICRO_KIND :작은이미지(정사각형) MINI_KIND (중간이미지)

        Bitmap mVideoThumnailBm 		=
                MediaStore.Video.Thumbnails.getThumbnail(mCrThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);

        Bitmap resizedThumbnail = Bitmap.createScaledBitmap(mVideoThumnailBm,200,160,true);
        if(resizedThumbnail!=null)
        {
            Log.d("resizedThumbnail","동영상의 썸네일의 가로 :"+resizedThumbnail.getWidth()+"입니다");
            Log.d("resizedThumbnail", "동영상의 썸네일의 세로 :" + resizedThumbnail.getHeight() + "입니다");
        }


        mCrThumb		=	null;
        options			=	null;



        return resizedThumbnail;

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