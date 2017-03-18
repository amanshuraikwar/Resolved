package com.sonu.resolved.base;

/**
 * Created by sonu on 18/3/17.
 */

public interface BaseMvpPresenter<T extends BaseMvpView> {
    void onAttach(T mvpView);
    void onDetach();
}
