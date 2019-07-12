package com.example.gorsovarch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.gorsovarch.Files.openFile;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
   List<RecentAppView> recentApps;
    ImageView doc, adobe, chrome;
    LinearLayout recent;
    final int DocumentsActivityID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        Runnable runnable = new CountDownRunner();
        Thread currTime = null;
        doc = (ImageView) findViewById(R.id.documents);
        adobe = (ImageView) findViewById(R.id.adobe);
        chrome = (ImageView) findViewById(R.id.chrome);
        doc.setOnClickListener(this);
        adobe.setOnClickListener(this);
        chrome.setOnClickListener(this);
        currTime = new Thread(runnable);
        currTime.start();
        recentApps = new ArrayList<>();
        recentApps.add(new RecentAppView("Empty", 1, "Empty"));
        recentApps.add(new RecentAppView("One", 1, "№1"));
        recentApps.add(new RecentAppView("Two", 2, "№2"));
        recentApps.add(new RecentAppView("Three", 1, "№3"));
        recentApps.add(new RecentAppView("Four", 2, "№4"));
        recentApps.add(new RecentAppView("Five", 1, "№5"));
        recentApps.add(new RecentAppView("Six", 2, "№6"));
        recentApps.add(new RecentAppView("Seven", 1, "№7"));
        recentApps.add(new RecentAppView("Eight", 2, "№8"));
        recentApps.add(new RecentAppView("Nine", 1, "№9"));
        recentApps.add(new RecentAppView("Ten", 2, "№10"));
        recentApps.add(new RecentAppView("Eleven", 1, "№11"));
        recentApps.add(new RecentAppView("Twelve", 2, "№12"));
        recent = (LinearLayout) findViewById(R.id.linearLayoutRecentApps);
        for(int i = 0; i < recentApps.size(); i++){
            recent.addView(getView(i));
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras;
            switch (requestCode) {
                case DocumentsActivityID:
                    extras = data.getExtras();
                    int maxIndex = extras.getInt("MAXINDEX");
                    for(int i = 0; i < maxIndex; i++){
                        String currName = extras.getString(i + "NAME");
                        int currIdImage = extras.getInt(i + "IDIMAGE");
                        String docName = extras.getString(i + "DOCUMENTNAME");
                        recentApps.add(1, new RecentAppView(currName, currIdImage, docName));
                        recent.addView(getView(1), 1);
                    }
                  //  List<RecentAppView> currApps = extras.getParcelable("RECENTAPPS");
                    //for(int i = 0; i < currApps.size(); i++)
                    //{
                      //  recentApps.add(1, currApps.get(i));
                       // recent.addView(getView(1), 1);
                   // }
                   // new_number = extras.getString("NUMBER");
                   // new_email = extras.getString("EMAIL");
                   // new_telegram = extras.getString("TELEGRAM");
                   // contacts.add(new Contact(new_name, new_number, new_email, new_telegram));
                   // uploadList();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view){
        Intent i;
        switch (view.getId()){
            case R.id.documents:
                i = new Intent(this, DocumentsActivity.class);
                startActivityForResult(i, DocumentsActivityID);

                break;
            case R.id.adobe:
                try {
                    Context ctx = this;
                    i = ctx.getPackageManager().getLaunchIntentForPackage("com.adobe.reader");
                    ctx.startActivity(i);
                }catch(Exception e){
                    i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("market://details?id=com.adobe.reader"));
                    startActivity(i);
                }
                break;
            case R.id.chrome:
                File file = new File("/storage/emulated/0/Download/lec8.pdf");
                Context context = this;
                openFile(file, context);
                break;
        }
    }
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    TextView txtCurrTime = (TextView)findViewById(R.id.textTime);
                    Calendar cal = Calendar.getInstance();
                    int hours = cal.get(Calendar.HOUR_OF_DAY);
                    int minutes = cal.get(Calendar.MINUTE);
                    String currHours = "" + hours;
                    String currMinutes = "" + minutes;
                    if(hours >= 0 && hours <= 9){
                        currHours = "0" + currHours;
                    }
                    if(minutes >= 0 && minutes <= 9){
                        currMinutes = "0" + currMinutes;
                    }
                    String curTime = currHours + ":" + currMinutes;
                    txtCurrTime.setText(curTime);
                }catch (Exception e) {}
            }
        });


    }


    class CountDownRunner implements Runnable{
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }
    public View getView(int index){
        View currView;
        LayoutInflater inflater1 = getLayoutInflater();
        if(index == 0){
            currView = inflater1.inflate(R.layout.recent_app_empty, null, false);
            ((TextView) currView.findViewById(R.id.app_name)).setText(recentApps.get(index).name);
            ((TextView) currView.findViewById(R.id.doc_name)).setText(recentApps.get(index).documentName);
            ((ImageView) currView.findViewById(R.id.image)).setImageResource(R.drawable.i1);
        }
       else {
            currView = inflater1.inflate(R.layout.recent_app, null, false);
            ((TextView) currView.findViewById(R.id.app_name)).setText(recentApps.get(index).name);
            ((TextView) currView.findViewById(R.id.doc_name)).setText(recentApps.get(index).documentName);
            ((ImageView) currView.findViewById(R.id.image)).setImageResource(R.drawable.i1);
        }
        return currView;
    }
}
