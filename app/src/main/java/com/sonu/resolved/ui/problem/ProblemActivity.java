package com.sonu.resolved.ui.problem;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sonu.resolved.MyApplication;
import com.sonu.resolved.R;
import com.sonu.resolved.data.network.model.Comment;
import com.sonu.resolved.data.network.model.Problem;
import com.sonu.resolved.di.ActivityContext;
import com.sonu.resolved.di.component.DaggerActivityComponent;
import com.sonu.resolved.di.module.ActivityModule;
import com.sonu.resolved.ui.problem.adapter.Adapter;
import com.sonu.resolved.ui.problem.adapter.TypeFactory;
import com.sonu.resolved.ui.problem.adapter.TypeFactoryForList;
import com.sonu.resolved.ui.problem.adapter.Visitable;
import com.sonu.resolved.ui.problem.adapter.visitables.CommentVisitable;
import com.sonu.resolved.ui.problem.adapter.visitables.ProblemStatsVisitable;
import com.sonu.resolved.ui.problem.adapter.visitables.ProblemVisitable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sonu on 18/3/17.
 */

public class ProblemActivity extends AppCompatActivity implements ProblemMvpView{

    private static final String TAG = ProblemActivity.class.getSimpleName();
    private int pid;

    @Inject
    ProblemMvpPresenter mPresenter;

    @Inject
    @ActivityContext
    Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.problemTitleTv)
    TextView problemTitleTv;

    @BindView(R.id.problemDescriptionTv)
    TextView problemDescriptionTv;

    @BindView(R.id.myCommentEt)
    EditText myCommentEt;

    @BindView(R.id.problemDataRv)
    RecyclerView problemDataRv;

    @BindView(R.id.postCommentBtn)
    Button postCommentButton;

    @BindView(R.id.postCommentPb)
    ProgressBar postCommentPb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((MyApplication)getApplicationContext()).getApplicationComponent())
                .build()
                .inject(this);

        Log.i(TAG, "mPresenter="+mPresenter);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        pid = bundle.getInt("pid");

        problemTitleTv.setText(bundle.getString("title"));
        problemDescriptionTv.setText(bundle.getString("description"));

        myCommentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addComment(pid+"", myCommentEt.getText().toString().trim());
            }
        });

        TypeFactory typeFactory = new TypeFactoryForList();
        ProblemVisitable problemVisitable = new ProblemVisitable();
        problemVisitable.setTitle(bundle.getString("title"));
        problemVisitable.setDescription(bundle.getString("description"));

        problemDataRv.setLayoutManager(new LinearLayoutManager(ProblemActivity.this));
        problemDataRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ArrayList<Visitable> arrayList = new ArrayList<>();
        arrayList.add(problemVisitable);
        arrayList.add(new ProblemStatsVisitable());

        problemDataRv.setAdapter(new Adapter(
                arrayList,
                typeFactory
        ));

        mPresenter.getComments(pid);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onAttach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onDetach();
    }

    @Override
    public void addCommentToList(Comment comment) {
        CommentVisitable commentVisitable = new CommentVisitable();
        commentVisitable.setCid(comment.getCid());
        commentVisitable.setComment_text(comment.getComment_text());
        commentVisitable.setDate(comment.getDate());
        commentVisitable.setTime(comment.getTime());
        commentVisitable.setPid(comment.getPid());
        commentVisitable.setDownvotes(comment.getDownvotes());
        commentVisitable.setUpvotes(comment.getUpvotes());
        commentVisitable.setUsername(comment.getUsername());

        ((Adapter)problemDataRv.getAdapter()).addComment(commentVisitable);
    }

    @Override
    public void removeAllData() {
        ((Adapter)problemDataRv.getAdapter()).removeAll();
    }

    @Override
    public void refreshComments() {
        myCommentEt.setText("");
        mPresenter.getComments(pid);
    }

    @Override
    public void startCommentPostLoading() {
        postCommentButton.setVisibility(View.GONE);
        postCommentPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopCommentPostLoading() {
        postCommentButton.setVisibility(View.VISIBLE);
        postCommentPb.setVisibility(View.GONE);
    }
}
