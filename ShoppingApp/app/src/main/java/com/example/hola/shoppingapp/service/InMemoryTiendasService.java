package com.example.hola.shoppingapp.service;

import com.example.hola.shoppingapp.model.Tienda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by daa on 06/02/2017.
 */

public class InMemoryTiendasService implements TiendasService {

    private List<Tienda> listaTiendas = new ArrayList<>();

    public InMemoryTiendasService() {
        Tienda t = new Tienda();
        t.set_id(getNextId());
        t.setNombre("Tienda 1");
        t.setRating(5);
        t.setService(7);
        t.setTelefono("34534535");
        t.setPrize(3);
        t.setWeb("http://www.");
        listaTiendas.add(t);

        t = new Tienda();
        t.set_id(getNextId());
        t.setNombre("Tienda 2");
        t.setRating(3);
        t.setService(5);
        t.setTelefono("123123");
        t.setPrize(5);
        t.setWeb("http://www.");
        listaTiendas.add(t);

        t = new Tienda();
        t.set_id(getNextId());
        t.setNombre("Tienda 3");
        t.setRating(1);
        t.setService(7);
        t.setTelefono("1236564654");
        t.setPrize(10);
        t.setWeb("http://www.");
        listaTiendas.add(t);

        for(int i=0; i< 50; i++){
            t = new Tienda();
            t.set_id(getNextId());
            t.setNombre("Tienda " + i);
            t.setRating(i%5);
            t.setService(7%10);
            t.setTelefono("1236564654");
            t.setPrize((i+30)%10);
            t.setWeb("http://www.google.es");
            listaTiendas.add(t);
        }
    }

    @Override
    public List<Tienda> getAllTiendas() {
        return new ArrayList<>(listaTiendas);
    }

    @Override
    public Tienda getTienda(long id) {
        for(Tienda t:listaTiendas){
            if(t.get_id()==id) return t;
        }
        return null;
    }

    @Override
    public void removeTienda(long id) {
        for(Iterator<Tienda> iter=listaTiendas.iterator(); iter.hasNext();){
            Tienda t = iter.next();
            if(t.get_id()==id) iter.remove();
        }
    }

    @Override
    public void saveTienda(Tienda t) {
        t.set_id((getNextId()));
        listaTiendas.add(t);
    }

    private long lastId = 0L;
    private synchronized long getNextId(){
        return ++lastId;
    }
}
