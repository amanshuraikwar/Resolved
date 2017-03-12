package com.sonu.resolved.data.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sonu.resolved.data.network.model.User;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.Request;

/**
 * Created by sonu on 10/3/17.
 */

public class AppApiHelper implements ApiHelper{

    private static final String TAG = AppApiHelper.class.getSimpleName();

    private RequestHandler mRequestHandler;

    public AppApiHelper(RequestHandler requestHandler) {
        this.mRequestHandler = requestHandler;
    }

    @Override
    public Observable<Integer> checkUser(final String username, final String pasword) {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                User user = getUserInfo(username);

                if(user == null) {
                    return -1;
                }

                if(user.getPassword().equals(pasword)) {
                    return 1;
                }

                return 0;
            }
        });
    }

    private User getUserInfo(String username) throws IOException{
        String url = String.format(ApiEndpoints.GET_USER_INFO, username);
        Request request = RequestGenerator.get(url);
        String body = mRequestHandler.request(request);
        Log.i(TAG, "getUserInfo():response-body:"+body);

        if(body.equals("null")) {
            return null;
        }

        User[] users = new Gson().fromJson(body, User[].class);

        Log.i(TAG, "getUserInfo():user:"+users[0]);

        return users[0];
    }
}
