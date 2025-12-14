/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.coinview.model.Cryptocurrency;
import com.example.coinview.model.PriceAlert;

@Database(
        entities = {Cryptocurrency.class, PriceAlert.class},
        version = 2,
        exportSchema = false
)
@TypeConverters(Converters.class)  // ✅ 确保有这行
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "coinview_database";
    private static volatile AppDatabase INSTANCE;

    public abstract CryptocurrencyDao cryptocurrencyDao();
    public abstract PriceAlertDao priceAlertDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}


