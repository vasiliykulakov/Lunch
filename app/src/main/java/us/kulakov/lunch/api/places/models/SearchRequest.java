package us.kulakov.lunch.api.places.models;

public class SearchRequest {
    private final String mFood;
    private final String mLocation;
    private final PlaceSearchSortMethod mSortMethod;

    public SearchRequest(String food, String location, PlaceSearchSortMethod sortMethod) {
        mFood = food;
        mLocation = location;
        mSortMethod = sortMethod;
    }

    public String getFood() {
        return mFood;
    }

    public String getLocation() {
        return mLocation;
    }

    public PlaceSearchSortMethod getSortMethod() {
        return mSortMethod;
    }
}
