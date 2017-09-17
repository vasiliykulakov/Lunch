package us.kulakov.lunch.config;

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
