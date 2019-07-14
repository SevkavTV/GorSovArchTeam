package com.example.gorsovarch;

import android.app.Activity;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

public class RecentAppView extends Activity {
    String name, documentName;
    int imageId;
    RecentAppView(String name, int imageId, String documentName){
        this.name = name;
        this.imageId = imageId;
        this.documentName = documentName;
    }
    String getName(){
        return  name;
    }

}
