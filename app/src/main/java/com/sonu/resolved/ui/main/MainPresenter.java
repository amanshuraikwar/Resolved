package com.sonu.resolved.ui.main;

import com.sonu.resolved.ui.login.LoginMvpView;

import javax.inject.Inject;

/**
 * Created by sonu on 7/3/17.
 */

public class MainPresenter implements MainMvpPresenter{

    private MainMvpView mMainMvpView;

    @Inject
    MainPresenter() {
        //empty
    }

    @Override
    public void onAttach(MainMvpView mainMvpView) {
        this.mMainMvpView = mainMvpView;
    }

    private MainMvpView getMvpView() {
        return this.mMainMvpView;
    }

    @Override
    public void onGoogleApiConnected() {
        getMvpView().relocateUser();
    }

    @Override
    public void getProblems(double lat, double lon) {

    }

    @Override
    public void addProblemFabOnClick() {
        getMvpView().openAddProblemSheet();
    }

    @Override
    public void relocateFabOnClick() {
        getMvpView().relocateUser();
    }

    @Override
    public void saveProblemBtnOnClick() {

    }

    @Override
    public void cancelBtnOnClick() {
        getMvpView().closeAddProblemSheet();
    }
}
