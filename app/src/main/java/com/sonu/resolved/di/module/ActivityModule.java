package com.sonu.resolved.di.module;

import android.app.Activity;
import android.content.Context;

import com.sonu.resolved.di.PerActivity;
import com.sonu.resolved.ui.login.LoginMvpPresenter;
import com.sonu.resolved.ui.login.LoginMvpView;
import com.sonu.resolved.ui.login.LoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sonu on 3/3/17.
 */

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    Context getContext() {
        return this.mActivity;
    }

    @Provides
    @PerActivity
    LoginMvpPresenter getLoginMvpPresenter(LoginPresenter loginPresenter) {
        return loginPresenter;
    }

}
