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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coinview.MainActivity;
import com.example.coinview.R;
import com.example.coinview.auth.FirebaseAuthManager;
import com.example.coinview.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

/**
 * Login Activity with Firebase Authentication
 * Supports Email/Password and Google Sign-In
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding;
    private FirebaseAuthManager authManager;

    // Google Sign-In launcher
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "🔵 onCreate started");

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        try {
            // Initialize View Binding
            binding = ActivityLoginBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            Log.d(TAG, "✅ View binding initialized");

            // Initialize Firebase Auth Manager
            authManager = FirebaseAuthManager.getInstance();

            // ✅ 使用正确的 Web Client ID
            String webClientId = "67445333884-l088ruij1t2m11fd3qfdsat73c0q7d7o.apps.googleusercontent.com";
            Log.d(TAG, "🔑 Using Web Client ID: " + webClientId);

            authManager.initializeGoogleSignIn(this, webClientId);
            Log.d(TAG, "✅ Firebase Auth Manager initialized");

            // Check if user is already logged in
            if (authManager.isUserLoggedIn()) {
                Log.d(TAG, "👤 User already logged in");
                navigateToMain();
                return;
            }

            // Initialize Google Sign-In launcher
            initializeGoogleSignInLauncher();
            Log.d(TAG, "✅ Google Sign-In launcher initialized");

            // Set up click listeners
            setupClickListeners();
            Log.d(TAG, "✅ Click listeners setup completed");

        } catch (Exception e) {
            Log.e(TAG, "❌ Error in onCreate", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initialize Google Sign-In launcher
     */
    private void initializeGoogleSignInLauncher() {
        Log.d(TAG, "🔵 Initializing Google Sign-In launcher");

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "🔵 ActivityResult received");
                    Log.d(TAG, "Result Code: " + result.getResultCode());
                    Log.d(TAG, "RESULT_OK: " + RESULT_OK);
                    Log.d(TAG, "RESULT_CANCELED: " + RESULT_CANCELED);

                    if (result.getResultCode() == RESULT_OK) {
                        Log.d(TAG, "✅ Result OK - processing sign-in");
                        Intent data = result.getData();

                        if (data == null) {
                            Log.e(TAG, "❌ Intent data is NULL");
                            showLoading(false);
                            Toast.makeText(this, "Error: No data received from Google Sign-In",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Log.d(TAG, "✅ Intent data received, getting account");
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleGoogleSignInResult(task);

                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Log.d(TAG, "⚠️ Google Sign-In cancelled by user");
                        showLoading(false);
                        Toast.makeText(this, "Sign-in cancelled", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e(TAG, "❌ Unexpected result code: " + result.getResultCode());
                        showLoading(false);
                        Toast.makeText(this, "Sign-in failed with code: " + result.getResultCode(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        );

        Log.d(TAG, "✅ Google Sign-In launcher registered");
    }

    /**
     * Set up all click listeners
     */
    private void setupClickListeners() {
        // Login button
        binding.btnLogin.setOnClickListener(v -> handleEmailLogin());

        // Sign up link
        binding.tvSignupLink.setOnClickListener(v -> navigateToSignUp());

        // Forgot password link
        binding.tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());

        // Guest mode button
        binding.btnGuestMode.setOnClickListener(v -> continueAsGuest());

        // Google Sign-In button
        binding.btnGoogleSignIn.setOnClickListener(v -> handleGoogleSignIn());
    }

    /**
     * Handle email/password login
     */
    private void handleEmailLogin() {
        Log.d(TAG, "🔵 handleEmailLogin called");

        String email = getTextFromEditText(binding.etUsername);
        String password = getTextFromEditText(binding.etPassword);

        // Validate input
        if (!validateInput(email, password)) {
            return;
        }

        // Show loading
        showLoading(true);

        // Sign in with Firebase
        authManager.signInWithEmail(email, password, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                showLoading(false);
                Log.d(TAG, "✅ Email login successful: " + user.getEmail());
                Toast.makeText(LoginActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            }

            @Override
            public void onFailure(Exception exception) {
                showLoading(false);
                Log.e(TAG, "❌ Email login failed", exception);
                String errorMessage = getErrorMessage(exception);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Handle Google Sign-In
     */
    private void handleGoogleSignIn() {
        Log.d(TAG, "🔵 handleGoogleSignIn called");

        if (authManager.getGoogleSignInClient() == null) {
            Log.e(TAG, "❌ GoogleSignInClient is NULL!");
            Toast.makeText(this, "Google Sign-In not initialized", Toast.LENGTH_LONG).show();
            return;
        }

        showLoading(true);
        Log.d(TAG, "✅ Loading shown, getting sign-in intent");

        try {
            Intent signInIntent = authManager.getGoogleSignInClient().getSignInIntent();
            Log.d(TAG, "✅ Sign-in intent created, launching activity");
            googleSignInLauncher.launch(signInIntent);
            Log.d(TAG, "✅ Activity launched");

        } catch (Exception e) {
            Log.e(TAG, "❌ Error launching Google Sign-In", e);
            showLoading(false);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Handle Google Sign-In result
     */
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "🔵 handleGoogleSignInResult called");

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "✅ Google Sign-In successful");
            Log.d(TAG, "📧 Email: " + account.getEmail());
            Log.d(TAG, "👤 Display Name: " + account.getDisplayName());
            Log.d(TAG, "🔑 ID Token: " + (account.getIdToken() != null ? "Present (" + account.getIdToken().length() + " chars)" : "NULL"));

            if (account.getIdToken() == null) {
                Log.e(TAG, "❌ ID Token is NULL - Web Client ID may be incorrect");
                Toast.makeText(this, "Configuration error: No ID token received\nPlease check Web Client ID",
                        Toast.LENGTH_LONG).show();
                showLoading(false);
                return;
            }

            Log.d(TAG, "🔵 Signing in to Firebase with Google credential");

            // Sign in to Firebase with Google account
            authManager.signInWithGoogle(account, new FirebaseAuthManager.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    showLoading(false);
                    Log.d(TAG, "✅ Firebase authentication successful");
                    Log.d(TAG, "👤 User: " + user.getDisplayName() + " (" + user.getEmail() + ")");
                    Toast.makeText(LoginActivity.this, "Welcome " + user.getDisplayName() + "!",
                            Toast.LENGTH_SHORT).show();
                    navigateToMain();
                }

                @Override
                public void onFailure(Exception exception) {
                    showLoading(false);
                    Log.e(TAG, "❌ Firebase authentication failed", exception);
                    Toast.makeText(LoginActivity.this,
                            "Authentication failed: " + exception.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (ApiException e) {
            showLoading(false);

            // ✅ 详细的错误信息
            int statusCode = e.getStatusCode();
            Log.e(TAG, "❌ Google Sign-In failed with ApiException");
            Log.e(TAG, "📊 Status Code: " + statusCode);
            Log.e(TAG, "📝 Status Message: " + e.getStatusMessage());
            Log.e(TAG, "🔍 Local Message: " + e.getLocalizedMessage());
            e.printStackTrace();

            String errorMessage;
            switch (statusCode) {
                case 10:
                    errorMessage = "Developer Error (10): SHA-1 fingerprint mismatch or not configured in Firebase/Google Cloud Console";
                    Log.e(TAG, "💡 Solution: Check SHA-1 in Firebase Console and Google Cloud Console");
                    break;
                case 12500:
                    errorMessage = "Sign-In Error (12500): Google Play Services outdated or not available";
                    Log.e(TAG, "💡 Solution: Update Google Play Services on device");
                    break;
                case 12501:
                    errorMessage = "Sign-In Cancelled (12501): User cancelled the sign-in flow";
                    break;
                case 12502:
                    errorMessage = "Sign-In Error (12502): Network error occurred";
                    Log.e(TAG, "💡 Solution: Check internet connection");
                    break;
                case 7:
                    errorMessage = "Network Error (7): No internet connection";
                    break;
                default:
                    errorMessage = "Sign-In Error (" + statusCode + "): " + e.getStatusMessage();
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Navigate to Sign Up activity
     */
    private void navigateToSignUp() {
        Log.d(TAG, "🔵 navigateToSignUp called");
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    /**
     * Show forgot password dialog
     */
    private void showForgotPasswordDialog() {
        Log.d(TAG, "🔵 showForgotPasswordDialog called");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        TextInputEditText etEmail = dialogView.findViewById(R.id.et_email);

        new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Enter your email address to receive password reset instructions.")
                .setView(dialogView)
                .setPositiveButton("Send", (dialog, which) -> {
                    String email = getTextFromEditText(etEmail);
                    if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sendPasswordResetEmail(email);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Send password reset email
     */
    private void sendPasswordResetEmail(String email) {
        showLoading(true);

        authManager.sendPasswordResetEmail(email, new FirebaseAuthManager.ResetPasswordCallback() {
            @Override
            public void onSuccess() {
                showLoading(false);
                Toast.makeText(LoginActivity.this,
                        "Password reset email sent to " + email,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception exception) {
                showLoading(false);
                Toast.makeText(LoginActivity.this,
                        "Failed to send reset email: " + exception.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Continue as guest (skip login)
     */
    private void continueAsGuest() {
        Log.d(TAG, "🔵 continueAsGuest called");
        Toast.makeText(this, "Continuing as guest", Toast.LENGTH_SHORT).show();
        navigateToMain();
    }

    /**
     * Navigate to MainActivity
     */
    private void navigateToMain() {
        Log.d(TAG, "🔵 navigateToMain called");

        try {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            Log.d(TAG, "✅ Starting MainActivity");
            startActivity(intent);

            Log.d(TAG, "✅ Finishing LoginActivity");
            finish();

        } catch (Exception e) {
            Log.e(TAG, "❌ Error navigating to MainActivity", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Validate email and password input
     */
    private boolean validateInput(String email, String password) {
        // Validate email
        if (TextUtils.isEmpty(email)) {
            binding.tilUsername.setError("Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilUsername.setError("Please enter a valid email");
            return false;
        }

        binding.tilUsername.setError(null);

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

        return true;
    }

    /**
     * Get error message from exception
     */
    private String getErrorMessage(Exception exception) {
        String message = exception.getMessage();

        if (message != null) {
            if (message.contains("no user record")) {
                return "No account found with this email";
            } else if (message.contains("password is invalid")) {
                return "Incorrect password";
            } else if (message.contains("network error")) {
                return "Network error. Please check your connection";
            }
        }

        return "Login failed: " + message;
    }

    /**
     * Show/hide loading state
     */
    private void showLoading(boolean isLoading) {
        Log.d(TAG, "🔵 showLoading: " + isLoading);
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!isLoading);
        binding.btnGoogleSignIn.setEnabled(!isLoading);
        binding.btnGuestMode.setEnabled(!isLoading);
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
        Log.d(TAG, "🔵 onDestroy called");
        binding = null;
    }
}
