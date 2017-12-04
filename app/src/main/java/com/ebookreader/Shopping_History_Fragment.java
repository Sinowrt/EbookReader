package com.ebookreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Jacky on 2017/12/4.
 */

public class Shopping_History_Fragment extends Fragment {
    private View rootView;

    GridView mListView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (null == rootView) {
            rootView = inflater.inflate(R.layout.order_confirm_fragment, container,
                    false);
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();

        if (null != parent) {
            parent.removeView(rootView);
        }

        return rootView;

    }

    private void initView(View rootView){

    }

    private void init_Gview_Data(){

    }
}