package com.sonu.resolved.base;

/**
 * Created by sonu on 6/4/17.
 */

public class RestApiException extends Exception {
    private int statusCode;
    private String status;

    public RestApiException(int statusCode, String status, String message) {
        super(message);
        this.statusCode = statusCode;
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
