package com.example.gorsovarch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.gorsovarch.MyFTPClientFunctions;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import org.apache.commons.net.ftp.FTPClient;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button enter;
    Context context = this;
    String ssid_temp = "";
    //public static String username, password, HOST = "192.168.1.27";
    public static final String ssid_default = "4ipNet_Internan";
    private static final String TAG = "MainActivity";
    private static final String TEMP_FILENAME = "TAGtest.txt";
    private Context cntx = null;
    private MyFTPClientFunctions ftpclient = null;

    private EditText edtUserName, edtPassword;
    private ProgressDialog pd;

    private String[] fileList;

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (msg.what == 0) {
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                i.putExtra("User", edtUserName.getText().toString());
                startActivity(i);
                getFTPFileList();
            } else if (msg.what == 1) {
                showCustomDialog(fileList);
            } else if (msg.what == 2) {
                Toast.makeText(MainActivity.this, "Uploaded Successfully!",
                        Toast.LENGTH_LONG).show();
            } else if (msg.what == 3) {
                Toast.makeText(MainActivity.this, "Disconnected Successfully!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Неправильний логін чи парроль",
                        Toast.LENGTH_LONG).show();
            }

        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cntx = this.getBaseContext();
        getSupportActionBar().hide();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            WifiManager wifimanage = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiinfo = wifimanage.getConnectionInfo();
            ssid_temp = wifiinfo.getSSID();
        }
        edtUserName = (EditText) findViewById(R.id.editTextLogin);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        ftpclient = new MyFTPClientFunctions();
        enter = (Button) findViewById(R.id.btnEnter);
        enter.setTextColor(getResources().getColor(R.color.DimGrey));
        edtUserName.addTextChangedListener(loginTextWatcher);
        edtPassword.addTextChangedListener(loginTextWatcher);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline(MainActivity.this)) {
                    connectToFTPAddress();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Please check your internet connection!",
                            Toast.LENGTH_LONG).show();
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
                    ssid_temp = wifiinfo.getSSID();//Get the mac address of the currently connected network;
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
            String login = edtUserName.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (login.isEmpty() || password.isEmpty())
                enter.setTextColor(getResources().getColor(R.color.DimGrey));
            else enter.setTextColor(getResources().getColor(R.color.White));
            enter.setEnabled(!login.isEmpty() && !password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            String login = edtUserName.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (login.isEmpty() || password.isEmpty())
                enter.setTextColor(getResources().getColor(R.color.DimGrey));
            else enter.setTextColor(getResources().getColor(R.color.White));
            enter.setEnabled(!login.isEmpty() && !password.isEmpty());
        }
    };

    private void connectToFTPAddress() {

        final String host = "192.168.1.27";
        final String username = edtUserName.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();

        if (host.length() < 1) {
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
                    boolean status = false;
                    status = ftpclient.ftpConnect(host, username, password, 21);
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
    }

    private void getFTPFileList() {
       /* pd = ProgressDialog.show(MainActivity.this, "", "Getting Files...",
                true, false);

        new Thread(new Runnable() {

            @Override
            public void run() {
                fileList = ftpclient.ftpPrintFilesList("/");
                handler.sendEmptyMessage(1);
            }
        }).start();*/
    }

    public void createDummyFile() {

        try {
            File root = new File(Environment.getExternalStorageDirectory(),
                    "TAGFtp");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, TEMP_FILENAME);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append("Hi this is a sample file to upload for android FTP client example from TheAppGuruz!");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved : " + gpxfile.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void showCustomDialog(String[] fileList) {
        // custom dialog
       /* final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("/ Directory File List");

        TextView tvHeading = (TextView) dialog.findViewById(R.id.tvListHeading);
        tvHeading.setText(":: File List ::");

        if (fileList != null && fileList.length > 0) {
            ListView listView = (ListView) dialog
                    .findViewById(R.id.lstItemList);
            ArrayAdapter<String> fileListAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, fileList);
            listView.setAdapter(fileListAdapter);
        } else {
            tvHeading.setText(":: No Files ::");
        }

        Button dialogButton = (Button) dialog.findViewById(R.id.btnOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();*/
    }
}
