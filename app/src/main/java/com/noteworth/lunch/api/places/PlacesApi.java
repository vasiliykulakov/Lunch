package com.noteworth.lunch.api.places;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 *  Google Places API (https://developers.google.com/places/documentation/)
 */
public interface PlacesApi {
    @GET("/maps/api/place/nearbysearch/json")
    Observable<Void> nearby(@Query("key") String apiKey,
                            @Query("location") String location,
                            @Query("keyword") String keyword,
                            @Query("radius") String radius
    );

    @GET("/maps/api/place/nearbysearch/json?rankby=distance")
    Observable<Void> nearbyDistance(@Query("key") String apiKey,
                                    @Query("location") String location,
                                    @Query("keyword") String keyword
    );

    @GET("/maps/api/place/details/json")
    Observable<Void> details(@Query("key") String apiKey,
                             @Query("placeid") String placeId);
}