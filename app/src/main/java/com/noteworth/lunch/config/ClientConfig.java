package com.noteworth.lunch.config;

/**
 * Created by vkula1 on 9/17/17.
 */

public class ClientConfig {
    private final String mGoogleApiBaseUrl;
    private final String mGoogleApiKey;

    public ClientConfig(String googleApiBaseUrl, String googleApiKey) {
        mGoogleApiBaseUrl = googleApiBaseUrl;
        mGoogleApiKey = googleApiKey;
    }

    public String getGoogleApiKey() {
        return mGoogleApiKey;
    }

    public String getGoogleApiBaseUrl() {
        return mGoogleApiBaseUrl;
    }
}
