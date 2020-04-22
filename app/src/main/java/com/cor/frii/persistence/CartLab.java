package com.cor.frii.persistence;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import java.util.List;

public class CartLab {

    @SuppressLint("StaticFieldLeak")
    private static CartLab miInstance;

    private CartDao cartDao;
    private AppDatabase appDatabase;
    private Context ctx;

    private CartLab(Context context) {
        this.ctx = context.getApplicationContext();
        appDatabase = Room.databaseBuilder(this.ctx, AppDatabase.class, "freebusiness")
                .allowMainThreadQueries()
                .build();

    }

    public static synchronized CartLab getInstance(Context context) {
        if (miInstance == null) {
            miInstance = new CartLab(context);
        }

        return miInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
