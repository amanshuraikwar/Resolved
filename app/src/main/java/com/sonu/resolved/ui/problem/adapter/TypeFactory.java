package com.sonu.resolved.ui.problem.adapter;

import android.view.View;

import com.sonu.resolved.data.network.model.Comment;
import com.sonu.resolved.data.network.model.Problem;
import com.sonu.resolved.ui.problem.adapter.ViewHolders.AbstractViewHolder;
import com.sonu.resolved.ui.problem.adapter.visitables.ProblemStatsVisitable;

/**
 * Created by sonu on 20/3/17.
 */

public interface TypeFactory {
    int type(Comment comment);

    int type(Problem problem);

    int type(ProblemStatsVisitable problemStatsVisitable);

    AbstractViewHolder createViewHolder(View parent, int type);
}
