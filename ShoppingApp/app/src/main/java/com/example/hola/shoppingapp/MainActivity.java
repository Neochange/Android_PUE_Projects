package com.example.hola.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.xml.sax.helpers.XMLReaderAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListTiendasFragment.OnListItemFragmentListener{

    // Guardamos una variable con la animación cargada para usarla cada vez
    Animation remarkButtonAnimation;
    FloatingActionButton fab;

    // Tendriamos una url diferente en función de ser la app de release o de debug
    String URL = BuildConfig.SERVER_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cargamos el objeto Animation para que se hagan las modificaciones al hacer click
        remarkButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.beat);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remarkButton();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Llenamos el fragment principal con el fragment de la lista
        FragmentManager fman = getSupportFragmentManager();
        FragmentTransaction ftrans = fman.beginTransaction();
        // Usamos Replace en vez de add porque a cada giro de orientación vuelve a generar el
        // layout y metia un fragment encima de otro
        ftrans.replace(R.id.list_fragment, new ListTiendasFragment());
        ftrans.commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("MainActivity", "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.backup_menu_button){
            // Si se selecciona el menu de backUp, abrimos un menu conceptual para seleccionar
            // el tipo de backup y gestionamos el evento de click en ese submenu
            /*
            PopupMenu menu = new PopupMenu(this,findViewById(R.id.toolbar));
            menu.getMenuInflater().inflate(R.menu.backup_menu,menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    boolean result = false;
                    switch (item.getItemId()){
                        case R.id.backup_menu_cloud:
                            Log.i("onOptionsItemSelected", "Cloud backup selected");
                            if(item.isChecked()){
                                item.setChecked(false);
                            }
                            else{
                                item.setChecked(true);
                            }
                            // TODO guardar la preferencia de usuario
                            result = true;
                            break;
                        case R.id.backup_menu_sd:
                            Log.i("onOptionsItemSelected", "SD backup selected");
                            if(item.isChecked()){
                                item.setChecked(false);
                            }
                            else{
                                item.setChecked(true);
                            }
                            // TODO guardar la preferencia de usuario
                            result = true;
                            break;
                    }
                    return result; // Devolvemos true si hemos gestionado el menu correctamente
                }
            });
            menu.show();
             */

            Intent i = new Intent(this, MenuFrecuencia.class);
            startActivity(i);

        }
        else if(id == R.id.show_map_menu_button){
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListItemClick(long tienda_id) {
        DetailFragment detailFrag = new DetailFragment();
        detailFrag.setTienda_id(tienda_id);
        if( findViewById(R.id.detail_fragment) == null){
            // Llenamos el fragment principal con el fragment de detalle
            // Solo veriamos un fragment en cada momento
            FragmentManager fman = getSupportFragmentManager();
            FragmentTransaction ftrans = fman.beginTransaction();
            // Lo añadimos al BackStack para que cuando se aprete atrás, se vuelva a la ventana
            // anterior y no se salga de la app
            ftrans.replace(R.id.list_fragment, detailFrag).addToBackStack(null);
            ftrans.commit();
        }
        else{
            // Si tenemos el fragment de detalle lo rellenamos
            FragmentManager fman = getSupportFragmentManager();
            FragmentTransaction ftrans = fman.beginTransaction();

            ftrans.replace(R.id.detail_fragment, detailFrag);
            ftrans.commit();
        }
    }

    private void remarkButton(){
        fab.startAnimation(remarkButtonAnimation);
    }

}
