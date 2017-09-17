package us.kulakov.lunch.results;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import us.kulakov.lunch.di.rx.ReactiveModule;
import us.kulakov.lunch.results.di.ResultsScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Provides a reactive way to publish results for a recycler view, and a reactive way to listen
 * to events on the views (e.g. clicks).
 * This class is constructed by DI.
 * Based on https://gist.github.com/nightscape/23cd6fc45223afcc0ee892ca68012791
 */
@ResultsScope
public class ResultsAdapter<T> extends RecyclerView.Adapter<ResultsAdapter.ViewHolder<T>> {

    private final ViewAndViewHolderFactory<T> mViewAndViewHolderFactory;
    private final Scheduler mUiScheduler;
    private final CompositeDisposable mDisposables = new CompositeDisposable();
    private final PublishSubject<T> mItemClickSubject = PublishSubject.create();
    private final ArrayList<T> mNullResult = new ArrayList<>(0);

    @Nullable
    private Observable<List<T>> mResults;
    @NonNull
    private List<T> mCurrentResult;

    @Inject
    public ResultsAdapter(ViewAndViewHolderFactory<T> viewAndViewHolderFactory,
                          @Named(ReactiveModule.UI)Scheduler uiScheduler) {
        mViewAndViewHolderFactory = viewAndViewHolderFactory;
        mUiScheduler = uiScheduler;
        mCurrentResult = mNullResult;
    }

    public void dispose() {
        mDisposables.clear();
    }

    public void setResultStream(@Nullable Observable<List<T>> results) {
        mResults = results;
        mDisposables.clear();

        if(mResults == null) {
            mCurrentResult = mNullResult;
            return;
        }

        mDisposables.add(mResults
                .observeOn(mUiScheduler)
                .subscribe(result -> {
                            mCurrentResult = result;
                            notifyDataSetChanged();
                        },
                        throwable -> Timber.e(throwable, "Could not update displayed results"))
        );
    }

    // TODO: observe item click from presenter, and show the details view
    public Observable<T> observeItemClick() {
        return mItemClickSubject;
    }

    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int pViewType) {
        ViewAndViewHolderFactory.ViewAndHolder<T> viewAndHolder =
                mViewAndViewHolderFactory.createViewAndHolder(parent, pViewType);

        ViewHolder<T> viewHolder = viewAndHolder.viewHolder;

        mDisposables.add(RxView.clicks(viewAndHolder.view)
                .takeUntil(RxView.detaches(parent))
                // Map view clicks to the item model
                .map(object -> viewHolder.getCurrentItem())
                // Publish the click event to our subject
                .subscribe(mItemClickSubject::onNext, throwable ->
                        Timber.e(throwable, "Error listening for result item click events"))
        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        T item = mCurrentResult.get(position);
        holder.setCurrentItem(item);
    }

    @Override
    public int getItemCount() {
        return mCurrentResult.size();
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setCurrentItem(T t);
        public abstract T getCurrentItem();
    }

    public interface ViewAndViewHolderFactory<T> {

        class ViewAndHolder<T> {
            private final View view;
            private final ViewHolder<T> viewHolder;

            public ViewAndHolder(View view, ViewHolder<T> viewHolder) {
                this.view = view;
                this.viewHolder = viewHolder;
            }

            public View getView() {
                return view;
            }

            public ViewHolder<T> getViewHolder() {
                return viewHolder;
            }
        }
        ViewAndHolder<T> createViewAndHolder(@NonNull ViewGroup parent, int pViewType);
    }
}