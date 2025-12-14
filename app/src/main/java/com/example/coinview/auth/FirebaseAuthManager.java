/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.auth;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Firebase Authentication Manager
 * Handles email/password and Google Sign-In authentication
 */
public class FirebaseAuthManager {

    private static final String TAG = "FirebaseAuthManager";

    private final FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    // Singleton instance
    private static FirebaseAuthManager instance;

    private FirebaseAuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Get singleton instance
     */
    public static synchronized FirebaseAuthManager getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthManager();
        }
        return instance;
    }

    /**
     * Initialize Google Sign-In
     */
    public void initializeGoogleSignIn(Context context, String webClientId) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        Log.d(TAG, "Google Sign-In initialized");
    }

    /**
     * Get Google Sign-In client
     */
    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    /**
     * Get current user
     */
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    /**
     * Sign in with email and password
     */
    public void signInWithEmail(String email, String password, AuthCallback callback) {
        Log.d(TAG, "Signing in with email: " + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onSuccess(user);
                    } else {
                        Log.e(TAG, "signInWithEmail:failure", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    /**
     * Create account with email and password
     */
    public void createAccountWithEmail(String email, String password, AuthCallback callback) {
        Log.d(TAG, "Creating account with email: " + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Send verification email
                        if (user != null) {
                            sendEmailVerification(user);
                        }

                        callback.onSuccess(user);
                    } else {
                        Log.e(TAG, "createUserWithEmail:failure", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    /**
     * Sign in with Google
     */
    public void signInWithGoogle(GoogleSignInAccount account, AuthCallback callback) {
        Log.d(TAG, "signInWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onSuccess(user);
                    } else {
                        Log.e(TAG, "signInWithCredential:failure", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    /**
     * Send email verification
     */
    public void sendEmailVerification(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email verification sent");
                        } else {
                            Log.e(TAG, "Failed to send verification email", task.getException());
                        }
                    });
        }
    }

    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String email, ResetPasswordCallback callback) {
        Log.d(TAG, "Sending password reset email to: " + email);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Password reset email sent");
                        callback.onSuccess();
                    } else {
                        Log.e(TAG, "Failed to send password reset email", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    /**
     * Sign out
     */
    public void signOut(Context context) {
        Log.d(TAG, "Signing out");

        // Sign out from Firebase
        mAuth.signOut();

        // Sign out from Google
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut();
        }
    }

    /**
     * Delete account
     */
    public void deleteAccount(AuthCallback callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted");
                            callback.onSuccess(null);
                        } else {
                            Log.e(TAG, "Failed to delete account", task.getException());
                            callback.onFailure(task.getException());
                        }
                    });
        }
    }

    /**
     * Authentication callback interface
     */
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception exception);
    }

    /**
     * Password reset callback interface
     */
    public interface ResetPasswordCallback {
        void onSuccess();
        void onFailure(Exception exception);
    }
}
