package com.sonu.resolved.data;

import com.sonu.resolved.data.network.ApiHelper;
import com.sonu.resolved.data.network.model.Comment;
import com.sonu.resolved.data.network.model.Problem;
import com.sonu.resolved.data.prefs.PreferenceHelper;

import io.reactivex.Observable;

/**
 * Created by sonu on 12/3/17.
 */

public class AppDataManager implements DataManager{

    private ApiHelper mApiHelper;
    private PreferenceHelper mPreferenceHelper;

    public AppDataManager(ApiHelper apiHelper, PreferenceHelper preferenceHelper) {
        this.mApiHelper = apiHelper;
        this.mPreferenceHelper = preferenceHelper;
    }

    @Override
    public Observable<Integer> checkUser(String username, String pasword) {
        return mApiHelper.checkUser(username, pasword);
    }

    @Override
    public Observable<Boolean> checkIfEmailExists(String email) {
        return mApiHelper.checkIfEmailExists(email);
    }

    @Override
    public Observable<Boolean> checkIfUsernameExists(String username) {
        return mApiHelper.checkIfUsernameExists(username);
    }

    @Override
    public Observable<Integer> signUpUser(String username, String email, String password) {
        return mApiHelper.signUpUser(username, email, password);
    }

    @Override
    public Observable<Problem[]> getProblems() {
        return mApiHelper.getProblems();
    }

    @Override
    public Observable<Integer> addProblem(String title, String description, double latitude, double longitude) {
        return mApiHelper.addProblem(title, description, latitude, longitude);
    }

    @Override
    public Observable<Integer> addComment(String pid, String username, String commentText, String date, String time) {
        return mApiHelper.addComment(pid, username, commentText, date, time);
    }

    @Override
    public Observable<Comment[]> getComments(int pid) {
        return mApiHelper.getComments(pid);
    }

    @Override
    public boolean isUserLoggedIn() {
        return mPreferenceHelper.isUserLoggedIn();
    }

    @Override
    public void logInUser(String username, String email) {
        mPreferenceHelper.logInUser(username, email);
    }

    @Override
    public void logOutUser() {
        mPreferenceHelper.logOutUser();
    }

    @Override
    public String getSavedUsername() {
        return mPreferenceHelper.getSavedUsername();
    }

    @Override
    public String getSavedEmail() {
        return mPreferenceHelper.getSavedEmail();
    }
}
