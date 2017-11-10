package code.taxigp.com.usuariotaxigp1.ServiciosAsincronos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import code.taxigp.com.usuariotaxigp1.GestionMapas.MedidorDistancias;
import code.taxigp.com.usuariotaxigp1.LoginFirebase.DataUsuario;
import code.taxigp.com.usuariotaxigp1.LoginFirebase.GestorPreferencias;

/**
 * Created by Asus on 9/7/2017.
 */
/*Esta clase va a leer posiciones todos los conductores activos y devuelve la lista de conductores, posiciones y ids*/
public class SolicitudServicioAsincrono extends AsyncTask<String, String, String> {

    private Context context;
    private LatLng posicionUsuario;
    private InterfaseRegistroAsincrono listener;
    private int conductorMasCercano;
    public SolicitudServicioAsincrono(Context context, LatLng posicionUsuario, InterfaseRegistroAsincrono listener)
    {
        this.context = context;
        this.posicionUsuario = posicionUsuario;
        this.listener=listener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
        protected String doInBackground(String... url) {
        /*Leemos las preferencia de datos del usuario*/
        String doTask="0";
        GestorPreferencias objPreferencias = new GestorPreferencias(context);
        DataUsuario datosUsuario = objPreferencias.datosUsuario();

        /*Ejecutamos el medidor de distancias el cual lee todas las posiciones de los conductores activos y devuelve al mas cercano*/
        MedidorDistancias objMedidor = new MedidorDistancias(context, posicionUsuario);
        objMedidor.mtdCicloComparadorDistancias();
        conductorMasCercano = objMedidor.conductorCercanoPosicion;

        /*Condicionamos segun el resultado de la busqueda*/
        switch (conductorMasCercano)
        {
            case -404://Error de internet
                doTask="-404";
            break;
            case 0://Sin conductores cerca
                doTask="0";
                break;
        }
 //FALTA ENVIAR CORREO EN CASO DE BLOQUEAR
        if(conductorMasCercano!=0&&conductorMasCercano!=-404) {
            try {
                String URL;
                URL = "http://taxigp.com.co/Servicios/RegistrarServicio.php?nomusu=" + datosUsuario.getNombre() + "&direccion=" + datosUsuario.getDireccion() + "&coorusu=" + posicionUsuario.latitude + "," + posicionUsuario.longitude + "&idtaxista=" + conductorMasCercano + "";
                ClaseRegistrar objRegistro = new ClaseRegistrar();
                doTask = objRegistro.mtdRegistrar(URL, context);
            }catch (Exception e)
            {
                Toast.makeText(context, "Problema de conexión", Toast.LENGTH_SHORT).show();
            }

        }

        return doTask;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /*Avisamos el resultado del proceso*/
           listener.abstractMtdRegistroResultado(result,conductorMasCercano);

        }


}

