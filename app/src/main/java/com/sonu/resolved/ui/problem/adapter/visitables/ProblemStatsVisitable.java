package com.sonu.resolved.ui.problem.adapter.visitables;

import com.sonu.resolved.ui.problem.adapter.TypeFactory;
import com.sonu.resolved.ui.problem.adapter.Visitable;

/**
 * Created by sonu on 24/3/17.
 */

public class ProblemStatsVisitable implements Visitable {
    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
