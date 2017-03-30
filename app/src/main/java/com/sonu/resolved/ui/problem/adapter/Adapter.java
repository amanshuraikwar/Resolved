package com.sonu.resolved.ui.problem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sonu.resolved.ui.problem.adapter.ViewHolders.AbstractViewHolder;
import com.sonu.resolved.ui.problem.adapter.visitables.CommentVisitable;
import com.sonu.resolved.ui.problem.adapter.visitables.ProblemVisitable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonu on 20/3/17.
 */

public class Adapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> elements;
    private final TypeFactory typeFactory;

    public Adapter(List<Visitable> elements, TypeFactory typeFactory) {
        this.elements = elements;
        this.typeFactory = typeFactory;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View contactView = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(contactView, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(elements.get(position));
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    @Override
    public int getItemViewType(int position) {
        return elements.get(position).type(typeFactory);
    }

    public void addComment(CommentVisitable commentVisitable) {
        elements.add(commentVisitable);
        this.notifyDataSetChanged();
    }

    public void removeAll() {
        Visitable visitable = elements.get(0);
        Visitable visitable1 = elements.get(1);
        elements = new ArrayList<>();
        elements.add(visitable);
        elements.add(visitable1);
        this.notifyDataSetChanged();
    }
}

