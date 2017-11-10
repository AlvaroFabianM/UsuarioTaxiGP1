package code.taxigp.com.usuariotaxigp1;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import at.markushi.ui.CircleButton;
import code.taxigp.com.usuariotaxigp1.BaseDatosLocal.CLConexion;
import code.taxigp.com.usuariotaxigp1.GestionMapas.DescargaJsonLatLong;
import code.taxigp.com.usuariotaxigp1.GestionMapas.GestorDirecciones;
import code.taxigp.com.usuariotaxigp1.GestionMapas.InterfaseGeneracionPolilinea;
import code.taxigp.com.usuariotaxigp1.GestionMapas.MedidorDistancias;
import code.taxigp.com.usuariotaxigp1.LoginFirebase.DataUsuario;
import code.taxigp.com.usuariotaxigp1.LoginFirebase.GestorPreferencias;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.DatosConductor;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.InterfaseRegistroAsincrono;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.InterfaseSolicitudServicio;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.SolicitudServicioAsincrono;
import code.taxigp.com.usuariotaxigp1.ServiciosAsincronos.VerificarAceptacionServicioAsincrono;
import code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas.AsistenteDeVoz;

import static android.os.Build.VERSION_CODES.KITKAT;

public class MainUsuarios extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    public GoogleMap mapaUsuarios;
    public int permisoUbicacion = 2;//2=vacio//0=permisos aceptados//-1=sin permisos
    LocationManager locationManager;
    ProgressBar progressUbicacion;
    LatLng posicionUsuario;
    float rotacion;
    CircleButton btnPedirTaxi;
    View vistaSolicitudTaxi, vistaInformacionConductor;
    private  boolean buscando = false;
    private EditText nombreUsuSolicitud, direccionUsusSolicitud, celularUsuSolicitud;
    private Button btnSolicitarTaxiUsu;
    private TextView nombreCon,celCon,placaCond,tiempoEstimado;
    private ImageView fotoCond, imgEmpresa;
    private TextView textoDialogoProgreso;
    boolean conductorEncontrado = false;
    boolean  Guia = true;
    Dialog customDialog = null;
    Button btnAnim;
    String number="0";
    TextToSpeech asistente;
    int result =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usuarios);

         /*Lectura de preferencia de ubicación
        * //2=vacio//0=permisos aceptados//-1=sin permisos*/
        GestorPreferencias objPreferencias = new GestorPreferencias(MainUsuarios.this);
        permisoUbicacion = objPreferencias.permisoUbicacion();
        progressUbicacion = (ProgressBar)findViewById(R.id.progressUbicacion) ;
        btnPedirTaxi = (CircleButton)findViewById(R.id.btnPedirTaxi);
        /*Inflador registro normal*/
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vistaSolicitudTaxi= inflater.inflate(R.layout.vista_solicitud_taxi,null);
        nombreUsuSolicitud = (EditText) vistaSolicitudTaxi.findViewById(R.id.txNombreSolicitud);
        direccionUsusSolicitud = (EditText) vistaSolicitudTaxi.findViewById(R.id.txDireccionSolicitud);
        celularUsuSolicitud = (EditText) vistaSolicitudTaxi.findViewById(R.id.txTelefonoSolicitud);
        btnSolicitarTaxiUsu = (Button) vistaSolicitudTaxi.findViewById(R.id.btnSolicitarServicio);
         MainUsuarios myActivity;

        // The constructor receives a reference to the Activity.

        asistente = new TextToSpeech(MainUsuarios.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status ==TextToSpeech.SUCCESS)
                {
                    result=asistente.setLanguage(Locale.getDefault());
                    if(!Guia)
                    {asistenteDeVoz(getResources().getString(R.string.idTapTarget0));}
                }
                else
                {
                    Toast.makeText(MainUsuarios.this, "asistente de voz no disponible en sus dispositivo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        asistente.setLanguage(Locale.ENGLISH);
        if(result==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
        {
            Toast.makeText(MainUsuarios.this, "asistente de voz no disponible en sus dispositivo", Toast.LENGTH_SHORT).show();
        }
        else
        {



        }

        /*Listener boyon buscador de posicion*/
        findViewById(R.id.btnBuscame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (checkLocation()&&!buscando)//y tiene activo el GPS y no esta buscando
                {
                    progressUbicacion.setVisibility(View.VISIBLE);
                    LocListener();
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaUsuarios);
        mapFragment.getMapAsync(this);
        //Object ob = R.id.irAmPos;

        /*objetos de dialogo de informacion del conductor*/
        try {
            LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaInformacionConductor = inflater2.inflate(R.layout.vista_conductor, null);
            nombreCon = (TextView) vistaInformacionConductor.findViewById(R.id.txNomCon);
            celCon = (TextView) vistaInformacionConductor.findViewById(R.id.txCelCon);
            placaCond = (TextView) vistaInformacionConductor.findViewById(R.id.txPlacaCon);
            imgEmpresa = (ImageView) vistaInformacionConductor.findViewById(R.id.imgEmpTax);
            fotoCond = (ImageView) vistaInformacionConductor.findViewById(R.id.fotoCond);
            btnAnim = (Button) vistaInformacionConductor.findViewById(R.id.btnLLamarConductor);
            tiempoEstimado = (TextView) vistaInformacionConductor.findViewById(R.id.txTiempoEstimado);
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
     //   vistaDialogoProgresos= inflater.inflate(R.layout.dialogo_progresos,null);
      //  textoDialogoProgreso = (TextView)  vistaDialogoProgresos.findViewById(R.id.txDialogProgress);

  /*Dialogo de pedir servicio de taxi*/
        final MaterialStyledDialog dialogoRegistro = new MaterialStyledDialog.Builder(MainUsuarios.this)
                .setHeaderDrawable(R.drawable.img_header_taxi)/*(new IconicsDrawable(MenuPrincipal.this).icon(MaterialDesignIconic.Icon.gmi_blogger).color(Color.WHITE))*/
                .withDialogAnimation(true)
                //.setDescription("Ingrese sus datos para solicitar su taxi")
                .setHeaderColor(R.color.colorPrimary)
                .setCustomView(vistaSolicitudTaxi, 0, 0, 0, 0)
                .setNegativeText("Cancelar").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();

        /*Dialogo de pedir servicio de taxi*/
        final MaterialStyledDialog dialogoInfoConductor = new MaterialStyledDialog.Builder(MainUsuarios.this)
                .setHeaderDrawable(R.drawable.img_header_info)/*(new IconicsDrawable(MenuPrincipal.this).icon(MaterialDesignIconic.Icon.gmi_blogger).color(Color.WHITE))*/
                .withDialogAnimation(true)
                //.setDescription("Ingrese sus datos para solicitar su taxi")
                .setHeaderColor(R.color.colorPrimary)
                .setCustomView(vistaInformacionConductor, 10, 10, 10, 0)
                .setNegativeText("Nuevo servicio").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                        startActivity(new Intent(MainUsuarios.this,MainUsuarios.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }).setPositiveText("Ver mapa").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        /*Se reinician variables para solicitar nuevo servicio*/
                        Toast.makeText(MainUsuarios.this, "salir", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).build();

        btnSolicitarTaxiUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestorPreferencias objPreferencias = new GestorPreferencias(MainUsuarios.this);
                objPreferencias.mtdEscribirPreferenciasServicio(direccionUsusSolicitud.getText().toString(),celularUsuSolicitud.getText().toString());
                objPreferencias.mtdEscribirPreferenciasLogin(nombreUsuSolicitud.getText().toString(),"");
                mtdSolicitarTaxi();
                dialogoRegistro.dismiss();
                mtdDialogoDeProgresos();
               // Toast.makeText(MainUsuarios.this, ""+direccionUsusSolicitud.getText().toString()+nombreUsuSolicitud.getText().toString()+celularUsuSolicitud.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
          /*----------------Confuguraciones de la vista----------------------------------*/

        toolbar.setTitleTextColor(ContextCompat.getColor(MainUsuarios.this, R.color.primaryTextColor));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(MainUsuarios.this, R.drawable.ic_menu_black_24dp));
        /*Verificacion de servicios mapas*/


        /*inicio de la guia*/
        //---------------------------------Implementacion tap target---------------------------------
        SharedPreferences objDatos= PreferenceManager.getDefaultSharedPreferences(this);
       Guia=objDatos.getBoolean("Guia",false);

        if(!Guia) {
            final Display display = getWindowManager().getDefaultDisplay();
            final Drawable gpsIcon = ContextCompat.getDrawable(this, R.drawable.ic_place_black_24dp);
            final Rect droidTarget = new Rect(0, 0, gpsIcon.getIntrinsicWidth() * 1, gpsIcon.getIntrinsicHeight() * 1);
            droidTarget.offset(display.getWidth() / 2, display.getHeight() / 2);

           // final SpannableString sassyDesc = new SpannableString("Obten informacion turistica y hotelera");
           // sassyDesc.setSpan(new StyleSpan(Typeface.ITALIC), sassyDesc.length() - "somtimes".length(), sassyDesc.length(), 0);

            final TapTargetSequence sequence = new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(findViewById(R.id.btnBuscame),getResources().getString(R.string.idTituloUbicacion),getResources().getString(R.string.idUbicacion))
                                    .textColor(R.color.colorTexto)
                                    .outerCircleColor(R.color.colorAccent)
                                    .tintTarget(false),
                            TapTarget.forToolbarNavigationIcon(toolbar, "Servicios","Obten informacion turistica y hotelera").id(1)
                                    .textColor(R.color.colorTexto)
                                    .outerCircleColor(R.color.colorAccent)
                                    .tintTarget(true)
                            /*TapTarget.forToolbarOverflow(toolbar, "Ingreso al sistema", "Exclusivo para administradores y conductores").id(3)
                                    .textColor(R.color.colorTexto)
                                    .outerCircleColor(R.color.colorAccent)
                                    .tintTarget(false),*/
                            /*TapTarget.forBounds(droidTarget, "Tu ubicacion", "Suministra una direccion o activa tu gps para encontrarte")
                                    .icon(gpsIcon)
                                    .outerCircleColor(R.color.colorAccentAzul)
                                    .textColor(R.color.colorTexto)*/



                    )
                    .listener(new TapTargetSequence.Listener() {
                        @Override
                        public void onSequenceFinish() {
                            GestorPreferencias objPreferencias = new GestorPreferencias(MainUsuarios.this);
                            objPreferencias.borrarPreferenciaTutorisl(true);
                        }
                        @Override
                        public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                            Log.d("TapTargetView", "Clicked on " + lastTarget.id());

                           switch (lastTarget.id())
                            {
                                case 1:
                                    asistenteDeVoz(getResources().getString(R.string.idTapTarget3));
                                    break;

                                case -1:
                                    asistenteDeVoz(getResources().getString(R.string.idTapTarget1));

                                    break;

                            }


                        }
                        @Override
                        public void onSequenceCanceled(TapTarget lastTarget) {
                            final AlertDialog dialog = new AlertDialog.Builder(MainUsuarios.this)
                                    .setTitle("Tutorial cancelado")
                                    .setMessage("Puedes volver a activarlo en las opciones")
                                    .setPositiveButton(":(", null).show();
                        }
                    });


            //final SpannableString spannedDesc = new SpannableString("Solicita el servicio de taxi solo con presionar en este boton");
            //spannedDesc.setSpan(new UnderlineSpan(), spannedDesc.length() - "Toca el boton para continuar".length(), spannedDesc.length(), 0);
            TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.btnPedirTaxi), "¡Bienvenido!","Solicita el servicio de taxi solo con presionar en este boton")
                    .textColor(R.color.colorTexto).outerCircleColor(R.color.colorAccent)
                    .tintTarget(false), new TapTargetView.Listener() {

                @Override
                public void onTargetClick(TapTargetView view) {
                    super.onTargetClick(view);
                    Toast.makeText(MainUsuarios.this, "click"+view.getId(), Toast.LENGTH_SHORT).show();
                    sequence.start();
                    asistenteDeVoz(getResources().getString(R.string.idTapTargetMinus1));
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
        btnPedirTaxi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*Escriimos preferencias de usuario*/

                if(!conductorEncontrado)
                {
                dialogoRegistro.show();
                GestorPreferencias objPreferencias = new GestorPreferencias(MainUsuarios.this);
                DataUsuario datosDelUsuario = objPreferencias.datosUsuario();
                nombreUsuSolicitud.setText(datosDelUsuario.getNombre());
                celularUsuSolicitud.setText(datosDelUsuario.getTelefono());
                }
                else if(conductorEncontrado)
                {
                    dialogoInfoConductor.show();
                }
            }
        });

        /*Verificamos que se tengan los permisos antes de comenzar la busqueda de la ubicacion OBLIGATORIO*/
       // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
       //     ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
      //  }

      
        //------------Verificacion de PERMISOS-----------------------
        if (permisoUbicacion==0)//Cuando se tiene permisos
        {
            if(checkLocation())//y tiene activo el GPS
            {
                LocListener();
                progressUbicacion.setVisibility(View.VISIBLE);
            }
        }


    }

    /*Verfifcación de GPS encendido*/
    private boolean checkLocation() {
        if (!isLocationEnabled()) {
            showAlert();
        }
        return isLocationEnabled();
    }

    /*Si no esta prendido abre un dialogo para poder  acceder a las configuraciones y encenderlo*/
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Habilite su ubicación")
                .setMessage("Su GPS esta desactivado.\npor favor active su ubicación en las configuraciones")
                .setPositiveButton("Ir a configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),1;
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                        //startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }
    /*Metodo que verifica si el gps esta habilitado*/
    private boolean isLocationEnabled() {
        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    /*Recibe la llamda devuelta de la activacion del gps*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("","OnActivity Result...");
        super.onActivityResult(requestCode, resultCode, data);

        if (isLocationEnabled())
        {
            //inicia busqueda de posicion del usuario
            Toast.makeText(this, "Se activo el gps", Toast.LENGTH_SHORT).show();
            LocListener();
            progressUbicacion.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(this, "NO Se activo el gps", Toast.LENGTH_SHORT).show();
        }

    }

    /*Variable para verificar 2 veces la posicion antes de dibujarla sobre el mapa*/
    public int segundaUbicacion = 0;

    /*Metodo de ubicacion de usuarios, listener de cambio de posicion*/
        public void LocListener()
        {
 buscando=true;
            /*Se ejecuta cada vez que hay un cambio en la posicion del usuario*/
            final LocationListener milListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    location.getLatitude();
                    location.getLongitude();
                    rotacion=location.getBearing();
                    posicionUsuario = new LatLng(location.getLatitude(),location.getLongitude());
                        segundaUbicacion++;//incrementa en 1 cada vez que se llama a el cambio de posicion
                }

                /*Se ejecuta cada vez que hay un cambio en la configuracion del gps*/
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    switch (status) {
                        case LocationProvider.AVAILABLE:
                            Log.d("debug", "LocationProvider.AVAILABLE");
                            break;
                        case LocationProvider.OUT_OF_SERVICE:
                            Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                            Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                            break;
                    }
                }

                @Override
                public void onProviderEnabled(String provider) {
                    // Este metodo se ejecuta cuando el GPS es activado
                   // mensaje1.setText("GPS Activado");
                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            /*Verificamos que se tengan los permisos antes de comenzar la busqueda de la ubicacion OBLIGATORIO*/
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) milListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) milListener);

            /*En 5 segundos se verifica si ya se a leido la posicion del usuario 2 veces
            * esto puede ser opcional segun se vea la necesidad de acelerar el proceso
            * de busqueda*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    /*Si hay coordenadas entonces apagamos el buscador para evitar bug por tareas largas*/
                    if(segundaUbicacion>=2) {
                        segundaUbicacion=0;//reiniciamos el contador de busquedas para volver a buscar desde el boton de gps
                        locationManager.removeUpdates(milListener);
                        locationManager = null;
                        //Toast.makeText(MainUsuarios.this, "detiene busqueda", Toast.LENGTH_SHORT).show();
                        buscando=false;
                        progressUbicacion.setVisibility(View.GONE);
                        animacionCamara();
                    }
                    /*Si no han leido las coordenadas las dos veces entonces reiniciamos la busqueda*/
                    else
                    {
                        LocListener();
                       // Toast.makeText(MainUsuarios.this, "reinicia"+segundaUbicacion, Toast.LENGTH_SHORT).show();
                    }

                }
            },3000);//Valor en milisegundos para el reinicio de la busqueda

        }


    /*Se ejecuta cuando el mapa esta listo*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*configuracion mapa*/
        mapaUsuarios = googleMap;
        mapaUsuarios.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mapaUsuarios.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(5.690642, -73.088480) , 10.0f) );
        /*utilidades*/
        //habilitar utilidades google maps
        UiSettings utilidades = mapaUsuarios.getUiSettings();
        utilidades.setZoomControlsEnabled(true);
        utilidades.setMyLocationButtonEnabled(true);
        utilidades.isMyLocationButtonEnabled();
        utilidades.setMapToolbarEnabled(false);
    }

    public void animacionCamara() {
        try {
            CameraPosition position = new CameraPosition.Builder()
                    .target(posicionUsuario)
                    .zoom(16)
                   //.bearing(90)
                    .tilt(0)
                    .build();
            mapaUsuarios.animateCamera(CameraUpdateFactory.newCameraPosition(position),3500,null);
            mapaUsuarios.clear();
            MarkerOptions miPosicion = new MarkerOptions();
            miPosicion.position(posicionUsuario);
            miPosicion.title("Mi ubicación");
            miPosicion.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_usua_gps", 70, 70)));
            mapaUsuarios.addMarker(miPosicion);
            //mapaUsuarios.animateCamera(CameraUpdateFactory.newLatLngZoom(posicionUsuario, 14), 5000, null);

            //mtdDirecciones(userLocation);

        }catch (Exception e)
        {

            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }
    //-------------------------Metodo para redimensionar los marcadores---------------------------------
    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void mtdSolicitarTaxi()
    {
        InterfaseRegistroAsincrono resultadoRegistro = new InterfaseRegistroAsincrono() {
            @Override
            public void abstractMtdRegistroResultado(String resultado, int condCercano) {
                /*Recibimos el resultado del registro*/
                switch (resultado)
                {
                    case "1":

                        textoDialogoProgreso.setText(R.string.str_solicitud_enviada);
                        verificarAceptacionServicio(condCercano);
                        break;

                    case"0":
                        textoDialogoProgreso.setText(R.string.str_sin_conductore);
                        break;
                    case"-404":
                        textoDialogoProgreso.setText(R.string.str_sin_internet);
                        break;

                }
            }
        };
        SolicitudServicioAsincrono objRegistro = new SolicitudServicioAsincrono(MainUsuarios.this,posicionUsuario,resultadoRegistro);
        objRegistro.execute("");
    }

    public void mtdDialogoDeProgresos()
    {
            // con este tema personalizado evitamos los bordes por defecto
          customDialog = new Dialog(this);
            //deshabilitamos el título por defecto
            customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //obligamos al usuario a pulsar los botones para cerrarlo
            customDialog.setCancelable(true);
            //establecemos el contenido de nuestro dialog
            customDialog.setContentView(R.layout.dialogo_progresos);
            TextView titulo = (TextView) customDialog.findViewById(R.id.txDialogProgress);
        textoDialogoProgreso = titulo;
            titulo.setText(R.string.str_buscando);

            customDialog.show();

    }

    /*----------------------------Verificacion de acepatacion del servicio--------------------*/
  public void verificarAceptacionServicio(final int condCercano)
  {
      InterfaseSolicitudServicio listener = new InterfaseSolicitudServicio() {
          @Override
          public void abstractMtdRegistroResultado(String resultado, DatosConductor datosConductor) {
              if(resultado.equals("1")&&!conductorEncontrado) {
                  conductorEncontrado = true;
                  mtdSetDataConductorDialog(datosConductor);
                  customDialog.dismiss();
                  Toast.makeText(MainUsuarios.this, "ya hay conductor"+ datosConductor.getNombre(), Toast.LENGTH_SHORT).show();
              }
            }
      };

      VerificarAceptacionServicioAsincrono objVerificador = new VerificarAceptacionServicioAsincrono(MainUsuarios.this,condCercano,listener);
      objVerificador.execute("");
      Toast.makeText(this, "Buscando....", Toast.LENGTH_SHORT).show();

      if(!conductorEncontrado)
      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              verificarAceptacionServicio(condCercano);

          }
      },7000);
  }
   /*Carga de informacion del conductor al dialogo*/
  public void mtdSetDataConductorDialog(DatosConductor datosCond) {
      btnPedirTaxi.performClick();
      nombreCon.setText(datosCond.getNombre());
      celCon.setText(datosCond.getCelular());
      placaCond.setText(datosCond.getPlaca());
      Glide.with(this).load(datosCond.getUrlFoto()).into(fotoCond);

      int i = (int) MedidorDistancias.tiempo.doubleValue();
      tiempoEstimado.setText("Tiempo estimado "+ i +" minutos...");
      asistenteDeVoz(getResources().getString(R.string.conductorCamino)+ i +" minutos...");

      switch (Integer.parseInt(datosCond.getEmpresa())) {
          case 1:
              // empresa.setImageDrawable(getResources().getDrawable(R.drawable.asprotaxi));
              Glide.with(this).load(R.drawable.asprotaxi).into(imgEmpresa);
              break;
          case 2:
              Glide.with(this).load(R.drawable.cotradelsol).into(imgEmpresa);
              // imgEmp.setImageDrawable(getResources().getDrawable(R.drawable.cotradelsol));
              break;
          case 3:
              Glide.with(this).load(R.drawable.sogatxi).into(imgEmpresa);
              // imgEmp.setImageDrawable(getResources().getDrawable(R.drawable.sogatxi));
              break;
          case 4:
              Glide.with(this).load(R.drawable.transoga).into(imgEmpresa);
              // empresa.setImageDrawable(getResources().getDrawable(R.drawable.transoga));
              break;

      }
      number = datosCond.getCelular();
      mtdDibujarPolilinea(datosCond);

  }

  LatLng posicionCond;
  public void mtdDibujarPolilinea(DatosConductor datosCond)
  {
      String[] posicionConductor = new String[2];
      posicionConductor = datosCond.getCoordenadas().split(",");
      posicionCond = new LatLng(Double.parseDouble(posicionConductor[0]),Double.parseDouble(posicionConductor[1]));

      InterfaseGeneracionPolilinea listenerPolilinea = new InterfaseGeneracionPolilinea() {
          @Override
          public void abstractMtdRegistroResultado(PolylineOptions polilinea) {

              if(polilinea!=null)
              {
                  mapaUsuarios.addPolyline(polilinea);

                  MarkerOptions marcadorDestino= new MarkerOptions();
                  marcadorDestino.position(posicionCond);
                  marcadorDestino.title("Conductor");
                  //marcadorDestino.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_tax_gps));
                  marcadorDestino.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_cond_gps", 70, 70)));
                  mapaUsuarios.addMarker(marcadorDestino);
                  //mapaUsuarios.moveCamera(CameraUpdateFactory.newLatLngZoom(MainUsuario.userLocation, 14.5f))
              }
          }
      };

       /*Dibujamos Polilinea*/

      GestorDirecciones objDirecciones = new GestorDirecciones();
      String url = objDirecciones.obtenerDireccionesURL(posicionCond,posicionUsuario);
      DescargaJsonLatLong downloadTask = new DescargaJsonLatLong(listenerPolilinea);
      downloadTask.execute(url);

      //-----------Obtenemos hora y fecha atuales------------------

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      Date date = new Date();

      DateFormat horaActual = new SimpleDateFormat("HH:mm:ss");

      String fechaActual = dateFormat.format(date);

      String fehora = fechaActual + " " + horaActual.format(date);

      //-------------------------------------------------------------
      String sql;

      try {

          sql = "insert into Historial (Foto,Nombre,Celular,FechaHora,NumeroMovil,Placa,EmpresaTaxi) values ('"+datosCond.getUrlFoto()+"','"+datosCond.getNombre()+"','"+datosCond.getCelular()+"','"+ fehora +"','numero-movil','"+datosCond.getPlaca()+"',"+datosCond.getEmpresa()+")";
          CLConexion objHistorial = new CLConexion();

          objHistorial.mtdRegistrar(MainUsuarios.this,sql);

      }catch (Exception e){

      }

  }

  public void mtdLLamarConductor(final View v)
  {

      try {
          btnAnim.setBackgroundResource(R.drawable.btn_llamar_pre);
      }catch (Exception e)
      {
          Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
      }
      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              llamada(btnAnim);
          }
      }, 100);


  }

  public void llamada(Button btn)
  {
      btn.setBackgroundResource(R.drawable.btn_llamar_sin_pre);
      int permissionCheck = ContextCompat.checkSelfPermission(
              this, Manifest.permission.CALL_PHONE);
      if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
          Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 225);
      } else {
          Log.i("Mensaje", "Se tiene permiso!");
          Intent callIntent = new Intent(Intent.ACTION_CALL);
          callIntent.setData(Uri.parse("tel:"+number));
          startActivity(callIntent);
      }


     ;
  }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_usuarios, menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.mpTypeHibrido) {
            mapaUsuarios.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (id == R.id.mpTypeNormal) {
            mapaUsuarios.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.mpTypeTierra) {
            mapaUsuarios.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        } elseif (id == R.id.irAmPos) { /*reinstancia objeto localizacion
        esto por que se pone en null al encontrar la posicion del usuario
       //y tiene activo el GPS

            }

            return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.hoteles) {
           startActivity(new Intent(MainUsuarios.this, Sitios.class).putExtra("tipoSitios","Hoteles"));
        } else if (id == R.id.restaurantes) {
            startActivity(new Intent(MainUsuarios.this, Sitios.class).putExtra("tipoSitios","Restaurantes"));
        } else if (id == R.id.bares) {
            startActivity(new Intent(MainUsuarios.this, Sitios.class).putExtra("tipoSitios","Bares"));
        } else if (id == R.id.recreacionDeportes) {
            startActivity(new Intent(MainUsuarios.this, Sitios.class).putExtra("tipoSitios","Recreacion"));
        } else if (id == R.id.tarifas){
            startActivity(new Intent(MainUsuarios.this, Tarifas.class));
        }
        else if (id == R.id.buzonSugerencias) {
            startActivity(new Intent(MainUsuarios.this, BuzonSugerencias.class));
        } else if (id == R.id.historial) {
            startActivity(new Intent(MainUsuarios.this, Historial.class));

        } else if (id == R.id.tutorial) {
                try {
                    final AlertDialog.Builder constructorDialogo = new AlertDialog.Builder(MainUsuarios.this);
                    constructorDialogo.setTitle(R.string.str_activacion_tutorial);
                    constructorDialogo.setMessage(R.string.str_acepta_tutorial);
                    constructorDialogo.setPositiveButton(R.string.str_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GestorPreferencias objPreferencias = new GestorPreferencias(MainUsuarios.this);
                            objPreferencias.borrarPreferenciaTutorisl(false);
                            objPreferencias.asistente(false);
                           finish();
                          startActivity(new Intent(MainUsuarios.this,MainUsuarios.class));
                            overridePendingTransition(R.anim.right_in, R.anim.right_out);
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
                    Toast.makeText(MainUsuarios.this, "Problema al eliminar historial", Toast.LENGTH_SHORT).show();

            }

        }else if (id == R.id.compartir){

        } else if (id == R.id.salir_app) {
            LoginManager.getInstance().logOut();//Linea para cerrar sesion en facebook
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Sesion Terminada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginUser.class));
            GestorPreferencias objPreferencias = new GestorPreferencias(MainUsuarios.this);
            objPreferencias.login(false);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Toast.makeText(this, "backpressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  Toast.makeText(this, "pausa", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // Toast.makeText(this, "resume", Toast.LENGTH_SHORT).show();
    }//


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

    }


