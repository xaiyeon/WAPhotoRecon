package com.sunkoiwish.waphotorecon.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunkoiwish.waphotorecon.R;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

public class BBLogFragment extends Fragment{

    private static final String TAG = "BBLogFragment";

    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bblog, container, false);
        return view;
    }

}