package code.taxigp.com.usuariotaxigp1;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import code.taxigp.com.usuariotaxigp1.LoginFirebase.GestorPreferencias;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.InterfaseLecturaPlatos;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.TareaLecturaPlatosPorRestaurate;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.AdaptadorListaRestaurantes;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.AdaptadorPlatos;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjPlatosRestaurante;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjSitiosHotBarRec;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjSitiosRestaurantes;

public class DetalleRestaurantes extends AppCompatActivity {

    private ImageView image_scrolling_top;
    TextView txDecripcion,txTelefono,txDireccion;
    NestedScrollView scroll;
    Context context;
    ObjSitiosRestaurantes datos;
    CollapsingToolbarLayout toolbarCollapsing;
    FloatingActionButton btnNavegacion;
    Button btnLLmada;
    List<ObjPlatosRestaurante> listaPlatosPaso;
    AdaptadorPlatos adapterPlatos;
    ListView lvPlatos;
    int colorCoorporativo;

    TextToSpeech asistente;
    public int recargar=2;
    int result =0;
    private boolean Guia;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantes);

        btnLLmada = (Button) findViewById(R.id.btnReservaciones) ;
        btnNavegacion = (FloatingActionButton)findViewById(R.id.fabNavigation);
        toolbarCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image_scrolling_top = (ImageView) findViewById(R.id.image_scrolling_top);
        txDecripcion =(TextView) findViewById(R.id.txDescripcion);
        txTelefono =(TextView) findViewById(R.id.txTelefono);
        txDireccion =(TextView) findViewById(R.id.txDireccion);
        scroll =(NestedScrollView)findViewById(R.id.idNestScrollViewNoti);
        lvPlatos = (ListView) findViewById(R.id.listaPlatos) ;


        /*Leemos valores de la noticia a mostrar*/
        //Intent intent=getIntent();
        Bundle extras =getIntent().getExtras();
        if (extras != null) {
            String json = (String) extras.get("datos");
            Gson gson = new Gson(); // Or use new GsonBuilder().create();
            datos = gson.fromJson(json, ObjSitiosRestaurantes.class); // deserializes json into target2
            txDireccion.setText(datos.getDireccion());
            txTelefono.setText(datos.getTelefono());
            txDecripcion.setText(datos.getDescripcion());
            colorCoorporativo = Color.parseColor(datos.getColorCoorporativo());


            /*Asignacion de variables a objetos*/
            Glide.with(this).load(datos.getUrlFotoPrincipal()).into(image_scrolling_top);
            //txTitulo.setText(datos.getDescripcion());
        }
        // The constructor receives a reference to the Activity.
        SharedPreferences objDatos= PreferenceManager.getDefaultSharedPreferences(this);
        Guia=objDatos.getBoolean("Asistente",false);


        asistente = new TextToSpeech(DetalleRestaurantes.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status ==TextToSpeech.SUCCESS)
                {
                    result=asistente.setLanguage(Locale.getDefault());

                    if(!Guia)
                  {asistenteDeVoz(getResources().getString(R.string.idTapTarget5));}
                }
                else
                {
                    Toast.makeText(DetalleRestaurantes.this, "asistente de voz no disponible en sus dispositivo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        asistente.setLanguage(Locale.ENGLISH);
        if(result==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
        {
            Toast.makeText(DetalleRestaurantes.this, "asistente de voz no disponible en sus dispositivo", Toast.LENGTH_SHORT).show();
        }
        else
        {



        }

        if(!Guia) {
            TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.fabNavigation), "Asistente de navegación de Google", "Presione sobre este botón para iniciar guía asistida")
                    .textColor(R.color.colorTexto).outerCircleColor(R.color.colorAccentAzul)
                    .tintTarget(false), new TapTargetView.Listener() {

                @Override
                public void onTargetClick(TapTargetView view) {
                    super.onTargetClick(view);
                    Toast.makeText(DetalleRestaurantes.this, "click" + view.getId(), Toast.LENGTH_SHORT).show();
                    // sequence.start();
                    //asistenteDeVoz(getResources().getString(R.string.idTapTargetMinus1));
                    GestorPreferencias objPreferencias = new GestorPreferencias(DetalleRestaurantes.this);
                    objPreferencias.asistente(true);
                }

                @Override
                public void onOuterCircleClick(TapTargetView view) {
                    super.onOuterCircleClick(view);
                    Toast.makeText(view.getContext(), "Toque el botón para continuar…", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                    Log.d("TapTargetViewSample", "You dismissed me :(");
                }
            });
        }



        findViewById(R.id.fabNavigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtdIniciarNavegacion();
            }
        });


        if (this.getSupportActionBar() != null)
        this.getSupportActionBar().setTitle(datos.getNombre());
        setCollapsingToolbarLayoutTitle();
        toolbarCollapsing.setBackgroundColor(Color.parseColor(datos.getColorCoorporativo()));
       // btnNavegacion.setBackgroundTintList(Color.parseColor(datos.getColorCoorporativo()));
        btnNavegacion.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(datos.getColorCoorporativo())));
       // toolbar.setBackgroundColor(Color.parseColor(datos.getColorCoorporativo()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnLLmada.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(datos.getColorCoorporativo())));
        }
        else
        {
            btnLLmada.setBackgroundColor(Color.parseColor(datos.getColorCoorporativo()));
        }
        //---------------------------------------------------------
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        /*Lectura de platos*/
       InterfaseLecturaPlatos listenerLectura = new InterfaseLecturaPlatos() {
           @Override
           public void AbsMtdListaSitios(List<ObjPlatosRestaurante> listaPlatos) {
               listaPlatosPaso = listaPlatos;
               adapterPlatos = new AdaptadorPlatos(DetalleRestaurantes.this,listaPlatos,colorCoorporativo);
               lvPlatos.setAdapter(adapterPlatos);
           }
       };

        TareaLecturaPlatosPorRestaurate tareaLecturaPlatosPorRestaurate = new TareaLecturaPlatosPorRestaurate(DetalleRestaurantes.this,listenerLectura);
        tareaLecturaPlatosPorRestaurate.execute("http://taxigp.com.co/Servicios/ConsultarPlatosxRestaurante.php?restaurante="+datos.getIdRestaurante());

    }
    @Override
    public void onBackPressed() {
super.onBackPressed();

        }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void asistenteDeVoz(String text)
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            String utteranceId=this.hashCode() + "";
            asistente.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
        else if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            asistente.speak(text, TextToSpeech.QUEUE_FLUSH, map);

        }
    }

    private void setCollapsingToolbarLayoutTitle() {
        toolbarCollapsing.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbarCollapsing.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        //toolbarCollapsing.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
       // toolbarCollapsing.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }
    public void mtdIniciarNavegacion()
    {
        String [] coordenadasDestino = datos.getCoordenadas().split(",");

        Uri gmmIntentUri = Uri.parse("google.navigation:q="+coordenadasDestino[0]+","+coordenadasDestino[1]);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }
    public void llamada(View v)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso!");
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+datos.getTelefono()));
            startActivity(callIntent);
        }

    }


    }

