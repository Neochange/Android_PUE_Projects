package com.example.hola.shoppingapp;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hola.shoppingapp.model.Tienda;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListTiendasFragment extends Fragment {

    ListView list;
    private OnListItemFragmentListener mListener;

    public ListTiendasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( context instanceof OnListItemFragmentListener){
            mListener = (OnListItemFragmentListener)context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_tiendas, container, false);

        list = (ListView) v.findViewById(R.id.fragment_list_view);


        // Creamos el adapter
        final TiendasListAdapter adapter = new TiendasListAdapter();
        // Asociamos el Adapter a la ListView
        list.setAdapter(adapter);
        // Configuramos eventos de la ListView

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ListFragment", "Clicked on the id: " + id);
                mListener.onListItemClick(id);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            // Hemos añadido final al long de id para decir a los callback Onclick
            // que ese valor no va a cambiar
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setTitle(R.string.BorrarTienda_title);
                alertBuilder.setMessage(R.string.BorrarTienda_message);
                alertBuilder.setNegativeButton(R.string.BorrarTienda_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertBuilder.setPositiveButton(R.string.BorrarTienda_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Borramos la tienda con id
                        TiendasApplication.getInstance().getTiendasService().removeTienda(id);
                        // Avisamos al adapter que los datos han cambiado
                        // Esta función la hemos sobrescrito en nuestro adapter
                        adapter.notifyDataSetChanged();
                        Toast myToast = Toast.makeText(getActivity(),
                                                        R.string.list_fragment_delete_ok_message,
                                                        Toast.LENGTH_LONG);
                        myToast.show();
                        // dialog.cancel();

                    }
                });

                alertBuilder.show();
                return true; // Indicamos con este boolean que hemos usado el evento para el
                            // propósito que lo necesitabamos, si por ejemplo el Item tiene
                            // cosas dentro, con false el evento se propagaria hacia abajo
            }
        });

        return v;
    }


    public interface OnListItemFragmentListener{
        void onListItemClick(long tienda_id);
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

        // Sobreescribimos esta función para cuando los datos hayan cambiado
        @Override
        public void notifyDataSetChanged(){
            tiendas = TiendasApplication.getInstance().getTiendasService().getAllTiendas();
            super.notifyDataSetChanged();
        }



    }



}
