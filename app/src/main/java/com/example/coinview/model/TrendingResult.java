/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrendingResult {

    @SerializedName("coins")
    private List<TrendingCoinWrapper> coins;

    public List<TrendingCoinWrapper> getCoins() { return coins; }

    public static class TrendingCoinWrapper {
        @SerializedName("item")
        private TrendingCoin item;

        public TrendingCoin getItem() { return item; }
    }

    public static class TrendingCoin {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("symbol")
        private String symbol;

        @SerializedName("market_cap_rank")
        private int marketCapRank;

        @SerializedName("thumb")
        private String thumb;

        @SerializedName("price_btc")
        private double priceBtc;

        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getSymbol() { return symbol; }
        public int getMarketCapRank() { return marketCapRank; }
        public String getThumb() { return thumb; }
        public double getPriceBtc() { return priceBtc; }
    }
}
