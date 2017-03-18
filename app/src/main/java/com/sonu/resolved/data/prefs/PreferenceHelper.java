package com.sonu.resolved.data.prefs;

/**
 * Created by sonu on 10/3/17.
 */

public interface PreferenceHelper {
    boolean isUserLoggedIn();
    void logInUser(String username, String email);
    void logOutUser();
    String getSavedUsername();
    String getSavedEmail();

}
