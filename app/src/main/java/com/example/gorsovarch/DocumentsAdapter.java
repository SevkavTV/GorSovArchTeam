package com.example.gorsovarch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DocumentsAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Documents> data;
    private int layoutResourceId;


    DocumentsAdapter(Context context, ArrayList<Documents> data) {
        super(context, 0);
        this.context = context;
        this.data = data;
        this.layoutResourceId = R.layout.documentl_ist;

    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Documents getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        if (row == null) {
            LayoutInflater inflater =((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        TextView name = row.findViewById(R.id.name);
        ImageView image = row.findViewById(R.id.avatar);
        name.setText(data.get(position).name);
        if(data.get(position).downloaded)  image.setImageResource(R.drawable.done_icon);
        else  image.setImageResource(R.drawable.download_icon4);
        return row;
    }
}
