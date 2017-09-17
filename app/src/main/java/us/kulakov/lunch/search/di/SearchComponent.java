package us.kulakov.lunch.search.di;

import us.kulakov.lunch.results.di.ResultsScope;
import us.kulakov.lunch.search.SearchFragment;

import dagger.Subcomponent;

@ResultsScope
@Subcomponent // TODO: Add a module for search-specific deps
public interface SearchComponent {
    void inject(SearchFragment fragment);
}
