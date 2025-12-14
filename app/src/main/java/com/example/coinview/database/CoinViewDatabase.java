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

import com.example.coinview.model.Cryptocurrency;
import com.example.coinview.model.PriceAlert;

@Database(entities = {Cryptocurrency.class, PriceAlert.class}, version = 1, exportSchema = false)
public abstract class CoinViewDatabase extends RoomDatabase {

    private static CoinViewDatabase instance;

    public abstract CryptocurrencyDao cryptocurrencyDao();
    public abstract PriceAlertDao priceAlertDao();

    public static synchronized CoinViewDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            CoinViewDatabase.class,
                            "coinview_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
