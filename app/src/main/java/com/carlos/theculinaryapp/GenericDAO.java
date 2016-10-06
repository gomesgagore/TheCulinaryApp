package com.carlos.theculinaryapp;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class GenericDAO {
    public static final String FIREBASE_URL = "https://simplifiedcoding.firebaseio.com/";
    private static GenericDAO singleton;
    private static String TAG = "DAO";
    private JSONObject mainObject;
    private URL url;
    private StringBuilder result;

    public static GenericDAO getInstance(){
        if(singleton == null)singleton = new GenericDAO();
        return singleton;
    }


    private GenericDAO(){
        try {
            //updateDataFirebase("https://simplifiedcoding.firebaseio.com/");
            updateData("https://api.myjson.com/bins/3bze0");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void updateDataFirebase(String s) {

    }

    public void updateData(String newURL) throws JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection urlConnection = null;
        result = new StringBuilder();
        try {
            url = new URL(newURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                result.append(current);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {

                urlConnection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        mainObject = new JSONObject(result.toString());
    }

    public JSONObject getJSONObject() {
        return mainObject;
    }

    public JSONArray getAllUsers(){
        JSONArray usersArray;
        if(getInstance() != null){
            JSONObject j = getInstance().getJSONObject();
            if(j != null){
                try {
                    usersArray = j.getJSONArray("users");
                    return usersArray;
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return null;
    }

    public JSONArray getAllRecipes(){
        JSONArray recipesArray;
        if(getInstance() != null){
            JSONObject j = getInstance().getJSONObject();
            if(j != null){
                try {
                    recipesArray = j.getJSONArray("recipes");
                    return recipesArray;
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return null;
    }

    public UserInfo getUserInfo(String username){
        JSONArray users = getAllUsers();
        UserInfo u;
        if(users != null){
            for (int i = 0; i < users.length(); i++) {
                try {
                JSONObject j = users.getJSONObject(i);
                    if(j.getString("username").equals(username)){
                        u = new UserInfo(j.getString("name"), j.getString("profile_pic"));
                        return u;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    return null;
                }
            }
        }
        return null;
    }

    //TODO
    public boolean authUser(String username, String password){
        return false;
    }
}
