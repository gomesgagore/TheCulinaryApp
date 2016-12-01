package com.carlos.theculinaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SearchRecipe extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListAdapter customListAdapterRecipes;
    private GridView customListView;
    private GenericDAO dao = GenericDAO.getInstance();
    private FirebaseAuth auth = dao.getFirebaseAuth();
    private FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        customListView = (GridView) findViewById(R.id.result_list_search_recipe);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_search_recipe);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    public void updateResultView(ArrayList<RecipeItem> recipeItems){
            RecipeItem[] items = new RecipeItem[recipeItems.size()];
            for (int i = 0; i < recipeItems.size(); i++) {
                items[i] = recipeItems.get(i);
            }

            customListAdapterRecipes = new CustomAdapterRecipe(this, items);
            customListView.setAdapter(customListAdapterRecipes);
            customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RecipeItem i = (RecipeItem)customListView.getItemAtPosition(position);
                    Intent k = new Intent(SearchRecipe.this, RecipeActivity.class);
                    k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    k.putExtra("recipeUuid", i.getRecipeUuid());
                    startActivity(k);
                }
            });
    }

    public void onSearchButtonClick(View view){
        EditText queryInput = (EditText) findViewById(R.id.search_recipe_input);
        String query = String.valueOf(queryInput.getText());
        Log.e(SearchRecipe.class.getName(), query);
        dao.searchRecipesByText(query, this);
    }

    private void doLogout() {
        auth.signOut();
        Intent k = new Intent(SearchRecipe.this, LoginActivity.class);
        k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(k);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_go_to_your_own_profile) {
            Intent k = new Intent(new Intent(getApplicationContext(), ProfileActivity.class));
            k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            k.putExtra("userUuid", user.getUid());
            startActivity(k);
        }else if (id == R.id.nav_search_for_profile) {
            startActivity(new Intent(getApplicationContext(), SearchProfile.class));
        }else if (id == R.id.nav_search_for_ingredient){

        }else if (id == R.id.nav_create_recipe){

        }else if (id == R.id.nav_logout){
            doLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_drawer_layout_search_profile);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
