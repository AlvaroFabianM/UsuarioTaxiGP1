package code.taxigp.com.usuariotaxigp1.ServiciosAsincronos;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjPlatosRestaurante;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjSitiosRestaurantes;

/**
 * Created by Asus on 9/7/2017.
 */

    public class TareaLecturaPlatosPorRestaurate extends AsyncTask<String, Void, Boolean> {
    private Context con;
    boolean doTask =false;//Avisa que se leyeron los productos
    private InterfaseLecturaPlatos listener;//Herencia del listees
    private List<ObjPlatosRestaurante> listaPlatos = new ArrayList<>();//Lista de todos los productos

    public TareaLecturaPlatosPorRestaurate(Context _con, InterfaseLecturaPlatos listener) {
        this.listener=listener;//igualemaos el listener a el creado desde el fragmente
        con=_con;//igualamos contextos

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
        protected Boolean doInBackground(String... sqlUrl) {
           try {
               ClaseListar objListar = new ClaseListar();
               JSONArray jsonProductos =  objListar.mtdListar(sqlUrl[0],con);
               /*Lectura de todos los productos segun la consulta enviada al instancias la tarea*/

               if(jsonProductos.length()>0)
               {
               for (int i = 0; i < jsonProductos.length(); i++) {

                   ObjPlatosRestaurante sitioDePaso = new ObjPlatosRestaurante();

                   sitioDePaso.setNombrePlato(jsonProductos.getJSONObject(i).getString("nomPlato"));
                   sitioDePaso.setDescripcionPlato(jsonProductos.getJSONObject(i).getString("descripPlato"));
                   sitioDePaso.setPrecio(jsonProductos.getJSONObject(i).getString("precio"));
                   sitioDePaso.setFotoPlato(jsonProductos.getJSONObject(i).getString("fotoPlato"));

                   listaPlatos.add(sitioDePaso);

               }
               doTask = true;//Se informa que la tarea se realizo
               }

           }
           catch (Exception e)
           {

           }

        return doTask;
        }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        //Si la tarea se realizo correctamente envia los productos y marcas
        //al listener en el fragment
        if(aBoolean)
        {
          listener.AbsMtdListaSitios(listaPlatos);
        }
        else
        {
            Toast.makeText(con, "Problema de conexión, inténtelo nuevamente", Toast.LENGTH_SHORT).show();
        }

    }

   /* public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }*/

    }

