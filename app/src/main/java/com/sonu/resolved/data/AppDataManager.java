package com.sonu.resolved.data;

import com.sonu.resolved.data.network.ApiHelper;

import io.reactivex.Observable;

/**
 * Created by sonu on 12/3/17.
 */

public class AppDataManager implements DataManager{

    private ApiHelper mApiHelper;

    public AppDataManager(ApiHelper apiHelper) {
        this.mApiHelper = apiHelper;
    }

    @Override
    public Observable<Integer> checkUser(String username, String pasword) {
        return mApiHelper.checkUser(username, pasword);
    }
}
