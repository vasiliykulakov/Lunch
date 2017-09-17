package com.noteworth.lunch.landing;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.noteworth.lunch.R;
import com.noteworth.lunch.application.ApplicationComponent;
import com.noteworth.lunch.application.LunchApplication;
import com.noteworth.lunch.base.BaseActivity;
import com.noteworth.lunch.results.ResultsFragment;
import com.noteworth.lunch.search.SearchFragment;

/**
 * Created by vkula1 on 9/17/17.
 */

public class MainActivity extends BaseActivity implements RootView {

    private SearchFragment mSearchFragment;
    private ResultsFragment mResultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Create and attach this activity's fragments
            mSearchFragment = new SearchFragment();
            mResultsFragment = new ResultsFragment();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_search, mSearchFragment, SearchFragment.TAG);
            fragmentTransaction.replace(R.id.fragment_results, mResultsFragment, ResultsFragment.TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            mSearchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag(SearchFragment.TAG);
            mResultsFragment = (ResultsFragment) getSupportFragmentManager().findFragmentByTag(ResultsFragment.TAG);
        }
    }

    @Override
    protected void onInject(LunchApplication application, ApplicationComponent component) {

    }

    // RootView impl
    @Override
    public void showResultDetail(int detail) {

    }
}
