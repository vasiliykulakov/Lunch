package us.kulakov.lunch.application;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
class ApplicationModule {
    private final Application mApplication;

    ApplicationModule(@NonNull final Application application) {
        this.mApplication = application;
    }

    @Provides
    Context context() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }
}
