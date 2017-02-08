package com.example.hola.shoppingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hola.shoppingapp.model.Tienda;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    long tienda_id;

    public DetailFragment() {
    }


    public void setTienda_id(long tienda_id){
        this.tienda_id = tienda_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_detail, container, false);

        Tienda t = TiendasApplication.getInstance().getTiendasService().getTienda(tienda_id);
        // Si la vista si está inicializada, la reaprovecho
        TextView nombre = (TextView) view.findViewById(R.id.nombre_tienda);
        nombre.setText(t.getNombre());
        TextView precio = (TextView) view.findViewById(R.id.precio_tienda);
        precio.setText(String.valueOf(t.getPrize()));
        TextView servicio = (TextView) view.findViewById(R.id.servicio_tienda);
        servicio.setText(String.valueOf(t.getService()));
        RatingBar valoracion = (RatingBar) view.findViewById(R.id.ratingBar);
        valoracion.setRating(t.getRating());

        return view;
    }

}
