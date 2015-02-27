package com.example.ohhye.packagemovie.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;

/**
 * Created by ohhye on 2015-02-26.
 */

public class Edit_AddBGM extends Activity {
    ContentResolver contentResolver;
    MediaPlayer mediaPlayer;

    Cursor cursor;
    ListView listView;

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_edit_add_bgm);

        listView = (ListView)findViewById(R.id.edit_add_bgm_list);

        contentResolver = getContentResolver();
        cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);

        SimpleCursorAdapter simpleCursorAdapter =
                new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
                                        new String[]{MediaStore.MediaColumns.DISPLAY_NAME},
                                        new int[]{android.R.id.text1});

        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(onItemClickListener);

        startManagingCursor(cursor);
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position,
                                long id) {
            Toast.makeText(Edit_AddBGM.this,"click",Toast.LENGTH_SHORT).show();
        }
    };

}