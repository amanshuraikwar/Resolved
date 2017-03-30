package com.sonu.resolved.ui.problem.adapter.ViewHolders;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.sonu.resolved.R;
import com.sonu.resolved.ui.problem.adapter.visitables.CommentVisitable;

import butterknife.BindView;

/**
 * Created by sonu on 20/3/17.
 */

public class CommentViewHolder extends AbstractViewHolder<CommentVisitable> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_comment;

    @BindView(R.id.usernameTv)
    TextView usernameTv;

    @BindView(R.id.commentTextTv)
    TextView commentTextTv;

    @BindView(R.id.upvotesTv)
    TextView upvotesTv;

    @BindView(R.id.downvotesTv)
    TextView downvotesTv;

    public CommentViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(CommentVisitable element) {
        usernameTv.setText(element.getUsername()+":");
        commentTextTv.setText(element.getComment_text());
        upvotesTv.setText(element.getUpvotes()+"");
        downvotesTv.setText(element.getDownvotes()+"");
    }
}
