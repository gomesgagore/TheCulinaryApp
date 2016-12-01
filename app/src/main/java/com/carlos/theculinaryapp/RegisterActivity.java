package com.carlos.theculinaryapp;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private GenericDAO dao = GenericDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnSignUp = (Button) findViewById(R.id.register_button_register);
        final EditText inputEmail = (EditText) findViewById(R.id.new_login_form);
        final EditText inputPassword = (EditText) findViewById(R.id.new_pass_form);
        final EditText inputPasswordnd = (EditText) findViewById(R.id.confirm_pass_form);
        final EditText inputName = (EditText) findViewById(R.id.new_name_register);

        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usermail = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String passwordnd = inputPasswordnd.getText().toString();
                String name = inputName.getText().toString();
                if(password.equals("") || password.equals("") || name.equals("")){
                    Toast.makeText(RegisterActivity.this, "Nenhum campo pode ficar vazio", Toast.LENGTH_LONG);
                }
                if(password.equals(passwordnd)){
                    dao.RegistrarUsuario(RegisterActivity.this, usermail, password, name);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "As senhas estao diferentes",  Toast.LENGTH_LONG );
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_register);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        this.finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_login_item) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_drawer_layout_register);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
