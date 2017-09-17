package us.kulakov.lunch.api.places.models;

public class PlaceSearchItemModel {
    private final String mTitle;
    private final String mPlaceId;
    private final float mRating;
    private final String mImageUrl;

    public PlaceSearchItemModel(String title, String placeId, float rating, String imageUrl) {
        mTitle = title;
        mPlaceId = placeId;
        mRating = rating;
        mImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public float getRating() {
        return mRating;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
