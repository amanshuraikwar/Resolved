package com.sonu.resolved.ui.main;

import com.sonu.resolved.base.BaseMvpView;
import com.sonu.resolved.data.network.model.Problem;

import java.util.ArrayList;

/**
 * Created by sonu on 7/3/17.
 */

public interface MainMvpView extends BaseMvpView{
    void relocateUser();
    void displayProblems(ArrayList<Problem> problems);
    void openAddProblemSheet();
    void closeAddProblemSheet();
    void hideFabs();
    void showFabs();
    void addMarkerOnMap(Problem problem);
    void setProblemTitleError(String error);
    void setProblemDescriptionError(String error);
    void showLoading();
    void hideLoading();
    void focusCameraOn(double lat, double lon);
    void hideAddProblemFab();
    void showAddProblemFab();
    void startLoginActivity();
}
