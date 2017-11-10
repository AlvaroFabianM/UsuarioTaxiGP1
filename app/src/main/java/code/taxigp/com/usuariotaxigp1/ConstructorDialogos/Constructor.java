package code.taxigp.com.usuariotaxigp1.ConstructorDialogos;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import code.taxigp.com.usuariotaxigp1.LoginUser;
import code.taxigp.com.usuariotaxigp1.MainUsuarios;
import code.taxigp.com.usuariotaxigp1.R;

/**
 * Created by Asus on 20/10/2017.
 */

public class Constructor extends MainUsuarios {

    View vistaCrearCuenta;
    Context con;

    public Constructor(Context _con, View vistaCrearCuenta) {

        con=_con;

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.vistaCrearCuenta=vistaCrearCuenta;//= inflater.inflate(R.layout.vista_registro_normal,null);

        final MaterialStyledDialog dialogoRegistro = new MaterialStyledDialog.Builder(con)
                .setHeaderDrawable(R.drawable.img_header_taxi_usu)/*(new IconicsDrawable(MenuPrincipal.this).icon(MaterialDesignIconic.Icon.gmi_blogger).color(Color.WHITE))*/
                .withDialogAnimation(true)
                .setDescription("Complete los datos para registrarse")
                .setHeaderColor(R.color.colorPrimary)
                .setCustomView(vistaCrearCuenta, 0, 0, 0, 0)
                .setNegativeText("Cancelar").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();
        dialogoRegistro.show();

    }

}
