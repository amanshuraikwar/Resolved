package com.sonu.resolved.ui.main;

import com.sonu.resolved.base.BaseMvpPresenter;

/**
 * Created by sonu on 7/3/17.
 */

public interface MainMvpPresenter extends BaseMvpPresenter<MainMvpView>{
    void onGoogleApiConnected();
    void getProblems(double lat, double lon);
    void addProblemFabOnClick();
    void relocateFabOnClick();
    void saveProblemBtnOnClick(double latitude, double longitude, String title, String description);
    void cancelBtnOnClick();
}
