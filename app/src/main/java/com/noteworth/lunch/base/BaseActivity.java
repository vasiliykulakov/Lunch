package com.noteworth.lunch.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.noteworth.lunch.application.ApplicationComponent;
import com.noteworth.lunch.application.LunchApplication;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LunchApplication application = LunchApplication.get(this);
        onInject(application, application.component());
    }

    protected abstract void onInject(LunchApplication application, ApplicationComponent component);
}
