package us.kulakov.lunch.application;

import android.content.Context;

import us.kulakov.lunch.api.ApiModule;
import us.kulakov.lunch.api.network.NetworkModule;
import us.kulakov.lunch.config.ConfigModule;
import us.kulakov.lunch.di.rx.ReactiveModule;
import us.kulakov.lunch.landing.MainActivity;
import us.kulakov.lunch.results.di.ResultsComponent;
import us.kulakov.lunch.results.di.ResultsModule;
import us.kulakov.lunch.search.di.SearchComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        ApiModule.class,
        ConfigModule.class,
        ReactiveModule.class,
        NetworkModule.class,
})
public interface ApplicationComponent {
    Context context();

    SearchComponent plusSearch();
    ResultsComponent plus(ResultsModule resultsModule);

    void inject(MainActivity mainActivity);
}
