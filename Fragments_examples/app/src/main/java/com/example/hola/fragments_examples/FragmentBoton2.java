package com.example.hola.fragments_examples;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBoton2 extends Fragment {

    private OnFragment2InteractionListener mListener;

    public FragmentBoton2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fragment_boton2, container, false);

        Button button2 = (Button) v.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Fragment2", "Boton 2 clicado");

                // Como tengo que comunicarme con la Activity para modificar algo fuera
                // del fragment tengo que usar una interface
                mListener.onButton2Clicked();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragment2InteractionListener) {
            mListener = (OnFragment2InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragment2InteractionListener {
        // TODO: Update argument type and name
        void onButton2Clicked();
    }
}
