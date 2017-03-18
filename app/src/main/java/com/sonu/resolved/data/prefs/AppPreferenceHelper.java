package com.sonu.resolved.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.sonu.resolved.R;

/**
 * Created by sonu on 10/3/17.
 */

public class AppPreferenceHelper implements PreferenceHelper{

    private Context mContext;
    private SharedPreferences sharedPref;

    public AppPreferenceHelper(Context context) {
        this.mContext = context;
        sharedPref = this.mContext
                .getSharedPreferences(
                        this.mContext.getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE
                );
    }

    @Override
    public boolean isUserLoggedIn() {
        return getLoginStatus();
    }

    @Override
    public void logInUser(String username, String email) {
        setLoginStatus(true);
    }

    @Override
    public void logOutUser() {
        setLoginStatus(false);
    }

    @Override
    public String getSavedUsername(){
        return getSavedUsernameX();
    }

    @Override
    public String getSavedEmail() {
        return getSavedEmailX();
    }

    private void setLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(this.mContext.getString(R.string.saved_login_status), status);
        editor.apply();
    }

    private boolean getLoginStatus() {
        return sharedPref.getBoolean(this.mContext.getString(R.string.saved_login_status), false);
    }

    private void saveUsername(String username) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(this.mContext.getString(R.string.saved_username), username);
        editor.apply();
    }

    private String getSavedUsernameX() {
        return sharedPref.getString(this.mContext.getString(R.string.saved_username), null);
    }

    private void saveEmail(String email) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(this.mContext.getString(R.string.saved_email), email);
        editor.apply();
    }

    private String getSavedEmailX() {
        return sharedPref.getString(this.mContext.getString(R.string.saved_email), null);
    }
}
