package com.noteworth.lunch.api;

import com.google.gson.Gson;
import com.noteworth.lunch.api.places.PlacesApi;
import com.noteworth.lunch.config.ClientConfigProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vkula1 on 9/17/17.
 */
@Module
public class GoogleApiModule {

    @Provides
    @Singleton
    PlacesApi providePlacesApi(Gson gson, OkHttpClient okHttpClient,
                               ClientConfigProvider clientConfigProvider) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(clientConfigProvider.getConfig().getGoogleApiBaseUrl())
                .client(okHttpClient)
                .build()
                .create(PlacesApi.class);
    }
}
