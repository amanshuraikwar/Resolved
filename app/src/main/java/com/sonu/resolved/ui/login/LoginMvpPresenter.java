package com.sonu.resolved.ui.login;

import com.sonu.resolved.base.BaseMvpPresenter;

/**
 * Created by sonu on 3/3/17.
 */

public interface LoginMvpPresenter extends BaseMvpPresenter<LoginMvpView>{
    void loginClicked(String username, String password);
    void signupClicked(String username, String email, String password);
}
