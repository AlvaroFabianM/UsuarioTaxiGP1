package code.taxigp.com.usuariotaxigp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.InterfaseLecturaRestaurantes;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.InterfaseLugares;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.TareaLecturaRestaurantes;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.TareaLecturaSitios;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.AdaptadorListaRestaurantes;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.AdaptadorListaSitios;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjSitiosHotBarRec;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjSitiosRestaurantes;


public class Sitios extends AppCompatActivity {
    String tipoLugar;
    AdaptadorListaSitios adapter;
    AdaptadorListaRestaurantes adapterRes;
    ListView lista;
    List<ObjSitiosHotBarRec> listaSitiosPaso;
    List<ObjSitiosRestaurantes> listaSitiosPasoRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__sitios);

        if (this instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) this);
            if (activity.getSupportActionBar() != null)
                // activity.getSupportActionBar().setTitle();

                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lista = (ListView) findViewById(R.id.listaSitios);
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        Bundle extras =getIntent().getExtras();
        if (extras != null) {//ver si contiene datos
            tipoLugar = (String) extras.get("tipoSitios");
            Toast.makeText(this, ""+tipoLugar, Toast.LENGTH_SHORT).show();

        }
        InterfaseLecturaRestaurantes listenerRestaurantes = new InterfaseLecturaRestaurantes() {
            @Override
            public void AbsMtdListaSitios(List<ObjSitiosRestaurantes> listaSitios) {
                listaSitiosPasoRes = listaSitios;
                adapterRes = new AdaptadorListaRestaurantes(Sitios.this,listaSitios);
                lista.setAdapter(adapterRes);
            }
        };
        InterfaseLugares listenerLecturaSitios = new InterfaseLugares() {
            @Override
            public void AbsMtdListaSitios(List<ObjSitiosHotBarRec> listaSitios) {
                listaSitiosPaso = listaSitios;
                adapter = new AdaptadorListaSitios(Sitios.this,listaSitios);
                lista.setAdapter(adapter);


            }
        };
        TareaLecturaSitios tareaLecturaSitios = new TareaLecturaSitios(Sitios.this, listenerLecturaSitios);
        TareaLecturaRestaurantes tareaLecturaRestaurates = new TareaLecturaRestaurantes(Sitios.this, listenerRestaurantes);
        String urlSitios ="http://taxigp.com.co/Servicios/ListarLugar.php?tipoLugar=";
        switch (tipoLugar)
        {
            case"Hoteles":
                    tareaLecturaSitios.execute(urlSitios+"Hotel");
                break;
            case"Restaurantes":
                    tareaLecturaRestaurates.execute("http://taxigp.com.co/Servicios/ListarRestaurantes.php?");
            break;
            case"Bares":
                tareaLecturaSitios.execute(urlSitios+"Bar");
                break;
            case"Recreacion":
                tareaLecturaSitios.execute(urlSitios+"Recreación%20y%20Deporte");
                break;
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Sitios.this, ""+position, Toast.LENGTH_SHORT).show();
              /*Aqui redirecciona a actividad de detalles con base a la posicion del elemento clickeado*
              no llevamos en un extra la url de la foto principal y las 5 fotos adicionales, nombre del lugar
              descripcion, telefono y direccion/
               */
              String s;
              if(tipoLugar.equals("Hoteles")||tipoLugar.equals("Bares")||tipoLugar.equals("Recreacion")) {
                  s = (new Gson().toJson(listaSitiosPaso.get(position)));
                  startActivity(new Intent(Sitios.this, DetalleHotBarRec.class).putExtra("datos", s));
              }
              else if(tipoLugar.equals("Restaurantes"))
              {
                  s = (new Gson().toJson(listaSitiosPasoRes.get(position)));
                  startActivity(new Intent(Sitios.this, DetalleRestaurantes.class).putExtra("datos", s));
              }



            }
        });

        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().setTitle(tipoLugar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary2));
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---------------------------------------------------------
      //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



}
