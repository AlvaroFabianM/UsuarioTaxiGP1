package code.taxigp.com.usuariotaxigp1.ServiciosAsincronos;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Asus on 10/7/2017.
 */

public class ClaseRegistrar {



    public String mtdRegistrar(String url, Context context)
    {
        String verificacionEnvio ="0";
        StrictMode.ThreadPolicy Policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(Policy);

        //String url = "http://192.168.0.9:8080/serviciomoviles/registar.php?nombre="+nombre2+"&apellido="+apellido2+"&celular="+celular2+"";
        url=url.replace(" ","%20");
        //Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        try {
            HttpClient objCliente = new DefaultHttpClient();
            HttpGet objGet = new HttpGet(url);
            ResponseHandler<String> objHandler = new BasicResponseHandler();
            verificacionEnvio = objCliente.execute(objGet, objHandler);
          //  Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


return  verificacionEnvio;
    }



}
