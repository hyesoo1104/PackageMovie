package com.example.ohhye.packagemovie;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Edit_BgmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Edit_BgmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit_BgmFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_edit_bgm, container, false);
    }

}
