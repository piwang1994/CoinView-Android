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

import com.example.coinview.database.AppDatabase;
import com.example.coinview.database.PriceAlertDao;
import com.example.coinview.model.PriceAlert;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PriceAlertRepository {

    private static final String TAG = "PriceAlertRepository";

    private final PriceAlertDao priceAlertDao;
    private final ExecutorService executorService;

    private final LiveData<List<PriceAlert>> allAlerts;
    private final LiveData<List<PriceAlert>> activeAlerts;
    private final LiveData<Integer> alertCount;
    private final LiveData<Integer> activeAlertCount;

    public PriceAlertRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        priceAlertDao = database.priceAlertDao();
        executorService = Executors.newFixedThreadPool(2);

        allAlerts = priceAlertDao.getAllAlerts();
        activeAlerts = priceAlertDao.getActiveAlerts();
        alertCount = priceAlertDao.getAlertCount();
        activeAlertCount = priceAlertDao.getActiveAlertCount();
    }

    // ==================== Insert Operations ====================

    /**
     * Insert a new price alert
     */
    public void insert(PriceAlert priceAlert) {
        executorService.execute(() -> {
            try {
                priceAlertDao.insert(priceAlert);
                Log.d(TAG, "Price alert inserted: " + priceAlert.getAlertDescription());
            } catch (Exception e) {
                Log.e(TAG, "Error inserting price alert", e);
            }
        });
    }

    // ==================== Update Operations ====================

    /**
     * Update an existing price alert
     */
    public void update(PriceAlert priceAlert) {
        executorService.execute(() -> {
            try {
                priceAlertDao.update(priceAlert);
                Log.d(TAG, "Price alert updated: " + priceAlert.getAlertId());
            } catch (Exception e) {
                Log.e(TAG, "Error updating price alert", e);
            }
        });
    }

    /**
     * Update alert active status
     */
    public void updateActiveStatus(int alertId, boolean isActive) {
        executorService.execute(() -> {
            try {
                priceAlertDao.updateActiveStatus(alertId, isActive);
                Log.d(TAG, "Alert active status updated: " + alertId + " -> " + isActive);
            } catch (Exception e) {
                Log.e(TAG, "Error updating alert active status", e);
            }
        });
    }

    /**
     * Mark alert as triggered
     */
    public void markAsTriggered(int alertId) {
        executorService.execute(() -> {
            try {
                long triggeredAt = System.currentTimeMillis();
                priceAlertDao.markAsTriggered(alertId, triggeredAt);
                Log.d(TAG, "Alert marked as triggered: " + alertId);
            } catch (Exception e) {
                Log.e(TAG, "Error marking alert as triggered", e);
            }
        });
    }

    // ==================== Delete Operations ====================

    /**
     * Delete a price alert
     */
    public void delete(PriceAlert priceAlert) {
        executorService.execute(() -> {
            try {
                priceAlertDao.delete(priceAlert);
                Log.d(TAG, "Price alert deleted: " + priceAlert.getAlertId());
            } catch (Exception e) {
                Log.e(TAG, "Error deleting price alert", e);
            }
        });
    }

    /**
     * Delete all alerts for a specific coin
     */
    public void deleteAlertsForCoin(String coinId) {
        executorService.execute(() -> {
            try {
                priceAlertDao.deleteAlertsForCoin(coinId);
                Log.d(TAG, "All alerts deleted for coin: " + coinId);
            } catch (Exception e) {
                Log.e(TAG, "Error deleting alerts for coin", e);
            }
        });
    }

    /**
     * Delete all price alerts
     */
    public void deleteAll() {
        executorService.execute(() -> {
            try {
                priceAlertDao.deleteAll();
                Log.d(TAG, "All price alerts deleted");
            } catch (Exception e) {
                Log.e(TAG, "Error deleting all alerts", e);
            }
        });
    }

    /**
     * Delete old triggered alerts (older than 30 days)
     */
    public void deleteOldTriggeredAlerts() {
        executorService.execute(() -> {
            try {
                long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
                priceAlertDao.deleteOldTriggeredAlerts(thirtyDaysAgo);
                Log.d(TAG, "Old triggered alerts deleted");
            } catch (Exception e) {
                Log.e(TAG, "Error deleting old triggered alerts", e);
            }
        });
    }

    // ==================== Query Operations ====================

    /**
     * Get all price alerts
     */
    public LiveData<List<PriceAlert>> getAllAlerts() {
        return allAlerts;
    }

    /**
     * Get active price alerts
     */
    public LiveData<List<PriceAlert>> getActiveAlerts() {
        return activeAlerts;
    }

    /**
     * Get price alerts for a specific coin
     */
    public LiveData<List<PriceAlert>> getAlertsForCoin(String coinId) {  // ✅ 修改方法名
        return priceAlertDao.getAlertsForCoin(coinId);  // ✅ 修改调用
    }

    /**
     * Get triggered alerts
     */
    public LiveData<List<PriceAlert>> getTriggeredAlerts() {
        return priceAlertDao.getTriggeredAlerts();
    }

    /**
     * Get alert count
     */
    public LiveData<Integer> getAlertCount() {
        return alertCount;
    }

    /**
     * Get active alert count
     */
    public LiveData<Integer> getActiveAlertCount() {
        return activeAlertCount;
    }

    // ==================== Alert Checking ====================

    /**
     * Check if any alerts should be triggered for a coin
     * This method should be called periodically by a background service
     */
    public void checkAlertsForCoin(String coinId, double currentPrice) {
        executorService.execute(() -> {
            try {
                List<PriceAlert> alerts = priceAlertDao.getActiveAlertsForCoin(coinId);

                for (PriceAlert alert : alerts) {
                    if (alert.shouldTrigger(currentPrice)) {
                        // Mark alert as triggered
                        markAsTriggered(alert.getAlertId());

                        // TODO: Send notification to user
                        Log.d(TAG, "Alert triggered: " + alert.getAlertDescription() +
                                " Current price: $" + currentPrice);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking alerts for coin: " + coinId, e);
            }
        });
    }

    /**
     * Check all active alerts
     * This should be called by a periodic background worker
     */
    public void checkAllAlerts(List<com.example.coinview.model.Cryptocurrency> cryptocurrencies) {
        executorService.execute(() -> {
            try {
                for (com.example.coinview.model.Cryptocurrency crypto : cryptocurrencies) {
                    checkAlertsForCoin(crypto.getId(), crypto.getCurrentPrice());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking all alerts", e);
            }
        });
    }

    // ==================== Utility Methods ====================

    /**
     * Create a new price alert
     */
    public void createAlert(String coinId, String coinName, String coinSymbol,
                            double targetPrice, boolean isAbove) {
        PriceAlert alert = new PriceAlert(coinId, coinName, coinSymbol, targetPrice, isAbove);
        insert(alert);
    }

    /**
     * Toggle alert active status
     */
    public void toggleAlertStatus(PriceAlert alert) {
        updateActiveStatus(alert.getAlertId(), !alert.isActive());
    }

    /**
     * Cleanup - should be called when repository is no longer needed
     */
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            Log.d(TAG, "ExecutorService shutdown");
        }
    }
}
