/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coinview.adapter.CryptocurrencyAdapter;
import com.example.coinview.databinding.FragmentFavoritesBinding;
import com.example.coinview.model.Cryptocurrency;
import com.example.coinview.viewmodel.CryptocurrencyViewModel;

/**
 * Favorites Fragment - Display favorite cryptocurrencies
 */
public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";

    private FragmentFavoritesBinding binding;
    private CryptocurrencyViewModel viewModel;
    private CryptocurrencyAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(CryptocurrencyViewModel.class);

        // Setup UI components
        setupRecyclerView();
        observeViewModel();
    }

    /**
     * Setup RecyclerView with adapter
     */
    private void setupRecyclerView() {
        adapter = new CryptocurrencyAdapter();
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(adapter);

        // Set item click listeners
        adapter.setOnItemClickListener(new CryptocurrencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cryptocurrency coin) {
                showCoinDetails(coin);
            }

            @Override
            public void onFavoriteClick(Cryptocurrency coin) {
                removeFavorite(coin);
            }
        });
    }

    /**
     * Observe ViewModel data
     * 使用你的 ViewModel 中的方法名
     */
    private void observeViewModel() {
        // 使用 favoriteCryptocurrencies
        viewModel.getFavoriteCryptocurrencies().observe(getViewLifecycleOwner(), favorites -> {
            if (favorites != null) {
                if (favorites.isEmpty()) {
                    showEmptyState();
                } else {
                    hideEmptyState();
                    adapter.submitList(favorites);
                }
        }
        });
    }

    /**
     * Remove coin from favorites
     */
    private void removeFavorite(Cryptocurrency coin) {
        coin.setFavorite(false);
        viewModel.update(coin);

        Toast.makeText(requireContext(),
                "Removed from favorites",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Show coin details (placeholder for now)
     */
    private void showCoinDetails(Cryptocurrency coin) {
        Toast.makeText(requireContext(),
                "Clicked: " + coin.getName(),
                Toast.LENGTH_SHORT).show();

        // TODO: Navigate to CoinDetailFragment
    }

    /**
     * Show empty state when no favorites
     */
    private void showEmptyState() {
        binding.rvFavorites.setVisibility(View.GONE);
        binding.tvEmpty.setVisibility(View.VISIBLE);
    }

    /**
     * Hide empty state when favorites exist
     */
    private void hideEmptyState() {
        binding.rvFavorites.setVisibility(View.VISIBLE);
        binding.tvEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
