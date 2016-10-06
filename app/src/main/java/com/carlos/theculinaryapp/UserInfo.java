package com.carlos.theculinaryapp;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfo {
    private String name;
    private String profile_pic_url;

    public UserInfo(String name, String profile_pic_url) {
        this.name = name;
        this.profile_pic_url = profile_pic_url;
    }

    public String getName() {
        return name;
    }

    public String getProfilePicUrl() {
        return profile_pic_url;
    }

    public static void setProfileView(Context c, UserInfo u){
        SharedPreferences sp1 = c.getSharedPreferences("ProfileView", 0);
        SharedPreferences.Editor ed = sp1.edit();
        ed.putString("userRealName", u.getName());
        ed.putString("profilePicUrl", u.getProfilePicUrl());
        ed.commit();
    }
}
