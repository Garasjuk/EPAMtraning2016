package com.example.just.businesinfo.adapter;

import android.content.Context;
import android.util.Log;
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

    public static final String LOG_TAG = "MetalAdapterSetting.java";

    DatabaseHandler db;
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<MetalDataSet> objects;

    public MetalAdapterSetting(Context context, ArrayList<MetalDataSet> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DatabaseHandler(context);
    }

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


        ViewHolder holder = null;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.list_metal_setting, parent, false);
        }

        holder = new ViewHolder();
        holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        convertView.setTag(holder);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Log.v(LOG_TAG, String.valueOf(cb.isChecked()));

            }
        });

        final MetalDataSet p = getMetalDataSet(position);
        ((TextView) convertView.findViewById(R.id.name)).setText(p.getName());
        ((TextView) convertView.findViewById(R.id.nameEng)).setText(p.getNameEng());
        ((TextView) convertView.findViewById(R.id.nominal)).setText(p.getNominal());
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

//        Log.v("BOX_ADAPTER", "Status:" + p.getStatus() );
        if (p.getStatus().equals("true")) {
            Log.v("id", Integer.toString(p.getId()));

            ((CheckBox) convertView.findViewById(R.id.checkBox)).setChecked(true);
//            Log.v("BOX_ADAPTER", "true 3");
        } else {
//            Log.v("BOX_ADAPTER", "false"  );

            ((CheckBox) convertView.findViewById(R.id.checkBox)).setChecked(false);
        }

        final ViewHolder finalHolder = holder;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (finalHolder.checkBox.isChecked()) {
                    Log.v(LOG_TAG, String.valueOf(p.getId()) + " " + p.getName());
//                    db.updateStatus(p.getId(), true);
                } else {
                    Log.v(LOG_TAG, String.valueOf(p.getId()) + " " + p.getName());
//                    db.updateStatus(p.getId(),false);
                }
            }
        });

        return convertView;
    }

    MetalDataSet getMetalDataSet(int position) {
        return ((MetalDataSet) getItem(position));
    }
}
