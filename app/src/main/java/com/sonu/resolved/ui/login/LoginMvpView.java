package com.sonu.resolved.ui.login;

/**
 * Created by sonu on 3/3/17.
 */

public interface LoginMvpView {
    void hideEmailView();
    void showEmailView();
    void setUserNameError(String error);
    void setEmailError(String error);
    void setPasswordError(String error);
    void showLoading();
    void hideLoading();
    void showErrorSnackBar(String error);
    void startMainActivity();
}
