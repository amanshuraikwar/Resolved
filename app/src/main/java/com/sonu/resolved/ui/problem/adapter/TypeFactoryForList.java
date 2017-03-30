package com.sonu.resolved.ui.problem.adapter;

import android.view.View;

import com.sonu.resolved.data.network.model.Comment;
import com.sonu.resolved.data.network.model.Problem;
import com.sonu.resolved.ui.problem.adapter.ViewHolders.AbstractViewHolder;
import com.sonu.resolved.ui.problem.adapter.ViewHolders.CommentViewHolder;
import com.sonu.resolved.ui.problem.adapter.ViewHolders.ProblemStatsViewHolder;
import com.sonu.resolved.ui.problem.adapter.ViewHolders.ProblemViewHolder;
import com.sonu.resolved.ui.problem.adapter.visitables.ProblemStatsVisitable;

/**
 * Created by sonu on 20/3/17.
 */

public class TypeFactoryForList implements TypeFactory {
    @Override
    public int type(Comment comment) {
        return CommentViewHolder.LAYOUT;
    }

    @Override
    public int type(Problem problem) {
        return ProblemViewHolder.LAYOUT;
    }

    @Override
    public int type(ProblemStatsVisitable problemStatsVisitable) {
        return ProblemStatsViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder abstractViewHolder = null;

        switch (type) {
            case CommentViewHolder.LAYOUT:
                abstractViewHolder = new CommentViewHolder(parent);
                break;
            case ProblemViewHolder.LAYOUT:
                abstractViewHolder = new ProblemViewHolder(parent);
                break;
            case ProblemStatsViewHolder.LAYOUT:
                abstractViewHolder = new ProblemStatsViewHolder(parent);
                break;
        }

        return abstractViewHolder;
    }
}
