package com.sonu.resolved.di.module;

import android.app.Application;
import android.content.Context;

import com.sonu.resolved.data.AppDataManager;
import com.sonu.resolved.data.DataManager;
import com.sonu.resolved.di.ActivityContext;
import com.sonu.resolved.di.ApplicationContext;
import com.sonu.resolved.di.PerActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sonu on 9/3/17.
 */
@Module
public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    @ApplicationContext
    Context getContext() {
        return this.mApplication;
    }

}
