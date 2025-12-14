/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.coinview.api.CoinGeckoApiService;
import com.example.coinview.api.RetrofitClient;
import com.example.coinview.database.AppDatabase;
import com.example.coinview.database.CryptocurrencyDao;
import com.example.coinview.model.CoinDetail;
import com.example.coinview.model.Cryptocurrency;
import com.example.coinview.model.MarketChart;
import com.example.coinview.model.SearchResult;
import com.example.coinview.model.TrendingResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CryptocurrencyRepository {

    private static final String TAG = "CryptoRepository";

    private final CryptocurrencyDao cryptocurrencyDao;
    private final CoinGeckoApiService apiService;
    private final ExecutorService executorService;

    // LiveData from database
    private final LiveData<List<Cryptocurrency>> allCryptocurrencies;
    private final LiveData<List<Cryptocurrency>> favoriteCryptocurrencies;
    private final LiveData<Integer> cryptocurrencyCount;

    // LiveData for API responses
    private final MutableLiveData<List<Cryptocurrency>> cryptocurrenciesLiveData = new MutableLiveData<>();
    private final MutableLiveData<CoinDetail> coinDetailLiveData = new MutableLiveData<>();
    private final MutableLiveData<MarketChart> marketChartLiveData = new MutableLiveData<>();
    private final MutableLiveData<SearchResult> searchResultLiveData = new MutableLiveData<>();
    private final MutableLiveData<TrendingResult> trendingResultLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);

    public CryptocurrencyRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        cryptocurrencyDao = database.cryptocurrencyDao();
        apiService = RetrofitClient.getApiService();
        executorService = Executors.newFixedThreadPool(4);

        allCryptocurrencies = cryptocurrencyDao.getAllCryptocurrencies();
        favoriteCryptocurrencies = cryptocurrencyDao.getFavoriteCryptocurrencies();
        cryptocurrencyCount = cryptocurrencyDao.getCryptocurrencyCount();
    }

    // ==================== Getters for LiveData ====================

    public LiveData<List<Cryptocurrency>> getCryptocurrenciesLiveData() {
        return cryptocurrenciesLiveData;
    }

    public LiveData<CoinDetail> getCoinDetailLiveData() {
        return coinDetailLiveData;
    }

    public LiveData<MarketChart> getMarketChartLiveData() {
        return marketChartLiveData;
    }

    public LiveData<SearchResult> getSearchResultLiveData() {
        return searchResultLiveData;
    }

    public LiveData<TrendingResult> getTrendingResultLiveData() {
        return trendingResultLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    // ==================== Insert Operations ====================

    public void insert(Cryptocurrency cryptocurrency) {
        executorService.execute(() -> {
            try {
                cryptocurrency.setLastUpdatedTimestamp(System.currentTimeMillis());
                cryptocurrencyDao.insert(cryptocurrency);
                Log.d(TAG, "Cryptocurrency inserted: " + cryptocurrency.getName());
            } catch (Exception e) {
                Log.e(TAG, "Error inserting cryptocurrency", e);
            }
        });
    }

    public void insertAll(List<Cryptocurrency> cryptocurrencies) {
        executorService.execute(() -> {
            try {
                long currentTime = System.currentTimeMillis();
                for (Cryptocurrency crypto : cryptocurrencies) {
                    crypto.setLastUpdatedTimestamp(currentTime);
                }
                cryptocurrencyDao.insertAll(cryptocurrencies);
                Log.d(TAG, "Inserted " + cryptocurrencies.size() + " cryptocurrencies");
            } catch (Exception e) {
                Log.e(TAG, "Error inserting cryptocurrencies", e);
            }
        });
    }

    // ==================== Update Operations ====================

    public void update(Cryptocurrency cryptocurrency) {
        executorService.execute(() -> {
            try {
                cryptocurrency.setLastUpdatedTimestamp(System.currentTimeMillis());
                cryptocurrencyDao.update(cryptocurrency);
                Log.d(TAG, "Cryptocurrency updated: " + cryptocurrency.getName());
            } catch (Exception e) {
                Log.e(TAG, "Error updating cryptocurrency", e);
            }
        });
    }

    public void updateFavoriteStatus(String coinId, boolean isFavorite) {
        executorService.execute(() -> {
            try {
                cryptocurrencyDao.updateFavoriteStatus(coinId, isFavorite);
                Log.d(TAG, "Favorite status updated for: " + coinId + " -> " + isFavorite);
            } catch (Exception e) {
                Log.e(TAG, "Error updating favorite status", e);
            }
        });
    }

    // ==================== Delete Operations ====================

    public void delete(Cryptocurrency cryptocurrency) {
        executorService.execute(() -> {
            try {
                cryptocurrencyDao.delete(cryptocurrency);
                Log.d(TAG, "Cryptocurrency deleted: " + cryptocurrency.getName());
            } catch (Exception e) {
                Log.e(TAG, "Error deleting cryptocurrency", e);
            }
        });
    }

    public void deleteAll() {
        executorService.execute(() -> {
            try {
                cryptocurrencyDao.deleteAll();
                Log.d(TAG, "All cryptocurrencies deleted");
            } catch (Exception e) {
                Log.e(TAG, "Error deleting all cryptocurrencies", e);
            }
        });
    }

    public void deleteOldData() {
        executorService.execute(() -> {
            try {
                long twentyFourHoursAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
                cryptocurrencyDao.deleteOldData(twentyFourHoursAgo);
                Log.d(TAG, "Old data deleted");
            } catch (Exception e) {
                Log.e(TAG, "Error deleting old data", e);
            }
        });
    }

    // ==================== Query Operations ====================

    public LiveData<List<Cryptocurrency>> getAllCryptocurrencies() {
        return allCryptocurrencies;
    }

    public LiveData<Cryptocurrency> getCryptocurrencyById(String coinId) {
        return cryptocurrencyDao.getCryptocurrencyById(coinId);
    }

    public LiveData<List<Cryptocurrency>> getFavoriteCryptocurrencies() {
        return favoriteCryptocurrencies;
    }

    public LiveData<List<Cryptocurrency>> searchCryptocurrencies(String query) {
        return cryptocurrencyDao.searchCryptocurrencies(query);
    }

    public LiveData<List<Cryptocurrency>> getTopCryptocurrencies(int limit) {
        return cryptocurrencyDao.getTopCryptocurrencies(limit);
    }

    public LiveData<List<Cryptocurrency>> getGainers() {
        return cryptocurrencyDao.getGainers(0);
    }

    public LiveData<List<Cryptocurrency>> getLosers() {
        return cryptocurrencyDao.getLosers(0);
    }

    public LiveData<Integer> getCryptocurrencyCount() {
        return cryptocurrencyCount;
    }

    // ==================== API Operations ====================

    /**
     * Fetch market data from API
     */
    public void fetchMarketData(String currency, int perPage, int page) {
        loadingLiveData.postValue(true);

        apiService.getMarketData(currency, "market_cap_desc", perPage, page, true)  // ✅ 删除 "24h"
                .enqueue(new Callback<List<Cryptocurrency>>() {
                    @Override
                    public void onResponse(Call<List<Cryptocurrency>> call,
                                           Response<List<Cryptocurrency>> response) {
                        loadingLiveData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            List<Cryptocurrency> cryptocurrencies = response.body();
                            cryptocurrenciesLiveData.postValue(cryptocurrencies);

                            // Save to database
                            executorService.execute(() -> {
                                try {
                                    long currentTime = System.currentTimeMillis();
                                    for (Cryptocurrency crypto : cryptocurrencies) {
                                        crypto.setLastUpdatedTimestamp(currentTime);
                                    }
                                    cryptocurrencyDao.insertAll(cryptocurrencies);
                                    Log.d(TAG, "Market data saved: " + cryptocurrencies.size() + " items");
                                } catch (Exception e) {
                                    Log.e(TAG, "Error saving market data", e);
                                }
                            });
                        } else {
                            String error = "API response unsuccessful: " + response.code();
                            Log.e(TAG, error);
                            errorLiveData.postValue(error);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Cryptocurrency>> call, Throwable t) {
                        loadingLiveData.postValue(false);
                        String error = "API call failed: " + t.getMessage();
                        Log.e(TAG, error, t);
                        errorLiveData.postValue(error);
                    }
                });
    }

    /**
     * Fetch coin details from API
     */
    public void fetchCoinDetails(String coinId) {
        loadingLiveData.postValue(true);

        apiService.getCoinDetails(coinId, true, true, true, true, true, true)
                .enqueue(new Callback<CoinDetail>() {
                    @Override
                    public void onResponse(Call<CoinDetail> call, Response<CoinDetail> response) {
                        loadingLiveData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            coinDetailLiveData.postValue(response.body());
                            Log.d(TAG, "Coin details fetched: " + coinId);
                        } else {
                            String error = "Failed to fetch coin details: " + response.code();
                            Log.e(TAG, error);
                            errorLiveData.postValue(error);
                            coinDetailLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<CoinDetail> call, Throwable t) {
                        loadingLiveData.postValue(false);
                        String error = "Failed to fetch coin details: " + t.getMessage();
                        Log.e(TAG, error, t);
                        errorLiveData.postValue(error);
                        coinDetailLiveData.postValue(null);
                    }
                });
    }

    /**
     * Fetch market chart data from API
     */
    public void fetchMarketChart(String coinId, String currency, int days) {
        loadingLiveData.postValue(true);

        apiService.getMarketChart(coinId, currency, days)
                .enqueue(new Callback<MarketChart>() {
                    @Override
                    public void onResponse(Call<MarketChart> call, Response<MarketChart> response) {
                        loadingLiveData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            marketChartLiveData.postValue(response.body());
                            Log.d(TAG, "Market chart fetched: " + coinId);
                        } else {
                            String error = "Failed to fetch market chart: " + response.code();
                            Log.e(TAG, error);
                            errorLiveData.postValue(error);
                            marketChartLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<MarketChart> call, Throwable t) {
                        loadingLiveData.postValue(false);
                        String error = "Failed to fetch market chart: " + t.getMessage();
                        Log.e(TAG, error, t);
                        errorLiveData.postValue(error);
                        marketChartLiveData.postValue(null);
                    }
                });
    }

    /**
     * Search coins from API
     */
    public void searchCoins(String query) {
        loadingLiveData.postValue(true);

        apiService.searchCoins(query)
                .enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        loadingLiveData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            searchResultLiveData.postValue(response.body());
                            Log.d(TAG, "Search results fetched for: " + query);
                        } else {
                            String error = "Failed to search coins: " + response.code();
                            Log.e(TAG, error);
                            errorLiveData.postValue(error);
                            searchResultLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        loadingLiveData.postValue(false);
                        String error = "Failed to search coins: " + t.getMessage();
                        Log.e(TAG, error, t);
                        errorLiveData.postValue(error);
                        searchResultLiveData.postValue(null);
                    }
                });
    }

    /**
     * Fetch trending coins from API
     */
    public void fetchTrendingCoins() {
        loadingLiveData.postValue(true);

        apiService.getTrendingCoins()
                .enqueue(new Callback<TrendingResult>() {
                    @Override
                    public void onResponse(Call<TrendingResult> call,
                                           Response<TrendingResult> response) {
                        loadingLiveData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            trendingResultLiveData.postValue(response.body());
                            Log.d(TAG, "Trending coins fetched");
                        } else {
                            String error = "Failed to fetch trending coins: " + response.code();
                            Log.e(TAG, error);
                            errorLiveData.postValue(error);
                            trendingResultLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<TrendingResult> call, Throwable t) {
                        loadingLiveData.postValue(false);
                        String error = "Failed to fetch trending coins: " + t.getMessage();
                        Log.e(TAG, error, t);
                        errorLiveData.postValue(error);
                        trendingResultLiveData.postValue(null);
                    }
                });
    }

    // ==================== Utility Methods ====================

    public void toggleFavorite(Cryptocurrency cryptocurrency) {
        updateFavoriteStatus(cryptocurrency.getId(), !cryptocurrency.isFavorite());
    }

    public void refreshData() {
        fetchMarketData("usd", 100, 1);
    }

    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            Log.d(TAG, "ExecutorService shutdown");
        }
    }
}
