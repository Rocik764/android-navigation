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

/**
 * główny widok aplikacji, z listą -> (res/layout) widoku w activity_main.xml
 */
public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LocationAdapter locationAdapter;
    private ArrayList<LocationModel> locationModels;

    /**
     * najpierw wywołuje się ta metoda, w niej ustawiane są wszystkie potrzebne zmienne, głównie te powyżej:
     *
     * locationModels - array lista z modelem naszych pomiarów z gps, która przekazujemy do adaptera LocationAdapter
     * adapter ten jest tylko takim jakby szkieletem dla ListView z danymi, on sam nie wyświetla danych tylko
     * ustawia je w ListView locationListView
     *
     * locationManager - to manager lokalizacji, kod podany był w zadaniu odnośnie jego
     *
     * locationListener - prywatna klasa też podana w zadaniu w pliku co mi wysłałeś, ten listener służy za obsługę
     * wydarzeń związanych z lokalizacją, czyli jak się coś tam zmienia to on zrobi jakąś czynność
     */
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

    /**
     * nadpisana metoda odpytująca użytkownika o zezwolenie na korzystanie z lokalizacji, user musi zezwolić by cokolwiek działało
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        registerListener();
    }

    /**
     * to też podane w zadaniu było, po prostu zarejestrowanie naszego listenera z tej prywatnej klasy w managerze lokalizacji
     */
    private void registerListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    /**
     * tutaj utworzony model z danymi dodajemy do array listy przekazanej do adaptera a potem informujemy adapter, że była zmiana w liście
     * po prostu tutaj dodają się nowe dane z listenera lokalizacji a potem refreshuje się lista na widoku
     * size >= 10 bo w zadaniu jest "ostatnie 10 lub więcej" dlatego usuwam pierwotny element i dodaje nowy więc lista się zmienia co chwilę
     * ale wciąż nie ma więcej niż 10 elementów
     */
    protected void updateLocationList(LocationModel locationModel) {
        if (locationModels.size() >= 10) {
            locationModels.remove(0);
        }

        locationModels.add(locationModel);
        locationAdapter.notifyDataSetChanged();
    }

    /**
     * prywatna klasa listenera podana w zadaniu, tutaj tylko onLocationChanged() i processLocation() są użyteczne ale nie można usunąć tych innych
     * bo apka się wywala wtedy, tutaj tworzy się nasz model do array listy wyświetlanej na liście w widoku (kolejny element z danymi)
     * dodałem czas pomiaru, lat, lon, prędkość, bearing (kierunek?) oraz dystans tak jak było wymagane w #3 zadania
     */
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
