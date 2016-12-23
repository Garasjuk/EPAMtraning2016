package com.example.just.businesinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.just.businesinfo.Entity.MetalDataSet;
import com.example.just.businesinfo.R;
import java.util.ArrayList;

public class MetalAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private ArrayList<MetalDataSet> objects;

    public MetalAdapter(Context context, ArrayList<MetalDataSet> products) {
        objects = products;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.list_metal, parent, false);
        }

        final MetalDataSet p = getMetalDataSet(position);
        ((TextView) convertView.findViewById(R.id.Name)).setText(p.getName());
        ((TextView) convertView.findViewById(R.id.nameEng)).setText(p.getNameEng());
        ((TextView) convertView.findViewById(R.id.nominal)).setText(p.getNominal());
        ((TextView) convertView.findViewById(R.id.byn)).setText(p.getCertificateRubles());
        ((TextView) convertView.findViewById(R.id.usa)).setText(p.getBanksDollars());

        return convertView;
    }

    private MetalDataSet getMetalDataSet(int position) {
        return ((MetalDataSet) getItem(position));
    }
}
