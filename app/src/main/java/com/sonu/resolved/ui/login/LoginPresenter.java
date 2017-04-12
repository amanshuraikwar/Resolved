package com.sonu.resolved.ui.login;

import android.util.Log;

import com.sonu.resolved.base.BasePresenter;
import com.sonu.resolved.base.RestApiException;
import com.sonu.resolved.data.DataManager;
import com.sonu.resolved.utils.Checker;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.BooleanSubscription;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by sonu on 3/3/17.
 */

public class LoginPresenter extends BasePresenter<LoginMvpView> implements LoginMvpPresenter{

    private static final String TAG = LoginPresenter.class.getSimpleName();
    
    private String currentUsername, currentPassword, currentEmail;

    public LoginPresenter(DataManager dataManager){
        super(dataManager);
    }

    @Override
    public void loginClicked(final String username,final String password) {
        mMvpView.hideEmailView();

        if(isValidData(username, null, password)) {
            mMvpView.showLoading();

            Observable<Boolean> observable = mDataManager.checkUserCredentials(username, password);

            observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean value) {
                            Log.i(TAG, "Login:onNext():value="+value);
                            mMvpView.hideLoading();

                            if(value) {
                                mMvpView.startMainActivity();
                                mDataManager.logInUser(username, null);
                            } else {
                                mMvpView.setPasswordError("incorrect password");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError:called");
                            e.printStackTrace();
                            mMvpView.hideLoading();

                            if(e instanceof IOException) {
                                mMvpView.showErrorSnackBar(e.getLocalizedMessage());
                            } else if (e instanceof RestApiException) {
                                switch (((RestApiException)e).getStatusCode()) {
                                    case 812:
                                        mMvpView.setUserNameError("user does not exist");
                                        break;
                                    default:
                                        mMvpView.showErrorSnackBar(e.getMessage());
                                }
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
        mMvpView.showEmailView();

        if(isValidData(username, email, password)) {
            mMvpView.showLoading();

            Observable<Boolean> observable = mDataManager.signUpUser(username, email, password);

            observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean value) {
                            Log.i(TAG, "signupClicked:onNext:onNext():value="+value);
                            mMvpView.hideLoading();

                            if(value) {
                                mMvpView.startMainActivity();
                                mDataManager.logInUser(currentUsername, currentEmail);
                            } else {
                                mMvpView.showErrorSnackBar("could not sign up");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "signupClicked:onError:called");
                            e.printStackTrace();

                            mMvpView.hideLoading();
                            if(e instanceof IOException) {
                                mMvpView.showErrorSnackBar(e.getLocalizedMessage());
                            } else if (e instanceof RestApiException) {
                                switch (((RestApiException)e).getStatusCode()) {
                                    case 811:
                                        mMvpView.setUserNameError("username already exists");
                                        break;
                                    case 810:
                                        mMvpView.setEmailError("email already exists");
                                        break;
                                    default:
                                        mMvpView.showErrorSnackBar(e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void onDetach() {

    }

    private boolean isValidData(String username, String email, String password) {
        boolean returnVal = true;
        String error;

        if((error = Checker.username(username)) != null) {
            mMvpView.setUserNameError(error);
            returnVal = false;
        }
        if(email!=null) {
            if((error = Checker.email(email)) != null) {
                mMvpView.setEmailError(error);
                returnVal = false;
            }
        }
        if((error = Checker.password(password)) != null) {
            mMvpView.setPasswordError(error);
            returnVal = false;
        }

        return returnVal;
    }
}
