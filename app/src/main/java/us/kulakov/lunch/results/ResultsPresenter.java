package us.kulakov.lunch.results;

import android.support.v7.widget.RecyclerView;

import us.kulakov.lunch.api.places.PlacesProvider;
import us.kulakov.lunch.api.places.models.PlaceSearchItemModel;
import us.kulakov.lunch.base.BasePresenter;
import us.kulakov.lunch.di.rx.ReactiveModule;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

/**
 * Provides main business logic for displaying results
 */
public class ResultsPresenter extends BasePresenter<ResultsPresenter.ResultsView> {

    private final PlacesProvider mPlacesProvider;
    private final ResultsAdapter<PlaceSearchItemModel> mAdapter;
    private final Scheduler mUiScheduler;

    @Inject
    public ResultsPresenter(PlacesProvider placesProvider,
                            ResultsAdapter<PlaceSearchItemModel> adapter,
                            @Named(ReactiveModule.UI) Scheduler uiScheduler) {
        mPlacesProvider = placesProvider;
        mAdapter = adapter;
        mUiScheduler = uiScheduler;
    }

    @Override
    public void onAttach(ResultsView view) {
        super.onAttach(view);
        mAdapter.setResultStream(mPlacesProvider.observePlacesList().observeOn(mUiScheduler));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter.dispose();
    }

    public void bindAdapter(RecyclerView resultList) {
        resultList.setAdapter(mAdapter);
    }

    public interface ResultsView {

    }
}
