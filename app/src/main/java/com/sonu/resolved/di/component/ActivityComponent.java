package com.sonu.resolved.di.component;

import com.sonu.resolved.di.PerActivity;
import com.sonu.resolved.di.module.ActivityModule;
import com.sonu.resolved.di.module.ApplicationModule;
import com.sonu.resolved.di.module.DataModule;
import com.sonu.resolved.di.module.NetworkModule;
import com.sonu.resolved.ui.login.LoginActivity;
import com.sonu.resolved.ui.main.MainActivity;
import com.sonu.resolved.ui.problem.ProblemActivity;

import dagger.Component;

/**
 * Created by sonu on 3/3/17.
 */

@PerActivity
@Component( dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(ProblemActivity activity);
}
