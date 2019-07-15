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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static com.example.gorsovarch.Files.openFile;

public class DocumentsActivity extends AppCompatActivity {
    final static String LOG_TAG = "myLogs";

    final static String FILENAME = "file";

    final static String DIR_SD = "MyFiles";
    final static String FILENAME_SD = "fileSD";
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
            ArrayList<String> c = readFile();
         //  if(c.size() > 1) Toast.makeText(this, c.get(1), Toast.LENGTH_SHORT).show();
            for(int i = 0; i < c.size(); i++)
                if(c.get(i).equals("lec8.pdf"))  {ls.add(new Documents("lec8.pdf", "2000", true));break;}
          // else ls.add(new Documents("lec8.pdf", "2000", false));
        for(int i = 0; i < c.size(); i++)
            if(c.get(i).equals("contest-10122-uk.pdf"))  {ls.add(new Documents("contest-10122-uk.pdf", "2000", true));break;}
         //   else ls.add(new Documents("contest-10122-uk.pdf", "2000", false));
          //  ls.add(new Documents("contest-10122-uk.pdf", "30000", false));
        for(int i = 0; i < c.size(); i++)
            if(c.get(i).equals("storya-godinnika.docx")) { ls.add(new Documents("storya-godinnika.docx", "2000", true));break;}
            //else ls.add(new Documents("storya-godinnika.docx", "2000", false));
          //  ls.add(new Documents("storya-godinnika.docx", "4000000", false));
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
                                          //ArrayList<String> curr = readFile();
                                          //curr.add(p.name);
                                          writeFile(p.name);
                                         dialog.cancel();
                                         Toast.makeText(DocumentsActivity.this, p.name, Toast.LENGTH_SHORT).show();
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
    void writeFile(String curr) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME,  MODE_APPEND )));
            // пишем данные
            bw.write(curr + '\n');
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> readFile() {
        ArrayList<String> curr = new ArrayList<String>();
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                curr.add(str);
                Log.d(LOG_TAG, str);
            }
            return curr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return curr;
        } catch (IOException e) {
            e.printStackTrace();
            return curr;
        }
    }
}
