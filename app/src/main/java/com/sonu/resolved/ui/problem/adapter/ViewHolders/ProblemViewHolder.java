package com.sonu.resolved.ui.problem.adapter.ViewHolders;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.sonu.resolved.R;
import com.sonu.resolved.ui.problem.adapter.visitables.ProblemVisitable;

import butterknife.BindView;

/**
 * Created by sonu on 20/3/17.
 */

public class ProblemViewHolder extends AbstractViewHolder<ProblemVisitable> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_problem;

    @BindView(R.id.problemTitleTv)
    TextView problemTitleTv;

    @BindView(R.id.problemDescriptionTv)
    TextView problemDescriptionTv;

    public ProblemViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(ProblemVisitable element) {
        problemTitleTv.setText(element.getTitle());
        problemDescriptionTv.setText(element.getDescription());
    }
}
