package com.sonu.resolved.ui.login;

/**
 * Created by sonu on 3/3/17.
 */

public interface LoginMvpPresenter {
    void onAttach(LoginActivity loginActivity);
    void loginClicked(String username, String password);
    void signupClicked(String username, String email, String password);
}
