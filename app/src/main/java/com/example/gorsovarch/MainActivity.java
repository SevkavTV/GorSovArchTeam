package com.example.gorsovarch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.gorsovarch.MyFTPClientFunctions;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import org.apache.commons.net.ftp.FTPClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText editLogin, editPassword;
    Button enter;
    Context context = this;
    String ssid_temp = "";
    String username, password, HOST = "192.168.1.27";
    MyFTPClientFunctions clientFunctions;
    static MyFTPClientFunctions ftpclient;
    public static final String ssid_default = "4ipNet_Internan";
    ProgressDialog pd;
    Handler handler;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            WifiManager wifimanage = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiinfo = wifimanage.getConnectionInfo();
            ssid_temp = wifiinfo.getSSID();
        }
        editLogin = (EditText) findViewById(R.id.editTextLogin);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        enter = (Button) findViewById(R.id.btnEnter);
        enter.setTextColor(getResources().getColor(R.color.DimGrey));
        editLogin.addTextChangedListener(loginTextWatcher);
        editPassword.addTextChangedListener(loginTextWatcher);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editLogin.getText().toString();
                password = editPassword.getText().toString();
                clientFunctions = new MyFTPClientFunctions();
                    try{
                        connectToFTPAdress();
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    i.putExtra("User", username);
                    startActivity(i);
                    }
                    catch (Exception e){
                        Toast.makeText(MainActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        if (!ssid_temp.equals('"' + ssid_default + '"')) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Незахищене інтернет-з'єднання!")
                    .setMessage("Підключіться до захищеної мережі для доступу до системи!")
                    .setIcon(R.drawable.wifi)
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted keep going status
                    Log.d("mac", "PERMISSION GRANTED");
                    WifiManager wifimanage = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    WifiInfo wifiinfo = wifimanage.getConnectionInfo();
                    ssid_temp = wifiinfo.getBSSID();//Get the mac address of the currently connected network;
                } else {
                    Log.d("mac", "PERMISSION DENIED");
                }
        }
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String login = editLogin.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            if (login.isEmpty() || password.isEmpty())
                enter.setTextColor(getResources().getColor(R.color.DimGrey));
            else enter.setTextColor(getResources().getColor(R.color.White));
            enter.setEnabled(!login.isEmpty() && !password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            String login = editLogin.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            if (login.isEmpty() || password.isEmpty())
                enter.setTextColor(getResources().getColor(R.color.DimGrey));
            else enter.setTextColor(getResources().getColor(R.color.White));
            enter.setEnabled(!login.isEmpty() && !password.isEmpty());
        }
    };

    public void connectToFTPAdress() {
        handler = new Handler();
        status = false;
        if (HOST.length() < 1) {
            Toast.makeText(MainActivity.this, "Please Enter Host Address!",
                    Toast.LENGTH_LONG).show();
        } else if (username.length() < 1) {
            Toast.makeText(MainActivity.this, "Please Enter User Name!",
                    Toast.LENGTH_LONG).show();
        } else if (password.length() < 1) {
            Toast.makeText(MainActivity.this, "Please Enter Password!",
                    Toast.LENGTH_LONG).show();
        } else {
            pd = ProgressDialog.show(MainActivity.this, "", "Connecting...",
                    true, false);
            new Thread(new Runnable() {
                public void run() {
                    status = MyFTPClientFunctions.ftpclient.ftpConnect(HOST, username, password, 21);
                    if (status == true) {
                        Log.d(TAG, "Connection Success");
                        handler.sendEmptyMessage(0);
                    } else {
                        Log.d(TAG, "Connection failed");
                        handler.sendEmptyMessage(-1);
                    }
                }
            }).start();
        }
        pd.dismiss();
    }
}

