package code.taxigp.com.usuariotaxigp1.ServiciosAsincronos;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjSitiosHotBarRec;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjSitiosRestaurantes;

/**
 * Created by Asus on 9/7/2017.
 */

    public class TareaLecturaRestaurantes extends AsyncTask<String, Void, Boolean> {
    private Context con;
    boolean doTask =false;//Avisa que se leyeron los productos
    private InterfaseLecturaRestaurantes listener;//Herencia del listees
    private List<ObjSitiosRestaurantes> listaSitios = new ArrayList<>();//Lista de todos los productos

    public TareaLecturaRestaurantes(Context _con, InterfaseLecturaRestaurantes listener) {
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

                   ObjSitiosRestaurantes sitioDePaso = new ObjSitiosRestaurantes();

                   sitioDePaso.setNombre(jsonProductos.getJSONObject(i).getString("nombre"));
                   sitioDePaso.setDescripcion(jsonProductos.getJSONObject(i).getString("descripcion"));
                   sitioDePaso.setTelefono(jsonProductos.getJSONObject(i).getString("telefono"));
                   sitioDePaso.setDireccion(jsonProductos.getJSONObject(i).getString("direccion"));
                   sitioDePaso.setUrlFotoPrincipal(jsonProductos.getJSONObject(i).getString("fotoPrincipal"));
                   sitioDePaso.setCoordenadas(jsonProductos.getJSONObject(i).getString("coordenadas"));
                   sitioDePaso.setSuscripcion(Boolean.valueOf(jsonProductos.getJSONObject(i).getString("suscripcion")));
                   sitioDePaso.setIdRestaurante(jsonProductos.getJSONObject(i).getString("idRestaurante"));
                   sitioDePaso.setColorCoorporativo(jsonProductos.getJSONObject(i).getString("colorCorporativo"));
                   sitioDePaso.setFechaPago(jsonProductos.getJSONObject(i).getString("fechaPago"));


                   listaSitios.add(sitioDePaso);

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
          listener.AbsMtdListaSitios(listaSitios);
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

