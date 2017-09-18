package us.kulakov.lunch.api.places;

import us.kulakov.lunch.api.places.schema.autocomplete.AutocompleteResponse;
import us.kulakov.lunch.api.places.schema.details.PlaceDetailsResponse;
import us.kulakov.lunch.api.places.schema.geocode.GeocodeResults;
import us.kulakov.lunch.api.places.schema.search.PlacesSearchResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *  Google Places API (https://developers.google.com/places/documentation/)
 */
public interface PlacesApi {

    String RESPONSE_OK = "OK";
    String REQUEST_NEARBY_RADIUS = "80000";
    String REQUEST_NEARBY_RANKBY_DISTANCE = "distance";

    @GET("/maps/api/geocode/json")
    Observable<GeocodeResults> geocode(@Query("key") String apiKey,
                                       @Query("address") String address);

    @GET("/maps/api/place/nearbysearch/json")
    Observable<PlacesSearchResponse> nearby(@Query("key") String apiKey,
                                            @Query("location") String location,
                                            @Query("keyword") String keyword,
                                            // Note, when rankby = distance, radius must be null
                                            @Query("radius") String radius,
                                            @Query("rankby") String rankby
    );


    // TODO: implement
    @GET("/maps/api/place/details/json")
    Observable<PlaceDetailsResponse> details(@Query("key") String apiKey,
                                             @Query("placeid") String placeId);

    @GET("/maps/api/place/autocomplete/json?types=geocode")
    Observable<AutocompleteResponse> autocomplete(@Query("key") String apiKey,
                                                  @Query("input") String input
                                                  );
}