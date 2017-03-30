package com.sonu.resolved.data.network;

import com.sonu.resolved.data.network.model.Comment;
import com.sonu.resolved.data.network.model.Problem;

import io.reactivex.Observable;

/**
 * Created by sonu on 10/3/17.
 */

public interface ApiHelper {
    Observable<Integer> checkUser(String username, String pasword);
    Observable<Boolean> checkIfEmailExists(String email);
    Observable<Boolean> checkIfUsernameExists(String username);
    Observable<Integer> signUpUser(String username, String email, String password);
    Observable<Problem[]> getProblems();
    Observable<Integer> addProblem(String title, String description, double latitude, double longitude);
    Observable<Integer> addComment(String pid, String username, String commentText, String date, String time);
    Observable<Comment[]> getComments(int pid);
}
