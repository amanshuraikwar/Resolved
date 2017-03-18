package com.sonu.resolved.ui.login;

import android.app.Activity;
import android.util.Log;

import com.sonu.resolved.base.BasePresenter;
import com.sonu.resolved.data.DataManager;
import com.sonu.resolved.utils.Checker;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
                            mMvpView.hideLoading();

                            switch (value) {
                                case 0:
                                    mMvpView.setPasswordError("incorrect password");
                                    break;
                                case 1:
                                    mMvpView.startMainActivity();
                                    mDataManager.logInUser(username, null);
                                    break;
                                case -1:
                                    mMvpView.setUserNameError("user does not exist");
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError:called");
                            e.printStackTrace();

                            mMvpView.hideLoading();
                            if(e instanceof IOException) {
                                mMvpView.showErrorSnackBar(e.getLocalizedMessage());
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

            Observable<Boolean> observable = mDataManager.checkIfEmailExists(email);

            observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(emailExistsObserver);

            currentUsername = username;
            currentPassword = password;
            currentEmail = email;
        }
    }

    @Override
    public void onDetach() {

    }

    private Observer<Boolean> emailExistsObserver =
            new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Boolean value) {
                    Log.i(TAG, "Login:onNext():value="+value);
                    mMvpView.hideLoading();

                    if(value) {
                        mMvpView.setEmailError("this email already exists");
                    } else {
                        Observable<Boolean> observable = mDataManager.checkIfUsernameExists(currentUsername);

                        observable
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(usernameExistsObserver);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError:called");
                    e.printStackTrace();

                    mMvpView.hideLoading();
                    if(e instanceof IOException) {
                        mMvpView.showErrorSnackBar(e.getLocalizedMessage());
                    }
                }

                @Override
                public void onComplete() {

                }
            };

    private Observer<Boolean> usernameExistsObserver =
            new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Boolean value) {
                    Log.i(TAG, "Login:onNext():value="+value);
                    mMvpView.hideLoading();

                    if(value) {
                        mMvpView.setUserNameError("this username already exists");
                    } else {
                        Observable<Integer> observable = mDataManager.signUpUser(
                                currentUsername,
                                currentEmail,
                                currentPassword);

                        observable
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(signUpUserObserver);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError:called");
                    e.printStackTrace();

                    mMvpView.hideLoading();
                    if(e instanceof IOException) {
                        mMvpView.showErrorSnackBar(e.getLocalizedMessage());
                    }
                }

                @Override
                public void onComplete() {

                }
            };

    private Observer<Integer> signUpUserObserver =
            new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer value) {
                    Log.i(TAG, "Login:onNext():value="+value);
                    mMvpView.hideLoading();

                    if(value == 1) {
                        mMvpView.startMainActivity();
                        mDataManager.logInUser(currentUsername, currentEmail);
                    } else {
                        mMvpView.showErrorSnackBar("could not sign up");
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError:called");
                    e.printStackTrace();

                    mMvpView.hideLoading();
                    if(e instanceof IOException) {
                        mMvpView.showErrorSnackBar(e.getLocalizedMessage());
                    }
                }

                @Override
                public void onComplete() {

                }
            };

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
