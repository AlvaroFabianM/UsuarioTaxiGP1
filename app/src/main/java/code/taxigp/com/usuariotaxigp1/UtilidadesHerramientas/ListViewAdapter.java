package code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import code.taxigp.com.usuariotaxigp1.R;


/**
 * Created by Asus on 13/9/2017.
 */

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    String[] foto;
    String[] nombre;
    String[] celular;
    String[] fecha;
    String[] numInterno;
    String[] pla;
    int[] empresa;

    LayoutInflater inflater;
    public ListViewAdapter(Context context, String[] foto, String[] nom, String[] cel, String[] fecha, String[] numInterno, String[] placa, int[]emp) {
        this.context = context;
        this.foto = foto;
        this.nombre = nom;
        this.celular = cel;
        this.fecha= fecha;
        this.numInterno = numInterno;
        this.pla = placa;
        this.empresa = emp;
    }

    @Override
    public int getCount() {
        return  nombre.length;
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

        TextView nombre,celular,fecha,numInterno,plac;
        ImageView fotoCond, imagenEmp;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //creamos el view inflater para aceder a los atributos de la actividad list_row
        View itemView = inflater.inflate(R.layout.list_row, viewGroup, false);

        //referenciamos los textView y los imageView
        fotoCond = (ImageView) itemView.findViewById(R.id.imgConductor);
        nombre = (TextView) itemView.findViewById(R.id.txtNombre);
        celular = (TextView) itemView.findViewById(R.id.txtCelu);
        fecha = (TextView) itemView.findViewById(R.id.txtFecha);
        numInterno = (TextView) itemView.findViewById(R.id.txtNumInterno);
        plac = (TextView) itemView.findViewById(R.id.txtPlaca);
        imagenEmp = (ImageView) itemView.findViewById(R.id.imgEmpresa);

        //le damos valor a los textView
        Glide.with(context).load(foto[i]).into(fotoCond);
        nombre.setText(this.nombre[i]);
        celular.setText(this.celular[i]);
        fecha.setText(this.fecha[i]);
        numInterno.setText("Vehiculo N°: "+this.numInterno[i]);
        plac.setText("Placa N°: "+this.pla[i]);
        //Hacer condicion para cargar imagen de la empresa
        switch (empresa[i]){
            case 1:
                Glide.with(context).load(R.drawable.asprotaxi).into(imagenEmp);
                break;
            case 2:
                Glide.with(context).load(R.drawable.cotradelsol).into(imagenEmp);
                break;
            case 3:
                Glide.with(context).load(R.drawable.sogatxi).into(imagenEmp);
                break;
            case 4:
                Glide.with(context).load(R.drawable.transoga).into(imagenEmp);
                break;
            default:
                break;
        }



        return itemView;
    }
}
