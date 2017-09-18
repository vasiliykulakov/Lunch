package us.kulakov.lunch.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import us.kulakov.lunch.R;
import us.kulakov.lunch.api.places.models.PlaceSearchSortMethod;
import us.kulakov.lunch.api.places.models.SearchRequest;
import us.kulakov.lunch.application.LunchApplication;
import us.kulakov.lunch.base.BaseFragment;
import us.kulakov.lunch.di.rx.ReactiveModule;
import us.kulakov.lunch.utils.KeyboardUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * Handles presentation for the search portion at top of screen.
 */
public class SearchFragment extends BaseFragment implements SearchPresenter.SearchView {
    public static final String TAG = SearchFragment.class.getName();

    private CompositeDisposable mDisposables = new CompositeDisposable();
    private BehaviorSubject<String> mTextSearchInputSubject = BehaviorSubject.create();
    private BehaviorSubject<SearchRequest> mSearchActionSubject = BehaviorSubject.create();

    @Nullable
    private ArrayAdapter<String> mSortOrderAdapter;

    @BindView(R.id.text_location)
    AutoCompleteTextView mTextLocation;

    @BindView(R.id.text_food)
    AutoCompleteTextView mTextFood;

    @BindView(R.id.button_search)
    Button mButtonSearch;

    @BindView(R.id.progress_location_autocomplete)
    ProgressBar mAutocompleteProgressView;

    @BindView(R.id.sort_order_spinner)
    Spinner mSortOrderSpinner;

    @Inject
    @Named(ReactiveModule.UI)
    Scheduler mUiScheduler;

    @Inject
    KeyboardUtils mKeyboardUtils;

    @Inject
    SearchPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mPresenter.onAttach(this);

        ButterKnife.bind(this, view);

        initializeLocationText();

        initializeSpinner();

        mPresenter.initAutocompete();
        mPresenter.initGeocoding();
        mPresenter.initPlaceSearch();

        mDisposables.add(RxTextView.textChangeEvents(mTextLocation)
                // Skip the first emission since it isn't user-generated
                .skip(1)
                // Throttle, to avoid rapid emissions by e.g. quick typing
                .debounce(250, TimeUnit.MILLISECONDS)
                // Text change event becomes a string
                .map(textViewTextChangeEvent -> textViewTextChangeEvent.text().toString())
                // Don't report 1-character searches, they're not gonna be useful anyway
                .filter(s -> s.length() >= 2)
                // Only emit distinct strings
                .distinctUntilChanged()
                .observeOn(mUiScheduler)
                .subscribe(s -> {
                            mTextSearchInputSubject.onNext(s);
                            mAutocompleteProgressView.setVisibility(View.VISIBLE);
                        }, throwable ->
                                displayError(throwable, String.format("Failed to read text input. %s",
                                        throwable.getMessage()))
                ));

        mDisposables.add(mPresenter.observeAutocomplete()
                .observeOn(mUiScheduler)
                .subscribe(this::updateSuggestions, throwable ->
                        displayError(throwable, String.format("Failed to update suggestions. %s",
                                throwable.getMessage()))
                ));

        mDisposables.add(mPresenter.observeShouldDisallowSearching()
                .observeOn(mUiScheduler)
                .subscribe(isSearching -> mButtonSearch.setEnabled(!isSearching), throwable ->
                        displayError(throwable, String.format("Failed to update suggestions. %s",
                                throwable.getMessage()))
                ));

        return view;
    }

    private void initializeLocationText() {
        // TODO: Move this stuff to butterknife/xml
        mTextLocation.setThreshold(1);
        mTextLocation.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            mKeyboardUtils.dismissKeyboard(arg1);
        });
    }

    private void initializeSpinner() {
        // TODO: Create an actual adapter and inject it
        PlaceSearchSortMethod[] values = PlaceSearchSortMethod.values();
        String[] displayValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            displayValues[i] = values[i].name();
        }
        mSortOrderAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                displayValues);
        mSortOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortOrderSpinner.setAdapter(mSortOrderAdapter);
    }

    private void displayError(Throwable throwable, String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        Timber.e(throwable, message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetach();
        mDisposables.clear();
    }

    @Override
    protected void onInject(LunchApplication application) {
        application.component().plusSearch().inject(this);
    }

    // View events
    @OnClick(R.id.button_search)
    public void onSearch() {
        mKeyboardUtils.dismissKeyboard(getView());
        mSearchActionSubject.onNext(new SearchRequest(mTextFood.getText().toString(),
                mTextLocation.getText().toString(), getSelectedSortMethod()));
    }

    public PlaceSearchSortMethod getSelectedSortMethod() {
        if(mSortOrderSpinner != null && mSortOrderSpinner.getSelectedItem() instanceof String) {
            return PlaceSearchSortMethod.valueOf((String) mSortOrderSpinner.getSelectedItem());
        }
        return PlaceSearchSortMethod.DEFAULT;
    }


    // SearchView impl

    @Override
    public Observable<String> observeSearchText() {
        return mTextSearchInputSubject;
    }

    @Override
    public void locationAutocomplete(String locationString) {
        mTextLocation.setText(locationString);
        mTextLocation.dismissDropDown();
    }

    @Override
    public Observable<SearchRequest> observeSearchAction() {
        return mSearchActionSubject.hide();
    }

    private void updateSuggestions(List<String> suggestions) {
        mAutocompleteProgressView.setVisibility(View.GONE);
        // TODO: Implement custom adapter and update it instead of re-initializing
        // This will help avoid some unnecessary queries, and save autocompleted result's place ID
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.select_dialog_item, suggestions);
        mTextLocation.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
