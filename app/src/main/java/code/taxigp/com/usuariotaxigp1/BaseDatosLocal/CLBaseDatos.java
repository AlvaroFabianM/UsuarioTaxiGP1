package code.taxigp.com.usuariotaxigp1.BaseDatosLocal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Asus on 13/9/2017.
 */

public class CLBaseDatos extends SQLiteOpenHelper {
    public CLBaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {

        //Crear tabla Historial
        bd.execSQL("CREATE TABLE Historial (\n" +
                "    idHistorial INTEGER       PRIMARY KEY AUTOINCREMENT,\n" +
                "    Foto        VARCHAR (200),\n" +
                "    Nombre      VARCHAR (20),\n" +
                "    Celular     VARCHAR (10),\n" +
                "    FechaHora   VARCHAR (20),\n" +
                "    NumeroMovil VARCHAR (4),\n" +
                "    Placa       VARCHAR (4),\n" +
                "    EmpresaTaxi INTEGER (2) \n" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {

    }
}