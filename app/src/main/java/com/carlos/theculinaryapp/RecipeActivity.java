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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private GenericDAO dao = GenericDAO.getInstance();
    private FirebaseAuth auth = dao.getFirebaseAuth();
    private FirebaseUser user = auth.getCurrentUser();
    private String recipeUuid;
    private String ownerUuid;
    private static ImageView recipePictureImageView;
    private static TextView recipeNameTextView;
    private ListView ingredientListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        recipeUuid = intent.getStringExtra("recipeUuid");
        if(recipeUuid == null)Toast.makeText(this, "Something went wrong, this recipe does not exist", Toast.LENGTH_LONG).show();
        else dao.retriveRecipeData(recipeUuid, this);

        recipePictureImageView = (ImageView) findViewById(R.id.recipe_activity_picture);
        recipeNameTextView = (TextView) findViewById(R.id.name_text);
        ingredientListView = (ListView) findViewById(R.id.ingredient_list);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_recipe);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public static void setRecipePictureByURL(String picUrl){
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
        if(bmp != null && recipePictureImageView != null)recipePictureImageView.setImageBitmap(bmp);
    }

    public void setRecipeName(String profileName) {
        recipeNameTextView.setText(profileName);
    }

    public void setIngredientList(IngredientItem[] items) {
        final RecipeActivity r = RecipeActivity.this;
        CustomAdapterIngredient itemsAdapter = new CustomAdapterIngredient(this, items);
        ingredientListView.setAdapter(itemsAdapter);
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IngredientItem i = (IngredientItem)ingredientListView.getItemAtPosition(position);
                dao.retriveIngredientData(i, r);
            }
        });
    }

    private void doLogout() {
        auth.signOut();
        Intent k = new Intent(RecipeActivity.this, LoginActivity.class);
        k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(k);
        finish();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_go_to_your_own_profile) {
                Intent k = new Intent(new Intent(getApplicationContext(), ProfileActivity.class));
                k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                k.putExtra("userUuid", user.getUid());
                startActivity(k);
        }else if (id == R.id.nav_search_for_person){
            startActivity(new Intent(getApplicationContext(), SearchProfile.class));

        }else if (id == R.id.nav_search_for_recipe){

        }else if (id == R.id.nav_search_for_ingredient){

        }else if (id == R.id.nav_create_recipe){

        }else if (id == R.id.nav_add_this_recipe){

        }else if (id == R.id.nav_search_ingredient_nearby){

        }else if (id == R.id.nav_logout) {
            doLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_drawer_layout_recipe);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToMapSellers(IngredientItem i){
        Intent k = new Intent(RecipeActivity.this, IngredientSellers.class);
        k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Log.e("TAG", i.getName() + " "+ i.getUuid());
        k.putExtra("positions", i.getMapInfo());
        startActivity(k);
    }
}
