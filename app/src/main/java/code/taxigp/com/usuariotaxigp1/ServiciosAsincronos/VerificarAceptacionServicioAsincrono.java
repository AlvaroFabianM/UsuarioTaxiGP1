package code.taxigp.com.usuariotaxigp1.ServiciosAsincronos;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import code.taxigp.com.usuariotaxigp1.GestionMapas.MedidorDistancias;
import code.taxigp.com.usuariotaxigp1.LoginFirebase.DataUsuario;
import code.taxigp.com.usuariotaxigp1.LoginFirebase.GestorPreferencias;

/**
 * Created by Asus on 9/7/2017.
 */
/*Esta clase va a leer posiciones todos los conductores activos y devuelve la lista de conductores, posiciones y ids*/
public class VerificarAceptacionServicioAsincrono extends AsyncTask<String, String, String> {

    private Context context;
    private InterfaseSolicitudServicio listener;
    private int idConductorCercano;
    DatosConductor datosCond = new DatosConductor();
    public VerificarAceptacionServicioAsincrono(Context context, int idConductorCercano, InterfaseSolicitudServicio listener)
    {
        this.context = context;
        this.idConductorCercano = idConductorCercano;
        this.listener=listener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
        protected String doInBackground(String... url) {
        String doTask="0";

        try
        {
        ClaseListar objListar = new ClaseListar();
        JSONArray json =  objListar.mtdListar("http://taxigp.com.co/Servicios/ConsultaAceptacionServicio.php?id="+idConductorCercano,context);
        if(!json.getJSONObject(0).getString("nomCon").equals("")&&
                !json.getJSONObject(0).toString().equals("[]")) {


            datosCond.nombre = json.getJSONObject(0).getString("nomCon");
            datosCond.celular =json.getJSONObject(0).getString("celCon");
            datosCond.placa =json.getJSONObject(0).getString("placa");
            datosCond.empresa =json.getJSONObject(0).getString("emp");
            datosCond.coordenadas =json.getJSONObject(0).getString("coorCon");//IMPORTANTE TRAE LAS COORDENADAS DEL CONDUCTOR
            //datosCond.nombre =json.getJSONObject(0).getString("coorUsu");//IMPORTANTE TRAE LAS COORDENADAS DEL USUARIO
            datosCond.idServicio =json.getJSONObject(0).getString("idServ");//ID DEL SERVICIO A ELIMINAR
            datosCond.urlFoto =json.getJSONObject(0).getString("urlFoto");//url foto conductor


            doTask="1";
        }
        else
        {
            doTask="0";
        }
        }catch (Exception e)
        {

        }

        return doTask;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /*Avisamos el resultado del proceso*/
           listener.abstractMtdRegistroResultado(result,datosCond);

        }


}

