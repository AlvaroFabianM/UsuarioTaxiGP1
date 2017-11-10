package code.taxigp.com.usuariotaxigp1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.GestorMailAsincrono;
import ru.katso.livebutton.LiveButton;

//import code.taxigp.com.usuariotaxigp2017.R;

public class BuzonSugerencias extends AppCompatActivity {
    EditText nombre,correo,asunto,contenido;

    LiveButton btnEnviar;
    String cont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_buzon_sugerencias);
        if (this instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) this);
            if (activity.getSupportActionBar() != null)
                // activity.getSupportActionBar().setTitle();

                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        nombre = (EditText) findViewById(R.id.txtNom);
        correo = (EditText) findViewById(R.id.txtCorreo);
        asunto = (EditText) findViewById(R.id.txtAsunto);
        contenido = (EditText) findViewById(R.id.txtContenido);

        btnEnviar = (LiveButton) findViewById(R.id.btnEnviarCorreo);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cont = "<strong>Nombre: </strong>" + nombre.getText().toString() + "<br />" +
                        "<strong>Correo: </strong>" + correo.getText().toString() + "<br />" +
                        "<strong>Sugerencia : </strong>" + contenido.getText().toString() + "<br />";

                GestorMailAsincrono objGestor = new GestorMailAsincrono(BuzonSugerencias.this,cont,asunto.getText().toString());
                objGestor.execute();
                mtdLimpiarCampos();
            }
        });


    }

    private void mtdLimpiarCampos () {
        nombre.setText("");
        correo.setText("");
        asunto.setText("");
        contenido.setText("");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
