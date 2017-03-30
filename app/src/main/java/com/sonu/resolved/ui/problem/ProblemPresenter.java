package com.sonu.resolved.ui.problem;

import android.util.Log;

import com.sonu.resolved.base.BasePresenter;
import com.sonu.resolved.data.DataManager;
import com.sonu.resolved.data.network.model.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 18/3/17.
 */

public class ProblemPresenter extends BasePresenter<ProblemMvpView> implements ProblemMvpPresenter{

    private static final String TAG = ProblemPresenter.class.getSimpleName();

    public ProblemPresenter (DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onDetach() {

    }

    @Override
    public void addComment(String pid, String commentText) {
        java.util.Date dt = new java.util.Date();

        DateFormat df= new SimpleDateFormat("yyyy/MM/dd");
        DateFormat tf= new SimpleDateFormat("HH:mm:ss");

        Log.i(TAG, "addComment():date="+df.format(dt)+";time="+tf.format(dt));

        addCommentToDb(pid, mDataManager.getSavedUsername(), commentText, df.format(dt), tf.format(dt));
    }

    @Override
    public void getComments(int pid) {
        Observable<Comment[]> observable = mDataManager.getComments(pid);
        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Comment[] value) {
                        Log.d(TAG, "onNext():called");
                        mMvpView.removeAllData();
                        for(Comment comment : value) {
                            Log.i(TAG, comment.toString());
                            mMvpView.addCommentToList(comment);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError():called");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void addCommentToDb(String pid, String username, String commentText, String date, String time){
        Observable<Integer> observable = mDataManager.addComment(pid, username, commentText, date, time);
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
                        mMvpView.refreshComments();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError():called");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
