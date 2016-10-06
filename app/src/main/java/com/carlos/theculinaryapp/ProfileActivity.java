package com.carlos.theculinaryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {
    private GridView customListView;
    private ListAdapter customListAdapterRecipes;
    private ListAdapter customListAdapterPeople;

    private ProfileItem[] recipes = new ProfileItem[]{
            new ProfileItem("food1", "food1"), new ProfileItem("food2", "food2"),
            new ProfileItem("food3", "food3"), new ProfileItem("food4", "food4"),
            new ProfileItem("food5", "food5"), new ProfileItem("food6", "food6")
    };

    private ProfileItem[] people = new ProfileItem[]{
            new ProfileItem("Person 1", "people1"), new ProfileItem("Person 1", "people2"),
            new ProfileItem("Person 1", "people3"), new ProfileItem("Person 1", "people4"),
            new ProfileItem("Person 1", "people5"), new ProfileItem("Person 1", "people6")
    };
    private String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //TODO
        //if profile is user, show "go to your profile" button on menu.

        SharedPreferences sp1 = getSharedPreferences("ProfileView", 0);
        String spProfileName = sp1.getString("userRealName" ,null);
        String spProfilePic = sp1.getString("profilePicUrl" ,null);
        ImageView profilePic = (ImageView) findViewById(R.id.profile_image);
        TextView profileNameText = (TextView) findViewById(R.id.profile_text);
        URL url = null;
        if(spProfilePic != null)
            try {
                url = new URL(spProfilePic);
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                profilePic.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            }
        else profilePic.setImageResource(getResources().getIdentifier("people2", "drawable", getPackageName()));

        if(spProfileName != null || !spProfileName.equals(""))profileNameText.setText(spProfileName);
        else profileNameText.setText("No Name");

        ProfileItem[] temp1 = Arrays.copyOf(recipes, recipes.length + 1);
        temp1[temp1.length-1] = new ProfileItem("Add a recipe", "plus");
        recipes = temp1;

        ProfileItem[] temp2 = Arrays.copyOf(people, people.length + 1);
        temp2[temp2.length-1] = new ProfileItem("Add a person", "plus");
        people = temp2;

        customListAdapterPeople = new CustomAdapterProfile(this, people);
        customListAdapterRecipes = new CustomAdapterProfile(this, recipes);

        customListView = (GridView) findViewById(R.id.profile_list);
        customListView.setAdapter(customListAdapterRecipes);


    }

    public void setFriendsList(View v){
        customListView.setAdapter(customListAdapterPeople);
    }
    public void setRecipesList(View v){
        customListView.setAdapter(customListAdapterRecipes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                doLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doLogout() {
        SharedPreferences sp1 = this.getSharedPreferences("AuthInfo", Context.MODE_PRIVATE);
        sp1.edit().clear().commit();
        Intent k = new Intent(ProfileActivity.this, LoginActivity.class);
        k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(k);
        finish();
    }


}
