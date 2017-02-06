package com.example.hola.holamundo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mRes_text; // Ponemos una m para definir que la variable es propia de la clase
    private TextView mPeso_text;
    private TextView mAltura_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPeso_text = (TextView) findViewById(R.id.text_peso);
        mPeso_text.setFocusable(true);
        mPeso_text.requestFocus();

        mAltura_text = (TextView) findViewById(R.id.text_altura);

        // Usamos final para definir una variable de manera global en todo el codigo
        // O también podemos definir el objeto como una variable de la clase
        // Así evitamos hacer findViewById que es más costoso
        mRes_text = (TextView) findViewById(R.id.text_result);
        mRes_text.setHint("Aquí va el resultado");


        Button button = (Button) findViewById(R.id.button_imc);
        button.setText("Calcula IMC");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mRes_text.setHint("Has clicado");
                double imc = 0;
                try {
                    double peso = Double.valueOf(mPeso_text.getText().toString());
                    double altura = Double.valueOf(mAltura_text.getText().toString());
                    imc = calculaIMC(peso, altura);
                } catch (NumberFormatException e) {

                }
                mRes_text.setText(String.format("%.2f",imc));
            }
        });

    }


    private double calculaIMC (double peso, double altura){
        double res = 0;
        if ( peso != 0 && altura != 0){
            res = peso/(altura*altura);
        }
        return res;
    }



}

