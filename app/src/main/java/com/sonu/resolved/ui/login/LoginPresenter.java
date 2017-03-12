package com.sonu.resolved.ui.login;

import android.app.Activity;
import android.util.Log;

import com.sonu.resolved.MyApplication;
import com.sonu.resolved.data.DataManager;
import com.sonu.resolved.data.network.model.User;
import com.sonu.resolved.utils.Checker;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by sonu on 3/3/17.
 */

public class LoginPresenter implements LoginMvpPresenter{

    private static final String TAG = LoginPresenter.class.getSimpleName();

    private LoginMvpView mLoginMvpView;
    private Activity activity;

    private String currentPassword;

    private DataManager mDataManager;

    public LoginPresenter(DataManager dataManager){
        this.mDataManager = dataManager;
    }

    @Override
    public void onAttach(LoginActivity loginActivity) {
        this.activity = loginActivity;
        this.mLoginMvpView = loginActivity;
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
            currentPassword = password;
            getMvpView().showLoading();

            Observable<Integer> observable = mDataManager.checkUser(username, password);

            observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer value) {
                            Log.i(TAG, "Login:onNext():value="+value);
                            getMvpView().hideLoading();

                            switch (value) {
                                case 0:
                                    getMvpView().setPasswordError("incorrect password");
                                    break;
                                case 1:
                                    getMvpView().startMainActivity();
                                    break;
                                case -1:
                                    getMvpView().setUserNameError("user does not exist");
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError:called");
                            e.printStackTrace();

                            getMvpView().hideLoading();
                            if(e instanceof IOException) {
                                getMvpView().showErrorSnackBar(e.getLocalizedMessage());
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void signupClicked(String username, String email, String password) {
        String error;
        if((error = Checker.email(email)) != null) {
            getMvpView().showEmailView();
            getMvpView().setEmailError(error);
        }

        if((error = Checker.username(username)) != null) {
            getMvpView().setUserNameError(error);
        }

        if((error = Checker.password(username)) != null) {
            getMvpView().setPasswordError(error);
        }
    }
}
