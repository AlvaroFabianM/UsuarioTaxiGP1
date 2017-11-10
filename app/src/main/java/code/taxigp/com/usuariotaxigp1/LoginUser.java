package code.taxigp.com.usuariotaxigp1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import code.taxigp.com.usuariotaxigp1.LoginFirebase.GestorPreferencias;

public class LoginUser extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    /*Variables inicio sesion Google*/
    private GoogleApiClient googleApiClient;
    private SignInButton btnGoogle;
    public static final int codigo = 777;

    /*Variables login por registro normal*/
    EditText txCorreo, txContrasena;
    EditText txCorreoVRegis, txContrasenaVRegis;
    Button btnRegistroRegis, btnAbrirDialogRegis, btnLogin;
    View vistaCrearCuenta;

    /*variables inicio de sesion facebook*/
    CallbackManager callBackFaceBok;//Gestor de conexiones con facebook
    LoginButton btnLoginFace;//boton login
    ProgressBar progresLogin;

    /*Variables firebase auth*/
   private FirebaseAuth objDeAutenticacion;//objjeto de conexion con firebase
    private FirebaseAuth.AuthStateListener listenerAutenticacion;//Listener de evetos de autenticacion




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        /*Inflador registro normal*/
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vistaCrearCuenta= inflater.inflate(R.layout.vista_registro_normal,null);

        /*----------------------------Referencias----------------------------------------------------*/
          /*Referencias InicioFireBase*/
        txCorreoVRegis = (EditText) vistaCrearCuenta.findViewById(R.id.txCorreoRegis);
        txContrasenaVRegis = (EditText) vistaCrearCuenta.findViewById(R.id.txContrasenaRegis);
        btnRegistroRegis =(Button)vistaCrearCuenta.findViewById(R.id.btnCrearCuenta);
        btnAbrirDialogRegis =(Button) findViewById(R.id.btnAbrirDialogRegis);
        btnLogin = (Button)findViewById(R.id.btnLoginFire);

        /*Referencias FaceBook*/
        callBackFaceBok = CallbackManager.Factory.create();
        btnLoginFace = (LoginButton)findViewById(R.id.btnFacebook);
        progresLogin =(ProgressBar)findViewById(R.id.progressBar1);
        btnLoginFace.setReadPermissions(Arrays.asList("email"));//esta linea pide autorizacion para traer su correo desde facebook
        try {
            objDeAutenticacion = FirebaseAuth.getInstance();
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        /*Referencias Google*/
        btnGoogle = (SignInButton) findViewById(R.id.btnGoogle);
        txCorreo = (EditText) findViewById(R.id.txCorreo);
        txContrasena = (EditText) findViewById(R.id.txContrasena);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //si queremos el email del usuario autenticado
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, codigo);
            }
        });
        
        /*---------------------------Creacion dialogo registro fire base------------------------*/
        final MaterialStyledDialog dialogoRegistro = new MaterialStyledDialog.Builder(LoginUser.this)
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

        /*Abre el dialogo de registro*/
        btnAbrirDialogRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoRegistro.show();
            }
        });

        /*Envia datos a metodo de registro a firebase*/
        btnRegistroRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txCorreoVRegis.getText())) {
                    txCorreoVRegis.setError("Ingrese un correo");
                } else if (TextUtils.isEmpty(txContrasenaVRegis.getText())) {
                    txContrasenaVRegis.setError("Ingrese una contraseña");
                } else {
                    registrarFireBase(txCorreoVRegis.getText().toString(), txContrasenaVRegis.getText().toString());
                }
            }
        });

        /*Envia los datos al metodo que verifica si el usuario ya existe*/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(txCorreo.getText()))
                {
                    txCorreo.setError("Ingrese un correo");
                }
                else if (TextUtils.isEmpty(txContrasena.getText()))
                {
                    txContrasena.setError("Ingrese una contraseña");
                }
                else
                {
                    loginFireBase(txCorreo.getText().toString(),txContrasena.getText().toString());
                }

            }
        });
         /*Listener de inicio de sesion de facebook
        * Si se inicia correctamente envia los datos en el token al metodo
        * que los reenvia a firebase Auth*/
        btnLoginFace.registerCallback(callBackFaceBok, new FacebookCallback<LoginResult>() {//listener para el resultado de el login
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginUser.this, "Inicio correcto", Toast.LENGTH_SHORT).show();
                mtdAutenticacionFaceBook(loginResult.getAccessToken());//Enviamos el token con los datos para enviarlos a firebase
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginUser.this, "Inicio cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginUser.this, "Inicio erroneo", Toast.LENGTH_SHORT).show();
            }
        });
        
        /*Listener de inicio de sesion con fire base
        * Este listener recibe la autorizacion de firebase cuando el inicio es correcto con cualquiera de los metodos de inicio de sesión*/
        listenerAutenticacion = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();//El usuario recibe los datos enviados a firebase dese facebook
                if(usuario!=null)
                {
                    try {
                        Toast.makeText(LoginUser.this, "Sesion Iniciada con mail: " + usuario.getEmail(), Toast.LENGTH_SHORT).show();
                        mtdEscribirPreferencias(true);
                        goMainScreem("");
                    }catch (Exception e)
                    {
                        Toast.makeText(LoginUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(LoginUser.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                    mtdEscribirPreferencias(false);
                }

            }
        };

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*Facebook*/
        callBackFaceBok.onActivityResult(requestCode, resultCode, data);//Recibe los datos resultantes del intento de inicio de sesion
        /*----------------------------------------------*/
        if (requestCode == codigo) {
            //obtenemos un objeto resultado
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //creamos metodo resultadoInicioGoogle
            resultadoInicioGoogle(result);
        }

    }

    /*Google*/
    private void resultadoInicioGoogle(GoogleSignInResult result) {
        //recibimos datos del email
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
            //goMainScreem("Google");
        } else {
            Toast.makeText(this, "No se puede iniciar sesion", Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreem(String modoInicioSesion) {

        GestorPreferencias objPreferencias = new GestorPreferencias(LoginUser.this);
        int permisoUbicacion = objPreferencias.permisoUbicacion();
        Intent intent = new Intent(this, MainUsuarios.class);
        switch (permisoUbicacion)
        {
            case -1://sin permisos no autorizo el usuario
                intent.putExtra("modoInicio",modoInicioSesion);//inicia como google
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case 0://ya tiene permisos
                intent.putExtra("modoInicio",modoInicioSesion);//inicia como google
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case 2://aun no se han escrito
                intent = new Intent(this, Permisos.class);
                intent.putExtra("modoInicio",modoInicioSesion);//inicia como google
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
        }






    }

    /*Metodo que recibe el token de acceso de facebook y con ese token realiza el registro en firebase*/
    private void mtdAutenticacionFaceBook(AccessToken accessToken) {
        progresLogin.setVisibility(View.VISIBLE);
        btnLoginFace.setVisibility(View.GONE);
        AuthCredential credencial = FacebookAuthProvider.getCredential(accessToken.getToken());
      objDeAutenticacion.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginUser.this, "Registro correcto", Toast.LENGTH_SHORT).show();
                    progresLogin.setVisibility(View.GONE);
                    btnLoginFace.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(LoginUser.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    Log.e("SESION", task.getException().getMessage());
                }
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("", "firebaseAuthWithGoogle:" + acct.getId());

        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        objDeAutenticacion.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithCredential:success");
                            FirebaseUser user = objDeAutenticacion.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarFireBase(final String mail, final String contrasena) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginUser.this, "Registro correcto", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   try {
                       if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                           Toast.makeText(LoginUser.this, "Su cuenta ya se encuentra registrada", Toast.LENGTH_SHORT).show();
                           loginFireBase(mail,contrasena);
                       } else {
                           Toast.makeText(LoginUser.this, "Error al registrar" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }catch (Exception e)
                   {
                       Toast.makeText(LoginUser.this, "Error de conexón", Toast.LENGTH_SHORT).show();
                   }
                }
            }
        });

    }
    private void loginFireBase(String mail, String contrasena) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                    Toast.makeText(LoginUser.this, "Asegúrese de que su cuenta este registrada o verifique su conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void mtdEscribirPreferencias(boolean logIn)
    {
        try {
            GestorPreferencias objPreferencias = new GestorPreferencias(LoginUser.this);
            objPreferencias.login(logIn);
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(listenerAutenticacion);//iniciamos el listener de autenticacion

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listenerAutenticacion != null)
        {
            FirebaseAuth.getInstance().removeAuthStateListener(listenerAutenticacion);//Quitamos el listener de autenticacion
        }

    }





}