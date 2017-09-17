package com.noteworth.lunch.application;

import android.content.Context;

import com.noteworth.lunch.api.GoogleApiModule;
import com.noteworth.lunch.config.ConfigModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, GoogleApiModule.class, ConfigModule.class})
public interface ApplicationComponent {
    Context context();
}
