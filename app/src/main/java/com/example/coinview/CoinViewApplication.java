package com.example.coinview;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class CoinViewApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // ✅ 初始化 Firebase
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
