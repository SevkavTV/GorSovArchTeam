package com.example.gorsovarch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.gorsovarch.DocumentsActivity.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Files {

    static void openFile(File url, Context context) {
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
            check = true;
            Toast.makeText(context, "Файл не знайдено", Toast.LENGTH_SHORT).show();
        }
    }
 /* static  void writeFile() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    this.openFileOutput(FILENAME, MODE_APPEND)));

            // пишем данные
            bw.write("Содержимое файла");
            // закрываем поток
            bw.close();
          //  Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  static  void readFile() {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d(LOG_TAG, str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

 /*  static void writeFileSD(ArrayList<String> curr) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
         //   Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
       //Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            for(int i = 0; i < curr.size(); i++)
            bw.write(curr.get(i));
            // закрываем поток
           // bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   static ArrayList<String> readFileSD() {
       ArrayList<String> curr = new ArrayList<String>();
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return curr;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                curr.add(str);
               // Log.d(LOG_TAG, str);
            }
            return curr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return curr;
        } catch (IOException e) {
            e.printStackTrace();
            return curr;
        }
    }*/
}
