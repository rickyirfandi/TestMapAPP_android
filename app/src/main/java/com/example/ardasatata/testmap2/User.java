package com.example.ardasatata.testmap2;

/**
 * Created by ardasatata on 5/1/18.
 */

public class User {

    String id;
    String name;
    String email;
    String alamat;
    LatLng latLng;

    public User() {
    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
