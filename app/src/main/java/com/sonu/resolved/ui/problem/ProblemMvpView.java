package com.sonu.resolved.ui.problem;

import com.sonu.resolved.base.BaseMvpView;
import com.sonu.resolved.data.network.model.Comment;

/**
 * Created by sonu on 18/3/17.
 */

public interface ProblemMvpView extends BaseMvpView{
    void addCommentToList(Comment comment);
    void removeAllData();
    void refreshComments();
    void startCommentPostLoading();
    void stopCommentPostLoading();
}
