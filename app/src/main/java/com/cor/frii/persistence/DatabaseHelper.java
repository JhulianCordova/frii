package com.cor.frii.persistence;

import android.content.Context;

import com.cor.frii.persistence.entity.Acount;

public class DatabaseHelper {

    private Context context;


    public DatabaseHelper(Context context) {
        this.context = context;
    }

    public Acount login(String token) {

        return DatabaseClient.getInstance(context)
                .getAppDatabase()
                .getAcountDao()
                .login(token);
    }


}
