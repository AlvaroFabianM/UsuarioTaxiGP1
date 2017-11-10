package code.taxigp.com.usuariotaxigp1;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import code.taxigp.com.usuariotaxigp1.BaseDatosLocal.CLBaseDatos;
import code.taxigp.com.usuariotaxigp1.BaseDatosLocal.CLConexion;
import code.taxigp.com.usuariotaxigp1.R;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ListViewAdapter;

public class Historial extends AppCompatActivity {

    ListViewAdapter adapter;
    FloatingActionButton fbDelete;
    ListView lista;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

try {
    setContentView(R.layout.activity_historial);

    if (this instanceof AppCompatActivity) {
        AppCompatActivity activity = ((AppCompatActivity) this);
        if (activity.getSupportActionBar() != null)
            // activity.getSupportActionBar().setTitle();

            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
        } catch (Exception e) {
            Toast.makeText(this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        context = this;

        fbDelete = (FloatingActionButton)findViewById(R.id.fbDeleteHist);

        //Llamamos al metodo que realiza la consulta a la base de datos
        mtdConsultaHistorial();

        fbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtdConfirmarDelete();
            }
        });
    }

    private void mtdConsultaHistorial() {
        try {
            CLConexion objConexion = new CLConexion();
            List<String> consulta = new ArrayList<String>();
            consulta = objConexion.mtdConsultar(Historial.this);

            Object[] CONDUCTORES = consulta.toArray();

            if (CONDUCTORES.length > 0) {
                String[] foto, nom, cel, fec, placa, num;
                int[] emp;

                foto = new String[CONDUCTORES.length];
                nom = new String[CONDUCTORES.length];
                cel = new String[CONDUCTORES.length];
                fec = new String[CONDUCTORES.length];
                num = new String[CONDUCTORES.length];
                placa = new String[CONDUCTORES.length];
                emp = new int[CONDUCTORES.length];

                for (int i = 0; i < CONDUCTORES.length; i++) {
                    String[] paso = CONDUCTORES[i].toString().split("&");

                    foto[i] = paso[1];
                    nom[i] = paso[2];
                    cel[i] = paso[3];
                    fec[i] = paso[4];
                    num[i] = paso[5];
                    placa[i] = paso[6];
                    emp[i] = Integer.parseInt(paso[7]);

                }

                lista = (ListView) findViewById(R.id.lvHistorial);
                adapter = new ListViewAdapter(Historial.this, foto, nom, cel, fec, num, placa, emp);
                lista.setAdapter(adapter);
            }
        }catch (Exception e)
        {
            Toast.makeText(context, "No hay registros", Toast.LENGTH_SHORT).show();
        }
    }

    private void mtdConfirmarDelete() {
        try {
            final AlertDialog.Builder constructorDialogo = new AlertDialog.Builder(context);
            constructorDialogo.setTitle(R.string.str_informacion_importante);
            constructorDialogo.setMessage(R.string.str_sms_delete);
            constructorDialogo.setPositiveButton(R.string.str_borrar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String sql;

                    try {

                        sql = "delete from Historial";
                        CLConexion objHistorial = new CLConexion();

                        objHistorial.mtdRegistrar(context,sql);

                    }catch (Exception e){

                    }
                    Toast.makeText(context, "Historial eliminado", Toast.LENGTH_SHORT).show();
                    lista.setAdapter(null);

                }
            }).setNegativeButton(R.string.str_cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setIcon(R.drawable.img_taxi_fbutton_am).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    // System.exit(0);
                }
            });
            constructorDialogo.show();

        }catch (Exception e)
        {
            //En caso que se salga de contexto XD..
            Toast.makeText(context, "Problema al eliminar historial", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
