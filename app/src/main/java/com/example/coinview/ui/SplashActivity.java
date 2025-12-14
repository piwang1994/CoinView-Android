/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.coinview.MainActivity;
import com.example.coinview.R;
import com.example.coinview.viewmodel.UserViewModel;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Delay and check authentication status
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkAuthenticationStatus();
        }, SPLASH_DELAY);
    }

    /**
     * Check if user is logged in and navigate accordingly
     */
    private void checkAuthenticationStatus() {
        if (userViewModel.isUserLoggedIn()) {
            // User is logged in, go to MainActivity
            navigateToMain();
        } else {
            // User is not logged in, go to LoginActivity
            navigateToLogin();
        }
    }

    /**
     * Navigate to MainActivity
     */
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Navigate to LoginActivity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
