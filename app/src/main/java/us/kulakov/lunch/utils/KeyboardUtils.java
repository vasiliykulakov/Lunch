package us.kulakov.lunch.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Inject;

public class KeyboardUtils {
    private final Context mContext;

    @Inject
    public KeyboardUtils(Context context) {
        mContext = context;
    }

    public void dismissKeyboard(@Nullable View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
