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

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    List<RecentAppView> recentApps;
    ImageView doc, adobe, chrome;
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
        LinearLayout recent = (LinearLayout) findViewById(R.id.linearLayoutRecentApps);
        for(int i = 0; i < recentApps.size(); i++){
            recent.addView(getView(i));
        }
    }
    @Override
    public void onClick(View view){
        Intent i;
        switch (view.getId()){
            case R.id.documents:
                i = new Intent(this, DocumentsActivity.class);
                startActivity(i);
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
                openFile(file);
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
    private void openFile(File url) {
        Context context = this;
        try {

            Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }
}
