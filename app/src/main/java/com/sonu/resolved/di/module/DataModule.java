package com.sonu.resolved.di.module;

import com.sonu.resolved.data.AppDataManager;
import com.sonu.resolved.data.DataManager;
import com.sonu.resolved.data.network.ApiHelper;
import com.sonu.resolved.data.network.AppApiHelper;
import com.sonu.resolved.data.network.RequestHandler;

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
    AppDataManager getAppDataManager(ApiHelper apiHelper) {
        return new AppDataManager(apiHelper);
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
}
