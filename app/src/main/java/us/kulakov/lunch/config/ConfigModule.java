package us.kulakov.lunch.config;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigModule {
    @Provides
    ClientConfigProvider defaultClientConfigProvider(DefaultClientConfigProvider provider) {
        return provider;
    }
}
