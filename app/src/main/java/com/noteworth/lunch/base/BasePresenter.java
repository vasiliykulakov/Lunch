package com.noteworth.lunch.base;

public abstract class BasePresenter<T> {
    private T mView;

    protected void attach(T view) {
        if(isViewAttached()) {
            throw new IllegalStateException(
                    "View " + mView + " is already attached. Cannot attach " + view);
        }
        mView = view;
    }

    protected void detach() {
        mView = null;
    }

    public boolean isViewAttached() {
        return mView != null;
    }
}
