package code.taxigp.com.usuariotaxigp1.LoginFirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import code.taxigp.com.usuariotaxigp1.MainUsuarios;

/**
 * Created by Asus on 16/10/2017.
 */

public class GestorPreferencias {
    private Context context;
    private SharedPreferences objDatosEnviar;
    private SharedPreferences.Editor objEditor;// = objDatosEnviar.edit();

    @SuppressLint("CommitPrefEdits")
    public GestorPreferencias(Context context)
    {   this.context = context;
        objDatosEnviar = PreferenceManager.getDefaultSharedPreferences(context);
          objEditor = objDatosEnviar.edit();

    }


    public boolean mtdEcribirPermisoUbicacion(int valor)
    {
        objEditor.putInt("permisoUbicacion", valor);
        objEditor.apply();


        return true;
    }

    public boolean mtdEscribirPreferenciasLogin(String nombre, String correo)
    {
        objEditor.putString("nombre", nombre);
        objEditor.putString("correo", correo);
        objEditor.apply();


        return true;
    }
    public boolean mtdEscribirPreferenciasServicio(String direccion, String telefono)
    {
        objEditor.putString("direccion", direccion);
        objEditor.putString("telefono", telefono);
        objEditor.apply();


        return true;
    }
    public boolean mtdLimipiarPrefernciasLogOut()
    {
        objEditor.putBoolean("logIn", false);
        objEditor.putString("nombre", "");
        objEditor.putString("correo", "");
        objEditor.putString("direccion", "");
        objEditor.putString("telefono", "");
        return true;
    }

    public boolean login(boolean logIn)
    {
        objEditor.putBoolean("logIn", logIn);
        objEditor.apply();
        return true;
    }

    public boolean asistente(boolean asistente)
    {
        objEditor.putBoolean("Asistente", asistente);
        objEditor.apply();
        return true;
    }

    public boolean borrarPreferenciaTutorisl(boolean guia)
    {
        objEditor.putBoolean("Guia", guia);
        objEditor.apply();
        return true;
    }



    public boolean isLogin()
    {
       SharedPreferences objDatos= PreferenceManager.getDefaultSharedPreferences(context);
       //Traemos los datos y los guardamos en las nuevas variables que creamos//

        return objDatos.getBoolean("logIn",false);
    }
    public int permisoUbicacion()
    {
        SharedPreferences objDatos= PreferenceManager.getDefaultSharedPreferences(context);
        //Traemos los datos y los guardamos en las nuevas variables que creamos//

        return objDatos.getInt("permisoUbicacion",2);
    }

    public DataUsuario datosUsuario()
    {
        SharedPreferences objDatos= PreferenceManager.getDefaultSharedPreferences(context);

        String nombre = objDatos.getString("nombre","");
        String correo = objDatos.getString("correo","");
        String direccion = objDatos.getString("direccion","");
        String telefono = objDatos.getString("telefono","");

        DataUsuario datosUsua = new DataUsuario();
        datosUsua.setNombre(nombre);
        datosUsua.setDireccion(direccion);
        datosUsua.setCorreo(correo);
        datosUsua.setTelefono(telefono);


        return datosUsua;
    }



}
