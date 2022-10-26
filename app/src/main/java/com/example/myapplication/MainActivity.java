package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LocationAdapter locationAdapter;
    private ArrayList<LocationModel> locationModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationModels = new ArrayList<>();
        locationAdapter = new LocationAdapter(this, R.layout.location_list_item, locationModels);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        ListView locationListView = findViewById(R.id.locationListView);
        locationListView.setAdapter(locationAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }

            return;
        }

        registerListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        registerListener();
    }

    private void registerListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    protected void updateLocationList(LocationModel locationModel) {
        if (locationModels.size() >= 10) {
            locationModels.remove(0);
        }

        locationModels.add(locationModel);
        locationAdapter.notifyDataSetChanged();
    }

    private class MyLocationListener implements LocationListener {
        Location prevLocation = null;

        private void processLocation(Location location) {
            String currentTime = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date());
            LocationModel locationModel = new LocationModel(location.getLatitude(), location.getLongitude(), 0, 0, location.getSpeed(), currentTime);

            if (prevLocation != null) {
                float bearing = prevLocation.bearingTo(location);
                float distance = prevLocation.distanceTo(location);
                locationModel.setBearing(bearing);
                locationModel.setDistance(distance);
            }

            prevLocation = location;
            updateLocationList(locationModel);
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            processLocation(location);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {}

        @Override
        public void onProviderDisabled(@NonNull String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
