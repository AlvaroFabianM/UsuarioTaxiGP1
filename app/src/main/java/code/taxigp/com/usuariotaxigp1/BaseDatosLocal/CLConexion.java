package code.taxigp.com.usuariotaxigp1.BaseDatosLocal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 13/9/2017.
 */

public class CLConexion {

    public void mtdRegistrar(Context context,String sqlComand)
    {
        try {
            //Instanciamos la clase de la base de datos para poder escribir en ella
            CLBaseDatos ObjBD = new CLBaseDatos(context, "BDLocal", null, 1);
            //Abrimos la bd en modo escritura
            SQLiteDatabase base = ObjBD.getWritableDatabase();
            //Escribimos los datos traidos en los campos de la base de datos
            base.execSQL(sqlComand);

            //Toast.makeText(context, "Datos registrados", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
           // Toast.makeText(context, "Error al registrar "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public List<String> mtdConsultar (Context context){

        CLBaseDatos objBD= new CLBaseDatos(context,"BDLocal",null,1);
        SQLiteDatabase base= objBD.getReadableDatabase();
        Cursor cr= base.rawQuery("select * from Historial", null);
        List<String> Listado = new ArrayList<String>();

        while (cr.moveToNext()){
            Listado.add(cr.getString(0)+"&"+cr.getString(1)+"&"+cr.getString(2)+"&"+cr.getString(3)+"&"+cr.getString(4)+"&"+cr.getString(5)+"&"+cr.getString(6)+"&"+cr.getString(7));

        }
        return Listado;
    }

}
