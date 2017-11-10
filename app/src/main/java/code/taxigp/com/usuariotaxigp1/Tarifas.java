package code.taxigp.com.usuariotaxigp1;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;


public class Tarifas extends AppCompatActivity {

    FloatingActionButton fbExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifas);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.img_tarifas);

        fbExit = (FloatingActionButton)findViewById(R.id.fbExit);
        fbExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tarifas.super.onBackPressed();
            }
        });
    }
}
