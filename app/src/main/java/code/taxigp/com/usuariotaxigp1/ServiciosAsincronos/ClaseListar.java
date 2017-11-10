package code.taxigp.com.usuariotaxigp1.ServiciosAsincronos;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

/**
 * Created by Asus on 11/7/2017.
 */

public class ClaseListar {
  private JSONArray json;

    public JSONArray mtdListar(String url, Context context) {

        StrictMode.ThreadPolicy Policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(Policy);


        HttpClient objCliente = new DefaultHttpClient();
        HttpContext objContext = new BasicHttpContext();
        HttpGet objGet = new HttpGet(url);
        try {
            HttpResponse objResponse = objCliente.execute(objGet, objContext);
            HttpEntity objEntidad = objResponse.getEntity();
            String resultado = EntityUtils.toString(objEntidad, "UTF-8");

            json= new JSONArray(resultado);



        } catch (Exception e) {

            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return json;
    }

}

