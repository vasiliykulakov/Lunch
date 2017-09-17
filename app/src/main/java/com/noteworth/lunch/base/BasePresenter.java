package com.noteworth.lunch.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<T> {
    private final CompositeDisposable mAttachedDisposables = new CompositeDisposable();
    private final CompositeDisposable mVisibleDisposables = new CompositeDisposable();
    private T mView;

    /**
     * To be called when your view is attached to a presenter.
     *
     * @param view View attached to the presenter
     */
    @CallSuper
    public void onViewAttached(@NonNull final T view) {
        if (isViewAttached()) {
            throw new IllegalStateException(
                    "View " + this.mView + " is already attached. Cannot attach " + view);
        }
        this.mView = view;
    }

    /**
     * On mView will show. Called when your mView is about to be seen on the screen.
     */
    @CallSuper
    public void onViewWillShow(@NonNull final T view) {

    }

    /**
     * On mView will hide. Called when your mView is about to hide from the screen.
     */
    @CallSuper
    public void onViewWillHide() {
        mVisibleDisposables.clear();
    }

    /**
     * On mView detached. Intended as a cleanup process that should be called when the mView will no
     * longer be in use.
     */
    @CallSuper
    public void onViewDetached() {
        if (!isViewAttached()) {
            throw new IllegalStateException("View is already detached");
        }
        mView = null;

        mAttachedDisposables.clear();
    }

    /**
     * Dispose on mView will hide.
     *
     * @param disposable Disposable to be disposed of upon mView will hide
     */
    @CallSuper
    protected void disposeOnViewWillHide(@NonNull final Disposable disposable) {
        mVisibleDisposables.add(disposable);
    }

    /**
     * Dispose on mView detach.
     *
     * @param disposable Disposable to be disposed of upon mView detachment
     */
    @CallSuper
    protected void disposeOnViewDetach(@NonNull final Disposable disposable) {
        mAttachedDisposables.add(disposable);
    }

    public boolean isViewAttached() {
        return mView != null;
    }
}
