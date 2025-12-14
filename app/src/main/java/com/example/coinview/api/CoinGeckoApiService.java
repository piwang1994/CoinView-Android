/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.api;

import com.example.coinview.model.CoinDetail;
import com.example.coinview.model.Cryptocurrency;
import com.example.coinview.model.MarketChart;
import com.example.coinview.model.SearchResult;
import com.example.coinview.model.TrendingResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * CoinGecko API Service Interface
 * API Documentation: https://www.coingecko.com/en/api/documentation
 */
public interface CoinGeckoApiService {

    /**
     * Get market data for cryptocurrencies
     * Endpoint: /coins/markets
     *
     * @param currency Target currency (usd, eur, etc.)
     * @param order Sort order (market_cap_desc, volume_desc, etc.)
     * @param perPage Number of results per page (1-250)
     * @param page Page number
     * @param sparkline Include sparkline 7 days data
     * @return List of cryptocurrencies with market data
     */
    @GET("coins/markets")
    Call<List<Cryptocurrency>> getMarketData(
            @Query("vs_currency") String currency,
            @Query("order") String order,
            @Query("per_page") int perPage,
            @Query("page") int page,
            @Query("sparkline") boolean sparkline
    );

    /**
     * Get detailed information about a specific coin
     * Endpoint: /coins/{id}
     *
     * @param id Coin ID (bitcoin, ethereum, etc.)
     * @param localization Include localized languages
     * @param tickers Include ticker data
     * @param marketData Include market data
     * @param communityData Include community data
     * @param developerData Include developer data
     * @param sparkline Include sparkline data
     * @return Detailed coin information
     */
    @GET("coins/{id}")
    Call<CoinDetail> getCoinDetails(
            @Path("id") String id,
            @Query("localization") boolean localization,
            @Query("tickers") boolean tickers,
            @Query("market_data") boolean marketData,
            @Query("community_data") boolean communityData,
            @Query("developer_data") boolean developerData,
            @Query("sparkline") boolean sparkline
    );

    /**
     * Get historical market data (price, market cap, volume)
     * Endpoint: /coins/{id}/market_chart
     *
     * @param id Coin ID
     * @param currency Target currency
     * @param days Number of days (1, 7, 14, 30, 90, 180, 365, max)
     * @return Historical market data
     */
    @GET("coins/{id}/market_chart")
    Call<MarketChart> getMarketChart(
            @Path("id") String id,
            @Query("vs_currency") String currency,
            @Query("days") int days
    );

    /**
     * Search for coins, categories, and markets
     * Endpoint: /search
     *
     * @param query Search query
     * @return Search results
     */
    @GET("search")
    Call<SearchResult> searchCoins(
            @Query("query") String query
    );

    /**
     * Get trending coins
     * Endpoint: /search/trending
     *
     * @return Trending coins data
     */
    @GET("search/trending")
    Call<TrendingResult> getTrendingCoins();

    /**
     * Get global cryptocurrency data
     * Endpoint: /global
     *
     * @return Global market data
     */
    @GET("global")
    Call<Object> getGlobalData();

    /**
     * Get list of all supported coins
     * Endpoint: /coins/list
     *
     * @param includePlatform Include platform contract addresses
     * @return List of all coins
     */
    @GET("coins/list")
    Call<List<Object>> getCoinsList(
            @Query("include_platform") boolean includePlatform
    );
}
