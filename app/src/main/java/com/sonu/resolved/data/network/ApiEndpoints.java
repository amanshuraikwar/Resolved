package com.sonu.resolved.data.network;

/**
 * Created by sonu on 12/3/17.
 */

public class ApiEndpoints {
    public static final String BASE_ENDPOINT = "https://big-value-6648.nanoscaleapi.io/";
    public static final String GET_USER_INFO = BASE_ENDPOINT+"users/%s";
    public static final String GET_USER_INFO_BY_EMAIL = BASE_ENDPOINT+"userByEmail/%s";
    public static final String SIGN_UP_USER = BASE_ENDPOINT+"user";
    public static final String GET_PROBLEMS = BASE_ENDPOINT+"getProblems";
    public static final String ADD_PROBLEM = BASE_ENDPOINT+"addProblem";
    public static final String ADD_COMMENT = BASE_ENDPOINT+"addComment";
    public static final String GET_COMMENTS = BASE_ENDPOINT+"getComments/%s";
}

