package com.example.ardasatata.testmap2;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ardasatata on 4/27/18.
 */

public class Pedagang {
    LatLng latlng;
    boolean status;
    String namaDagang;
    String info;

    public Pedagang(){

    }

    public Pedagang(LatLng latlng, boolean status, String namaDagang, String info) {
        this.latlng = latlng;
        this.status = status;
        this.namaDagang = namaDagang;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNamaDagang() {
        return namaDagang;
    }

    public void setNamaDagang(String namaDagang) {
        this.namaDagang = namaDagang;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
