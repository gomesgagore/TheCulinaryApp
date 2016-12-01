package com.carlos.theculinaryapp;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class IngredientMapInformation implements Serializable {
    private String seller;
    //private LatLng latlong;
    private double latitude;
    private double longitude;
    private String price;

    public IngredientMapInformation(String seller, double latitude, double longitude, String information) {
        this.seller = seller;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price =  information;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
