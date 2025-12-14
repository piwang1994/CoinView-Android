/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.coinview.model.CoinDetail;
import com.example.coinview.model.Cryptocurrency;
import com.example.coinview.model.MarketChart;
import com.example.coinview.model.SearchResult;
import com.example.coinview.model.TrendingResult;
import com.example.coinview.repository.CryptocurrencyRepository;

import java.util.List;

public class CryptocurrencyViewModel extends AndroidViewModel {

    private final CryptocurrencyRepository repository;

    // LiveData from database
    private final LiveData<List<Cryptocurrency>> allCryptocurrencies;
    private final LiveData<List<Cryptocurrency>> favoriteCryptocurrencies;

    // LiveData from API
    private final LiveData<List<Cryptocurrency>> cryptocurrenciesFromApi;
    private final LiveData<CoinDetail> coinDetail;
    private final LiveData<MarketChart> marketChart;
    private final LiveData<SearchResult> searchResult;
    private final LiveData<TrendingResult> trendingResult;
    private final LiveData<String> error;
    private final LiveData<Boolean> loading;

    public CryptocurrencyViewModel(@NonNull Application application) {
        super(application);
        repository = new CryptocurrencyRepository(application);

        // Initialize LiveData from repository
        allCryptocurrencies = repository.getAllCryptocurrencies();
        favoriteCryptocurrencies = repository.getFavoriteCryptocurrencies();
        cryptocurrenciesFromApi = repository.getCryptocurrenciesLiveData();
        coinDetail = repository.getCoinDetailLiveData();
        marketChart = repository.getMarketChartLiveData();
        searchResult = repository.getSearchResultLiveData();
        trendingResult = repository.getTrendingResultLiveData();
        error = repository.getErrorLiveData();
        loading = repository.getLoadingLiveData();
    }

    // ==================== Getters for LiveData ====================

    /**
     * Get all cryptocurrencies from database
     */
    public LiveData<List<Cryptocurrency>> getAllCryptocurrencies() {
        return allCryptocurrencies;
    }

    /**
     * Get favorite cryptocurrencies from database
     */
    public LiveData<List<Cryptocurrency>> getFavoriteCryptocurrencies() {
        return favoriteCryptocurrencies;
    }

    /**
     * Get cryptocurrencies from API
     */
    public LiveData<List<Cryptocurrency>> getCryptocurrenciesFromApi() {
        return cryptocurrenciesFromApi;
    }

    /**
     * Get coin detail from API
     */
    public LiveData<CoinDetail> getCoinDetail() {
        return coinDetail;
    }

    /**
     * Get market chart from API
     */
    public LiveData<MarketChart> getMarketChart() {
        return marketChart;
    }

    /**
     * Get search result from API
     */
    public LiveData<SearchResult> getSearchResult() {
        return searchResult;
    }

    /**
     * Get trending result from API
     */
    public LiveData<TrendingResult> getTrendingResult() {
        return trendingResult;
    }

    /**
     * Get error message
     */
    public LiveData<String> getError() {
        return error;
    }

    /**
     * Get loading state
     */
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    // ==================== API Operations ====================

    /**
     * Fetch market data from API
     * @param currency Currency code (e.g., "usd")
     * @param perPage Number of items per page
     * @param page Page number
     */
    public void fetchMarketData(String currency, int perPage, int page) {
        repository.fetchMarketData(currency, perPage, page);
    }

    /**
     * Fetch market data with default parameters
     */
    public void fetchMarketData() {
        repository.fetchMarketData("usd", 100, 1);
    }

    /**
     * Refresh market data
     */
    public void refreshData() {
        repository.refreshData();
    }

    /**
     * Fetch coin details
     * @param coinId Coin ID
     */
    public void fetchCoinDetails(String coinId) {
        repository.fetchCoinDetails(coinId);
    }

    /**
     * Fetch market chart
     * @param coinId Coin ID
     * @param currency Currency code
     * @param days Number of days
     */
    public void fetchMarketChart(String coinId, String currency, int days) {
        repository.fetchMarketChart(coinId, currency, days);
    }

    /**
     * Search coins
     * @param query Search query
     */
    public void searchCoins(String query) {
        repository.searchCoins(query);
    }

    /**
     * Fetch trending coins
     */
    public void fetchTrendingCoins() {
        repository.fetchTrendingCoins();
    }

    // ==================== Database Operations ====================

    /**
     * Insert a cryptocurrency
     */
    public void insert(Cryptocurrency cryptocurrency) {
        repository.insert(cryptocurrency);
    }

    /**
     * Insert multiple cryptocurrencies
     */
    public void insertAll(List<Cryptocurrency> cryptocurrencies) {
        repository.insertAll(cryptocurrencies);
    }

    /**
     * Update a cryptocurrency
     */
    public void update(Cryptocurrency cryptocurrency) {
        repository.update(cryptocurrency);
    }

    /**
     * Delete a cryptocurrency
     */
    public void delete(Cryptocurrency cryptocurrency) {
        repository.delete(cryptocurrency);
    }

    /**
     * Delete all cryptocurrencies
     */
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * Delete old data
     */
    public void deleteOldData() {
        repository.deleteOldData();
    }

    // ==================== Query Operations ====================

    /**
     * Get cryptocurrency by ID
     */
    public LiveData<Cryptocurrency> getCryptocurrencyById(String coinId) {
        return repository.getCryptocurrencyById(coinId);
    }

    /**
     * Search cryptocurrencies in database
     */
    public LiveData<List<Cryptocurrency>> searchCryptocurrencies(String query) {
        return repository.searchCryptocurrencies(query);
    }

    /**
     * Get top N cryptocurrencies
     */
    public LiveData<List<Cryptocurrency>> getTopCryptocurrencies(int limit) {
        return repository.getTopCryptocurrencies(limit);
    }

    /**
     * Get gainers
     */
    public LiveData<List<Cryptocurrency>> getGainers() {
        return repository.getGainers();
    }

    /**
     * Get losers
     */
    public LiveData<List<Cryptocurrency>> getLosers() {
        return repository.getLosers();
    }

    /**
     * Get cryptocurrency count
     */
    public LiveData<Integer> getCryptocurrencyCount() {
        return repository.getCryptocurrencyCount();
    }

    // ==================== Favorite Operations ====================

    /**
     * Update favorite status
     */
    public void updateFavoriteStatus(String coinId, boolean isFavorite) {
        repository.updateFavoriteStatus(coinId, isFavorite);
    }

    /**
     * Toggle favorite status
     */
    public void toggleFavorite(Cryptocurrency cryptocurrency) {
        repository.toggleFavorite(cryptocurrency);
    }

    // ==================== Cleanup ====================

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.cleanup();
    }



}
