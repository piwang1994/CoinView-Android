/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.coinview.databinding.FragmentHomeBinding;
import com.example.coinview.model.Cryptocurrency;
import com.example.coinview.viewmodel.CryptocurrencyViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Home Fragment - Display cryptocurrency market data
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    private CryptocurrencyViewModel viewModel;
    private CryptocurrencyAdapter adapter;

    private List<Cryptocurrency> allCoins = new ArrayList<>();
    private boolean isShowingTrending = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(CryptocurrencyViewModel.class);

        // Setup UI components
        setupRecyclerView();
        setupSearchBar();
        setupTabs();
        observeViewModel();

        // Load initial data
        loadMarketData();
    }

    /**
     * Setup RecyclerView with adapter
     */
    private void setupRecyclerView() {
        adapter = new CryptocurrencyAdapter();
        binding.rvCoins.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCoins.setAdapter(adapter);

        // Set item click listeners
        adapter.setOnItemClickListener(new CryptocurrencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cryptocurrency coin) {
                showCoinDetails(coin);
            }

            @Override
            public void onFavoriteClick(Cryptocurrency coin) {
                toggleFavorite(coin);
            }
        });
    }

    /**
     * Setup search bar with text change listener
     */
    private void setupSearchBar() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCoins(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    /**
     * Setup tabs for Market/Trending switch
     */
    private void setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isShowingTrending = tab.getPosition() == 1;

                if (isShowingTrending) {
                    loadTrendingData();
                } else {
                    loadMarketData();
                }

                // Clear search when switching tabs
                binding.etSearch.setText("");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Refresh data on reselect
                if (isShowingTrending) {
                    loadTrendingData();
                } else {
                    loadMarketData();
                }
            }
        });
    }

    /**
     * Observe ViewModel LiveData
     * 使用你的 ViewModel 中的方法名
     */
    private void observeViewModel() {
        // Observe market data from API (使用 cryptocurrenciesFromApi)
        viewModel.getCryptocurrenciesFromApi().observe(getViewLifecycleOwner(), cryptocurrencies -> {
            if (cryptocurrencies != null && !isShowingTrending) {
                allCoins = new ArrayList<>(cryptocurrencies);
                adapter.submitList(cryptocurrencies);
                hideLoading();
            }
        });

        // Observe trending data (使用 trendingResult)
        viewModel.getTrendingResult().observe(getViewLifecycleOwner(), trendingResult -> {
            if (trendingResult != null && isShowingTrending) {
                // Convert TrendingResult to Cryptocurrency list
                List<Cryptocurrency> trendingCoins = convertTrendingToCoins(trendingResult);
                allCoins = new ArrayList<>(trendingCoins);
                adapter.submitList(trendingCoins);
                hideLoading();
            }
        });

        // Observe loading state
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });

        // Observe error messages
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                showError(error);
            }
        });
    }

    /**
     * Load market data from API
     */
    private void loadMarketData() {
        showLoading();
        viewModel.fetchMarketData("usd", 100, 1);  // ✅ 正确
    }

    /**
     * Load trending data from API
     */
    private void loadTrendingData() {
        showLoading();
        viewModel.fetchTrendingCoins();
    }

    /**
     * Filter coins based on search query
     */
    private void filterCoins(String query) {
        if (query.isEmpty()) {
            adapter.submitList(allCoins);
            return;
        }

        String lowerQuery = query.toLowerCase();
        List<Cryptocurrency> filtered = allCoins.stream()
                .filter(coin ->
                        coin.getName().toLowerCase().contains(lowerQuery) ||
                                coin.getSymbol().toLowerCase().contains(lowerQuery)
                )
                .collect(Collectors.toList());

        adapter.submitList(filtered);

        if (filtered.isEmpty()) {
            showError("No cryptocurrencies found");
        }
    }

    /**
     * Toggle favorite status of a coin
     */
    private void toggleFavorite(Cryptocurrency coin) {
        coin.setFavorite(!coin.isFavorite());
        viewModel.update(coin);

        String message = coin.isFavorite() ?
                "Added to favorites" : "Removed from favorites";
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show coin details (placeholder for now)
     */
    private void showCoinDetails(Cryptocurrency coin) {
        Toast.makeText(requireContext(),
                "Clicked: " + coin.getName(),
                Toast.LENGTH_SHORT).show();

        // TODO: Navigate to CoinDetailFragment
        // Bundle bundle = new Bundle();
        // bundle.putString("coinId", coin.getId());
        // NavHostFragment.findNavController(this)
        //         .navigate(R.id.action_nav_home_to_coinDetail, bundle);
    }

    /**
     * Convert TrendingResult to Cryptocurrency list
     */
    private List<Cryptocurrency> convertTrendingToCoins(com.example.coinview.model.TrendingResult trendingResult) {
        List<Cryptocurrency> coins = new ArrayList<>();

        try {
            if (trendingResult != null && trendingResult.getCoins() != null) {
                for (com.example.coinview.model.TrendingResult.TrendingCoinWrapper wrapper : trendingResult.getCoins()) {
                    if (wrapper != null && wrapper.getItem() != null) {
                        com.example.coinview.model.TrendingResult.TrendingCoin trendingCoin = wrapper.getItem();

                        // 创建 Cryptocurrency 对象
                        Cryptocurrency coin = new Cryptocurrency();
                        coin.setId(trendingCoin.getId());
                        coin.setSymbol(trendingCoin.getSymbol());
                        coin.setName(trendingCoin.getName());
                        coin.setImage(trendingCoin.getThumb());
                        coin.setMarketCapRank(trendingCoin.getMarketCapRank());

                        // 设置价格（BTC 价格）
                        coin.setCurrentPrice(trendingCoin.getPriceBtc());

                        // Trending 数据没有 24h 变化百分比
                        coin.setPriceChangePercentage24h(0.0);

                        coins.add(coin);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(),
                    "Error parsing trending data: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        return coins;
    }


    /**
     * Show loading indicator
     */
    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvCoins.setVisibility(View.GONE);
        binding.tvError.setVisibility(View.GONE);
    }

    /**
     * Hide loading indicator
     */
    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.rvCoins.setVisibility(View.VISIBLE);
        binding.tvError.setVisibility(View.GONE);
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        binding.progressBar.setVisibility(View.GONE);
        binding.rvCoins.setVisibility(View.GONE);
        binding.tvError.setVisibility(View.VISIBLE);
        binding.tvError.setText(message);

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
