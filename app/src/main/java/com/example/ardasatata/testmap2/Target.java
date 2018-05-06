package com.example.ardasatata.testmap2;

/**
 * Created by ardasatata on 5/4/18.
 */

public class Target {

    User user;
    LatLng latLng;

    public Target() {
    }

    public Target(User user, LatLng latLng) {
        this.user = user;
        this.latLng = latLng;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
