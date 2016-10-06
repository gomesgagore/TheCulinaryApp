package com.carlos.theculinaryapp;

public class ProfileItem {
    private String name;
    private String imageFile;

    public ProfileItem(String name, String imageFile) {
        this.name = name;
        this.imageFile = imageFile;
    }

    public String getName() {
        return name;
    }

    public String getImageFile() {
        return imageFile;
    }
}
