package com.carlos.theculinaryapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class IngredientItem {
    private String name;
    private String uuid;
    private ArrayList<IngredientMapInformation> mapInfo;

    public ArrayList<IngredientMapInformation> getMapInfo() {
        return mapInfo;
    }

    public void setMapInfo(ArrayList<IngredientMapInformation> mapInfo) {
        this.mapInfo = mapInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
