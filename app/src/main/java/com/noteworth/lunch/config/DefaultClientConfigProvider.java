package com.noteworth.lunch.config;

import javax.inject.Inject;

/**
 * Created by vkula1 on 9/17/17.
 */

public class DefaultClientConfigProvider implements ClientConfigProvider {
    public static final String NAME_CONFIG_DEFAULT = "default_config";

    private static final String GOOGLE_API_BASE_URL = "https://maps.googleapis.com";
    private static final String GOOGLE_API_KEY = "";

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
