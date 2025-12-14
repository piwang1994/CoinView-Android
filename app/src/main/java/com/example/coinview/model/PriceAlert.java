/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "price_alerts")
public class PriceAlert {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "alert_id")
    private int alertId;

    @NonNull
    @ColumnInfo(name = "coin_id")
    private String coinId;

    @ColumnInfo(name = "coin_name")
    private String coinName;

    @ColumnInfo(name = "coin_symbol")
    private String coinSymbol;

    @ColumnInfo(name = "target_price")
    private double targetPrice;

    @ColumnInfo(name = "is_above")
    private boolean isAbove;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "triggered_at")
    private long triggeredAt;

    // ==================== Constructors ====================

    /**
     * Default constructor - Room will use this
     */
    public PriceAlert() {
        this.coinId = "";
    }

    /**
     * Convenience constructor
     * Room will ignore this constructor
     */
    @Ignore  // ✅ 添加 @Ignore 注解
    public PriceAlert(@NonNull String coinId, String coinName, String coinSymbol,
                      double targetPrice, boolean isAbove) {
        this.coinId = coinId;
        this.coinName = coinName;
        this.coinSymbol = coinSymbol;
        this.targetPrice = targetPrice;
        this.isAbove = isAbove;
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
        this.triggeredAt = 0;
    }

    // ==================== Getters and Setters ====================

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    @NonNull
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(@NonNull String coinId) {
        this.coinId = coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public boolean isAbove() {
        return isAbove;
    }

    public void setAbove(boolean above) {
        isAbove = above;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(long triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    // ==================== Helper Methods ====================

    /**
     * Get alert description
     */
    public String getAlertDescription() {
        String direction = isAbove ? "above" : "below";
        return String.format("%s (%s) %s $%.2f",
                coinName, coinSymbol.toUpperCase(), direction, targetPrice);
    }

    /**
     * Get formatted target price
     */
    public String getFormattedTargetPrice() {
        return String.format("$%.2f", targetPrice);
    }

    /**
     * Check if alert should be triggered
     */
    public boolean shouldTrigger(double currentPrice) {
        if (!isActive) {
            return false;
        }

        if (isAbove) {
            return currentPrice >= targetPrice;
        } else {
            return currentPrice <= targetPrice;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "PriceAlert{" +
                "alertId=" + alertId +
                ", coinId='" + coinId + '\'' +
                ", coinName='" + coinName + '\'' +
                ", targetPrice=" + targetPrice +
                ", isAbove=" + isAbove +
                ", isActive=" + isActive +
                '}';
    }
}
