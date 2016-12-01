package com.carlos.theculinaryapp;

import android.app.Application;
import android.os.StrictMode;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GenericDAO.getInstance().setFirebaseContext(this);
    }
}
