package code.taxigp.com.usuariotaxigp1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class MainUsuario extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    ImageView foto;
    TextView nombre, email, id;
    Button salir, revocar;

    private GoogleApiClient googleApClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usuario);

        foto = (ImageView) findViewById(R.id.foto);
        nombre = (TextView) findViewById(R.id.txtNombre);
        email = (TextView) findViewById(R.id.txtEmail);
        id = (TextView) findViewById(R.id.txtId);

        salir = (Button) findViewById(R.id.btnSalir);
        revocar = (Button) findViewById(R.id.btnRevocar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(googleApClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()){
                           // goLogScreem();
                        }else {
                            Toast.makeText(getApplicationContext(), "No se pudo cerrar Sesion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        revocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   // FirebaseAuth.getInstance().signOut();
                  //  LoginManager.getInstance().logOut();//Linea para cerrar sesion en facebook

                Auth.GoogleSignInApi.signOut(googleApClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()){
                          //  goLogScreem();
                        }else {
                            Toast.makeText(getApplicationContext(), "No se pudo revocar el acceso", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApClient);
        if (opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            //recibimos datos del email
            nombre.setText(account.getDisplayName());
            email.setText(account.getEmail());
            id.setText(account.getId());

            //Glide.with(this).load(account.getPhotoUrl()).into(foto);

            //Log.d("MIAPP", account.getPhotoUrl().toString());
        }else {
            //goLogScreem();

        }
    }

    private void goLogScreem() {
      Intent intent = new Intent(this, LoginUser.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
