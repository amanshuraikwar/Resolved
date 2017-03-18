package com.sonu.resolved.base;

import com.sonu.resolved.data.DataManager;

/**
 * Created by sonu on 18/3/17.
 */

public abstract class BasePresenter<T extends BaseMvpView> implements BaseMvpPresenter<T>{
    protected T mMvpView;
    protected DataManager mDataManager;

    public BasePresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void onAttach(T mvpView) {
        this.mMvpView = mvpView;
    }
}
