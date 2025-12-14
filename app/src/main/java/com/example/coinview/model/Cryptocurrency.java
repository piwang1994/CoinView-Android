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
import androidx.room.TypeConverters;

import com.example.coinview.database.Converters;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "cryptocurrencies")
@TypeConverters(Converters.class)
public class Cryptocurrency {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private String id;

    @SerializedName("symbol")
    @ColumnInfo(name = "symbol")
    private String symbol;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("image")
    @ColumnInfo(name = "image")
    private String image;

    @SerializedName("current_price")
    @ColumnInfo(name = "current_price")
    private double currentPrice;

    @SerializedName("market_cap")
    @ColumnInfo(name = "market_cap")
    private double marketCap;  // ✅ 改为 double

    @SerializedName("market_cap_rank")
    @ColumnInfo(name = "market_cap_rank")
    private int marketCapRank;

    @SerializedName("total_volume")
    @ColumnInfo(name = "total_volume")
    private double totalVolume;  // ✅ 改为 double

    @SerializedName("high_24h")
    @ColumnInfo(name = "high_24h")
    private double high24h;

    @SerializedName("low_24h")
    @ColumnInfo(name = "low_24h")
    private double low24h;

    @SerializedName("price_change_24h")
    @ColumnInfo(name = "price_change_24h")
    private double priceChange24h;

    @SerializedName("price_change_percentage_24h")
    @ColumnInfo(name = "price_change_percentage_24h")
    private double priceChangePercentage24h;

    @SerializedName("circulating_supply")
    @ColumnInfo(name = "circulating_supply")
    private double circulatingSupply;

    @SerializedName("total_supply")
    @ColumnInfo(name = "total_supply")
    private double totalSupply;

    @SerializedName("max_supply")
    @ColumnInfo(name = "max_supply")
    private double maxSupply;

    @SerializedName("ath")
    @ColumnInfo(name = "ath")
    private double ath;

    @SerializedName("ath_change_percentage")
    @ColumnInfo(name = "ath_change_percentage")
    private double athChangePercentage;

    @SerializedName("ath_date")
    @ColumnInfo(name = "ath_date")
    private String athDate;

    @SerializedName("atl")
    @ColumnInfo(name = "atl")
    private double atl;

    @SerializedName("atl_change_percentage")
    @ColumnInfo(name = "atl_change_percentage")
    private double atlChangePercentage;

    @SerializedName("atl_date")
    @ColumnInfo(name = "atl_date")
    private String atlDate;

    @SerializedName("last_updated")
    @ColumnInfo(name = "last_updated")
    private String lastUpdated;

    @SerializedName("sparkline_in_7d")
    @ColumnInfo(name = "sparkline_in_7d")
    private SparklineData sparklineIn7d;

    // Local database fields
    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    @ColumnInfo(name = "last_updated_timestamp")
    private long lastUpdatedTimestamp;

    // ==================== Constructors ====================

    /**
     * Default constructor - Room will use this
     */
    public Cryptocurrency() {
        this.id = "";
    }

    /**
     * Convenience constructor for creating instances programmatically
     * Room will ignore this constructor
     */
    @Ignore  // ✅ 添加 @Ignore 注解
    public Cryptocurrency(@NonNull String id, String symbol, String name, String image,
                          double currentPrice, double marketCap, int marketCapRank) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.image = image;
        this.currentPrice = currentPrice;
        this.marketCap = marketCap;
        this.marketCapRank = marketCapRank;
        this.isFavorite = false;
        this.lastUpdatedTimestamp = System.currentTimeMillis();
    }

    // ==================== Getters and Setters ====================

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public int getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(int marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public double getHigh24h() {
        return high24h;
    }

    public void setHigh24h(double high24h) {
        this.high24h = high24h;
    }

    public double getLow24h() {
        return low24h;
    }

    public void setLow24h(double low24h) {
        this.low24h = low24h;
    }

    public double getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(double priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public void setPriceChangePercentage24h(double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(double circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(double maxSupply) {
        this.maxSupply = maxSupply;
    }

    public double getAth() {
        return ath;
    }

    public void setAth(double ath) {
        this.ath = ath;
    }

    public double getAthChangePercentage() {
        return athChangePercentage;
    }

    public void setAthChangePercentage(double athChangePercentage) {
        this.athChangePercentage = athChangePercentage;
    }

    public String getAthDate() {
        return athDate;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public double getAtl() {
        return atl;
    }

    public void setAtl(double atl) {
        this.atl = atl;
    }

    public double getAtlChangePercentage() {
        return atlChangePercentage;
    }

    public void setAtlChangePercentage(double atlChangePercentage) {
        this.atlChangePercentage = atlChangePercentage;
    }

    public String getAtlDate() {
        return atlDate;
    }

    public void setAtlDate(String atlDate) {
        this.atlDate = atlDate;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public SparklineData getSparklineIn7d() {
        return sparklineIn7d;
    }

    public void setSparklineIn7d(SparklineData sparklineIn7d) {
        this.sparklineIn7d = sparklineIn7d;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    // ==================== Helper Methods ====================

    /**
     * Format price with currency symbol
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", currentPrice);
    }

    /**
     * Format market cap
     */
    public String getFormattedMarketCap() {
        if (marketCap >= 1_000_000_000_000.0) {
            return String.format("$%.2fT", marketCap / 1_000_000_000_000.0);
        } else if (marketCap >= 1_000_000_000.0) {
            return String.format("$%.2fB", marketCap / 1_000_000_000.0);
        } else if (marketCap >= 1_000_000.0) {
            return String.format("$%.2fM", marketCap / 1_000_000.0);
        } else {
            return String.format("$%.2f", marketCap);
        }
    }

    /**
     * Format price change percentage
     */
    public String getFormattedPriceChangePercentage() {
        return String.format("%.2f%%", priceChangePercentage24h);
    }

    /**
     * Check if price is up
     */
    public boolean isPriceUp() {
        return priceChangePercentage24h > 0;
    }

    /**
     * Get price change color (for UI)
     */
    public int getPriceChangeColor() {
        return isPriceUp() ? android.graphics.Color.GREEN : android.graphics.Color.RED;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cryptocurrency{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", marketCapRank=" + marketCapRank +
                ", priceChangePercentage24h=" + priceChangePercentage24h +
                ", isFavorite=" + isFavorite +
                '}';
    }

    // ==================== Inner Class ====================

    public static class SparklineData {
        @SerializedName("price")
        private List<Double> price;

        public List<Double> getPrice() {
            return price;
        }

        public void setPrice(List<Double> price) {
            this.price = price;
        }
    }
}
