/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.database;

import androidx.room.TypeConverter;

import com.example.coinview.model.Cryptocurrency;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Type converters for Room database
 * Converts complex types to primitive types that Room can store
 */
public class Converters {

    private static final Gson gson = new Gson();

    /**
     * Convert SparklineData object to JSON string
     */
    @TypeConverter
    public static String fromSparklineData(Cryptocurrency.SparklineData sparklineData) {
        if (sparklineData == null) {
            return null;
        }
        return gson.toJson(sparklineData);
    }

    /**
     * Convert JSON string to SparklineData object
     */
    @TypeConverter
    public static Cryptocurrency.SparklineData toSparklineData(String sparklineDataString) {
        if (sparklineDataString == null) {
            return null;
        }
        Type type = new TypeToken<Cryptocurrency.SparklineData>() {}.getType();
        return gson.fromJson(sparklineDataString, type);
    }

    /**
     * Convert List<Double> to JSON string
     */
    @TypeConverter
    public static String fromDoubleList(List<Double> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }

    /**
     * Convert JSON string to List<Double>
     */
    @TypeConverter
    public static List<Double> toDoubleList(String listString) {
        if (listString == null) {
            return null;
        }
        Type type = new TypeToken<List<Double>>() {}.getType();
        return gson.fromJson(listString, type);
    }

    /**
     * Convert List<String> to JSON string
     */
    @TypeConverter
    public static String fromStringList(List<String> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }

    /**
     * Convert JSON string to List<String>
     */
    @TypeConverter
    public static List<String> toStringList(String listString) {
        if (listString == null) {
            return null;
        }
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(listString, type);
    }

    /**
     * Convert List<List<Object>> to JSON string (for market chart data)
     */
    @TypeConverter
    public static String fromObjectListList(List<List<Object>> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }

    /**
     * Convert JSON string to List<List<Object>>
     */
    @TypeConverter
    public static List<List<Object>> toObjectListList(String listString) {
        if (listString == null) {
            return null;
        }
        Type type = new TypeToken<List<List<Object>>>() {}.getType();
        return gson.fromJson(listString, type);
    }
}
