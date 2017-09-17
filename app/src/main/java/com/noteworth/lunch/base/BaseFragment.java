package com.noteworth.lunch.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.noteworth.lunch.application.LunchApplication;

/**
 * Created by vkula1 on 9/17/17.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onInject(LunchApplication.get(context));
    }

    protected abstract void onInject(LunchApplication application);
}
