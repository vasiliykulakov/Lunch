package us.kulakov.lunch.api;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import us.kulakov.lunch.api.places.PlacesApi;
import us.kulakov.lunch.config.ClientConfigProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides
    @Singleton
    PlacesApi providePlacesApi(Gson gson, OkHttpClient okHttpClient,
                               ClientConfigProvider clientConfigProvider) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(clientConfigProvider.getConfig().getGoogleApiBaseUrl())
                .client(okHttpClient)
                .build()
                .create(PlacesApi.class);
    }
}
