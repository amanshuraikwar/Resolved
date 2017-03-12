package com.sonu.resolved.data.network;

import android.support.annotation.NonNull;

import okhttp3.Request;

public class RequestGenerator {

    private static void addDefaultHeaders(@NonNull Request.Builder builder) {
        builder.header("Accept", "application/json");
    }

    public static Request get(@NonNull String url) {
        Request.Builder builder = new Request.Builder().url(url);
        addDefaultHeaders(builder);
        return builder.build();
    }
}
