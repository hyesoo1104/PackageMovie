package com.example.ohhye.packagemovie.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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


public class Edit_TransFrgment extends Fragment {
 
    Context mContext;
    ArrayList<TransData> dataArr = new ArrayList<TransData>();
    ListView transList;
    TransAdapter mAdapter;

    View rootView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        rootView = inflater.inflate(R.layout.fragment_edit_trans, container, false);

        transList = (ListView)rootView.findViewById(R.id.transListView);

        //리스트 아이템 추가
        dataArr.add(new TransData(BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_diptoblack), "Dip to Black",  "점점 어두워지는 전환 효과") );
        dataArr.add(new TransData(BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_diptowhite), "Dip to White",  "점점 밝아지는 전환 효과") );
        dataArr.add(new TransData(BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_crossdissolve), "Cross Dissolve",  "두 영상이 겹쳐지는 전환 효과") );

        mAdapter = new TransAdapter(mContext, R.layout.item_edit_trans_list, dataArr);
        transList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        transList.setAdapter(mAdapter);

        transList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int size = parent.getChildCount();
                for(int i=0; i<size; i++)
                {
                    parent.getChildAt(i).findViewById(R.id.trans_list_item_checked_area).setBackgroundColor(Color.rgb(175, 175, 175));

                }
                TransData item = mAdapter.transDataArr.get(position);
                view.findViewById(R.id.trans_list_item_checked_area).setBackgroundColor(Color.rgb(255, 198, 0));
                mAdapter.notifyDataSetChanged();
            }
        });




        return rootView;
    }



    //-----------------------------------------------TransData--------------------------------------------------------------------------
    class TransData{
        Bitmap transImg;
        String name;
        String description;

        TransData(Bitmap _transImg, String _name, String _description){
            transImg = _transImg;
            name = _name;
            description = _description;
        }
    }


    //-----------------------------------------------Adapter--------------------------------------------------------------------------

    class TransAdapter extends BaseAdapter {
        Context context;
        int layoutId;
        ArrayList<TransData> transDataArr;
        LayoutInflater Inflater;


        TransAdapter(Context _context, int _layoutId, ArrayList<TransData> _transDataArr) {
            context = _context;
            layoutId = _layoutId;
            transDataArr = _transDataArr;
            Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return transDataArr.size();
        }

        @Override
        public Object getItem(int position) {
            return transDataArr.get(position);
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

            ImageView checkedArea = (ImageView)convertView.findViewById(R.id.trans_list_item_checked_area);

            ImageView transImage = (ImageView)convertView.findViewById(R.id.trans_list_item_image);
            transImage.setImageBitmap(transDataArr.get(position).transImg);

            TextView transName = (TextView)convertView.findViewById(R.id.trans_list_item_trans_name);
            transName.setText(transDataArr.get(position).name);

            TextView transDesciption = (TextView)convertView.findViewById(R.id.trans_list_item_trans_description);
            transDesciption.setText(transDataArr.get(position).description);


            return convertView;
        }
    }
}
