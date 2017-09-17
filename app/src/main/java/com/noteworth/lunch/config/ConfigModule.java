package com.noteworth.lunch.config;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vkula1 on 9/17/17.
 */

@Module
public class ConfigModule {
    @Provides
    @Named(DefaultClientConfigProvider.NAME_CONFIG_DEFAULT)
    ClientConfigProvider defaultClientConfigProvider(DefaultClientConfigProvider provider) {
        return provider;
    }
}
