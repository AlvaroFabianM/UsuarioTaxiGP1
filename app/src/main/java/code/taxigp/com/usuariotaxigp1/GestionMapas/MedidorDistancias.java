package code.taxigp.com.usuariotaxigp1.GestionMapas;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.ClaseListar;

/**
 * Created by Asus on 15/7/2017.
 */

public class MedidorDistancias {
    private Context context;
    private String [] posicionIdConductor = new String[2];//variable paso para comparar distancias
    public static Double tiempo;




    public int conductorCercanoPosicion=0;
    private LatLng posicionUsuarioLatLon;
    
    public MedidorDistancias(Context context, LatLng posicionUsuarioLatLon) {
        this.context=context;
        this.posicionUsuarioLatLon = posicionUsuarioLatLon;

    }

    public void mtdCicloComparadorDistancias()
    {
         LatLng posicionConductorInd;//variable de paso para enviar a metodo comparador
        try {

          ClaseListar objListar = new ClaseListar();
           JSONArray json =  objListar.mtdListar("http://taxigp.com.co/Servicios/ListarPosicionesDinamicas.php?",context);
            if(!json.toString().equals("[]")) {//si el Json no viene vacio

                for(int i=0; i<json.length();i++)
                {
                    posicionIdConductor[0]=json.getJSONObject(i).getString("Posicion");
                    posicionIdConductor[1]=json.getJSONObject(i).getString("idTaxista");

                    String[]latlon = posicionIdConductor[0].split(",");

                    posicionConductorInd = new LatLng(Double.parseDouble(latlon[0]),Double.parseDouble(latlon[1]));
                    int idTaxista=Integer.parseInt(posicionIdConductor[1]);

                    conductorCercanoPosicion=distanciaCoord(posicionConductorInd,posicionUsuarioLatLon,idTaxista);
                }

            }

        }catch (Exception e){
             //Toast.makeText(context, "Error de conexión "+e.getMessage(), Toast.LENGTH_SHORT).show();
           conductorCercanoPosicion=-404;//significa error de internet
        }
    }


    private int distanciaCoord(LatLng usuario, LatLng contextductor, int idConducCercano) {
        double distancia =0;
        try {
            double radioTierra = 6371;//en kilómetros
            double dLat = Math.toRadians(contextductor.latitude - usuario.latitude);
            double dLng = Math.toRadians(contextductor.longitude - usuario.longitude);
            double sindLat = Math.sin(dLat / 2);
            double sindLng = Math.sin(dLng / 2);
            double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                    * Math.cos(Math.toRadians(usuario.latitude)) * Math.cos(Math.toRadians(contextductor.latitude));
            double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
            distancia = radioTierra * va2;




        }catch (Exception e)
        {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        return  idConductorCercano(idConducCercano,distancia);


    }
    
    private double distanciaMemo=0;
    private boolean referencia = false;//hace que se tome la primera posicion como referencia
    private int contextductorCercano = 0;
    private int idConductorCercano(int idConductorCercano, double distancia)
    {
        final double velocidadConstantePorMinuto = 180;
        if(!referencia)
        {
            distanciaMemo=distancia;//se guarda la distancia en memoria
            contextductorCercano = idConductorCercano;
            tiempo = (distanciaMemo*1000)/velocidadConstantePorMinuto;
            referencia=true;

        }
        else if(distancia<distanciaMemo)
        {
            distanciaMemo=distancia;//se guarda la distancia en memoria
            contextductorCercano = idConductorCercano;
           // tiempoDistancia[0]=String.valueOf(distanciaMemo);
            tiempo = (distanciaMemo*1000)/velocidadConstantePorMinuto;
        }
        if(tiempo<1)//Si el tiempo d llegada es menor a un minuto el tiempo estimado es igaul a 1
        {
            tiempo=1d;
        }


        return contextductorCercano;
    }
    public int getConductorCercanoPosicion() {
        return conductorCercanoPosicion;
    }
}
