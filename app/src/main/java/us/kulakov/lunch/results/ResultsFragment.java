package us.kulakov.lunch.results;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.kulakov.lunch.R;
import us.kulakov.lunch.application.LunchApplication;
import us.kulakov.lunch.base.BaseFragment;
import us.kulakov.lunch.results.di.ResultsModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Provides presentation of results
 */
public class ResultsFragment extends BaseFragment implements ResultsPresenter.ResultsView {
    public static final String TAG = ResultsFragment.class.getName();

    @Inject
    ResultsPresenter mPresenter;

    @BindView(R.id.result_list)
    RecyclerView mResultList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        mPresenter.onAttach(this);
        ButterKnife.bind(this, view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mResultList.setLayoutManager(mLayoutManager);
        mResultList.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mPresenter.bindAdapter(mResultList);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetach();
    }

    @Override
    protected void onInject(LunchApplication application) {
        application.component().plus(new ResultsModule()).inject(this);
    }
}
