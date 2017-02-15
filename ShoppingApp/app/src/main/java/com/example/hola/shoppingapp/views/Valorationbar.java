package com.example.hola.shoppingapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.hola.shoppingapp.R;

/**
 * Created by daa on 13/02/2017.
 */

// Extendemos TextView para crear una vista aprovechando un TextView
public class Valorationbar extends TextView {

    private float valoracion; // Guardamos la valoración de la tienda
    private int color2; // Guardamos los colores que serán parametros(propiedades) de nuestro TextView
    private int color4;
    private int color6;
    private int color8;
    private int color10;

    Paint paintText;
    Paint paintRect;

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;

        // Le pedimos al view que repinte nuestro Canvas
        invalidate();
        requestLayout();
    }

    public Valorationbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ValorationBarOptions, 0,0);
        color2 = a.getInt(R.styleable.ValorationBarOptions_color2, R.color.colorAccent);
        color4 = a.getInt(R.styleable.ValorationBarOptions_color4, R.color.colorAccent);
        color6 = a.getInt(R.styleable.ValorationBarOptions_color6, R.color.colorAccent);
        color8 = a.getInt(R.styleable.ValorationBarOptions_color8, R.color.colorAccent);
        color10 = a.getInt(R.styleable.ValorationBarOptions_color10, R.color.colorAccent);
        a.recycle(); // LLamamos a recycle para liberar los atributos de este stylable

        init();

    }

    private void init(){
        paintText=new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(getResources().getColor(R.color.colorPrimaryDark));
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(20);

        paintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    public void onDraw(Canvas canvas){

        float height = getMeasuredHeight();
        float width = getMeasuredWidth();
        int linecolor = color2;

        if(valoracion <=2) linecolor = color2;
        else if (valoracion <=4) linecolor = color4;
        else if (valoracion <=6) linecolor = color6;
        else if (valoracion <=8) linecolor = color8;
        else linecolor = color10;

        float linewidth =width*valoracion/10;
        paintRect.setColor(linecolor);
        paintRect.setStrokeWidth(linewidth);
        // Pintamos un linea del grosor del objeto desde el 0 y del tamaño variable según la
        // valoración de la tienda
        canvas.drawLine(0, height/2, linewidth, height/2, paintRect);

        float textSize=height*2/3;
        paintText.setTextSize(textSize);
        // Para pintar el texto centrado primero miramos cuanto va a ocupar
        Rect rect = new Rect();
        String texto = ""+valoracion;
        // calculamos el rectangulo que ocupa el recto
        paintText.getTextBounds(texto,0,texto.length(),rect);

        float textYpos = (height/2+ rect.height()/2);
        float textXpos = linewidth/2;
        // Para que pinte el texto hay que darle como coordenada Y la esquina inferior izquierda
        canvas.drawText(texto,textXpos,textYpos,paintText);

        super.onDraw(canvas);
    }
}
