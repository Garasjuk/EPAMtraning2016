package com.example.just.businesinfo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.just.businesinfo.R;
import com.example.just.businesinfo.connect.DatabaseHandler;
import com.example.just.businesinfo.connect.ParsedDataSet;

import java.util.ArrayList;

/**
 * Created by just on 27.10.2016.
 */

public class BoxAdapter extends BaseAdapter {

    DatabaseHandler db;
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ParsedDataSet> objects;

    public BoxAdapter(Context context, ArrayList<ParsedDataSet> products) {
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
            convertView = lInflater.inflate(R.layout.list_setting, parent, false);
        }

        holder = new ViewHolder();
        holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        convertView.setTag(holder);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Log.v("BOX_ADAPTER id", String.valueOf(cb.isChecked()));

            }
        });

        final ParsedDataSet p = getParsedDataSet(position);
        ((TextView) convertView.findViewById(R.id.nameEng)).setText(p.getCharCode());
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

//        Log.v("BOX_ADAPTER", "Status:" + p.getStatus() );
        if (p.getStatus().equals("true")) {
            Log.v("id", Integer.toString(p.getId()));

//            ( view.findViewById(R.id.checkBox)).set(p.getId());

            ((CheckBox) convertView.findViewById(R.id.checkBox)).setChecked(true);
//            Log.v("BOX_ADAPTER", "true 3");

        } else {
//            Log.v("BOX_ADAPTER", "false"  );

//            ((CheckBox) view.findViewById(R.id.checkBox)).setId(p.getId());
            ((CheckBox) convertView.findViewById(R.id.checkBox)).setChecked(false);
        }


        final ViewHolder finalHolder = holder;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (finalHolder.checkBox.isChecked()) {
                    Log.v("CheckBox true", String.valueOf(p.getId()) + " " + p.getCharCode());
                    db.updateStatus(p.getId(), true);
                } else {
                    Log.v("CheckBox false", String.valueOf(p.getId()) + " " + p.getCharCode());
                    db.updateStatus(p.getId(),false);
                }
            }
        });


//        checkBox.setOnCheckedChangeListener(
//                new CompoundButton.OnCheckedChangeListener() {
//
//                    @Override
//                    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
//                        if (isChecked) {
//                            Log.v("CheckBox true", String.valueOf(p.getId()) +" "+ p.getCharCode());
//                            db.updateStatus(p.getId(),true);
//                        } else {
//                            Log.v("CheckBox false", String.valueOf(p.getId()) +" "+ p.getCharCode() );
//                            db.updateStatus(p.getId(),false);
//                        }
//                    }
//                });
        return convertView;
    }


    ParsedDataSet getParsedDataSet(int position) {
        return ((ParsedDataSet) getItem(position));
    }
}
