package com.cor.frii.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ECart.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartDao getCartDao();
}
