package com.example.hola.shoppingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hola.shoppingapp.model.Tienda;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListTiendasFragment extends Fragment {

    ListView list;

    public ListTiendasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_tiendas, container, false);

        list = (ListView) v.findViewById(R.id.fragment_list_view);


        // Creamos el adapter
        TiendasListAdapter adapter = new TiendasListAdapter();
        // Asociamos el Adapter a la ListView
        list.setAdapter(adapter);
        // Configuramos eventos de la ListView


        return v;
    }


    // Creamos el Adapter que nos permitirá asociar la ListView al objeto con las Tiendas
    // Hay Adapter sencillos para los casos más típicos, como el StringAdapter
    public class TiendasListAdapter extends BaseAdapter{

        List<Tienda> tiendas;

        public TiendasListAdapter(){
            tiendas = TiendasApplication.getInstance().getTiendasService().getAllTiendas();
        }

        @Override
        public int getCount() {
            return tiendas.size();
        }

        @Override
        public Object getItem(int position) {
            return tiendas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return tiendas.get(position).get_id();
        }

        // Getview se ejecuta cada vez que el firmware tiene que mostrar un elemento nuevo que
        // no se estaba mostrando hasta ahora
        // Si tenemos muchos elementos, podemos usar otros tipos de listas como Recyclelist
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){ // Si la vista no está inicializada
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView= inflater.inflate(R.layout.fragment_list_tiendas_item,parent,false);
            }
            // Si la vista si está inicializada, la reaprovecho
            TextView nombre = (TextView) convertView.findViewById(R.id.tienda_name);
            TextView precio = (TextView) convertView.findViewById(R.id.tienda_precio);
            TextView servicio = (TextView) convertView.findViewById(R.id.tienda_servicio);
            RatingBar valoracion = (RatingBar) convertView.findViewById(R.id.tienda_rating);

            Tienda t = (Tienda) getItem(position);
            nombre.setText(t.getNombre());
            precio.setText(String.valueOf(t.getPrize()));
            servicio.setText(String.valueOf(t.getService()));
            valoracion.setRating(t.getRating());

            return convertView;
        }



        // TODO: que pasa cuando cambian los datos?


    }

}
