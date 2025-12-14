/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MarketChart {

    @SerializedName("prices")
    private List<List<Double>> prices;

    @SerializedName("market_caps")
    private List<List<Double>> marketCaps;

    @SerializedName("total_volumes")
    private List<List<Double>> totalVolumes;

    public List<List<Double>> getPrices() { return prices; }
    public List<List<Double>> getMarketCaps() { return marketCaps; }
    public List<List<Double>> getTotalVolumes() { return totalVolumes; }

    // Helper class for chart entries
    public static class ChartEntry {
        private long timestamp;
        private double value;

        public ChartEntry(long timestamp, double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public long getTimestamp() { return timestamp; }
        public double getValue() { return value; }
    }
}
