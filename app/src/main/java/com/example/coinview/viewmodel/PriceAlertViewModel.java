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

import com.example.coinview.model.PriceAlert;
import com.example.coinview.repository.PriceAlertRepository;

import java.util.List;

public class PriceAlertViewModel extends AndroidViewModel {

    private final PriceAlertRepository repository;

    private final LiveData<List<PriceAlert>> allAlerts;
    private final LiveData<List<PriceAlert>> activeAlerts;
    private final LiveData<Integer> alertCount;
    private final LiveData<Integer> activeAlertCount;

    public PriceAlertViewModel(@NonNull Application application) {
        super(application);
        repository = new PriceAlertRepository(application);

        allAlerts = repository.getAllAlerts();
        activeAlerts = repository.getActiveAlerts();
        alertCount = repository.getAlertCount();
        activeAlertCount = repository.getActiveAlertCount();
    }

    // ==================== Insert Operations ====================

    public void insert(PriceAlert priceAlert) {
        repository.insert(priceAlert);
    }

    public void createAlert(String coinId, String coinName, String coinSymbol,
                            double targetPrice, boolean isAbove) {
        repository.createAlert(coinId, coinName, coinSymbol, targetPrice, isAbove);
    }

    // ==================== Update Operations ====================

    public void update(PriceAlert priceAlert) {
        repository.update(priceAlert);
    }

    public void updateActiveStatus(int alertId, boolean isActive) {
        repository.updateActiveStatus(alertId, isActive);
    }

    public void toggleAlertStatus(PriceAlert alert) {
        repository.toggleAlertStatus(alert);
    }

    public void markAsTriggered(int alertId) {
        repository.markAsTriggered(alertId);
    }

    // ==================== Delete Operations ====================

    public void delete(PriceAlert priceAlert) {
        repository.delete(priceAlert);
    }

    public void deleteAlertsForCoin(String coinId) {
        repository.deleteAlertsForCoin(coinId);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteOldTriggeredAlerts() {
        repository.deleteOldTriggeredAlerts();
    }

    // ==================== Query Operations ====================

    public LiveData<List<PriceAlert>> getAllAlerts() {
        return allAlerts;
    }

    public LiveData<List<PriceAlert>> getActiveAlerts() {
        return activeAlerts;
    }

    public LiveData<List<PriceAlert>> getAlertsForCoin(String coinId) {  // ✅ 确保方法名正确
        return repository.getAlertsForCoin(coinId);
    }

    public LiveData<List<PriceAlert>> getTriggeredAlerts() {
        return repository.getTriggeredAlerts();
    }

    public LiveData<Integer> getAlertCount() {
        return alertCount;
    }

    public LiveData<Integer> getActiveAlertCount() {
        return activeAlertCount;
    }

    // ==================== Alert Checking ====================

    public void checkAlertsForCoin(String coinId, double currentPrice) {
        repository.checkAlertsForCoin(coinId, currentPrice);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.cleanup();
    }
}
