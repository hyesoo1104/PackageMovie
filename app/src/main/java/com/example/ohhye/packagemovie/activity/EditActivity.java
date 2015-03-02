package com.example.ohhye.packagemovie.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.fragment.Edit_BgmFragment;
import com.example.ohhye.packagemovie.fragment.Edit_ListFragment;
import com.example.ohhye.packagemovie.fragment.Edit_TransFrgment;
import com.example.ohhye.packagemovie.object.Snapmovie;
import com.example.ohhye.packagemovie.util.Edit_AddBGM;

/**
 * Created by ohhye on 2015-01-26.
 */
public class EditActivity  extends Activity implements View.OnClickListener {

    private final int SELECT_MOVIE = 2;

    //Button
    Button btn_edit_add;
    Button btn_edit_list;
    Button btn_edit_text;
    Button btn_edit_trans;
    Button btn_edit_bgm;
    Button btn_edit_complete;

    //Fragment
    private int FRAGMENT_FLAG = 1; //1 : 리스트, 2 : BGM




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_edit);

        Snapmovie.getSnapmovie().resetSnapmovie();

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
        fragmentTransaction.replace(R.id.edit_fragment, new Edit_ListFragment());
        fragmentTransaction.commit();
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
                else if(FRAGMENT_FLAG==2){
                    Log.d("MyTag","Add BGM Click");
                    addBGM();
                }
                break;

            case R.id.btn_edit_list:
                Log.d("MyTag","list btn click");
                FRAGMENT_FLAG = 1;
                btn_edit_text.setBackgroundResource(R.drawable.btn_edit_text);
                fr = new Edit_ListFragment();
                setFragment(fr);
                break;

            case R.id.btn_edit_text:
                if(FRAGMENT_FLAG==1) addText();
                break;

            case R.id.btn_edit_trans:
                FRAGMENT_FLAG = 0;
                btn_edit_text.setBackgroundResource(R.drawable.btn_edit_text_unable);
                Log.d("MyTag","trans btn click");
                fr = new Edit_TransFrgment();
                setFragment(fr);
                break;

            case R.id.btn_edit_bgm:
                Log.d("MyTag","bgm btn click");
                FRAGMENT_FLAG = 2;
                btn_edit_text.setBackgroundResource(R.drawable.btn_edit_text_unable);
                fr = new Edit_BgmFragment();
                setFragment(fr);
                break;

            case R.id.btn_edit_complete:
                break;
        }


    }



    //--------------------------------------------자막추가----------------------------------------------------------------------
    private void addText(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("자막 추가");
        alert.setMessage("추가할 자막의 내용을 입력하세요.");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

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

            }
        });

        alert.show();
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
                Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() );
            }

        }
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
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}