package com.cor.frii.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cor.frii.persistence.entity.Acount;

import java.util.List;

@Dao
public interface AcountDao {

    @Query("SELECT * FROM acount")
    List<Acount> getUsers();

    @Query("SELECT * FROM acount WHERE token LIKE :token")
    Acount getUser(String token);

    @Insert
    void addUser(Acount... user);

    @Delete
    void deleteUser(Acount... user);

    @Update
    void updateUser(Acount... user);

    @Query("SELECT * FROM acount WHERE token LIKE :token")
    Acount login(String token);

}
