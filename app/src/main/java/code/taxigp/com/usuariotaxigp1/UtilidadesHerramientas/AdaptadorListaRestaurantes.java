package code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas;

import android.content.Context;
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

public class AdaptadorListaRestaurantes extends BaseAdapter {
    // Declare Variables
    private Context context;
    private List<ObjSitiosRestaurantes> listaSitios;

    LayoutInflater inflater;
    public AdaptadorListaRestaurantes(Context context, List<ObjSitiosRestaurantes> listaSitios){
        this.context = context;
        this.listaSitios=listaSitios;

    }

    @Override
    public int getCount() {
        return  listaSitios.size();
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

        TextView nombreSitio,descripSitio,telefonoSitio,dirSitio;
        ImageView imgSitio, vipStar;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //creamos el view inflater para aceder a los atributos de la actividad list_row
        View itemView = inflater.inflate(R.layout.list_modelo_lugares, viewGroup, false);

        //referenciamos los textView y los imageView
        imgSitio = (ImageView) itemView.findViewById(R.id.imgLugar);
        vipStar = (ImageView) itemView.findViewById(R.id.imgVip);
        nombreSitio = (TextView) itemView.findViewById(R.id.txtNombreSit);
        descripSitio = (TextView) itemView.findViewById(R.id.txtDescripSit);
        telefonoSitio = (TextView) itemView.findViewById(R.id.txtTelefotoSit);
        dirSitio = (TextView) itemView.findViewById(R.id.txDireccioSit);

        //le damos valor a los textView
        Glide.with(context).load(listaSitios.get(i).getUrlFotoPrincipal()).into(imgSitio);

         if(listaSitios.get(i).isSuscripcion())
         {
             vipStar.setVisibility(View.VISIBLE);
         }
         else
         {
             vipStar.setVisibility(View.GONE);
         }
        nombreSitio.setText(listaSitios.get(i).getNombre());
        descripSitio.setText(listaSitios.get(i).getDescripcion());
        telefonoSitio.setText(listaSitios.get(i).getTelefono());
        dirSitio.setText(listaSitios.get(i).getDireccion());

        return itemView;
    }
}
