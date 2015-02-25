package com.example.ohhye.packagemovie;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by ohhye on 2015-02-26.
 */

class MP3Filter implements FilenameFilter{
    public boolean accept(File dir, String name){
        return (name.endsWith(".mp3"));   //확장자가 mp3인지 확인
    }
}


public class Edit_AddBGM extends Activity {
    private Cursor audiocursor;
    private int audio_column_index;
    ListView audiolist;
    int count;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_bgm);
        init_phone_audio_grid();
    }

    private void init_phone_audio_grid() {
        System.gc();
        String[] proj = { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE };
        audiocursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, null);
        count = audiocursor.getCount();
        audiolist = (ListView) findViewById(R.id.PhoneAudioList);
        audiolist.setAdapter(new audioAdapter(getApplicationContext()));
        audiolist.setOnItemClickListener(audiogridlistener);
    }

    private OnItemClickListener audiogridlistener = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position,
                                long id) {
            System.gc();
            audio_column_index = audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            audiocursor.moveToPosition(position);
//            String filename = audiocursor.getString(audio_column_index);
//            Intent intent = new Intent(Edit_AddBGM.this, Viewaudio.class);
//            intent.putExtra("audiofilename", filename);
//            startActivity(intent);
        }
    };

    public class audioAdapter extends BaseAdapter {
        private Context vContext;

        public audioAdapter(Context c) {
            vContext = c;
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            System.gc();
            TextView tv = new TextView(vContext.getApplicationContext());
            String id = null;
            if (convertView == null) {
                audio_column_index = audiocursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                audiocursor.moveToPosition(position);
                id = audiocursor.getString(audio_column_index);
                audio_column_index = audiocursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
                audiocursor.moveToPosition(position);
                tv.setText(id);
            } else
                tv = (TextView) convertView;

            return tv;
        }
    }
}