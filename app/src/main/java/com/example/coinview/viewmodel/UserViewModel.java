/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.coinview.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final LiveData<String> user;  // Changed from FirebaseUser to String
    private final LiveData<String> error;
    private final LiveData<Boolean> loading;

    // Validation errors
    private final MutableLiveData<String> emailError;
    private final MutableLiveData<String> passwordError;
    private final MutableLiveData<String> usernameError;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        user = repository.getUserLiveData();
        error = repository.getErrorLiveData();
        loading = repository.getLoadingLiveData();

        emailError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        usernameError = new MutableLiveData<>();
    }

    /**
     * Login with email and password
     */
    public void login(String email, String password) {
        if (validateLogin(email, password)) {
            repository.login(email, password);
        }
    }

    /**
     * Register new user
     */
    public void register(String email, String password, String username) {
        if (validateRegistration(email, password, username)) {
            repository.register(email, password, username);
        }
    }

    /**
     * Logout current user
     */
    public void logout() {
        repository.logout();
    }

    /**
     * Send password reset email
     */
    public void resetPassword(String email) {
        if (validateEmail(email)) {
            repository.resetPassword(email);
        }
    }

    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return repository.isUserLoggedIn();
    }

    /**
     * Get current user email
     */
    public String getCurrentUserEmail() {
        return repository.getCurrentUserEmail();
    }

    // ==================== Validation Methods ====================

    private boolean validateLogin(String email, String password) {
        boolean isValid = true;

        if (!validateEmail(email)) {
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordError.setValue("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordError.setValue("Password must be at least 6 characters");
            isValid = false;
        } else {
            passwordError.setValue(null);
        }

        return isValid;
    }

    private boolean validateRegistration(String email, String password, String username) {
        boolean isValid = true;

        if (TextUtils.isEmpty(username)) {
            usernameError.setValue("Username is required");
            isValid = false;
        } else if (username.length() < 3) {
            usernameError.setValue("Username must be at least 3 characters");
            isValid = false;
        } else {
            usernameError.setValue(null);
        }

        if (!validateEmail(email)) {
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordError.setValue("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordError.setValue("Password must be at least 6 characters");
            isValid = false;
        } else {
            passwordError.setValue(null);
        }

        return isValid;
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            emailError.setValue("Email is required");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setValue("Invalid email format");
            return false;
        } else {
            emailError.setValue(null);
            return true;
        }
    }

    // ==================== LiveData Getters ====================

    public LiveData<String> getUser() {
        return user;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<String> getUsernameError() {
        return usernameError;
    }
}
