package com.sonu.resolved.ui.login;

import com.sonu.resolved.utils.Checker;

import javax.inject.Inject;

/**
 * Created by sonu on 3/3/17.
 */

public class LoginPresenter implements LoginMvpPresenter{

    private LoginMvpView mLoginMvpView;

    @Inject
    LoginPresenter(){
        //empty
    }

    @Override
    public void onAttach(LoginMvpView loginMvpView) {
        this.mLoginMvpView = loginMvpView;
    }

    private LoginMvpView getMvpView() {
        return this.mLoginMvpView;
    }

    private boolean isValidData(String username, String email, String password) {
        boolean returnVal = true;
        String error;

        if((error = Checker.username(username)) != null) {
            getMvpView().setUserNameError(error);
            returnVal = false;
        }
        if(email!=null) {
            if((error = Checker.email(email)) != null) {
                getMvpView().setEmailError(error);
                returnVal = false;
            }
        }
        if((error = Checker.password(password)) != null) {
            getMvpView().setPasswordError(error);
            returnVal = false;
        }

        return returnVal;
    }

    @Override
    public void loginClicked(String username, String password) {
        getMvpView().hideEmailView();
        if(isValidData(username, null, password)) {
            getMvpView().showLoading();
        }
    }

    @Override
    public void signupClicked(String username, String email, String password) {
        String error;
        if((error = Checker.email(email)) != null) {
            getMvpView().showEmailView();
            getMvpView().setEmailError(error);
        }
    }
}
