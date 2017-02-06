package com.example.hola.shoppingapp.service;

import com.example.hola.shoppingapp.model.Tienda;

import java.util.List;

/**
 * Created by daa on 06/02/2017.
 */


// Usamos el patrón interface para poder cambiar la capa de persistencia más facilmente
// en un futuro.
public interface TiendasService {

    public List<Tienda> getAllTiendas();
    public Tienda getTienda(long id);
    public void removeTienda(long id);
    public void saveTienda(Tienda t);

}
