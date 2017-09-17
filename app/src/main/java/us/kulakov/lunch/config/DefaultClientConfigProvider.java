package us.kulakov.lunch.config;

import javax.inject.Inject;

/**
 * Provides default client (this app) configuration, the simplest possible implementation in lieu
 * of time.
 */
public class DefaultClientConfigProvider implements ClientConfigProvider {
    private static final String GOOGLE_API_BASE_URL = "https://maps.googleapis.com";
    private static final String GOOGLE_API_KEY = "AIzaSyACA2jHLQELLycySMM7L8lfZ2oL_a-42oA";

    private ClientConfig mCurrentConfig;

    @Inject
    public DefaultClientConfigProvider() {
    }

    @Override
    public synchronized ClientConfig getConfig() {
        if(mCurrentConfig == null) {
            mCurrentConfig = new ClientConfig(GOOGLE_API_BASE_URL, GOOGLE_API_KEY);
        }
        return mCurrentConfig;
    }
}
