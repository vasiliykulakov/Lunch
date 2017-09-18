package us.kulakov.lunch.landing;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import us.kulakov.lunch.R;
import us.kulakov.lunch.application.ApplicationComponent;
import us.kulakov.lunch.application.LunchApplication;
import us.kulakov.lunch.base.BaseActivity;
import us.kulakov.lunch.results.ResultsFragment;
import us.kulakov.lunch.search.SearchFragment;
import us.kulakov.lunch.utils.KeyboardUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private SearchFragment mSearchFragment;
    private ResultsFragment mResultsFragment;

    @Inject
    KeyboardUtils mKeyboardUtils;

    @BindView(R.id.root_layout)
    ViewGroup mRootLayout;

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
        component.inject(this);
    }

    @OnClick(R.id.root_layout)
    public void rootLayoutClick() {
        mKeyboardUtils.dismissKeyboard(mRootLayout);
    }
}
