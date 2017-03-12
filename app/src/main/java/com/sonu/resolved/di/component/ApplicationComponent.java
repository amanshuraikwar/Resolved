package com.sonu.resolved.di.component;

import com.sonu.resolved.data.DataManager;
import com.sonu.resolved.di.module.ActivityModule;
import com.sonu.resolved.di.module.ApplicationModule;
import com.sonu.resolved.di.module.DataModule;
import com.sonu.resolved.di.module.NetworkModule;
import com.sonu.resolved.ui.login.LoginActivity;
import com.sonu.resolved.ui.login.LoginPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sonu on 9/3/17.
 */
@Singleton
@Component(modules = {ApplicationModule.class, DataModule.class, NetworkModule.class})
public interface ApplicationComponent {
    DataManager getDataManager();
}
