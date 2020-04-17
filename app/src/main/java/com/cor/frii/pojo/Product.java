package com.cor.frii.pojo;

public class Product {

    private int Id;
    private String Name;
    private String Description;
    private float Price;
    private String UnitMeasurement;
    private int Size;
    private String Url;

    public Product(int id, String name, String description, float price, String unitMeasurement, int size, String url) {
        Id = id;
        Name = name;
        Description = description;
        Price = price;
        UnitMeasurement = unitMeasurement;
        Size = size;
        Url = url;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public String getUnitMeasurement() {
        return UnitMeasurement;
    }

    public void setUnitMeasurement(String unitMeasurement) {
        UnitMeasurement = unitMeasurement;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
