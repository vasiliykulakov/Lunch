package us.kulakov.lunch.application;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import us.kulakov.lunch.BuildConfig;
import us.kulakov.lunch.application.DaggerApplicationComponent;

import timber.log.Timber;

public class LunchApplication extends Application {

    private final ApplicationComponent mApplicationComponent = createComponent();

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        setupTimber();
    }

    public static LunchApplication get(Context context) {
        return (LunchApplication)context.getApplicationContext();
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
