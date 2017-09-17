package us.kulakov.lunch.results.di;

import us.kulakov.lunch.results.ResultsFragment;

import dagger.Subcomponent;

/**
 * Dagger container for results-related dependencies
 */
@ResultsScope
@Subcomponent(modules = { ResultsModule.class })
public interface ResultsComponent {
    void inject(ResultsFragment resultsFragment);
}
