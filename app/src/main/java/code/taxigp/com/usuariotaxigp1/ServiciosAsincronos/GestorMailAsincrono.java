package code.taxigp.com.usuariotaxigp1.ServiciosAsincronos;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import code.taxigp.com.usuariotaxigp1.R;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Asus on 11/9/2017.
 */

public class GestorMailAsincrono extends AsyncTask<Void, Void, String> {

    private Session session;//Se utiliza para iniciar una sesion de manera abstracta en gmail
   // private String correo,contrasena;//Correo y contraseña de quien envia el correo
    private Context con;
    private String contenido;
    private String asunto;

    public GestorMailAsincrono(Context _con, String _contenido, String _asunto) {
        con =_con;
        contenido =_contenido;
        asunto = _asunto;
        ///correo="correoCamara";
        //contrasena="contrasenaCamara";
    }



    @Override
    protected String doInBackground(Void... voids) {

        String doTask = "0";
        StrictMode.ThreadPolicy  policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host","smtp.googlemail.com");
        propiedades.put("mail.smtp.socketFactory.port","465");
        propiedades.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        propiedades.put("mail.smtp.auth","true");
        propiedades.put("mail.smtp.port","465");

        try
        {
            session = Session.getDefaultInstance(propiedades, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("app@camarasogamoso.org","ccs0g4m0s02017");
                }
            });
            if(session!=null)
            {

                javax.mail.Message mensaje = new MimeMessage(session);
                mensaje.setFrom(new InternetAddress("app@camarasogamoso.org"));
                mensaje.setSubject(asunto);//
               // mensaje.setRecipients(javax.mail.Message.RecipientType.TO,InternetAddress.parse("alvarof1034@gmail.com"));
                 mensaje.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse("empresarial@camarasogamoso.org"));

                mensaje.setContent(contenido,"text/html; charset=utf-8");
                //mtdCompararCorreoEnDB(txtNuevoMail.getText().toString());

                Transport.send(mensaje);
              doTask = "1";





            }
            else
            {
               // Toast.makeText(con, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
          //  Toast.makeText(con, "Verifique su dirección de Email"+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return doTask;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("1"))
        {
            mtdMostrarConfirmacion(asunto);
        }
        else
        {
            Toast.makeText(con, "No se realizo la inscripción, revise su conexión a internet", Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(s);
    }

    private void mtdMostrarConfirmacion(String asunto) {
        try {
            final AlertDialog.Builder constructorDialogo = new AlertDialog.Builder(con);
            constructorDialogo.setTitle("¡Registro Exitoso!");
            constructorDialogo.setMessage(asunto);
            constructorDialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setIcon(R.drawable.img_taxi).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    // System.exit(0);
                }
            });
            constructorDialogo.show();

        }catch (Exception e)
        {
            //En caso que se salga de contexto XD..
            Toast.makeText(con, "¡Registro Exitoso!", Toast.LENGTH_SHORT).show();
        }
    }
}
