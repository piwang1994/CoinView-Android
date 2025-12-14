/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coinview.MainActivity;
import com.example.coinview.R;
import com.example.coinview.auth.FirebaseAuthManager;
import com.example.coinview.databinding.ActivitySignUpBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

/**
 * Sign Up Activity
 * Handles user registration with email and password
 */
public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private ActivitySignUpBinding binding;
    private FirebaseAuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate started");

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        try {
            // Initialize View Binding
            binding = ActivitySignUpBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            Log.d(TAG, "View binding initialized");

            // Initialize Firebase Auth Manager
            authManager = FirebaseAuthManager.getInstance();

            // Set up click listeners
            setupClickListeners();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Set up all click listeners
     */
    private void setupClickListeners() {
        // Sign up button
        binding.btnSignUp.setOnClickListener(v -> handleSignUp());

        // Back to login link
        binding.tvLoginLink.setOnClickListener(v -> finish());
    }

    /**
     * Handle sign up
     */
    private void handleSignUp() {
        Log.d(TAG, "handleSignUp called");

        String email = getTextFromEditText(binding.etEmail);
        String password = getTextFromEditText(binding.etPassword);
        String confirmPassword = getTextFromEditText(binding.etConfirmPassword);

        // Validate input
        if (!validateInput(email, password, confirmPassword)) {
            return;
        }

        // Show loading
        showLoading(true);

        // Create account with Firebase
        authManager.createAccountWithEmail(email, password, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                showLoading(false);
                Log.d(TAG, "Sign up successful: " + user.getEmail());

                Toast.makeText(SignUpActivity.this,
                        "Account created! Please check your email for verification.",
                        Toast.LENGTH_LONG).show();

                navigateToMain();
            }

            @Override
            public void onFailure(Exception exception) {
                showLoading(false);
                Log.e(TAG, "Sign up failed", exception);

                String errorMessage = getErrorMessage(exception);
                Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Navigate to MainActivity
     */
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Validate input
     */
    private boolean validateInput(String email, String password, String confirmPassword) {
        // Validate email
        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Please enter a valid email");
            return false;
        }

        binding.tilEmail.setError(null);

        // Validate password
        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            binding.tilPassword.setError("Password must be at least 6 characters");
            return false;
        }

        binding.tilPassword.setError(null);

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            binding.tilConfirmPassword.setError("Please confirm your password");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            binding.tilConfirmPassword.setError("Passwords do not match");
            return false;
        }

        binding.tilConfirmPassword.setError(null);

        return true;
    }

    /**
     * Get error message from exception
     */
    private String getErrorMessage(Exception exception) {
        String message = exception.getMessage();

        if (message != null) {
            if (message.contains("email address is already in use")) {
                return "This email is already registered";
            } else if (message.contains("password is invalid")) {
                return "Password must be at least 6 characters";
            } else if (message.contains("network error")) {
                return "Network error. Please check your connection";
            }
        }

        return "Sign up failed: " + message;
    }

    /**
     * Show/hide loading state
     */
    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnSignUp.setEnabled(!isLoading);
    }

    /**
     * Get text from EditText safely
     */
    private String getTextFromEditText(TextInputEditText editText) {
        if (editText != null && editText.getText() != null) {
            return editText.getText().toString().trim();
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
