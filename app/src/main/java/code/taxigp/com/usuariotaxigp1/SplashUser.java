package code.taxigp.com.usuariotaxigp1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import code.taxigp.com.usuariotaxigp1.BaseDatosLocal.CLBaseDatos;
import code.taxigp.com.usuariotaxigp1.LoginFirebase.GestorPreferencias;
import code.taxigp.com.usuariotaxigp1.R;
import pl.droidsonroids.gif.GifImageView;

public class SplashUser extends AppCompatActivity {

    ImageView imgFade;
    GifImageView imgTaxi;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) { try {
        CLBaseDatos objDB = new CLBaseDatos(this, "BDLocal", null, 1);
        SQLiteDatabase base = objDB.getWritableDatabase();
        //Toast.makeText(this, "base creada", Toast.LENGTH_SHORT).show();

    } catch (Exception e) {
        Toast.makeText(this, "error al crear DB" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
        super.onCreate(savedInstanceState);




        GestorPreferencias objPrefrencias = new GestorPreferencias(SplashUser.this);
        final boolean logIn = objPrefrencias.isLogin();


        /*cuando la version de api es menor o igual a la 20 entonces ponemos el permso de ubicacion en 0*/
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
            objPrefrencias.mtdEcribirPermisoUbicacion(0);
        }






        setContentView(R.layout.activity_splash_user);
        imgFade=(ImageView)findViewById(R.id.imgFade);
        imgTaxi=(GifImageView)findViewById(R.id.gifTaxi);
        progress =(ProgressBar) findViewById(R.id.progressSplash) ;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(logIn)
                {
                    startActivity(new Intent(SplashUser.this,MainUsuarios.class));
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    finish();
                }
                else
                {

                    startActivity(new Intent(SplashUser.this,LoginUser.class));
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    finish();
                }

            }
        },4500);//Valor en milisegundos para el inicio de la actividad
        fadeOutAndHideImage(imgFade);
    }

    private void fadeOutAndHideImage(final ImageView img)
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(2000);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                progress.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                Animation animacionTaxi = AnimationUtils.loadAnimation(SplashUser.this, R.anim.animacion_imagen_taxi);
                animacionTaxi.setFillAfter(true);

                imgTaxi.setAnimation(animacionTaxi);

            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);
    }
    }
