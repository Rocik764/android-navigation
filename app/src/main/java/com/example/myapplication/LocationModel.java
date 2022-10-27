package com.example.myapplication;

/**
 * klasa odpowiedzialna za wymodelowanie danych dla naszej listy, są tu wszystkie dane jakie były wymagane
 * w zadaniu, czyli lat, long, dystans, kierunek, prędkość i jeszcze dodany czas pomiaru by było wiadomo
 * które jest które itp.
 */
public class LocationModel {
    private double latitude;
    private double longitude;
    private float bearing;
    private float distance;
    private float speed;
    private String measureTime;

    public LocationModel(double latitude, double longitude, float bearing, float distance, float speed, String measureTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
        this.distance = distance;
        this.speed = speed;
        this.measureTime = measureTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }
}
