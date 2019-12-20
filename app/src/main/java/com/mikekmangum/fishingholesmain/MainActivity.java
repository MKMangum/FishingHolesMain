package com.mikekmangum.fishingholesmain;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get the id of first image view
        ImageView fishImageview = (ImageView) findViewById(R.id.fishImageView);
    }
    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Load main menu after 3s = 3000ms
                Intent myIntent = new Intent(MainActivity.this, MainMenu.class);

                MainActivity.this.startActivity(myIntent);
            }
        }, 3000);


    }

}
