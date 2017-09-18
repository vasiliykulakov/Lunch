package us.kulakov.lunch.api.places;

import android.util.Pair;

import us.kulakov.lunch.api.places.models.PlaceItemModelComparator;
import us.kulakov.lunch.api.places.models.PlaceSearchItemModel;
import us.kulakov.lunch.api.places.models.PlaceSearchSortMethod;
import us.kulakov.lunch.api.places.models.SearchRequest;
import us.kulakov.lunch.api.places.schema.Location;
import us.kulakov.lunch.api.places.schema.search.PlacesSearchResponse;
import us.kulakov.lunch.config.ClientConfigProvider;
import us.kulakov.lunch.di.rx.ReactiveModule;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;


@Singleton
public class PlacesProvider {
    private final BehaviorSubject<List<PlaceSearchItemModel>> mResultsSubject
            = BehaviorSubject.create();

    private final BehaviorSubject<Boolean> mSearchWorkingSubject = BehaviorSubject.create();
    private final PublishSubject<String> mGeocodedResultSubject = PublishSubject.create();

    private final PlacesApi mPlacesApi;
    private final ClientConfigProvider mConfigProvider;
    private final PlaceItemModelComparator mModelComparator;
    private final Scheduler mIoScheduler;

    @Inject
    public PlacesProvider(PlacesApi placesApi,
                          ClientConfigProvider configProvider,
                          PlaceItemModelComparator modelComparator,
                          @Named(ReactiveModule.IO) Scheduler ioScheduler) {
        mPlacesApi = placesApi;
        mConfigProvider = configProvider;
        mModelComparator = modelComparator;
        mIoScheduler = ioScheduler;
        mSearchWorkingSubject.onNext(Boolean.FALSE);
    }

    private String getKey() {
        return mConfigProvider.getConfig().getGoogleApiKey();
    }

    public Observable<List<PlaceSearchItemModel>> observePlacesList(SearchRequest request) {
        return Observable.just(request)
                // Let observers know search is in progress
                .doOnNext(searchRequest -> mSearchWorkingSubject.onNext(Boolean.TRUE))
                .filter(searchRequest -> searchRequest.getLocation() != null)
                // Since we need lat/long for nearby Places API searches, we must first map address
                // to lat/long, using the geocode API.
                .flatMap(searchRequest ->  {

                    Observable<Location> locationObservable =
                            mPlacesApi.geocode(getKey(), searchRequest.getLocation())
                                    .filter(geocodeResults ->
                                            geocodeResults.getResults() != null &&
                                                    geocodeResults.getResults().size() > 0)
                                    // Map results object to a single result
                                    .map(geocodeResults -> geocodeResults.getResults().get(0))
                                    .filter(result -> result.getGeometry() != null)
                                    // Let others know what address we actually geocoded
                                    .doOnNext(geocodeResult ->
                                            mGeocodedResultSubject.onNext(
                                                    geocodeResult.getFormattedAddress()))
                                    // Map single result to just location
                                    .map(result -> result.getGeometry().getLocation())
                                    .subscribeOn(mIoScheduler);

                    // Since we still need the original search request, we zip it with the
                    // location result we just got
                    return Observable.zip(locationObservable, Observable.just(searchRequest), Pair::new);
                })
                .flatMap(locationSearchRequestPair -> {
                    SearchRequest searchRequest = locationSearchRequestPair.second;
                    String food = searchRequest.getFood();
                    Location location = locationSearchRequestPair.first;
                    String locationParam = String.format(Locale.US, "%s,%s",
                            location.getLat(), location.getLng());

                    boolean isDistanceSearch =
                            (searchRequest.getSortMethod() == PlaceSearchSortMethod.DISTANCE);

                    // The API takes inputs conditionally depending on how we want to rank our
                    // search results (e.g. nearest-distance search can't specify a radius)

                    String rankby = isDistanceSearch ?
                            PlacesApi.REQUEST_NEARBY_RANKBY_DISTANCE :
                            null;   // Don't provide the rank param (use default "best" ranking)

                    String radius = isDistanceSearch ? null : PlacesApi.REQUEST_NEARBY_RADIUS;

                    if(searchRequest.getSortMethod() == PlaceSearchSortMethod.DISTANCE) {
                        rankby = PlacesApi.REQUEST_NEARBY_RANKBY_DISTANCE;
                    }

                    // Look up nearby places using places API
                    return mPlacesApi.nearby(getKey(), locationParam, food, radius, rankby)
                            .filter(placesSearchResponse ->
                                    placesSearchResponse.getResults() != null &&
                                            placesSearchResponse.getResults().size() > 0)
                            // Map response to a list of results
                            .map(PlacesSearchResponse::getResults)
                            // Map results to a list of result models
                            .flatMap(results ->
                                    Observable.fromIterable(results)
                                            .map(result ->
                                                    new PlaceSearchItemModel(
                                                            result.getName(),
                                                            result.getPlaceId(),
                                                            result.getRating() == null ? 0.0f :
                                                                    result.getRating().floatValue(),
                                                            result.getIcon())
                                            )
                                            .toList()
                                            // API doesn't supporting sorting by "rating", AFAIk, so
                                            // we sort manually in that case.
                                            .map(placeSearchItemModels -> {
                                                if(searchRequest.getSortMethod() == PlaceSearchSortMethod.RATING) {
                                                    Collections.sort(placeSearchItemModels, mModelComparator::compareByRank);
                                                }
                                                return placeSearchItemModels;
                                            })
                                            .toObservable()
                                            .observeOn(mIoScheduler));

                })
                // Let observers know searching isn't in progress anymore
                .doOnNext(searchRequest -> mSearchWorkingSubject.onNext(Boolean.FALSE))
                .doOnNext(mResultsSubject::onNext)
                .subscribeOn(mIoScheduler);
    }

    public Observable<String> observeGeocodedResult() {
        return mGeocodedResultSubject.hide();
    }

    public Observable<List<PlaceSearchItemModel>> observePlacesList() {
        return mResultsSubject.hide();
    }

    public Observable<Boolean> observeIsSearchInProgress() {
        return mSearchWorkingSubject.hide();
    }

    public void clear() {
        mResultsSubject.onComplete();
    }
}
