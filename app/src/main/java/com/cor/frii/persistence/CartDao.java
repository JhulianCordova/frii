package com.cor.frii.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM cart")
    List<ECart> getCart();

    @Query("SELECT * FROM cart WHERE uid LIKE :uid")
    ECart getCart(String uid);

    @Insert
    void addCart(ECart... cart);

    @Delete
    void deleteCart(ECart... cart);

    @Update
    void updateCart(ECart... cart);
}
