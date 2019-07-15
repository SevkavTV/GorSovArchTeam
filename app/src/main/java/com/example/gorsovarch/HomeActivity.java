package com.example.gorsovarch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.gorsovarch.Files.openFile;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    List<RecentAppView> recentApps;
    ArrayList<String> arrList;
    Button closeAll;
    Context context;
    LinearLayout recent;
    boolean currCheck = false;
    final int DocumentsActivityID = 1;
    ImageView doc, adobe, chrome, profile;
    SlidingUpPanelLayout slup;
    FragmentManager fm;
    ProgressDialog pd;
    Handler handler;
    TextView tv;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        getSupportActionBar().hide();
        Runnable runnable = new CountDownRunner();
        Thread currTime = null;
        doc = (ImageView) findViewById(R.id.documents);
        adobe = (ImageView) findViewById(R.id.adobe);
        chrome = (ImageView) findViewById(R.id.chrome);
        closeAll = (Button)findViewById(R.id.button);
        closeAll.setOnClickListener(this);
        profile = (ImageView) findViewById(R.id.profile);
        slup = findViewById(R.id.slup);
        doc.setOnClickListener(this);
        adobe.setOnClickListener(this);
        chrome.setOnClickListener(this);
        profile.setOnClickListener(this);
        tv = (TextView)findViewById(R.id.userName);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("User");
        tv.setText("@"+username);
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
    arrList = new ArrayList<String>();
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
                        int size = recentApps.size();
                        for(int j = 1; j < recentApps.size(); j++){
                            if(currName.equals(recentApps.get(j).getName())){
                             //  Toast.makeText(HomeActivity.this, recentApps.get(j).getName() + " " + currName, Toast.LENGTH_SHORT).show();
                                recent.removeViewAt(j);
                                recentApps.remove(j);
                                j--;
                            }
                        }
                        recentApps.add(1, new RecentAppView(currName, currIdImage, docName));
                        recent.addView(getView(1), 1);
                    }
                    /*for(int i = 1; i < recentApps.size() - 1; i++)
                        for(int j = i + 1; j < recentApps.size(); j++){
                            if( ( recentApps.get(i) ).getName() == ( recentApps.get(j) ).getName()){
                                recentApps.remove( j );
                                recent.removeViewAt( j );
                                j--;
                            //    File file = new File("/storage/emulated/0/Download/lec8.pdf");
                              //  Context context = this;
                               // openFile(file, context);
                            }
                        }*/
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
                    i = new Intent(this, ChromeActivity.class);
                    startActivity(i);
                break;
            case R.id.profile:
                View v1 = slup.getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bm = v1.getDrawingCache();
                int y = v1.getHeight()/32;
                int z = v1.getHeight()/14;
                int height = bm.getHeight() - y - z;
                Bitmap new_bm = Bitmap.createBitmap(bm, 0, y, bm.getWidth(), height);
                Bitmap blur_bm = BlurBuilder.blur(this, new_bm);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                blur_bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent in1 = new Intent(this, ExitActivity.class);
                in1.putExtra("background",byteArray);
                in1.putExtra("User", username);
                startActivity(in1);
                break;
            case R.id.button:
                for(int j = 1; j < recentApps.size(); j++){
                    boolean f = false;
                    for(int k = 0; k < arrList.size(); k++)
                        if(recentApps.get(j).getName().equals(arrList.get(k))) {f = true; break;}
                    if(!f) {recent.removeViewAt(j);recentApps.remove(j);j--;
                    }
                    currCheck = false;
                    closeAll.setVisibility(View.INVISIBLE);

                }

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

    // 150103
    public View getView(final int index){
        final View currView;
         final String currName;
        LayoutInflater inflater1 = getLayoutInflater();
        if(index == 0){
            currName = recentApps.get(index).name;
            currView = inflater1.inflate(R.layout.recent_app_empty, null, false);
            ((TextView) currView.findViewById(R.id.app_name)).setText(recentApps.get(index).name);
            ((TextView) currView.findViewById(R.id.doc_name)).setText(recentApps.get(index).documentName);
            ((ImageView) currView.findViewById(R.id.image)).setImageResource(R.drawable.i1);
            return  currView;
        }
       else {
            currName = recentApps.get(index).name;
            currView = inflater1.inflate(R.layout.recent_app, null, false);
            ((TextView) currView.findViewById(R.id.app_name)).setText(recentApps.get(index).name);
            ((TextView) currView.findViewById(R.id.doc_name)).setText(recentApps.get(index).documentName);
            ((ImageView) currView.findViewById(R.id.image)).setImageResource(R.drawable.i1);
            currView.findViewById(R.id.imageButtonCancel).setVisibility(View.INVISIBLE);
            currView.findViewById(R.id.imageButtonDelete).setVisibility(View.INVISIBLE);
            currView.findViewById(R.id.imageButtonLock).setVisibility(View.INVISIBLE);
        }
        currView.findViewById(R.id.imageButtonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 1; i < recentApps.size(); i++)
                    if(recentApps.get(i).getName().equals(currName)){
                        recent.removeViewAt(i);
                        recentApps.remove(i);
                        break;
                    }
                for(int i = 0; i < arrList.size(); i++)
                    if(arrList.get(i).equals(currName)) arrList.remove(i);
                    currCheck = false;
                    closeAll.setVisibility(View.INVISIBLE);
            }
        });
       currView.findViewById(R.id.imageButtonLock).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               currView.findViewById(R.id.imageButtonLock).setEnabled(false);
               currCheck = false;
               arrList.add(currName);
               currView.findViewById(R.id.imageButtonCancel).setVisibility(View.INVISIBLE);
               currView.findViewById(R.id.imageButtonDelete).setVisibility(View.INVISIBLE);
               currView.findViewById(R.id.imageButtonLock).setVisibility(View.INVISIBLE);
               closeAll.setVisibility(View.INVISIBLE);
           }
       });
        currView.findViewById(R.id.imageButtonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            currCheck = false;
                currView.findViewById(R.id.imageButtonCancel).setVisibility(View.INVISIBLE);
                currView.findViewById(R.id.imageButtonDelete).setVisibility(View.INVISIBLE);
                currView.findViewById(R.id.imageButtonLock).setVisibility(View.INVISIBLE);
                closeAll.setVisibility(View.INVISIBLE);
            }
        });
       currView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(!currCheck){
               Documents currDocument;
               for(int i = 0; i < recentApps.size(); i++){
                   if(recentApps.get(i).getName().equals(currName)) {recentApps.add(1, recentApps.get(i));
                   recent.addView(getView(1), 1);
                   recentApps.remove(i + 1);recent.removeViewAt(i + 1); break;}
               }
               File file = new File("/storage/emulated/0/Download/" + currName);
               openFile(file, context);}
           }
       });
       currView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               if(!currCheck){
                   currCheck = true;
                   currView.findViewById(R.id.imageButtonCancel).setVisibility(View.VISIBLE);
                   currView.findViewById(R.id.imageButtonDelete).setVisibility(View.VISIBLE);
                   currView.findViewById(R.id.imageButtonLock).setVisibility(View.VISIBLE);
                   closeAll.setVisibility(View.VISIBLE);
               return true;}
               return  false;
           }
       });
        return currView;
    }
}
