package com.sonu.resolved.di.module;

import android.content.Context;

import com.sonu.resolved.data.AppDataManager;
import com.sonu.resolved.data.DataManager;
import com.sonu.resolved.data.network.ApiHelper;
import com.sonu.resolved.data.network.AppApiHelper;
import com.sonu.resolved.data.network.RequestHandler;
import com.sonu.resolved.data.prefs.AppPreferenceHelper;
import com.sonu.resolved.data.prefs.PreferenceHelper;
import com.sonu.resolved.di.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sonu on 12/3/17.
 */

@Module
public class DataModule {
    @Provides
    @Singleton
    DataManager getDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    AppDataManager getAppDataManager(ApiHelper apiHelper, PreferenceHelper preferenceHelper) {
        return new AppDataManager(apiHelper, preferenceHelper);
    }

    @Provides
    @Singleton
    ApiHelper getApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    AppApiHelper getAppApiHelper(RequestHandler requestHandler) {
        return new AppApiHelper(requestHandler);
    }

    @Provides
    @Singleton
    PreferenceHelper getPreferenceHelper(AppPreferenceHelper appPreferenceHelper) {
        return appPreferenceHelper;
    }

    @Provides
    @Singleton
    AppPreferenceHelper getAppPreferenceHelper(@ApplicationContext Context context) {
        return new AppPreferenceHelper(context);
    }
}
