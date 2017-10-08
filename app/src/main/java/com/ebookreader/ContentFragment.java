package com.ebookreader;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContentFragment extends Fragment {

    public static final String TAG = "MyFragment";
    private String str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_picview, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        //get data
        str = getArguments().getString(TAG);
        tv_title.setText(str);
        return view;
    }
}