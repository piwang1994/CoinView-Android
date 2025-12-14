/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchResult {

    @SerializedName("coins")
    private List<CoinSearchItem> coins;

    public List<CoinSearchItem> getCoins() { return coins; }

    public static class CoinSearchItem {
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

        @SerializedName("large")
        private String large;

        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getSymbol() { return symbol; }
        public int getMarketCapRank() { return marketCapRank; }
        public String getThumb() { return thumb; }
        public String getLarge() { return large; }
    }
}
