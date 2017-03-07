package com.sonu.resolved.ui.main;

/**
 * Created by sonu on 7/3/17.
 */

public interface MainMvpPresenter {
    void onAttach(MainMvpView mainMvpView);
    void onGoogleApiConnected();
    void getProblems(double lat, double lon);
    void addProblemFabOnClick();
    void relocateFabOnClick();
    void saveProblemBtnOnClick();
    void cancelBtnOnClick();
}
