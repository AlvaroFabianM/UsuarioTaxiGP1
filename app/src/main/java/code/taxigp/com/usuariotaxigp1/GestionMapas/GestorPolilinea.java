package code.taxigp.com.usuariotaxigp1.GestionMapas;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import android.support.v7.app.AppCompatActivity.getResources();
//import com.example.asus.actividadbd.Act4Muicipios;
//import com.solutions.codesoftware.taxigp.MainActivity
//import com.example.asus.actividadbd.DirectionsJSONParser;

/**
 * Created by Asus on 9/7/2017.
 */
public class GestorPolilinea extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

    InterfaseGeneracionPolilinea listener;
public GestorPolilinea(InterfaseGeneracionPolilinea listener)
{
    this.listener=listener;
}

    public PolylineOptions lineOptions = null;
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try{
            jObject = new JSONObject(jsonData[0]);
            DirectionsJSONParser parser = new DirectionsJSONParser();

            routes = parser.parse(jObject);
        }catch(Exception e){
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points = null;
        MarkerOptions markerOptions = new MarkerOptions();

        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = result.get(i);

            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(9);
            lineOptions.color(Color.rgb(66,103,178));
        }
        if(lineOptions!=null) {
          /*Enviamos los datos de la poliliea a la main activity para dibujarla alla*/
            listener.abstractMtdRegistroResultado(lineOptions);

            /*
            MainUsuario.mMap.addPolyline(lineOptions);

            MarkerOptions marcadorDestino= new MarkerOptions();
            marcadorDestino.position(VerificarAceptacionServicio.posConductor);
            marcadorDestino.title(VerificarAceptacionServicio.INFO_CONDUCTOR[0]);
            //marcadorDestino.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_tax_gps));
            marcadorDestino.icon(BitmapDescriptorFactory.fromBitmap(MainUsuario.ic_conductor));
            MainUsuario.mMap.addMarker(marcadorDestino);
            MainUsuario.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainUsuario.userLocation, 14.5f));*/
            /*if(User.conductorLog!=0) {
                MainConductor.mMapCon.addPolyline(lineOptions);

            }
            else {
                MainActivity.mMap.addPolyline(lineOptions);
            }*/


            //Toast.makeText(Act4Muicipios.this, ""+lineOptions.toString(), Toast.LENGTH_SHORT).show();
        }
    }




}