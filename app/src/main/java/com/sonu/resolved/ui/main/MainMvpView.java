package com.sonu.resolved.ui.main;

import com.sonu.resolved.data.network.model.Problem;

import java.util.ArrayList;

/**
 * Created by sonu on 7/3/17.
 */

public interface MainMvpView {
    void relocateUser();
    void displayProblems(ArrayList<Problem> problems);
    void openAddProblemSheet();
    void closeAddProblemSheet();
    void hideFabs();
    void showFabs();
}
