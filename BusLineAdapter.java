package com.example.ine5424.smartufsc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anna on 5/27/17.
 */

public class BusLineAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<BusLine> mDataSource;

    public BusLineAdapter(Context context, ArrayList<BusLine> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.activity_main, parent, false);
        // Get title element
  //      TextView titleTextView = (TextView) rowView.findViewById(R.id.list_item);

        BusLine busLine = (BusLine) getItem(position);

//        titleTextView.setText(busLine.getName());
//      subtitleTextView.setText(busLine.description);

        return rowView;
    }
}
