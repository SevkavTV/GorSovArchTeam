package com.example.gorsovarch;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import com.example.gorsovarch.DocumentsActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.gorsovarch.DocumentsActivity.fileList;
import static com.example.gorsovarch.DocumentsActivity.mishaXyuSosi;

public class MyFTPClientFunctions extends AppCompatActivity {
    ArrayList<String> FL;
    FTPFile[] ftpFiles;
    private static final String TAG = "MyFTPClientFunctions";
    public static FTPClient mFTPClient;
    public static MyFTPClientFunctions ftpclient = new MyFTPClientFunctions();
    public boolean ftpConnect(String host, String username, String password,
                              int port) {
        boolean status = false;
        try {
            mFTPClient = new FTPClient();
            mFTPClient.connect(host, port);
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                status = mFTPClient.login(username, password);
                if(status){
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
                return status;}
            }
        } catch (Exception e) {
            Log.d(TAG, "Error: could not connect to host " + host);
        }
        return status;
    }
    public boolean ftpDisconnect() {
        try {
            //mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
            return false;
        }
    }
    public String ftpGetCurrentWorkingDirectory() {
        try {
            String workingDir = mFTPClient.printWorkingDirectory();
            return workingDir;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not get current working directory.");
        }
        return null;
    }
  /*  public ArrayList<String> ftpPrintFilesList(final String dir_path) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                FTPFile[] ftpFiles = new FTPFile[0];
                try {
                    ftpFiles = mFTPClient.listFiles(dir_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int length = ftpFiles.length;
                for (int i = 0; i < length; i++) {
                    String name = ftpFiles[i].getName();
                    System.out.println("SOMEFILE"+name);
                    DocumentsActivity.fileList.add(name);

                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        return DocumentsActivity.fileList;

    }*/
    public boolean ftpDownload(String srcFilePath, String desFilePath) {
        boolean status = false;
        try {
            FileOutputStream desFileStream = new FileOutputStream(desFilePath);
            status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
            desFileStream.close();
            return status;
        } catch (Exception e) {
            Log.d(TAG, "download failed");
        }
        return status;
    }
    public ArrayList<String> ftpPrintFilesList(final String dir_path) {

       // final ArrayList<String> fileList = new ArrayList<>();
        FL = new ArrayList<>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    ftpFiles = mFTPClient.listFiles(dir_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int length = ftpFiles.length;
                //System.out.println("MISHA SOSI CHLEN !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + length);
               // Toast.makeText(DocumentsActivity.class, length + "", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < length; i++) {
                    String name = ftpFiles[i].getName();
                    System.out.println("SOMEFILE"+name);
                    FL.add(name);
                    DocumentsActivity.mishaXyuSosi.add(name);
                  //  System.out.println(mishaXyuSosi.size() + " FL SIZE = <- ");
                  // if(i == length - 1) return;
                }
             //   DocumentsActivity.mishaXyuSosi = FL;
             //   return FL;
            }
        });
        t.start();
      /*  try {
            t.join();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
*/
        return FL;

    }
    public boolean ftpUpload(String srcFilePath, String desFileName,
                             String desDirectory, Context context) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFilePath);
            status = mFTPClient.storeFile(desFileName, srcFileStream);
            srcFileStream.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "upload failed: " + e);
        }
        return status;
    }
}