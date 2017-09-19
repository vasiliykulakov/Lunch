package us.kulakov.lunch.uitest.di;


public final class EspressoPages {

    private final static PagesComponent sPageComponent = DaggerPagesComponent.create();

    public static PagesComponent getPagesComponent() {
        return sPageComponent;
    }
}
