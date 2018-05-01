package com.example.ardasatata.testmap2;

/**
 * Created by ardasatata on 5/2/18.
 */



public class LatLng {

    private Double latitude;
    private Double longitude;

    public LatLng() {

    }

    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
