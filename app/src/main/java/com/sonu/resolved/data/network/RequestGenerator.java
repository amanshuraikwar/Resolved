package com.sonu.resolved.data.network;

import android.support.annotation.NonNull;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestGenerator {

    private static void addDefaultHeaders(@NonNull Request.Builder builder) {
        builder.header("Accept", "application/json");
    }

    public static Request get(@NonNull String url) {
        Request.Builder builder = new Request.Builder().url(url);
        addDefaultHeaders(builder);
        return builder.build();
    }

    public static Request put(@NonNull String url, String jsonBody) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonBody);

        Request.Builder builder = new Request.Builder().url(url).put(body);
        addDefaultHeaders(builder);
        return builder.build();
    }

    public static Request post(@NonNull String url, String jsonBody) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonBody);

        Request.Builder builder = new Request.Builder().url(url).post(body);
        addDefaultHeaders(builder);
        return builder.build();
    }
}
