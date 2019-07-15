package com.example.gorsovarch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.gorsovarch.Files.openFile;

public class DocumentsActivity extends AppCompatActivity {
    ArrayList< Documents > ls;
    List<RecentAppView> recentapp;
    public static ArrayList<String> mishaXyuSosi;
    Context context;
    ListView lv;
    static public boolean check = false;
    public int MAXINDEX = 0;
    Intent intentBack;
    DocumentsAdapter documentsAdapter;
    ProgressDialog pd;
    public static ArrayList<String> fileList = new ArrayList<>();
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        context = this;
        handler = new Handler();
        mishaXyuSosi = new ArrayList<String>();
        getFTPFileList();
            Toast.makeText(this, "" + mishaXyuSosi.size(), Toast.LENGTH_SHORT).show();
            ls = new ArrayList<Documents>();
            for(int i = 0; i < DocumentsActivity.fileList.size(); i++) {
                ls.add(new Documents(DocumentsActivity.fileList.get(i), "2000", false));
            }
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
                      File file = new File("/storage/emulated/0/Download/" + p.name);
                      openFile(file, context);
                      if(!check){
                          intentBack.putExtra(MAXINDEX +"NAME", p.name);
                          intentBack.putExtra(MAXINDEX + "IDIMAGE", 1);
                          intentBack.putExtra(MAXINDEX + "DOCUMENTNAME", " xuy ");
                          MAXINDEX++;
                      }
                      check = false;
              }
          }
      });
        }

    @Override
    public void onBackPressed() {
        intentBack.putExtra("MAXINDEX", MAXINDEX);
        setResult(RESULT_OK, intentBack);
        finish();
    }
   private void getFTPFileList() {
        pd = ProgressDialog.show(DocumentsActivity.this, "", "Getting Files...",
                true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DocumentsActivity.fileList = MyFTPClientFunctions.ftpclient.ftpPrintFilesList("/");
                    handler.sendEmptyMessage(1);
                }catch (Exception e){
                    Toast.makeText(DocumentsActivity.this, "Cannot get list of documents", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        pd.dismiss();
    }
}
