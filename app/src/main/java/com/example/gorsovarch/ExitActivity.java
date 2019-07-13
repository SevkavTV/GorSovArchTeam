package com.example.gorsovarch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ExitActivity extends AppCompatActivity {
    Button btnExit;
    ImageView btnCancel;
    ConstraintLayout cl;
    ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        getSupportActionBar().hide();
        cl = findViewById(R.id.constraintLayout);
        btnCancel = findViewById(R.id.cancel);
        btnExit = findViewById(R.id.exit);
        im = findViewById(R.id.imageView);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExitActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        Bundle extras = getIntent().getExtras();
        //Bitmap background = extras.getParcelable("BACKGROUND");
        //im.setImageBitmap(background);
        byte[] byteArray = extras.getByteArray("background");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(),bmp);
        cl.setBackground(bitmapDrawable);
    }
}
