package com.example.hola.shoppingapp;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hola.shoppingapp.model.Tienda;
import com.example.hola.shoppingapp.views.Valorationbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    long tienda_id;
    Tienda t; // la tienda actual en cada momento
    Valorationbar precio;
    Valorationbar servicio;

    public DetailFragment() {
    }


    public void setTienda_id(long tienda_id){
        this.tienda_id = tienda_id;
    }

    // Ejecutamos una animación sobre las valorationBar precio/servicio
    @Override
    public void onResume() {
        super.onResume();
        // El animador buscará el segundo parametro en la clase como "setValoracion"
        // La animación irá desde el tercer parametro hasta el cuarto
        // Se podría crear también un animación en un resource y hacer LoadAnimation
        ObjectAnimator animatorPrice = ObjectAnimator.ofFloat(precio,
                                                              "valoracion",
                                                              0.0f,
                                                              (float) t.getPrize());

        animatorPrice.setDuration(1000);
        ObjectAnimator animatorService = ObjectAnimator.ofFloat(servicio,
                "valoracion",
                0.0f,
                (float) t.getService());

        animatorService.setDuration(1000);
        animatorPrice.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorService.setInterpolator(new AccelerateInterpolator());
        animatorPrice.start();
        animatorService.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_detail, container, false);

        t = TiendasApplication.getInstance().getTiendasService().getTienda(tienda_id);
        // Si la vista si está inicializada, la reaprovecho
        TextView nombre = (TextView) view.findViewById(R.id.nombre_tienda);
        nombre.setText(t.getNombre());

        // Cambiamos precio y servicio para que pasen a ser nuestra barra custom (ValorationBar)
        precio = (Valorationbar) view.findViewById(R.id.precio_tienda);
        precio.setValoracion(t.getPrize());

        servicio = (Valorationbar) view.findViewById(R.id.servicio_tienda);
        servicio.setValoracion(t.getService());

        RatingBar valoracion = (RatingBar) view.findViewById(R.id.ratingBar);
        valoracion.setRating(t.getRating());

        Button web_button = (Button) view.findViewById(R.id.web_button);
        web_button.setOnClickListener(this);
        Button call_button = (Button) view.findViewById(R.id.call_button);
        call_button.setOnClickListener(this);

        Button edit_button = (Button) view.findViewById(R.id.edit_button);
        edit_button.setOnClickListener(this);

        if (t.getWeb() != null && !t.getWeb().isEmpty()){
            web_button.setVisibility(View.VISIBLE);
        }
        else{
            web_button.setVisibility(View.INVISIBLE);
        }

        if (t.getTelefono() != null && !t.getTelefono().isEmpty()){
            call_button.setVisibility(View.VISIBLE);
        }
        else{
            call_button.setVisibility(View.INVISIBLE);
        }

        return view;
    }


    // Esta es otra manera de recoger el evento de click de un botón, pero todos los eventos de click
    // van a esta función así que tenemos que ver que objeto nos ha generado el evento
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call_button:
                Log.i("DetailFragment", "Estás intentando llamar");
                // Creamos un Intent para que se abra la aplicación de teléfono del sistema
                // o la que elija el usuario, ya que es un Intent implicito
                // ACTION_VIEW abre la activity de llamar pero no llama
                Intent call = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+ t.getTelefono()));
                startActivity(call);
                break;
            case R.id.web_button:
                Log.i("DetailFragment", "Estás intentando abrir la web");
                Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(t.getWeb()));
                startActivity(web);
                break;

            case R.id.edit_button:
                Log.i("DetailFragment", "Abriendo una activity de edit");
                // Como estamos en fragment, tl primer objeto tiene que ser algo que contenga
                // el contexto (una Activity, un service..)
                Intent editar = new Intent(getActivity(), EditTienda.class);
                editar.putExtra(EditTienda.TIENDA_ID_EXTRA_KEY, tienda_id);
                startActivity(editar);
                break;
        }

    }
}
