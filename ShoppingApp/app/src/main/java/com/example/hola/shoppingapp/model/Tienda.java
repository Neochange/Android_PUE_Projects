package com.example.hola.shoppingapp.model;

/**
 * Created by daa on 06/02/2017.
 */

public class Tienda {

    private long _id;
    private String nombre;
    private int rating;
    private int service;
    private int prize;
    private String web;
    private String telefono;

    public Tienda(long _id, String nombre, int rating, int service, int prize, String web, String telefono) {
        this._id = _id;
        this.nombre = nombre;
        this.rating = rating;
        this.service = service;
        this.prize = prize;
        this.web = web;
        this.telefono = telefono;
    }

    public Tienda() {

    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRating() {
        return rating;
    }

    public int getService() {
        return service;
    }

    public int getPrize() {
        return prize;
    }

    public String getWeb() {
        return web;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setService(int service) {
        this.service = service;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
