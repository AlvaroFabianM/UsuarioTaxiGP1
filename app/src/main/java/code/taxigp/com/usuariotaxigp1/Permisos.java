package code.taxigp.com.usuariotaxigp1;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import code.taxigp.com.usuariotaxigp1.LoginFirebase.GestorPreferencias;
//import code.taxigp.com.usuariotaxigp2017.R;

public class Permisos extends AppCompatActivity {
    public static int AUTORIZACION_UBICACION = 0;
    public static int SIN_AUTORIZACION_UBICACION = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos);

        findViewById(R.id.btnAceptarPermisos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtdSolicitudPermisos();
            }
        });
    }



    public void mtdSolicitudPermisos()
    {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        else{
            startActivity(new Intent(Permisos.this,MainUsuarios.class));
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*Verificacion de aceptacion de permisos y escritura en preferencias*/
        GestorPreferencias objPreferencias = new GestorPreferencias(Permisos.this);
        if(grantResults[0]==AUTORIZACION_UBICACION)
        {
            objPreferencias.mtdEcribirPermisoUbicacion(AUTORIZACION_UBICACION);
            startActivity(new Intent(Permisos.this,MainUsuarios.class));
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            finish();
        }
        else if(grantResults[0]==SIN_AUTORIZACION_UBICACION)
        {
mtdSinPermisos();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void mtdSinPermisos()
    {   final GestorPreferencias objPreferencias = new GestorPreferencias(Permisos.this);
        final AlertDialog.Builder constructorDialogo = new AlertDialog.Builder(this);
        constructorDialogo.setTitle("¡Información importante!");
        constructorDialogo.setMessage("Si no acepta los permisos no tendrá acceso a todas las funciones de la aplicación aunque puede solicitar el servicio solo suministrando su dirección");
        constructorDialogo.setPositiveButton("Aceptar Permisos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(Permisos.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            }
        }).setNegativeButton("Continuar sin permisos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                objPreferencias.mtdEcribirPermisoUbicacion(SIN_AUTORIZACION_UBICACION);
                startActivity(new Intent(Permisos.this,MainUsuarios.class));
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                finish();
            }
        }).setIcon(R.drawable.img_taxi_fbutton_am).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // System.exit(0);
            }
        });
        constructorDialogo.show();

    }
}
