package us.kulakov.lunch.results.di;

import android.view.LayoutInflater;
import android.view.View;

import us.kulakov.lunch.R;
import us.kulakov.lunch.api.places.models.PlaceSearchItemModel;
import us.kulakov.lunch.results.ResultViewHolder;
import us.kulakov.lunch.results.ResultsAdapter;
import us.kulakov.lunch.utils.ImageViewUtils;

import dagger.Module;
import dagger.Provides;

/**
 * Constructs some dependencies for the results screen.
 */
@Module
public class ResultsModule {
    @ResultsScope
    @Provides
    public ResultsAdapter.ViewAndViewHolderFactory<PlaceSearchItemModel> viewAndViewHolderFactory(ImageViewUtils imageViewUtils) {
        return (parent, pViewType) -> {
            View view1 =
                    LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.result_list_item, parent, false);
            return new ResultsAdapter.ViewAndViewHolderFactory.ViewAndHolder<>(view1,
                    new ResultViewHolder(view1, imageViewUtils));
        };
    }
}
