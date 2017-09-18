package us.kulakov.lunch.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import us.kulakov.lunch.application.ApplicationComponent;
import us.kulakov.lunch.application.LunchApplication;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LunchApplication application = LunchApplication.get(this);
        onInject(application, application.component());
    }

    protected abstract void onInject(LunchApplication application, ApplicationComponent component);
}
