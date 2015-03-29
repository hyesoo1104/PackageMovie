package com.example.ohhye.packagemovie.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.singletone_object.Snapmovie;

import java.util.ArrayList;


public class Edit_BgmFragment extends Fragment {

    ListView bgmList;
    static BGMAdapter mAdapter;
    Context mContext;
    static ArrayList<BGMData> dataArr = new ArrayList<BGMData>();
    MediaPlayer mp;
    View rootView;
    View temp;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
        Log.d("onAttach","Attach");
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

        Log.d("onCreateView","onCreateView");
        bgmList = (ListView)rootView.findViewById(R.id.bgmListView);

       /* dataArr.add(new BGMData(R.drawable.icon_bgm_no, "Original",  "녹화된 동영상의 소리가 재생됩니다.", null) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_bikerides, "Bike Rides",  "genre1", "android.resource://" + mContext.getPackageName() + "/"+R.raw.bike_rides) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_blueskies, "Blue Skies",  "genre2", "android.resource://" + mContext.getPackageName() + "/"+R.raw.blue_skies) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_dixeoutlandish, "Dixie Outlandish",  "genre3","android.resource://" + mContext.getPackageName() + "/"+R.raw.dixie_outlandish) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_grassyhill, "Grassy Hill",  "genre4","android.resource://" + mContext.getPackageName() + "/"+R.raw.grassy_hill) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_ifihadachiken, "If I Had A Chicken",  "genre5","android.resource://" + mContext.getPackageName() + "/"+R.raw.if_i_had_a_chicken) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_jackinthebox, "Jack In The Box",  "genre6","android.resource://" + mContext.getPackageName() + "/"+R.raw.jack_in_the_box) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_moringscroll, "Morining Scroll",  "genre7","android.resource://" + mContext.getPackageName() + "/"+R.raw.morning_stroll) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_mrpink, "Mr Pink",  "genre8", "android.resource://" + mContext.getPackageName() + "/"+R.raw.mr_pink) );*/


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
                String path = item.path;
                view.findViewById(R.id.bgm_list_item_checked_area).setBackgroundColor(Color.rgb(255, 198, 0));

                Snapmovie.getSnapmovie().setBGMType(position);

                mAdapter.notifyDataSetChanged();
                if(mAdapter.bgmDataArr.get(position).path!=null) {
                    mp = MediaPlayer.create(mContext, Uri.parse(path));
                    mp.start();
                }
            }
        });


        return rootView;

    }


    public static void addDefaultBGM(Context mContext){
        dataArr.add(new BGMData(R.drawable.icon_bgm_no, "Original",  "녹화된 동영상의 소리가 재생됩니다.", null) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_bikerides, "Bike Rides",  "Bounce", "android.resource://" + mContext.getPackageName() + "/"+R.raw.bike_rides) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_blueskies, "Blue Skies",  "Soft", "android.resource://" + mContext.getPackageName() + "/"+R.raw.blue_skies) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_dixeoutlandish, "Dixie Outlandish",  "Jazz","android.resource://" + mContext.getPackageName() + "/"+R.raw.dixie_outlandish) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_grassyhill, "Grassy Hill",  "Alternative Rock","android.resource://" + mContext.getPackageName() + "/"+R.raw.grassy_hill) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_ifihadachiken, "If I Had A Chicken",  "Happy","android.resource://" + mContext.getPackageName() + "/"+R.raw.if_i_had_a_chicken) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_jackinthebox, "Jack In The Box",  "Ordinary","android.resource://" + mContext.getPackageName() + "/"+R.raw.jack_in_the_box) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_moringscroll, "Morning Scroll",  "Fresh","android.resource://" + mContext.getPackageName() + "/"+R.raw.morning_stroll) );
        dataArr.add(new BGMData(R.drawable.icon_bgm_mrpink, "Mr Pink",  "Reggae", "android.resource://" + mContext.getPackageName() + "/"+R.raw.mr_pink) );
    }

    public static void addCustomBGM(String name, String path, String artist){
        BGMData bgm_data = new BGMData( R.drawable.icon_bgm_custom, name,artist, path);
        dataArr.add(bgm_data);
        mAdapter.notifyDataSetChanged();
    }

    public static void clearBGMList(){
        dataArr.clear();
    }
}



//-----------------------------------------------BGMData--------------------------------------------------------------------------
class BGMData{
    int bgmImg;
    String name;
    String genre;
    String path;

    BGMData(int _bgmImg, String _name, String _genre, String _path){
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
        bgmImage.setImageBitmap( BitmapFactory.decodeResource(context.getResources(), bgmDataArr.get(position).bgmImg));

        TextView bgmName = (TextView)convertView.findViewById(R.id.bgm_list_item_bgm_name);
        bgmName.setText(bgmDataArr.get(position).name);

        TextView bgmGenre = (TextView)convertView.findViewById(R.id.bgm_list_item_bgm_genre);
        bgmGenre.setText(bgmDataArr.get(position).genre);


        return convertView;
    }


}
