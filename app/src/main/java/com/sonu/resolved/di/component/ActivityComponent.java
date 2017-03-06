package com.sonu.resolved.di.component;

import com.sonu.resolved.di.PerActivity;
import com.sonu.resolved.di.module.ActivityModule;
import com.sonu.resolved.ui.login.LoginActivity;

import dagger.Component;

/**
 * Created by sonu on 3/3/17.
 */

@PerActivity
@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(LoginActivity activity);
}
