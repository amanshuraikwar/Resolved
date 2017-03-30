package com.sonu.resolved.ui.problem.adapter.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by meier on 29/12/2016.
 */

public abstract class AbstractViewHolder<T> extends RecyclerView.ViewHolder {
    AbstractViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public abstract void bind(T element);
}
