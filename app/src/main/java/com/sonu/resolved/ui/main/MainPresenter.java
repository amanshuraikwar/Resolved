package com.sonu.resolved.ui.main;

import android.util.Log;

import com.sonu.resolved.base.BasePresenter;
import com.sonu.resolved.data.DataManager;
import com.sonu.resolved.data.network.model.Problem;
import com.sonu.resolved.ui.login.LoginMvpView;
import com.sonu.resolved.utils.Checker;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 7/3/17.
 */

public class MainPresenter extends BasePresenter<MainMvpView> implements MainMvpPresenter{

    private static final String TAG = MainPresenter.class.getSimpleName();
    
    public MainPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onAttach(MainMvpView mvpView) {
        super.onAttach(mvpView);

        if(!mDataManager.isUserLoggedIn()) {
            mvpView.startLoginActivity();
        }
    }

    @Override
    public void onDetach() {
        
    }
    
    @Override
    public void onGoogleApiConnected() {
        mMvpView.relocateUser();
    }

    @Override
    public void getProblems(double lat, double lon) {
        Observable<Problem[]> observable = mDataManager.getProblems();

        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Problem[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Problem[] value) {
                        for(Problem problem : value) {
                            Log.i(TAG, problem.toString());
                            mMvpView.addMarkerOnMap(problem);
                        }
                        mMvpView.clusterMarkers();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void addProblemFabOnClick() {
        mMvpView.openAddProblemSheet();
    }

    @Override
    public void relocateFabOnClick() {
        mMvpView.relocateUser();
    }

    @Override
    public void saveProblemBtnOnClick(final double latitude, final double longitude, final String title, final String description) {
        if(isValidProblemData(title, description)) {
            mMvpView.showLoading();
            Observable<Integer> observable = mDataManager.addProblem(title, description, latitude, longitude);
            observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer value) {
                            Log.d(TAG, "onNext():called");
                            Log.i(TAG, "onNext():value="+value);
                            mMvpView.hideLoading();
                            mMvpView.closeAddProblemSheet();

                            mMvpView.focusCameraOn(latitude, longitude);

                            Problem temp = new Problem();
                            temp.setTitle(title);
                            temp.setDescription(description);
                            temp.setLatitude(latitude);
                            temp.setLongitude(longitude);
                            mMvpView.addMarkerOnMap(temp);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mMvpView.hideLoading();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private boolean isValidProblemData(String title, String description) {
            boolean returnVal = true;
            String error;

            if((error = Checker.problemTitle(title)) != null) {
                mMvpView.setProblemTitleError(error);
                returnVal = false;
            }

        if((error = Checker.problemDescription(description)) != null) {
            mMvpView.setProblemDescriptionError(error);
            returnVal = false;
        }

        return returnVal;
    }

    @Override
    public void cancelBtnOnClick() {
        mMvpView.closeAddProblemSheet();
    }
}
