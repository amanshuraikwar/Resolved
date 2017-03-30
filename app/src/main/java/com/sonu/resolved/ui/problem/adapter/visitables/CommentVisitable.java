package com.sonu.resolved.ui.problem.adapter.visitables;

import com.sonu.resolved.data.network.model.Comment;
import com.sonu.resolved.ui.problem.adapter.TypeFactory;
import com.sonu.resolved.ui.problem.adapter.Visitable;

/**
 * Created by sonu on 20/3/17.
 */

public class CommentVisitable extends Comment implements Visitable {

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
