package us.kulakov.lunch.utils;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

public class ToastUtils {

    @Inject
    public ToastUtils() {
    }

    public void makeErrorToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
