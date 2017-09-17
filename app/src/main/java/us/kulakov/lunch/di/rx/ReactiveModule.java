package us.kulakov.lunch.di.rx;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Provides RxJava-related dependencies, particularly schedulers. Passing schedulers into ctors is
 * nice because we can mock them easier during testing (even though RxJava/RxAndroid do allow mocks
 * of their own.
 */
@Module
public class ReactiveModule {

    public static final String UI = "ui";

    public static final String COMPUTATION = "computation";

    public static final String IO = "io";

    @Provides
    @Named(UI)
    Scheduler uiScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Named(COMPUTATION)
    Scheduler computationScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @Named(IO)
    Scheduler ioScheduler() {
        return Schedulers.io();
    }

}
