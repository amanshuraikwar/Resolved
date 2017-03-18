package com.sonu.resolved.ui.login;

import com.sonu.resolved.base.BaseMvpView;

/**
 * Created by sonu on 3/3/17.
 */

public interface LoginMvpView extends BaseMvpView{
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
