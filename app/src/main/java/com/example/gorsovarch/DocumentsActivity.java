package com.example.gorsovarch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.gorsovarch.Files.openFile;

public class DocumentsActivity extends AppCompatActivity {
    ArrayList< Documents > ls;
    List<RecentAppView> recentapp;
    Context context;
    ListView lv;
    static public boolean check = false;
    public int MAXINDEX = 0;
    Intent intentBack;
    DocumentsAdapter documentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        context = this;
            ls = new ArrayList<Documents>();
            ls.add(new Documents("lec8.pdf", "2000", false));
            ls.add(new Documents("contest-10122-uk.pdf", "30000", false));
            ls.add(new Documents("storya-godinnika.docx", "4000000", false));
            ls.add(new Documents("alfutova.pdf", "2000", false));
            ls.add(new Documents("Голос-0001.mp3", "30000", false));
            ls.add(new Documents("300000000", "4000000", false));
            ls.add(new Documents("100000000", "2000", false));
            ls.add(new Documents("20000000", "30000", false));
            ls.add(new Documents("300000000", "4000000", false));
            ls.add(new Documents("100000000", "2000", false));
            ls.add(new Documents("20000000", "30000", false));
            ls.add(new Documents("300000000", "4000000", false));
            lv = (ListView) findViewById(R.id.listView);
            documentsAdapter = new DocumentsAdapter(this, ls);
            lv.setAdapter(documentsAdapter);
            intentBack = new Intent();
      lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              final int INDEX = i;
              final Documents p = ls.get(i);
              if(!ls.get(i).downloaded){
                  AlertDialog.Builder builder = new AlertDialog.Builder(DocumentsActivity.this);
                  builder.setTitle("Завантажити файл " + p.name + "?")
                          .setPositiveButton("Так",
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(DialogInterface dialog, int id) {
                                         p.downloaded = true;
                                          ls.set(INDEX, p);
                                          documentsAdapter = new DocumentsAdapter(DocumentsActivity.this, ls);
                                          lv.setAdapter(documentsAdapter);
                                         dialog.cancel();
                                      }
                                  })
                          .setNegativeButton("Ні",
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(DialogInterface dialog, int id) {
                                          dialog.cancel();
                                      }
                                  });
                  AlertDialog alert = builder.create();
                  alert.show();
              }
              else{
                //  recentApps.add(new RecentAppView(p.name, 1, " xuy "));
                  //recentapp.add(new RecentAppView(p.name, 1, " xuy "));

                      File file = new File("/storage/emulated/0/Download/" + p.name);
                      openFile(file, context);
                      if(!check){
                          intentBack.putExtra(MAXINDEX +"NAME", p.name);
                          intentBack.putExtra(MAXINDEX + "IDIMAGE", 1);
                          intentBack.putExtra(MAXINDEX + "DOCUMENTNAME", " xuy ");
                          MAXINDEX++;
                      }
                      check = false;
                //  LinearLayout recent = (LinearLayout) findViewById(R.id.linearLayoutRecentApps);
              //   recent.addView(getView(1), 1);
              }
          }
      });
        }

    @Override
    public void onBackPressed() {
      //  intentBack.putExtra("RECENTAPPS", (Parcelable) recentapp);
        intentBack.putExtra("MAXINDEX", MAXINDEX);
        setResult(RESULT_OK, intentBack);
        finish();
     //   super.onBackPressed();
    }
}
