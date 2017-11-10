package code.taxigp.com.usuariotaxigp1;

import android.*;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;

import code.taxigp.com.usuariotaxigp1.LoginFirebase.GestorPreferencias;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.ObjSitiosHotBarRec;

public class DetalleHotBarRec extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private ImageView image_scrolling_top;
    private SliderLayout Slider;
    TextView txDecripcion,txTelefono,txDireccion;
    NestedScrollView scroll;
    Context context;
    ObjSitiosHotBarRec datos;
    CollapsingToolbarLayout toolbarCollapsing;


    TextToSpeech asistente;
    public int recargar=2;
    int result =0;
    private boolean Guia;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoteles_bares_recreacion);


        toolbarCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image_scrolling_top = (ImageView) findViewById(R.id.image_scrolling_top);
        txDecripcion =(TextView) findViewById(R.id.txDescripcion);
        txTelefono =(TextView) findViewById(R.id.txTelefono);
        txDireccion =(TextView) findViewById(R.id.txDireccion);
        scroll =(NestedScrollView)findViewById(R.id.idNestScrollViewNoti);

        /*Leemos valores de la noticia a mostrar*/
        //Intent intent=getIntent();
        Bundle extras =getIntent().getExtras();
        if (extras != null) {
            String json = (String) extras.get("datos");
            Gson gson = new Gson(); // Or use new GsonBuilder().create();
            datos = gson.fromJson(json, ObjSitiosHotBarRec.class); // deserializes json into target2
            txDireccion.setText(datos.getDireccion());
            txTelefono.setText(datos.getTelefono());
            txDecripcion.setText(datos.getDescripcion());
            /*Asignacion de variables a objetos*/
            Glide.with(this).load(datos.getUrlFotoPrincipal()).into(image_scrolling_top);
            //txTitulo.setText(datos.getDescripcion());
        }
        // The constructor receives a reference to the Activity.
        SharedPreferences objDatos= PreferenceManager.getDefaultSharedPreferences(this);
        Guia=objDatos.getBoolean("Asistente",false);


        asistente = new TextToSpeech(DetalleHotBarRec.this, new TextToSpeech.OnInitListener() {
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
                    Toast.makeText(DetalleHotBarRec.this, "asistente de voz no disponible en sus dispositivo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        asistente.setLanguage(Locale.ENGLISH);
        if(result==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
        {
            Toast.makeText(DetalleHotBarRec.this, "asistente de voz no disponible en sus dispositivo", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DetalleHotBarRec.this, "click" + view.getId(), Toast.LENGTH_SHORT).show();
                    // sequence.start();
                    //asistenteDeVoz(getResources().getString(R.string.idTapTargetMinus1));
                    GestorPreferencias objPreferencias = new GestorPreferencias(DetalleHotBarRec.this);
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



        Slider = (SliderLayout)findViewById(R.id.slider);
        context=this;

        /*Se montan imagenes segun ciclo*/
        HashMap<String, String> url_maps = new HashMap<String, String>();

         // url_maps.put("titulo1",datos.getFotoUno());
          url_maps.put("nombre1",datos.getFotoUno());
          url_maps.put("nombre 2",datos.getFotoDos());
          url_maps.put("nombre3", datos.getFotoTres());
          url_maps.put( "nombre 4",datos.getFotoCuatro());
        url_maps.put( "nombre 5",datos.getFotoCinco());

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                     .description(datos.getNombre())
                    .image(url_maps.get(name))
                    .setOnSliderClickListener(DetalleHotBarRec.this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);


            Slider.addSlider(textSliderView);


        }
        //--------------Configuraciones slider---------------------
        Slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        Slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        Slider.setCustomAnimation(new DescriptionAnimation());
        Slider.setDuration(5000);
        Slider.addOnPageChangeListener(this);
        Slider.startAutoCycle();

        findViewById(R.id.fabNavigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtdIniciarNavegacion();
            }
        });


        if (this.getSupportActionBar() != null)
        this.getSupportActionBar().setTitle(datos.getNombre());
        setCollapsingToolbarLayoutTitle();
        //---------------------------------------------------------
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


    }
    @Override
    public void onBackPressed() {
super.onBackPressed();

        }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //  Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void asistenteDeVoz(String text)
    {
        if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            String utteranceId=this.hashCode() + "";
            asistente.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
        else if(android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
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

