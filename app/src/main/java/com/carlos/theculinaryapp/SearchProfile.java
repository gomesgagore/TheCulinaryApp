package com.carlos.theculinaryapp;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SearchProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListAdapter customListAdapterPeople;
    private GridView customListView;
    private GenericDAO dao = GenericDAO.getInstance();
    private FirebaseAuth auth = dao.getFirebaseAuth();
    private FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);

        customListView = (GridView) findViewById(R.id.result_list_search_profile);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_search_profile);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    public void updateResultView(ArrayList<ProfileItem> profileItems){
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
                    Intent k = new Intent(SearchProfile.this, ProfileActivity.class);
                    k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    k.putExtra("userUuid", i.getUseruuid());
                    startActivity(k);
                }
            });
    }

    public void onSearchButtonClick(View view){
        EditText queryInput = (EditText) findViewById(R.id.search_recipe_input);
        String query = String.valueOf(queryInput.getText());
        Log.e(SearchProfile.class.getName(), query);
        dao.searchUsersByText(query, this);
    }

    private void doLogout() {
        auth.signOut();
        Intent k = new Intent(SearchProfile.this, LoginActivity.class);
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
        }else if (id == R.id.nav_search_for_recipe) {
            startActivity(new Intent(getApplicationContext(), SearchRecipe.class));
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
