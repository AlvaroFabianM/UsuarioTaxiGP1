package code.taxigp.com.usuariotaxigp1.ServiciosAsincronos;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjSitiosHotBarRec;

/**
 * Created by Asus on 9/7/2017.
 */

    public class TareaLecturaSitios extends AsyncTask<String, Void, Boolean> {
    private Context con;
    boolean doTask =false;//Avisa que se leyeron los productos
    private InterfaseLugares listener;//Herencia del listees
    private List<ObjSitiosHotBarRec> listaSitios = new ArrayList<>();//Lista de todos los productos
    private List<ObjSitiosHotBarRec> datos;

    public TareaLecturaSitios(Context _con,InterfaseLugares listener) {
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

                   ObjSitiosHotBarRec sitioDePaso = new ObjSitiosHotBarRec();

                   sitioDePaso.setNombre(jsonProductos.getJSONObject(i).getString("nombre"));
                   sitioDePaso.setDescripcion(jsonProductos.getJSONObject(i).getString("descripcion"));
                   sitioDePaso.setTelefono(jsonProductos.getJSONObject(i).getString("telefono"));
                   sitioDePaso.setDireccion(jsonProductos.getJSONObject(i).getString("direccion"));
                   sitioDePaso.setUrlFotoPrincipal(jsonProductos.getJSONObject(i).getString("fotoPrincipal"));
                   sitioDePaso.setCoordenadas(jsonProductos.getJSONObject(i).getString("coordenadas"));
                   sitioDePaso.setTipoLugar(jsonProductos.getJSONObject(i).getString("tipoLugar"));
                  // boolean vip = Boolean.valueOf(jsonProductos.getJSONObject(i).getString("vip"));
                   sitioDePaso.setFotoUno(jsonProductos.getJSONObject(i).getString("fotoUno"));
                   sitioDePaso.setFotoDos(jsonProductos.getJSONObject(i).getString("fotoDos"));
                   sitioDePaso.setFotoTres(jsonProductos.getJSONObject(i).getString("fotoTres"));
                   sitioDePaso.setFotoCuatro(jsonProductos.getJSONObject(i).getString("fotoCuatro"));
                   sitioDePaso.setFotoCinco(jsonProductos.getJSONObject(i).getString("fotoCinco"));
                   sitioDePaso.setSuscripcion(true);

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

