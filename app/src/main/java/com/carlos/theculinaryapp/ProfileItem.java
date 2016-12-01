package com.carlos.theculinaryapp;

public class ProfileItem {
    private String name;
    private String imageFile;
    private String useruuid;

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

    public String getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(String useruuid) {
        this.useruuid = useruuid;
    }
}
