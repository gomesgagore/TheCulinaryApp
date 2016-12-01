package com.carlos.theculinaryapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private GridView customListView;
    private ListAdapter customListAdapterRecipes;
    private ListAdapter customListAdapterPeople;
    private GenericDAO dao = GenericDAO.getInstance();
    private FirebaseAuth auth = dao.getFirebaseAuth();
    private FirebaseUser user = auth.getCurrentUser();
    private String useruuid;
    private String TAG = "ProfileActivity";
    private static TextView profileNameTextView;
    private static ImageView profilePictureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(user == null)doLogout();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        useruuid = intent.getStringExtra("userUuid");
        if(useruuid == null)useruuid = user.getUid();
        dao.retriveProfileData(useruuid, this);

        profilePictureImageView = (ImageView) findViewById(R.id.profile_image);
        profileNameTextView = (TextView) findViewById(R.id.profile_text);

        customListView = (GridView) findViewById(R.id.profile_list);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_profile);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void setFriendsListByView(View v){
        customListView.setAdapter(customListAdapterPeople);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfileItem i = (ProfileItem)customListView.getItemAtPosition(position);
                Intent k = new Intent(ProfileActivity.this, ProfileActivity.class);
                k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                k.putExtra("userUuid", i.getUseruuid());
                startActivity(k);
            }
        });
    }
    public void setRecipesListByView(View v) {
        customListView.setAdapter(customListAdapterRecipes);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeItem i = (RecipeItem)customListView.getItemAtPosition(position);
                Intent k = new Intent(ProfileActivity.this, RecipeActivity.class);
                k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                k.putExtra("recipeUuid", i.getRecipeUuid());
                startActivity(k);
            }
        });
    }

    public void updateRecipeList(ArrayList<RecipeItem> profileItems){
        RecipeItem[] items = new RecipeItem[profileItems.size()];
        for (int i = 0; i < profileItems.size(); i++) {
            items[i] = profileItems.get(i);
        }
        customListAdapterRecipes = new CustomAdapterRecipe(this, items);
        customListView.setAdapter(customListAdapterRecipes);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeItem i = (RecipeItem)customListView.getItemAtPosition(position);
                Intent k = new Intent(ProfileActivity.this, RecipeActivity.class);
                k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                k.putExtra("recipeUuid", i.getRecipeUuid());
                startActivity(k);
            }
        });
    }

    public void updateProfileList(ArrayList<ProfileItem> profileItems){
        ProfileItem[] items = new ProfileItem[profileItems.size()];
        for (int i = 0; i < profileItems.size(); i++) {
            items[i] = profileItems.get(i);
        }
        customListAdapterPeople = new CustomAdapterProfile(this, items);
        customListView.setAdapter(customListAdapterPeople);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfileItem i = (ProfileItem)customListView.getItemAtPosition(position);
                Intent k = new Intent(ProfileActivity.this, ProfileActivity.class);
                k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                k.putExtra("userUuid", i.getUseruuid());
                startActivity(k);
            }
        });
    }

    public static void setProfilePictureByURL(String picUrl){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bitmap bmp = null;
        URL url;
        if(picUrl != null)
            try {

                url = new URL(picUrl);
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    Log.e("Error", e.getMessage());
                }
            } catch (MalformedURLException e) {
                Log.e("Error", e.getMessage());
            }
        if(bmp != null && profilePictureImageView != null)profilePictureImageView.setImageBitmap(bmp);
    }

    private void doLogout() {
        auth.signOut();
        Intent k = new Intent(ProfileActivity.this, LoginActivity.class);
        k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(k);
        finish();
    }

    public void setProfileName(String profileName) {
        profileNameTextView.setText(profileName);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_register) {
            doLogout();
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }else if (id == R.id.nav_go_to_your_own_profile) {
            if(useruuid != user.getUid()){
                Intent k = new Intent(new Intent(getApplicationContext(), ProfileActivity.class));
                k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                k.putExtra("userUuid", user.getUid());
                startActivity(k);
            }else Toast.makeText(this, "You are in your profile already", Toast.LENGTH_LONG).show();
        }else if (id == R.id.nav_search_for_person){
            startActivity(new Intent(getApplicationContext(), SearchProfile.class));

        }else if (id == R.id.nav_search_for_recipe){
            startActivity(new Intent(getApplicationContext(), SearchRecipe.class));
        }else if (id == R.id.nav_search_for_ingredient){

        }else if (id == R.id.nav_create_recipe){

        }else if (id == R.id.nav_add_this_person_as_a_friend){
            if(!useruuid.equals(user.getUid())) {
                dao.addAsFriend(user.getUid(), useruuid, this);
            }
            else Toast.makeText(this, "You can't be a friend of yourself", Toast.LENGTH_LONG).show();
        }else if (id == R.id.nav_logout) {
            doLogout();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_drawer_layout_profile);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
