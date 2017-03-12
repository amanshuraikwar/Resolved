package com.sonu.resolved;

import android.app.Application;

import com.sonu.resolved.di.component.ApplicationComponent;

import com.sonu.resolved.di.component.DaggerApplicationComponent;
import com.sonu.resolved.di.module.ApplicationModule;
import com.sonu.resolved.di.module.DataModule;
import com.sonu.resolved.di.module.NetworkModule;

/**
 * Created by sonu on 9/3/17.
 */

public class MyApplication extends Application {

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .dataModule(new DataModule())
                .networkModule(new NetworkModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent()  {
        return applicationComponent;
    }
}
