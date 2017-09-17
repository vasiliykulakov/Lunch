package us.kulakov.lunch.search;

import android.content.Context;

import us.kulakov.lunch.api.places.PlacesApi;
import us.kulakov.lunch.api.places.PlacesProvider;
import us.kulakov.lunch.api.places.models.PlaceSearchItemModel;
import us.kulakov.lunch.api.places.models.SearchRequest;
import us.kulakov.lunch.api.places.schema.autocomplete.AutocompleteResponse;
import us.kulakov.lunch.api.places.schema.autocomplete.Prediction;
import us.kulakov.lunch.base.BasePresenter;
import us.kulakov.lunch.config.ClientConfigProvider;
import us.kulakov.lunch.di.rx.ReactiveModule;
import us.kulakov.lunch.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class SearchPresenter extends BasePresenter<SearchPresenter.SearchView> {

    private final PlacesApi mPlacesApi;
    private final Context mContext;
    private final ClientConfigProvider mConfigProvider;
    private final PlacesProvider mPlacesProvider;
    private final ToastUtils mToastUtils;
    private final Scheduler mIoScheduler;
    private final Scheduler mUiScheduler;

    private final PublishSubject<List<String>> mAutocompleteSubject = PublishSubject.create();

    @Inject
    public SearchPresenter(PlacesApi placesApi,
                           Context context,
                           ClientConfigProvider configProvider,
                           PlacesProvider placesProvider,
                           ToastUtils toastUtils,
                           @Named(ReactiveModule.IO) Scheduler ioScheduler,
                           @Named(ReactiveModule.UI) Scheduler uiScheduler) {
        mPlacesApi = placesApi;
        mContext = context;
        mConfigProvider = configProvider;
        mPlacesProvider = placesProvider;
        mToastUtils = toastUtils;
        mIoScheduler = ioScheduler;
        mUiScheduler = uiScheduler;
    }

    /**
     * Observes the text search field and performs autocomplete
     * @see SearchView#observeSearchText()
     * @see #observeAutocomplete()
     */
    public void initAutocompete() {
        SearchView view = getView();
        // Observe the search text field, and perform autocomplete
        addDisposable(view.observeSearchText()
                // Map search string to a set of suggestions
                .flatMap(searchString -> mPlacesApi
                        .autocomplete(getKey(), searchString)
                        .subscribeOn(mIoScheduler)
                        .doOnNext(autocompleteResponse -> {
                            String status = autocompleteResponse.getStatus();
                            // Fire an error if we didn't get an "OK" from Google
                            if(!PlacesApi.RESPONSE_OK.equals(status)) {
                                throw new Exception(String.format("Google API failure: %s", status));
                            }
                        })
                        .filter(autocompleteResponse ->
                                autocompleteResponse.getPredictions() != null)
                        // Map a response into a list of predictions
                        .map(AutocompleteResponse::getPredictions)
                        // Map the list of predictions into a list of strings
                        .flatMap(list ->
                                Observable.fromIterable(list)
                                        .map(Prediction::getDescription)
                                        .toList()
                                        .toObservable()
                        ))
                .subscribeOn(mIoScheduler)
                .observeOn(mUiScheduler)
                // Publish that list of results to the autocomplete observers
                .subscribe(mAutocompleteSubject::onNext, e -> {
                    String userFacingError = String.format("Couldn't autocomplete: %s", e.getMessage());
                    mToastUtils.makeErrorToast(mContext, userFacingError);
                    Timber.e(e, "Trouble fetching autocomplete results");
                })
        );
    }

    /**
     * Tell the view about geocoding results, whenever they happen.
     * @see SearchView#locationAutocomplete(String)
     */
    public void initGeocoding() {
        SearchView view = getView();
        addDisposable(mPlacesProvider.observeGeocodedResult()
                .observeOn(mIoScheduler)
                .subscribeOn(mUiScheduler)
                .subscribe(view::locationAutocomplete,
                        t -> Timber.e(t, "Error updating geocoding results to view"))
        );
    }

    /**
     * Observes when a search action is needed, performs place search
     * @see SearchView#observeSearchAction()
     * @see PlacesProvider#observePlacesList(SearchRequest)
     */
    public void initPlaceSearch() {
        SearchView view = getView();
        addDisposable(view.observeSearchAction()
                // Forward the request to places provider to return us an observable list of results
                .flatMap(mPlacesProvider::observePlacesList)
                // If there's an error, just show an empty result list instead of handling it
                .onErrorResumeNext(throwable -> {
                    return Observable.<List<PlaceSearchItemModel>>just(new ArrayList<>(0));
                })
                .observeOn(mUiScheduler)
                .subscribeWith(new DisposableObserver<List<PlaceSearchItemModel>>() {

                    @Override
                    public void onNext(List<PlaceSearchItemModel> results) {
                        // Update / hide search UI when we have a non-empty set?
                    }

                    @Override
                    public void onError(Throwable e) {
                        String userFacingError =
                                String.format("Cannot get search results: %s", e.getMessage());
                        mToastUtils.makeErrorToast(mContext, userFacingError);
                        Timber.e(e, "Could not get nearby places");
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    private String getKey() {
        return mConfigProvider.getConfig().getGoogleApiKey();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public Observable<Boolean> observeShouldDisallowSearching() {
        return mPlacesProvider.observeIsSearchInProgress();
    }

    public Observable<List<String>> observeAutocomplete() {
        return mAutocompleteSubject.hide();
    }

    public interface SearchView {
        Observable<String> observeSearchText();
        Observable<SearchRequest> observeSearchAction();
        void locationAutocomplete(String locationString);
    }
}
