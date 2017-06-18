package com.example.ine5424.smartufsc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anna on 5/27/17.
 */

public class BusLineAdapter extends ArrayAdapter<BusLine> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<BusLine> mDataSource;

    public BusLineAdapter(Context context, ArrayList<BusLine> items) {
        super(context, R.layout.activity_main, items);
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public BusLine getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDataSource.get(position).getCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        BusLine l = getItem(position);
        textView.setText(l.getCode() + " " + l.getName());

        return rowView;
    }
}
