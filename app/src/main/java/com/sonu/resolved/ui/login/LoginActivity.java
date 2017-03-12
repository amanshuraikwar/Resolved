package com.sonu.resolved.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sonu.resolved.MyApplication;
import com.sonu.resolved.R;

import com.sonu.resolved.di.ActivityContext;
import com.sonu.resolved.di.component.DaggerActivityComponent;
import com.sonu.resolved.di.module.ActivityModule;
import com.sonu.resolved.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sonu on 3/3/17.
 */

public class LoginActivity extends AppCompatActivity implements LoginMvpView{

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    LoginMvpPresenter mPresenter;

    private Snackbar errorSnackBar;

    @Inject
    @ActivityContext
    Context mContext;

    @BindView(R.id.pageTitleTv)
    TextView pageTitleTv;

    @BindView(R.id.usernameEt)
    EditText usernameEt;

    @BindView(R.id.usernameTil)
    TextInputLayout usernameTil;

    @BindView(R.id.emailEt)
    EditText emailEt;

    @BindView(R.id.emailTil)
    TextInputLayout emailTil;

    @BindView(R.id.passwordEt)
    EditText passwordEt;

    @BindView(R.id.passwordTil)
    TextInputLayout passwordTil;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.buttonLayout)
    View buttonLayout;

    @BindView(R.id.layoutParentLl)
    LinearLayout layoutParentLl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((MyApplication)getApplicationContext()).getApplicationComponent())
                .build()
                .inject(this);

        ButterKnife.bind(this);

        Log.i(TAG, "mPresenter="+mPresenter);
        mPresenter.onAttach(this);

        usernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                usernameTil.setErrorEnabled(false);
            }
        });

        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emailTil.setErrorEnabled(false);
            }
        });

        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordTil.setErrorEnabled(false);
            }
        });

        errorSnackBar = Snackbar.make(layoutParentLl, "", Snackbar.LENGTH_SHORT);
    }

    @OnClick(R.id.loginBtn)
    public void onLoginClick(View v) {
        Log.d(TAG,"onLoginClick");
        pageTitleTv.setText("Login");

        setUserNameError(null);
        setEmailError(null);
        setPasswordError(null);

        mPresenter.loginClicked(
                usernameEt.getText().toString().trim(),
                passwordEt.getText().toString().trim());
    }

    @OnClick(R.id.signupBtn)
    public void onSignupClick(View v) {
        Log.d(TAG,"onSignupClick");
        pageTitleTv.setText("Signup");

        setUserNameError(null);
        setEmailError(null);
        setPasswordError(null);

        mPresenter.signupClicked(
                usernameEt.getText().toString().trim(),
                emailEt.getText().toString().trim(),
                passwordEt.getText().toString().trim());
    }

    @Override
    public void hideEmailView() {
        emailTil.setVisibility(View.GONE);
        emailEt.setText("");
    }

    @Override
    public void showEmailView() {
        emailTil.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUserNameError(String error) {
        if(error == null) {
            usernameTil.setErrorEnabled(false);
        } else {
            usernameTil.setError(error);
        }
    }

    @Override
    public void setEmailError(String error) {
        if(error == null) {
            emailTil.setErrorEnabled(false);
        } else {
            emailTil.setError(error);
        }
    }

    @Override
    public void setPasswordError(String error) {
        if(error == null) {
            passwordTil.setErrorEnabled(false);
        } else {
            passwordTil.setError(error);
        }
    }

    @Override
    public void showLoading() {
        buttonLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        usernameEt.setEnabled(false);
        emailEt.setEnabled(false);
        passwordEt.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        buttonLayout.setVisibility(View.VISIBLE);

        usernameEt.setEnabled(true);
        emailEt.setEnabled(true);
        passwordEt.setEnabled(true);
    }

    @Override
    public void showErrorSnackBar(String error) {
        errorSnackBar.setText(error);
        errorSnackBar.show();
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
