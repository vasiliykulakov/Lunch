package us.kulakov.lunch.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import us.kulakov.lunch.application.LunchApplication;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onInject(LunchApplication.get(context));
    }

    protected abstract void onInject(LunchApplication application);
}
