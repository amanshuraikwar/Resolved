package com.sonu.resolved.di.module;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.sonu.resolved.di.PerActivity;
import com.sonu.resolved.ui.login.LoginMvpPresenter;
import com.sonu.resolved.ui.login.LoginMvpView;
import com.sonu.resolved.ui.login.LoginPresenter;
import com.sonu.resolved.ui.main.MainMvpPresenter;
import com.sonu.resolved.ui.main.MainPresenter;

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

    @Provides
    @PerActivity
    MainMvpPresenter getMainMvpPresenter(MainPresenter mainPresenter) {
        return mainPresenter;
    }

    @Provides
    @PerActivity
    GoogleApiClient getGoogleApiClient() {
        return new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks)mActivity)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)mActivity)
                .addApi(LocationServices.API)
                .build();
    }

}
