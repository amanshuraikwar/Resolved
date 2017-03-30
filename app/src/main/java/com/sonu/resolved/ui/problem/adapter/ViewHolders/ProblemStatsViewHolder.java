package com.sonu.resolved.ui.problem.adapter.ViewHolders;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.sonu.resolved.R;
import com.sonu.resolved.ui.problem.adapter.visitables.ProblemStatsVisitable;

/**
 * Created by sonu on 24/3/17.
 */

public class ProblemStatsViewHolder extends AbstractViewHolder<ProblemStatsVisitable> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_problem_stats;

    public ProblemStatsViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(ProblemStatsVisitable element) {

    }
}
