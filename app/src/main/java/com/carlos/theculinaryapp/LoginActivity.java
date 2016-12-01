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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = "LoginActivity";
    private GenericDAO dao = GenericDAO.getInstance();
    private FirebaseAuth auth = dao.getFirebaseAuth();
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = auth.getCurrentUser();
        if(user != null){
            goToProfileActivity();
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_login);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void checkInputs(View view){
        EditText userIn = (EditText) findViewById(R.id.login_form);
        EditText passIn = (EditText) findViewById(R.id.password_login);
        String unm = userIn.getText().toString();
        String pass = passIn.getText().toString();
        if(!unm.equals("") && !pass.equals("")){
        unm.trim();
        pass.trim();
            auth.signInWithEmailAndPassword(unm, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //TODO create database data;
                        goToProfileActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Fail " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void goToProfileActivity(){
        Intent k = new Intent(LoginActivity.this, ProfileActivity.class);
        k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        user = auth.getCurrentUser();
        if(user != null)k.putExtra("userUuid", user.getUid());
        startActivity(k);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_register) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_drawer_layout_login);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
