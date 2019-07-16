package com.example.gorsovarch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.gorsovarch.MyFTPClientFunctions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExitActivity extends AppCompatActivity {
    Button btnExit;
    ImageView btnCancel;
    ConstraintLayout cl;
    ImageView im;
    TextView tv;
    MyFTPClientFunctions clientFunctions;
    private MyFTPClientFunctions ftpclient = null;
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 2) {
                Toast.makeText(ExitActivity.this, "Uploaded Successfully!",
                        Toast.LENGTH_LONG).show();
            } else if (msg.what == 3) {
                Toast.makeText(ExitActivity.this, "Ви вийшли!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ExitActivity.this, "Unable to Perform Action!",
                        Toast.LENGTH_LONG).show();
            }

        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        ftpclient = new MyFTPClientFunctions();
        getSupportActionBar().hide();
        tv = (TextView)findViewById(R.id.userName);
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("User");
        tv.setText("@"+username);
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
        clientFunctions = new MyFTPClientFunctions();
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftpclient.ftpDisconnect();
                handler.sendEmptyMessage(3);
                Intent i = new Intent(ExitActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        Bundle extras1 = getIntent().getExtras();
        byte[] byteArray = extras1.getByteArray("background");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(),bmp);
        cl.setBackground(bitmapDrawable);
    }
}
