package com.cor.frii.persistence.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "cart")
public class ECart {

    @NonNull
    @PrimaryKey
    private String uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private float price;

    @ColumnInfo(name = "cantidad")
    private int cantidad;

    @ColumnInfo(name = "total")
    private float total;


    public ECart() {
        uid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ECart{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", cantidad=" + cantidad +
                ", total=" + total +
                '}';
    }
}
