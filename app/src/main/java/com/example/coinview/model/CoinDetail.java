/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.model;

import com.google.gson.annotations.SerializedName;

public class CoinDetail {

    @SerializedName("id")
    private String id;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private Description description;

    @SerializedName("image")
    private ImageUrls image;

    @SerializedName("market_data")
    private MarketData marketData;

    @SerializedName("market_cap_rank")
    private int marketCapRank;

    // Inner classes
    public static class Description {
        @SerializedName("en")
        private String en;

        public String getEn() { return en; }
    }

    public static class ImageUrls {
        @SerializedName("thumb")
        private String thumb;

        @SerializedName("small")
        private String small;

        @SerializedName("large")
        private String large;

        public String getThumb() { return thumb; }
        public String getSmall() { return small; }
        public String getLarge() { return large; }
    }

    public static class MarketData {
        @SerializedName("current_price")
        private PriceData currentPrice;

        @SerializedName("high_24h")
        private PriceData high24h;

        @SerializedName("low_24h")
        private PriceData low24h;

        @SerializedName("market_cap")
        private PriceData marketCap;

        @SerializedName("total_volume")
        private PriceData totalVolume;

        @SerializedName("circulating_supply")
        private double circulatingSupply;

        @SerializedName("total_supply")
        private double totalSupply;

        @SerializedName("ath")
        private PriceData ath;

        @SerializedName("ath_date")
        private DateData athDate;

        @SerializedName("price_change_percentage_24h")
        private double priceChangePercentage24h;

        // Getters
        public PriceData getCurrentPrice() { return currentPrice; }
        public PriceData getHigh24h() { return high24h; }
        public PriceData getLow24h() { return low24h; }
        public PriceData getMarketCap() { return marketCap; }
        public PriceData getTotalVolume() { return totalVolume; }
        public double getCirculatingSupply() { return circulatingSupply; }
        public double getTotalSupply() { return totalSupply; }
        public PriceData getAth() { return ath; }
        public DateData getAthDate() { return athDate; }
        public double getPriceChangePercentage24h() { return priceChangePercentage24h; }
    }

    public static class PriceData {
        @SerializedName("usd")
        private double usd;

        public double getUsd() { return usd; }
    }

    public static class DateData {
        @SerializedName("usd")
        private String usd;

        public String getUsd() { return usd; }
    }

    // Main getters
    public String getId() { return id; }
    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public Description getDescription() { return description; }
    public ImageUrls getImage() { return image; }
    public MarketData getMarketData() { return marketData; }
    public int getMarketCapRank() { return marketCapRank; }
}
