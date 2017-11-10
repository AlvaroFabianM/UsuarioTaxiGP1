package code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import code.taxigp.com.usuariotaxigp1.R;


/**
 * Created by Asus on 13/9/2017.
 */

public class AdaptadorPlatos extends BaseAdapter {
    // Declare Variables
    private Context context;
    private List<ObjPlatosRestaurante> listaPlatos;
    private int colorCoorporativo;

    LayoutInflater inflater;
    public AdaptadorPlatos(Context context, List<ObjPlatosRestaurante> listaPlatos, int colorCoorporativo){
        this.context = context;
        this.listaPlatos=listaPlatos;
        this.colorCoorporativo = colorCoorporativo;

    }

    @Override
    public int getCount() {
        return  listaPlatos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        // Declare Variables

        TextView nomPlato,descripPlato,precioPlato;
        ImageView imgPlato;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //creamos el view inflater para aceder a los atributos de la actividad list_row
        View itemView = inflater.inflate(R.layout.list_modelo_platos, viewGroup, false);

        //referenciamos los textView y los imageView

        imgPlato = (ImageView) itemView.findViewById(R.id.imgPlato);
        nomPlato = (TextView) itemView.findViewById(R.id.txtNombrePlato);
        descripPlato = (TextView) itemView.findViewById(R.id.txtDescripPlato);
        precioPlato = (TextView) itemView.findViewById(R.id.txtPrecioPlato);

        //le damos valor a los textView
        Glide.with(context).load(listaPlatos.get(i).getFotoPlato()).into(imgPlato);
        nomPlato.setText(listaPlatos.get(i).getNombrePlato());
        descripPlato.setText(listaPlatos.get(i).getDescripcionPlato());
        precioPlato.setText("$"+listaPlatos.get(i).getPrecio());
        precioPlato.setTextColor(colorCoorporativo);

        return itemView;
    }
}
