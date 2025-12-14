/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 *
 * Temporary User Repository without Firebase
 */
package com.example.coinview.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private static final String PREFS_NAME = "CoinViewPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";

    private final SharedPreferences sharedPreferences;

    private final MutableLiveData<String> userLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<Boolean> loadingLiveData;

    public UserRepository(Application application) {
        sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            String email = sharedPreferences.getString(KEY_USER_EMAIL, null);
            userLiveData.postValue(email);
        }
    }

    /**
     * Login with email and password (Mock implementation)
     */
    public void login(String email, String password) {
        loadingLiveData.postValue(true);

        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            loadingLiveData.postValue(false);

            // Simple validation
            if (email.isEmpty() || password.isEmpty()) {
                errorLiveData.postValue("Email and password are required");
                return;
            }

            if (password.length() < 6) {
                errorLiveData.postValue("Password must be at least 6 characters");
                return;
            }

            // Save login state
            sharedPreferences.edit()
                    .putBoolean(KEY_IS_LOGGED_IN, true)
                    .putString(KEY_USER_EMAIL, email)
                    .apply();

            userLiveData.postValue(email);
            Log.d(TAG, "Login successful: " + email);
        }, 1000);
    }

    /**
     * Register new user (Mock implementation)
     */
    public void register(String email, String password, String username) {
        loadingLiveData.postValue(true);

        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            loadingLiveData.postValue(false);

            // Save login state
            sharedPreferences.edit()
                    .putBoolean(KEY_IS_LOGGED_IN, true)
                    .putString(KEY_USER_EMAIL, email)
                    .apply();

            userLiveData.postValue(email);
            Log.d(TAG, "Registration successful: " + email);
        }, 1000);
    }

    /**
     * Logout current user
     */
    public void logout() {
        sharedPreferences.edit()
                .putBoolean(KEY_IS_LOGGED_IN, false)
                .remove(KEY_USER_EMAIL)
                .apply();

        userLiveData.postValue(null);
        Log.d(TAG, "User logged out");
    }

    /**
     * Send password reset email (Mock implementation)
     */
    public void resetPassword(String email) {
        loadingLiveData.postValue(true);

        new android.os.Handler().postDelayed(() -> {
            loadingLiveData.postValue(false);
            Log.d(TAG, "Password reset email sent to: " + email);
        }, 1000);
    }

    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Get current user email
     */
    public String getCurrentUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    // LiveData getters
    public MutableLiveData<String> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
