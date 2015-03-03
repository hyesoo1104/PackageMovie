package com.example.ohhye.packagemovie.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
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


public class Edit_ListFragment extends Fragment {


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
        sceneList.setOnDragListener(DragListener);
        return rootView;
    }


    /*
    *   LongClick
    */

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            DragShadow dragShadow = new DragShadow(view);
            Log.d("long clicked", "pos" + " " + position);
            view.startDrag(null, dragShadow, view, 0);
            return false;
        }

    }

    View.OnLongClickListener longListen = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {

            DragShadow dragShadow = new DragShadow(v);

            ClipData data = ClipData.newPlainText("", "");

            v.startDrag(data, dragShadow, v, 0);

            return false;
        }
    };

    private class DragShadow extends View.DragShadowBuilder {

        View greyBox=null;

        public DragShadow(View view) {
            super(view);
            // TODO Auto-generated constructor stub
            greyBox = view;
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            // TODO Auto-generated method stub
            // super.onDrawShadow(canvas);

            greyBox.draw(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize,
                                           Point shadowTouchPoint) {
            // TODO Auto-generated method stub
            // super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
            View v = getView();

            int height = (int) v.getHeight();
            int width = (int) v.getWidth();

            greyBox.setBackgroundColor(Color.rgb(225,225,225));
            shadowSize.set(width, height);

            shadowTouchPoint.set(width / 2, height / 2); //기준점??? 터치포인트가 쉐도우이미지 가운데로됨.

        }


    }

    View.OnDragListener DragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            // TODO Auto-generated method stub

            int dragEvent = event.getAction();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("Drag ", "Entered");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Drag ", "Exit");
                    break;
                case DragEvent.ACTION_DROP:
                    Log.i("Drag ", "Drop");


                    break;
                default:
                    break;
            }

            return true;
        }

    };


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
