package com.sonu.resolved.data.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sonu.resolved.data.network.model.Comment;
import com.sonu.resolved.data.network.model.Problem;
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

    @Override
    public Observable<Boolean> checkIfEmailExists(final String email) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                User user = getUserInfoByEmail(email);

                return user != null;

            }
        });
    }

    @Override
    public Observable<Boolean> checkIfUsernameExists(final String username) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                User user = getUserInfo(username);

                return user != null;

            }
        });
    }

    @Override
    public Observable<Integer> signUpUser(final String username,
                                          final String email,
                                          final String password) {

        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return addUserToDb(username, email, password);
            }
        });
    }

    @Override
    public Observable<Problem[]> getProblems() {
        return Observable.fromCallable(new Callable<Problem[]>() {
            @Override
            public Problem[] call() throws Exception {
                return getProblemsFromDb();
            }
        });
    }

    @Override
    public Observable<Integer> addProblem(final String title,
                                          final String description,
                                          final double latitude,
                                          final double longitude) {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return addProblemToDb(title, description, latitude, longitude);
            }
        }) ;
    }

    @Override
    public Observable<Integer> addComment(final String pid,
                                          final String username,
                                          final String commentText,
                                          final String date,
                                          final String time) {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return addProblemToDb(pid, username, commentText, date, time);
            }
        }) ;
    }

    @Override
    public Observable<Comment[]> getComments(final int pid) {
        return Observable.fromCallable(new Callable<Comment[]>() {
            @Override
            public Comment[] call() throws Exception {
                return getCommentsFromDb(pid);
            }
        }) ;
    }

    private Comment[] getCommentsFromDb(int pid) throws IOException{
        String url = String.format(ApiEndpoints.GET_COMMENTS, pid);
        Request request = RequestGenerator.get(url);
        String body = mRequestHandler.request(request);
        Log.i(TAG, "getCommentsFromDb():response-body:"+body);

        if(body.equals("null")) {
            return null;
        }

        Comment[] comments = new Gson().fromJson(body, Comment[].class);

        //Log.i(TAG, "getUserInfo():user:"+problems[0]);

        return comments;
    }

    private int addProblemToDb(String pid,
                               String username,
                               String commentText,
                               String date,
                               String time) throws IOException{
        String url = ApiEndpoints.ADD_COMMENT;
        Request request = RequestGenerator.put(url,
                "{\"pid\" : "+pid
                        +", \"username\" : \""+username
                        +"\", \"commentText\" : \""+ commentText
                        +"\", \"date\" : \""+ date
                        +"\", \"time\" : \""+time+"\"}");

        String body = mRequestHandler.request(request);

        Log.i(TAG, "addProblemToDb():response-body:"+body);

        return 1;
    }

    private int addProblemToDb(String title,
                               String description,
                               double latitude,
                               double longitude) throws IOException{
        String url = ApiEndpoints.ADD_PROBLEM;
        Request request = RequestGenerator.put(url,
                "{\"title\" : \""+title
                        +"\", \"desc\" : \""+description
                        +"\", \"latitude\" : "+latitude
                        +", \"longitude\" : "+longitude+"}");

        String body = mRequestHandler.request(request);

        Log.i(TAG, "addProblemToDb():response-body:"+body);

        return 1;
    }

    private Problem[] getProblemsFromDb() throws IOException{
        String url = ApiEndpoints.GET_PROBLEMS;
        Request request = RequestGenerator.get(url);
        String body = mRequestHandler.request(request);
        Log.i(TAG, "getProblemsFromDb():response-body:"+body);

        if(body.equals("null")) {
            return null;
        }

        Problem[] problems = new Gson().fromJson(body, Problem[].class);

        //Log.i(TAG, "getUserInfo():user:"+problems[0]);

        return problems;
    }

    private int addUserToDb(String username, String email, String password) throws IOException {
        String url = ApiEndpoints.SIGN_UP_USER;
        Request request = RequestGenerator.put(url,
                "{\"username\" : \""+username
                        +"\", \"email\" : \""+email
                        +"\", \"password\" : \""+password+"\"}");

        String body = mRequestHandler.request(request);

        Log.i(TAG, "addUserToDb():response-body:"+body);

        return 1;
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

    private User getUserInfoByEmail(String email) throws IOException{
        String url = String.format(ApiEndpoints.GET_USER_INFO_BY_EMAIL, email);
        Request request = RequestGenerator.get(url);
        String body = mRequestHandler.request(request);
        Log.i(TAG, "getUserInfoNyEmail():response-body:"+body);

        if(body.equals("null")) {
            return null;
        }

        User[] users = new Gson().fromJson(body, User[].class);

        Log.i(TAG, "getUserInfoNyEmail():user:"+users[0]);

        return users[0];
    }
}
