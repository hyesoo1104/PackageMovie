package com.example.ohhye.packagemovie.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.object.Snapmovie;

import java.util.ArrayList;


public class Edit_BgmFragment extends Fragment {

    ListView bgmList;
    BGMAdapter mAdapter;
    Context mContext;
    ArrayList<BGMData> dataArr = new ArrayList<BGMData>();
    MediaPlayer mp;
    View rootView;
    View temp;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        if(mp!=null) {
            mp.release();
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_edit_bgm, container, false);


        bgmList = (ListView)rootView.findViewById(R.id.bgmListView);

        dataArr.add(new BGMData(BitmapFactory.decodeResource(getResources(),
                R.drawable.bgm_temp_image), "Original",  "녹화된 동영상의 소리가 재생됩니다.", 0) );
        dataArr.add(new BGMData(BitmapFactory.decodeResource(getResources(),
                R.drawable.bgm_temp_image), "Bike Rides",  "genre1", R.raw.bike_rides) );
        dataArr.add(new BGMData(BitmapFactory.decodeResource(getResources(),
                R.drawable.bgm_temp_image), "Blue Skies",  "genre2", R.raw.blue_skies) );
        dataArr.add(new BGMData(BitmapFactory.decodeResource(getResources(),
                R.drawable.bgm_temp_image), "Dixie Outlandish",  "genre3", R.raw.dixie_outlandish) );
        dataArr.add(new BGMData(BitmapFactory.decodeResource(getResources(),
                R.drawable.bgm_temp_image), "Grassy Hill",  "genre4", R.raw.grassy_hill) );
        dataArr.add(new BGMData(BitmapFactory.decodeResource(getResources(),
                R.drawable.bgm_temp_image), "If I Had A Chicken",  "genre5", R.raw.if_i_had_a_chicken) );
        dataArr.add(new BGMData(BitmapFactory.decodeResource(getResources(),
                R.drawable.bgm_temp_image), "Jack In The Box",  "genre6", R.raw.jack_in_the_box) );
        dataArr.add(new BGMData(BitmapFactory.decodeResource(getResources(),
                R.drawable.bgm_temp_image), "Morining Scroll",  "genre7", R.raw.morning_stroll) );
        dataArr.add(new BGMData(BitmapFactory.decodeResource(getResources(),
                R.drawable.bgm_temp_image), "Mr Pink",  "genre8", R.raw.mr_pink) );

        mAdapter = new BGMAdapter(mContext, R.layout.item_edit_bgm_list, dataArr);



        bgmList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        bgmList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        //처음 오리지날 BGM으로 셋팅




        bgmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int size = parent.getChildCount();
                for(int i=0; i<size; i++)
                {
                    parent.getChildAt(i).findViewById(R.id.bgm_list_item_checked_area).setBackgroundColor(Color.rgb(203, 203, 203));
                }
                if(mp!=null){
                    mp.stop();
                }
                BGMData item = mAdapter.bgmDataArr.get(position);
                int path = item.path;
                view.findViewById(R.id.bgm_list_item_checked_area).setBackgroundColor(Color.rgb(255, 198, 0));

                Snapmovie.getSnapmovie().setBGMType(position);

                mAdapter.notifyDataSetChanged();
                if(mAdapter.bgmDataArr.get(position).path!=0) {
                    mp = MediaPlayer.create(mContext, path);
                    mp.start();
                }
            }
        });


        return rootView;

    }



}



//-----------------------------------------------BGMData--------------------------------------------------------------------------
class BGMData{
    Bitmap bgmImg;
    String name;
    String genre;
    int path;

    BGMData(Bitmap _bgmImg, String _name, String _genre, int _path){
        bgmImg = _bgmImg;
        name = _name;
        genre = _genre;
        path = _path;
    }
}

//-----------------------------------------------Adapter--------------------------------------------------------------------------

class BGMAdapter extends BaseAdapter {
    Context context;
    int layoutId;
    ArrayList<BGMData> bgmDataArr;
    LayoutInflater Inflater;


    BGMAdapter(Context _context, int _layoutId, ArrayList<BGMData> _bgmDataArr){
        context = _context;
        layoutId = _layoutId;
        bgmDataArr = _bgmDataArr;
        Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bgmDataArr.size();
    }

    @Override
    public Object getItem(int position) {
        return bgmDataArr.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        // 1번 구역
        if (convertView == null)  {
            convertView = Inflater.inflate(layoutId, parent, false);
        }


        ImageView checkedArea = (ImageView)convertView.findViewById(R.id.bgm_list_item_checked_area);
        if(position==Snapmovie.getSnapmovie().getBGMType())
        {
            checkedArea.setBackgroundColor(Color.rgb(255, 198, 0));
        }
        else
        {
            checkedArea.setBackgroundColor(Color.rgb(203, 203, 203));
        }


        ImageView bgmImage = (ImageView)convertView.findViewById(R.id.bgm_list_item_image);
        bgmImage.setImageBitmap(bgmDataArr.get(position).bgmImg);

        TextView bgmName = (TextView)convertView.findViewById(R.id.bgm_list_item_bgm_name);
        bgmName.setText(bgmDataArr.get(position).name);

        TextView bgmGenre = (TextView)convertView.findViewById(R.id.bgm_list_item_bgm_genre);
        bgmGenre.setText(bgmDataArr.get(position).genre);


        return convertView;
    }


}
