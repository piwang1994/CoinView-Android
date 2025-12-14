/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coinview.model.Cryptocurrency;

import java.util.List;

@Dao
public interface CryptocurrencyDao {

    /**
     * Insert a cryptocurrency
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cryptocurrency cryptocurrency);

    /**
     * Insert multiple cryptocurrencies
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Cryptocurrency> cryptocurrencies);

    /**
     * Update a cryptocurrency
     */
    @Update
    void update(Cryptocurrency cryptocurrency);

    /**
     * Delete a cryptocurrency
     */
    @Delete
    void delete(Cryptocurrency cryptocurrency);

    /**
     * Get all cryptocurrencies ordered by market cap rank
     */
    @Query("SELECT * FROM cryptocurrencies ORDER BY market_cap_rank ASC")  // ✅ 修改这里
    LiveData<List<Cryptocurrency>> getAllCryptocurrencies();

    /**
     * Get cryptocurrency by ID
     */
    @Query("SELECT * FROM cryptocurrencies WHERE id = :coinId LIMIT 1")
    LiveData<Cryptocurrency> getCryptocurrencyById(String coinId);

    /**
     * Get favorite cryptocurrencies
     */
    @Query("SELECT * FROM cryptocurrencies WHERE is_favorite = 1 ORDER BY market_cap_rank ASC")  // ✅ 修改这里
    LiveData<List<Cryptocurrency>> getFavoriteCryptocurrencies();

    /**
     * Search cryptocurrencies by name or symbol
     */
    @Query("SELECT * FROM cryptocurrencies WHERE name LIKE '%' || :query || '%' OR symbol LIKE '%' || :query || '%' ORDER BY market_cap_rank ASC")  // ✅ 修改这里
    LiveData<List<Cryptocurrency>> searchCryptocurrencies(String query);

    /**
     * Get top N cryptocurrencies by market cap
     */
    @Query("SELECT * FROM cryptocurrencies ORDER BY market_cap_rank ASC LIMIT :limit")  // ✅ 修改这里
    LiveData<List<Cryptocurrency>> getTopCryptocurrencies(int limit);

    /**
     * Update favorite status
     */
    @Query("UPDATE cryptocurrencies SET is_favorite = :isFavorite WHERE id = :coinId")
    void updateFavoriteStatus(String coinId, boolean isFavorite);

    /**
     * Delete all cryptocurrencies
     */
    @Query("DELETE FROM cryptocurrencies")
    void deleteAll();

    /**
     * Get cryptocurrency count
     */
    @Query("SELECT COUNT(*) FROM cryptocurrencies")
    LiveData<Integer> getCryptocurrencyCount();

    /**
     * Check if cryptocurrency exists
     */
    @Query("SELECT EXISTS(SELECT 1 FROM cryptocurrencies WHERE id = :coinId LIMIT 1)")
    boolean exists(String coinId);

    /**
     * Get cryptocurrencies with price change greater than threshold
     */
    @Query("SELECT * FROM cryptocurrencies WHERE price_change_percentage_24h > :threshold ORDER BY price_change_percentage_24h DESC")  // ✅ 修改这里
    LiveData<List<Cryptocurrency>> getGainers(double threshold);

    /**
     * Get cryptocurrencies with price change less than threshold
     */
    @Query("SELECT * FROM cryptocurrencies WHERE price_change_percentage_24h < :threshold ORDER BY price_change_percentage_24h ASC")  // ✅ 修改这里
    LiveData<List<Cryptocurrency>> getLosers(double threshold);

    /**
     * Get cryptocurrencies by market cap range
     */
    @Query("SELECT * FROM cryptocurrencies WHERE market_cap BETWEEN :minCap AND :maxCap ORDER BY market_cap_rank ASC")  // ✅ 修改这里
    LiveData<List<Cryptocurrency>> getCryptocurrenciesByMarketCapRange(long minCap, long maxCap);

    /**
     * Get recently updated cryptocurrencies
     */
    @Query("SELECT * FROM cryptocurrencies ORDER BY last_updated_timestamp DESC LIMIT :limit")
    LiveData<List<Cryptocurrency>> getRecentlyUpdated(int limit);

    /**
     * Delete old data (older than specified timestamp)
     */
    @Query("DELETE FROM cryptocurrencies WHERE last_updated_timestamp < :timestamp")
    void deleteOldData(long timestamp);
}
