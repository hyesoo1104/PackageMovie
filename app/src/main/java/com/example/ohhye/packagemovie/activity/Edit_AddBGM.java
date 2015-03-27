package com.example.ohhye.packagemovie.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.fragment.Edit_BgmFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ohhye on 2015-02-26.
 */

public class Edit_AddBGM extends ListActivity {
    private static final String MEDIA_PATH = new String("/sdcard/Music");
    // ROOT 경로를 지정합니다.
    private List<String> songs = new ArrayList<String>();
    private MediaPlayer mp = new MediaPlayer();
    private int currentPosition = 0;

    String[][] mSongList = null;
    Cursor mCursor;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_bgm);
        updateSongList();

    }

    public void updateSongList() {
      /*  File home = new File(MEDIA_PATH);
        if (home.listFiles(new Mp3Filter()).length > 0) {
            for (File file : home.listFiles(new Mp3Filter())) {
                songs.add(file.getName());
            }
            ArrayAdapter<String> songList = new ArrayAdapter<String>(this,R.layout.item_custom_bgm, songs);
            setListAdapter(songList);
        }*/

        ArrayAdapter<String> songList = new ArrayAdapter<String>(this, R.layout.item_custom_bgm,R.id.custom_bgm_name,songs);

        String[] pro = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST
        };

        mCursor = (Cursor)this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, pro, null, null, null);

        if(mCursor != null)
        {
            mSongList = new String[mCursor.getCount()][3];
            mCursor.moveToFirst();
            for(int i = 0 ; i < mCursor.getCount() ; i++)
            {
                mCursor.moveToPosition(i);
                mSongList[i][0] = mCursor.getString(0);
                mSongList[i][1] = mCursor.getString(1);
                mSongList[i][2] = mCursor.getString(2);
                songList.add(mSongList[i][1]);
            }
        }
        setListAdapter(songList);
    }

    // List 아이템을 클릭했을 때의 event를 처리합니다.
    protected void onListItemClick(ListView l, View v, int position, long id) {
        currentPosition = position;
        Log.d("BGM List","Name : "+mSongList[position][1]+"  //  "+"Path  :  "+mSongList[position][0]);


        String fileExtend = getExtension(mSongList[position][0]);
        if(fileExtend.equals("mp3")){
            Edit_BgmFragment.addCustomBGM(mSongList[position][1], mSongList[position][0], mSongList[position][2]);
            this.finish();
        }
        else{
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setMessage("MP3 형식의 음악만 사용할 수 있습니다.");
            ab.setPositiveButton("확인", null);
            ab.show();
        }

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
