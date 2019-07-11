package com.example.gorsovarch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    List<RecentAppView> recentApps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recentApps = new ArrayList<>();
        recentApps.add(new RecentAppView("Empty", 1, "Empty"));
        recentApps.add(new RecentAppView("xuy", 1, "perda"));
        recentApps.add(new RecentAppView("pizda", 2, "gandon"));
        recentApps.add(new RecentAppView("xuy", 1, "perda"));
        recentApps.add(new RecentAppView("pizda", 2, "gandon"));
        recentApps.add(new RecentAppView("xuy", 1, "perda"));
        recentApps.add(new RecentAppView("pizda", 2, "gandon"));
        recentApps.add(new RecentAppView("xuy", 1, "perda"));
        recentApps.add(new RecentAppView("pizda", 2, "gandon"));
        recentApps.add(new RecentAppView("xuy", 1, "perda"));
        recentApps.add(new RecentAppView("pizda", 2, "gandon"));
        recentApps.add(new RecentAppView("xuy", 1, "perda"));
        recentApps.add(new RecentAppView("pizda", 2, "gandon"));
        LinearLayout recent = (LinearLayout) findViewById(R.id.linearLayoutRecentApps);
        for(int i = 0; i < recentApps.size(); i++){
            recent.addView(getView(i));
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
