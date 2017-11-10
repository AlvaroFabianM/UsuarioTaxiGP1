package code.taxigp.com.usuariotaxigp1.GestionMapas;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Asus on 9/7/2017.
 */

/*Clase para estructurar el url con el cual obtenemos las direcciones del web service de google*/
public class GestorDirecciones {
    public GestorDirecciones() {
    }

    //-------------------Metodo obtener direccioes------------------------------------
    public String obtenerDireccionesURL(LatLng origin, LatLng dest){

        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        String sensor = "sensor=false";

        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
        //retorna la url para las direcciones del servicio google
    }

}
