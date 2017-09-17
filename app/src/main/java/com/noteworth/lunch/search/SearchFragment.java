package com.noteworth.lunch.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noteworth.lunch.R;
import com.noteworth.lunch.application.LunchApplication;
import com.noteworth.lunch.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by vkula1 on 9/17/17.
 */

public class SearchFragment extends BaseFragment {
    public static final String TAG = SearchFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void onInject(LunchApplication application) {

    }
}
