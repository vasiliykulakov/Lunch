package us.kulakov.lunch.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<T> {
    private T mView;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    @CallSuper
    public void onAttach(T view) {
        if(isViewAttached()) {
            throw new IllegalStateException(
                    "View " + mView + " is already attached. Cannot onAttach " + view);
        }
        mView = view;
    }

    @CallSuper
    public void onDetach() {
        mDisposables.clear();
        mView = null;
    }

    protected void addDisposable(Disposable disposable) {
        if(!mDisposables.isDisposed()) {
            mDisposables.add(disposable);
        }
    }

    @NonNull
    public T getView() {
        if(!isViewAttached()) {
            throw new RuntimeException("View wanted, but is not attached.");
        }
        return mView;
    }

    public boolean isViewAttached() {
        return mView != null;
    }
}
