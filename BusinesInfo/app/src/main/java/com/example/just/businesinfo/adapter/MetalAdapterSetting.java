package com.example.just.businesinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.just.businesinfo.Entity.MetalDataSet;
import com.example.just.businesinfo.R;
import com.example.just.businesinfo.connect.DatabaseHandler;

import java.util.ArrayList;

public class MetalAdapterSetting extends BaseAdapter {

    private DatabaseHandler db;
    private LayoutInflater lInflater;
    private ArrayList<MetalDataSet> objects;

    public MetalAdapterSetting(Context context, ArrayList<MetalDataSet> products) {
        objects = products;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DatabaseHandler(context);
    }

    // ToDo
    private class ViewHolder {

        CheckBox checkBox;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.list_metal_setting, parent, false);
        }

        holder = new ViewHolder();
        holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        convertView.setTag(holder);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        final MetalDataSet p = getMetalDataSet(position);
        ((TextView) convertView.findViewById(R.id.name)).setText(p.getName());
        ((TextView) convertView.findViewById(R.id.nameEng)).setText(p.getNameEng());
        ((TextView) convertView.findViewById(R.id.nominal)).setText(p.getNominal());

        if (p.getStatus().equals("true")) {

            ((CheckBox) convertView.findViewById(R.id.checkBox)).setChecked(true);
        } else {
            ((CheckBox) convertView.findViewById(R.id.checkBox)).setChecked(false);
        }

        final ViewHolder finalHolder = holder;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.checkBox.isChecked()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.updateMetalStatus(p.getId(), true);
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.updateMetalStatus(p.getId(), false);
                        }
                    }).start();
                }
            }
        });
        return convertView;
    }

    private MetalDataSet getMetalDataSet(int position) {
        return ((MetalDataSet) getItem(position));
    }
}
