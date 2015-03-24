package com.example.ohhye.packagemovie.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.util.DndListView;
import com.example.ohhye.packagemovie.util.Network;

import java.util.ArrayList;

public class Edit_SceneListFragment extends ListFragment implements DndListView.DragListener, DndListView.DropListener {

    boolean isLoad = false;
    Network net;
    Context mContext;

    static ArrayList<SceneData> dataArr = new ArrayList<SceneData>();
    DndListView sceneList;
    static SceneListAdapter mAdapter;

    int selectedItemPosition = -1;

    ImageView removeArea = null;

    View greyBox=null;
    View rootView;

    private boolean mLockListView;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();

    }

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    // private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static Edit_SceneListFragment newInstance(String param1, String param2) {
        Edit_SceneListFragment fragment = new Edit_SceneListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Edit_SceneListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//
//
//        // TODO: Change Adapter to display your content
//        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        sceneList = (DndListView)view.findViewById(android.R.id.list);

        net = new Network(mContext);


        //데이터 리셋
        clearArr();


        //리스트 아이템 추가
        dataArr.add(new SceneData("path1", BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene1",  "03:20") );
        dataArr.add(new SceneData("path2",BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene2",  "00:02") );
        dataArr.add(new SceneData("path3",BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene3",  "01:15") );
        dataArr.add(new SceneData("path4", BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene4",  "03:20") );
        dataArr.add(new SceneData("path5",BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene5",  "00:02") );
        dataArr.add(new SceneData("path6",BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene6",  "01:15") );
        dataArr.add(new SceneData("path7", BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene7",  "03:20") );
        dataArr.add(new SceneData("path8",BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene8",  "00:02") );
        dataArr.add(new SceneData("path9",BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene9",  "01:15") );
        dataArr.add(new SceneData("path6",BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene10",  "01:15") );
        dataArr.add(new SceneData("path7", BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene11",  "03:20") );
        dataArr.add(new SceneData("path8",BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene12",  "00:02") );
        dataArr.add(new SceneData("path9",BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher), "Scene13",  "01:15") );

        mAdapter = new SceneListAdapter(mContext, R.layout.item_edit_scene_list, dataArr);
        sceneList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        sceneList.setAdapter(mAdapter);


        sceneList.setDragListener(this);
        sceneList.setDropListener(this);

        // Set OnItemClickListener so we can be notified on item clicks
        sceneList.setOnItemLongClickListener(new ListViewItemLongClickListener());

        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void drag(int from, int to) {
        Log.d("Drag","Drag!!!!");
    }

    @Override
    public void drop(int from, int to) {
        Log.d("Drop","Drop!!!!");
        SceneData item = dataArr.remove(from);
        dataArr.add(to, item);
        mAdapter.notifyDataSetChanged();

    }




    /*---------------------------------------------------------------------------------------------------------------
    *   LongClick
    ---------------------------------------------------------------------------------------------------------------*/

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
           removeItem(position);
           return false;
        }
    }


    /*---------------------------------------------------------------------------------------------------------------
  *  Data Array Clear
  ---------------------------------------------------------------------------------------------------------------*/
    public static void clearArr(){
        dataArr.clear();
    }

    /*---------------------------------------------------------------------------------------------------------------
 *   AddItem
 ---------------------------------------------------------------------------------------------------------------*/
    public static void addItem(String path,Bitmap thumbnail, String name, String duration){
        dataArr.add(new SceneData(path,thumbnail,name,duration));
        mAdapter.notifyDataSetChanged();
    }

    /*---------------------------------------------------------------------------------------------------------------
    *   RemoveItem
    ---------------------------------------------------------------------------------------------------------------*/
    public static void removeItem(int position){
        dataArr.remove(position);
        mAdapter.notifyDataSetChanged();
    }




    /*---------------------------------------------------------------------------------------------------------------
    *   SceneData
   --------------------------------------------------------------------------------------------------------------- */
    static class SceneData{
        String path;        //null이면 자막
        Bitmap thumbnail;
        String name;
        String duration;

        SceneData(String _path,Bitmap _Img, String _name, String _duration){
            path = _path;
            thumbnail = _Img;
            name = _name;
            duration = _duration;
        }
    }


    /* ---------------------------------------------------------------------------------------------------------------
    *   Adapter
    ---------------------------------------------------------------------------------------------------------------*/

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
            itemThumbnail.setImageBitmap(sceneDataArr.get(position).thumbnail);

            TextView sceneName = (TextView)convertView.findViewById(R.id.scene_list_item_name);
            sceneName.setText(sceneDataArr.get(position).name);

            TextView sceneDesciption = (TextView)convertView.findViewById(R.id.scene_list_item_duration);
            sceneDesciption.setText(sceneDataArr.get(position).duration);


            return convertView;
        }
    }



 /*   *//**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     *//*
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }*/



}
