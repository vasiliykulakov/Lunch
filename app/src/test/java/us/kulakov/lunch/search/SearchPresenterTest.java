package us.kulakov.lunch.search;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import us.kulakov.lunch.api.places.PlacesApi;
import us.kulakov.lunch.api.places.PlacesProvider;
import us.kulakov.lunch.api.places.models.PlaceSearchItemModel;
import us.kulakov.lunch.api.places.models.PlaceSearchSortMethod;
import us.kulakov.lunch.api.places.models.SearchRequest;
import us.kulakov.lunch.api.places.schema.autocomplete.AutocompleteResponse;
import us.kulakov.lunch.api.places.schema.autocomplete.Prediction;
import us.kulakov.lunch.config.ClientConfig;
import us.kulakov.lunch.config.ClientConfigProvider;
import us.kulakov.lunch.utils.ToastUtils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchPresenterTest {
    private SearchPresenter mPresenter;
    private final String mApiKey = "oNuQNoSl73hmCh";

    @Mock private PlacesProvider mPlacesProvider;
    @Mock private ClientConfigProvider mConfigProvider;
    @Mock private Context mContext;
    @Mock private PlacesApi mPlacesApi;
    @Mock private SearchPresenter.SearchView mView;
    @Mock private ClientConfig mConfig;
    @Mock private ToastUtils mToastUtils;

    private TestScheduler mScheduler = new TestScheduler();

    @Before
    public void setUp() throws Exception {
        mPresenter = new SearchPresenter(mPlacesApi, mContext, mConfigProvider, mPlacesProvider,
                mToastUtils, mScheduler, mScheduler);

        when(mConfigProvider.getConfig()).thenReturn(mConfig);
        when(mConfig.getGoogleApiKey()).thenReturn(mApiKey);

        mPresenter.onAttach(mView);
    }

    /**
     * Verifies interaction between various APIs and observable elements that deal with
     * autocomplete. Note that the services themselves aren't tested, we're only testing presenter.
     */
    @Test
    public void initAutocomplete_observesLocationTextChanges_autocompletes() throws Exception {
        // Expecting autocomplete results to be published here, so set up a test observer to
        // look at the values.
        TestObserver<List<String>> testAutocompleteObserver =
                mPresenter.observeAutocomplete().test();

        // The search input term that we'll be autocompleting
        String searchTerm = "pFRLsDXC9vLEMH";

        // Description we expect from the autocomplete API
        String predictionDescription = "Expected description";

        // Mock the complete response from the autocomplete API
        AutocompleteResponse autocompleteResponse = new AutocompleteResponse(
                Collections.singletonList(new Prediction(predictionDescription, null, null)),
                PlacesApi.RESPONSE_OK);

        // Emit our search term when observed
        when(mView.observeSearchText()).thenReturn(Observable.just(searchTerm));

        // Emit the test-double response when autocomplete API observed
        when(mPlacesApi.autocomplete(anyString(), anyString()))
                .thenReturn(Observable.just(autocompleteResponse));

        // Run the test
        mPresenter.initAutocompete();

        mScheduler.triggerActions();

        // Verify invocations
        verify(mView).observeSearchText();
        verify(mPlacesApi).autocomplete(mApiKey, searchTerm);
        // Verify the result is expected
        testAutocompleteObserver
                .assertNoErrors()
                .assertValue(strings -> strings.size() == 1)
                .assertValue(strings -> strings.get(0).equals(predictionDescription));
    }

    /**
     * Verify that when a geocoding result is received, the view is notified to update its location
     * field with the value.
     */
    @Test
    public void initGeocoding_receivesGeocodedResult_updatesView() throws Exception {
        String place1 = "pFRLsDXC9vLEMH";
        String place2 = "Place2";

        when(mPlacesProvider.observeGeocodedResult()).thenReturn(Observable.just(place1, place2));

        mPresenter.initGeocoding();

        mScheduler.triggerActions();

        verify(mView).locationAutocomplete(place1);
        verify(mView).locationAutocomplete(place2);

    }

    @Test
    public void initPlaceSearch_doesNotThrow() throws Exception {
        // Since the search presenter doesn't handle the results coming back,
        // we just want to make sure there's no error when requesting results.

        SearchRequest sourceRequest = new SearchRequest("Apples", "15 Maiden Lane", PlaceSearchSortMethod.RATING);
        PlaceSearchItemModel mockPlace = new PlaceSearchItemModel("nobody", "2222", 3.5f, "http://www.github.com/logo.png");
        List<PlaceSearchItemModel> mockPlaces = Collections.singletonList(mockPlace);

        when(mView.observeSearchAction()).thenReturn(Observable.just(sourceRequest));
        when(mPlacesProvider.observePlacesList(sourceRequest)).thenReturn(Observable.just(mockPlaces));

        mPresenter.initPlaceSearch();
    }

    @After
    public void tearDown() throws Exception {
        mPresenter.onDetach();
    }
}
