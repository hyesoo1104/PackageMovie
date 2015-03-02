package com.example.ohhye.packagemovie.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;


public class Edit_ListFragment extends Fragment{


    Context mContext;
    ArrayList<SceneData> dataArr = new ArrayList<SceneData>();
    ListView sceneList;
    SceneListAdapter mAdapter;

    View rootView;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        rootView = inflater.inflate(R.layout.fragment_edit_list, container, false);

        sceneList = (ListView)rootView.findViewById(R.id.edit_scene_list);

        //리스트 아이템 추가
        dataArr.add(new SceneData(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene1",  "03:20") );
        dataArr.add(new SceneData(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene2",  "00:02") );
        dataArr.add(new SceneData(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene3",  "01:15") );

        mAdapter = new SceneListAdapter(mContext, R.layout.item_edit_scene_list, dataArr);
        sceneList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        sceneList.setAdapter(mAdapter);

        sceneList.setOnItemLongClickListener(new ListViewItemLongClickListener());
        return rootView;
    }


    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {


            return false;
        }

    }

    //-----------------------------------------------AddItem--------------------------------------------------------------------------
    public void addItem(ArrayList<SceneData> dataArr, Bitmap thumbnail, String name, String duration){
        dataArr.add(new SceneData(thumbnail,name,duration));
    }


    //-----------------------------------------------SceneData--------------------------------------------------------------------------
    class SceneData{
        Bitmap thumbnail;
        String name;
        String duration;

        SceneData(Bitmap _Img, String _name, String _duration){
            thumbnail = _Img;
            name = _name;
            duration = _duration;
        }
    }


    //-----------------------------------------------Adapter--------------------------------------------------------------------------

    class SceneListAdapter extends BaseAdapter {
        Context context;
        int layoutId;
        ArrayList<SceneData> sceneDataArr;
        LayoutInflater Inflater;


        SceneListAdapter(Context _context, int _layoutId, ArrayList<SceneData> _sceneDataArr) {
            context = _context;
            layoutId = _layoutId;
            sceneDataArr = _sceneDataArr;
            Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return sceneDataArr.size();
        }

        @Override
        public Object getItem(int position) {
            return sceneDataArr.get(position);
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

            ImageView itemThumbnail = (ImageView)convertView.findViewById(R.id.scene_list_item_thumbnail);

            TextView sceneName = (TextView)convertView.findViewById(R.id.scene_list_item_name);
            sceneName.setText(sceneDataArr.get(position).name);

            TextView sceneDesciption = (TextView)convertView.findViewById(R.id.scene_list_item_duration);
            sceneDesciption.setText(sceneDataArr.get(position).duration);


            return convertView;
        }
    }

}
