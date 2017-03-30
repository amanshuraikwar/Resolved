package com.sonu.resolved.ui.problem;

import com.sonu.resolved.base.BaseMvpPresenter;
import com.sonu.resolved.base.BasePresenter;

/**
 * Created by sonu on 18/3/17.
 */

public interface ProblemMvpPresenter extends BaseMvpPresenter<ProblemMvpView>{
    void addComment(String pid, String commentText);
    void getComments(int pid);
}
