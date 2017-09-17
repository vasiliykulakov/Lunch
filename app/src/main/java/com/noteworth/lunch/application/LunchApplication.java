package com.noteworth.lunch.application;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.noteworth.lunch.BuildConfig;

import timber.log.Timber;

public class LunchApplication extends Application {

    private final ApplicationComponent mApplicationComponent = createComponent();

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        setupTimber();
    }

    protected ApplicationComponent createComponent() {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent component() {
        return mApplicationComponent;
    }

    private void setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
