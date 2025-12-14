/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coinview.databinding.FragmentSettingsBinding;

/**
 * Settings Fragment - App settings and user preferences
 */
public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupClickListeners();
    }

    /**
     * Setup click listeners for settings items
     */
    private void setupClickListeners() {
        // Currency settings
        binding.layoutCurrency.setOnClickListener(v -> {
            Toast.makeText(requireContext(),
                    "Currency settings coming soon",
                    Toast.LENGTH_SHORT).show();
        });

        // Notifications
        binding.layoutNotifications.setOnClickListener(v -> {
            Toast.makeText(requireContext(),
                    "Notification settings coming soon",
                    Toast.LENGTH_SHORT).show();
        });

        // Theme
        binding.layoutTheme.setOnClickListener(v -> {
            Toast.makeText(requireContext(),
                    "Theme settings coming soon",
                    Toast.LENGTH_SHORT).show();
        });

        // About
        binding.layoutAbout.setOnClickListener(v -> {
            showAboutDialog();
        });

        // Logout
        binding.btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    /**
     * Show about dialog
     */
    private void showAboutDialog() {
        Toast.makeText(requireContext(),
                "CoinView v1.0\nAuthor: WANGQINGBIN\nITMD 555",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Logout user
     */
    private void logout() {
        // Clear user session
        // Navigate to login screen
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
