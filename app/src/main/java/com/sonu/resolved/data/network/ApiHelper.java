package com.sonu.resolved.data.network;

import io.reactivex.Observable;

/**
 * Created by sonu on 10/3/17.
 */

public interface ApiHelper {
    Observable<Integer> checkUser(String username, String pasword);
}
