package com.tewari.covitiet;

public class CreateUser {
    public CreateUser()
    {}
    public CreateUser(String name, String email, String password, String date, String isSharing, String lat, String lng, String speed) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date = date;
        this.isSharing = isSharing;
        this.lat = lat;
        this.lng = lng;
        this.speed=speed;
    }

    public String name, email, password, date, isSharing, lat, lng, speed;

}
