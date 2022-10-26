package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LocationAdapter extends ArrayAdapter<LocationModel> {
    private final LayoutInflater layoutInflater;

    public LocationAdapter(Context context, int resource, ArrayList<LocationModel> locationModels) {
        super(context, resource, locationModels);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.location_list_item, viewGroup, false);
        }

        LocationModel item = getItem(i);

        ((TextView) convertView.findViewById(R.id.positionAndTimeView)).setText("Odczyt nr: " + ++i +  " - " + item.getMeasureTime() + " - Prędkość: " + item.getSpeed());
        ((TextView) convertView.findViewById(R.id.latitudeView)).setText("Lat: " + item.getLatitude());
        ((TextView) convertView.findViewById(R.id.longitudeView)).setText("Lon: " + item.getLongitude());
        ((TextView) convertView.findViewById(R.id.bearingView)).setText("Bearing: " +item.getBearing());
        ((TextView) convertView.findViewById(R.id.distanceView)).setText("Distance: " + item.getDistance());

        return convertView;
    }
}
