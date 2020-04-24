package com.cor.frii.persistence.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "acount")
public class Acount {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "dni")
    private String dni;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "telefono")
    private String telefono;

    @ColumnInfo(name = "direccion")
    private String direccion;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "token")
    private String token;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "company_id")
    private String company_id;
    //--------

    @ColumnInfo(name = "company_name")
    private String company_name;

    @ColumnInfo(name = "company_phone")
    private String company_phone;

    @ColumnInfo(name = "company_address")
    private String company_address;

    @ColumnInfo(name = "company_latitude")
    private String company_latitude;

    @ColumnInfo(name = "company_longitude")
    private String company_longitude;
    //---

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "company_ruc")
    private String company_ruc;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_latitude() {
        return company_latitude;
    }

    public void setCompany_latitude(String company_latitude) {
        this.company_latitude = company_latitude;
    }

    public String getCompany_longitude() {
        return company_longitude;
    }

    public void setCompany_longitude(String company_longitude) {
        this.company_longitude = company_longitude;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCompany_ruc() {
        return company_ruc;
    }

    public void setCompany_ruc(String company_ruc) {
        this.company_ruc = company_ruc;
    }
}
