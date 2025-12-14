/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.coinview.databinding.ActivityMainBinding;
import com.example.coinview.viewmodel.CryptocurrencyViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main Activity
 * Container for navigation fragments with Bottom Navigation
 * Contains three main fragments: Home, Favorites, and Settings
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int NOTIFICATION_PERMISSION_CODE = 100;

    private ActivityMainBinding binding;
    private NavController navController;
    private CryptocurrencyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate started");

        try {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "super.onCreate completed");

            // Initialize View Binding
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            Log.d(TAG, "setContentView completed");

            // ✅ 请求通知权限 (Android 13+)
            requestNotificationPermission();

            // Initialize ViewModel
            initializeViewModel();
            Log.d(TAG, "ViewModel initialized");

            // Setup window insets for edge-to-edge display
            setupWindowInsets();
            Log.d(TAG, "Window insets setup completed");

            // Setup navigation components
            setupNavigation();
            Log.d(TAG, "Navigation setup completed");

            // Fetch initial data
            fetchInitialData();
            Log.d(TAG, "Initial data fetch started");

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error initializing app: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * ✅ 请求通知权限 (仅 Android 13+)
     */
    private void requestNotificationPermission() {
        // 只在 Android 13 (API 33) 及以上版本请求通知权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "Requesting POST_NOTIFICATIONS permission");

                // 检查是否应该显示权限说明
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.POST_NOTIFICATIONS)) {
                    // 可以在这里显示一个对话框解释为什么需要这个权限
                    Toast.makeText(this,
                            "通知权限用于接收重要的价格提醒",
                            Toast.LENGTH_LONG).show();
                }

                // 请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            } else {
                Log.d(TAG, "POST_NOTIFICATIONS permission already granted");
            }
        } else {
            Log.d(TAG, "Android version < 13, no need to request POST_NOTIFICATIONS");
        }
    }

    /**
     * ✅ 处理权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "POST_NOTIFICATIONS permission granted");
                Toast.makeText(this, "通知权限已授予", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "POST_NOTIFICATIONS permission denied");
                Toast.makeText(this,
                        "通知权限被拒绝,您将无法收到价格提醒",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Initialize ViewModel
     */
    private void initializeViewModel() {
        try {
            viewModel = new ViewModelProvider(this).get(CryptocurrencyViewModel.class);
            Log.d(TAG, "CryptocurrencyViewModel created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating ViewModel", e);
            Toast.makeText(this, "Error creating ViewModel", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Setup window insets for edge-to-edge display
     */
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Setup navigation components
     */
    private void setupNavigation() {
        try {
            // Method 1: Using NavHostFragment (more reliable)
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);

            if (navHostFragment == null) {
                Log.e(TAG, "NavHostFragment is null!");
                Toast.makeText(this, "Navigation setup failed", Toast.LENGTH_SHORT).show();
                return;
            }

            navController = navHostFragment.getNavController();
            Log.d(TAG, "NavController obtained from NavHostFragment");

            // Setup BottomNavigationView with NavController
            BottomNavigationView bottomNav = binding.bottomNavigation;
            if (bottomNav == null) {
                Log.e(TAG, "BottomNavigationView is null!");
                return;
            }

            NavigationUI.setupWithNavController(bottomNav, navController);
            Log.d(TAG, "BottomNavigationView setup completed");

        } catch (Exception e) {
            Log.e(TAG, "Error in setupNavigation", e);
            Toast.makeText(this, "Navigation error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fetch initial data from API
     */
    private void fetchInitialData() {
        if (viewModel == null) {
            Log.e(TAG, "ViewModel is null, cannot fetch data");
            return;
        }

        try {
            // Fetch market data with default parameters
            // Parameters: currency, perPage, page
            viewModel.fetchMarketData("usd", 100, 1);
            Log.d(TAG, "Market data fetch initiated");

            // Fetch trending coins
            viewModel.fetchTrendingCoins();
            Log.d(TAG, "Trending coins fetch initiated");

        } catch (Exception e) {
            Log.e(TAG, "Error fetching initial data", e);
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get ViewModel instance
     * Allows fragments to access the shared ViewModel
     *
     * @return CryptocurrencyViewModel instance
     */
    public CryptocurrencyViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");

        // Refresh data when activity resumes
        if (viewModel != null) {
            try {
                viewModel.refreshData();
                Log.d(TAG, "Data refresh initiated");
            } catch (Exception e) {
                Log.e(TAG, "Error refreshing data", e);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (navController != null) {
            return navController.navigateUp() || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");

        // Clean up binding to prevent memory leaks
        binding = null;
        viewModel = null;
    }
}
