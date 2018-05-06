package com.example.ardasatata.testmap2;



/**
 * Created by ardasatata on 4/27/18.
 */

public class Pedagang {
    LatLng latlng;
    boolean status;
    String namaDagang;
    String info;
    String id;
    String email;
    Target target;

    public Pedagang(){

    }

    public Pedagang(String id, String email) {
        this.id = id;
        this.email = email;
        this.latlng = new LatLng(0.0,0.0);
        this.target = new Target(null,null);
        this.namaDagang = "";
        this.info = "";
        this.status = false;
    }

    public Pedagang(LatLng latlng, boolean status, String namaDagang, String info) {
        this.latlng = latlng;
        this.status = status;
        this.namaDagang = namaDagang;
        this.info = info;
    }

    public Pedagang(String id,LatLng latlng, boolean status, String namaDagang, String info) {
        this.id = id;
        this.latlng = latlng;
        this.status = status;
        this.namaDagang = namaDagang;
        this.info = info;
    }

    public Pedagang(LatLng latlng, boolean status, String namaDagang, String info, String id, String email, Target target) {
        this.latlng = latlng;
        this.status = status;
        this.namaDagang = namaDagang;
        this.info = info;
        this.id = id;
        this.email = email;
        this.target = target;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
