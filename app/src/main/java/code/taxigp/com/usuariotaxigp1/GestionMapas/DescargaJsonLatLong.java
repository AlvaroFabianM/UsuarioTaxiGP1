package code.taxigp.com.usuariotaxigp1.GestionMapas;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Asus on 9/7/2017.
 */

    //------------------------Aqui lo que hacemos es conectar con el web service y
// obtener el resultado, que cabe recalcar lo solicitamos en formato JSON, despues
// mandamos llamar una tarea asincrona para interpretar el resultado, mismo que le pasamos como parametro.
    public class DescargaJsonLatLong extends AsyncTask<String, Void, String> {
InterfaseGeneracionPolilinea listener;
    public DescargaJsonLatLong(InterfaseGeneracionPolilinea listener)
    {
        this.listener= listener;
    }
        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try{
                DescargaUrl descargaUrl = new DescargaUrl();
                data = descargaUrl.downloadUrl(url[0]);
            }catch(Exception e){
                //Toast.makeText(DownloadTask.this, "", Toast.LENGTH_SHORT).show();
                 Log.d("ERROR AL OBTENER INFO",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            GestorPolilinea parserTask = new GestorPolilinea(listener);

            parserTask.execute(result);

            // parserTask.mtdtrazarRutas(mMap);
        }
    }

