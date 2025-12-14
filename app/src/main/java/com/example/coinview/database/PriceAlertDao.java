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

import com.example.coinview.model.PriceAlert;

import java.util.List;

@Dao
public interface PriceAlertDao {

    /**
     * Insert a price alert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PriceAlert priceAlert);

    /**
     * Update a price alert
     */
    @Update
    void update(PriceAlert priceAlert);

    /**
     * Delete a price alert
     */
    @Delete
    void delete(PriceAlert priceAlert);

    /**
     * Get all price alerts
     */
    @Query("SELECT * FROM price_alerts ORDER BY created_at DESC")  // ✅ 使用 created_at
    LiveData<List<PriceAlert>> getAllAlerts();

    /**
     * Get active price alerts
     */
    @Query("SELECT * FROM price_alerts WHERE is_active = 1 ORDER BY created_at DESC")  // ✅ 使用 is_active 和 created_at
    LiveData<List<PriceAlert>> getActiveAlerts();

    /**
     * Get price alerts for a specific coin
     */
    @Query("SELECT * FROM price_alerts WHERE coin_id = :coinId ORDER BY created_at DESC")  // ✅ 使用 coin_id 和 created_at
    LiveData<List<PriceAlert>> getAlertsForCoin(String coinId);

    /**
     * Get active alerts for a specific coin
     */
    @Query("SELECT * FROM price_alerts WHERE coin_id = :coinId AND is_active = 1")  // ✅ 使用 coin_id 和 is_active
    List<PriceAlert> getActiveAlertsForCoin(String coinId);

    /**
     * Get triggered alerts
     */
    @Query("SELECT * FROM price_alerts WHERE triggered_at > 0 ORDER BY triggered_at DESC")  // ✅ 使用 triggered_at
    LiveData<List<PriceAlert>> getTriggeredAlerts();

    /**
     * Update alert active status
     */
    @Query("UPDATE price_alerts SET is_active = :isActive WHERE alert_id = :alertId")  // ✅ 使用 is_active 和 alert_id
    void updateActiveStatus(int alertId, boolean isActive);

    /**
     * Mark alert as triggered
     */
    @Query("UPDATE price_alerts SET is_active = 0, triggered_at = :triggeredAt WHERE alert_id = :alertId")  // ✅ 使用 is_active, triggered_at, alert_id
    void markAsTriggered(int alertId, long triggeredAt);

    /**
     * Delete all alerts for a coin
     */
    @Query("DELETE FROM price_alerts WHERE coin_id = :coinId")  // ✅ 使用 coin_id
    void deleteAlertsForCoin(String coinId);

    /**
     * Delete all alerts
     */
    @Query("DELETE FROM price_alerts")
    void deleteAll();

    /**
     * Get alert count
     */
    @Query("SELECT COUNT(*) FROM price_alerts")
    LiveData<Integer> getAlertCount();

    /**
     * Get active alert count
     */
    @Query("SELECT COUNT(*) FROM price_alerts WHERE is_active = 1")  // ✅ 使用 is_active
    LiveData<Integer> getActiveAlertCount();

    /**
     * Delete old triggered alerts
     */
    @Query("DELETE FROM price_alerts WHERE is_active = 0 AND triggered_at < :timestamp")  // ✅ 使用 is_active 和 triggered_at
    void deleteOldTriggeredAlerts(long timestamp);
}
