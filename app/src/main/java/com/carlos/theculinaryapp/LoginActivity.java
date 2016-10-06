package com.carlos.theculinaryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private JSONArray usersAuthArray;
    private String TAG = "LoginActivity";
    private GenericDAO dao = GenericDAO.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sp1 = this.getSharedPreferences("AuthInfo", Context.MODE_PRIVATE);
        String unm = sp1.getString("username", null);
        String pass = sp1.getString("password", null);
        if(unm != null && pass != null){
            if(checkAuth(unm, pass)){
                goToLoginActivity();
            }
        }

    }

    public void goToRegister(View view){
        Intent k = new Intent(LoginActivity.this, ProfileActivity.class);
        startActivity(k);
    }

    public void checkInputs(View view){
        EditText userIn = (EditText) findViewById(R.id.login_form);
        EditText passIn = (EditText) findViewById(R.id.password_login);
        String unm = userIn.getText().toString();
        String pass = passIn.getText().toString();
        if(checkAuth(unm, pass) == false) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_login_toast), Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences sp1 = this.getSharedPreferences("AuthInfo", 0);
            SharedPreferences.Editor ed = sp1.edit();
            ed.putString("username", unm);
            ed.putString("password", pass);
            ed.commit();
            goToLoginActivity();
        }
    }

    public void getUsersDAO(){
        if(dao != null){
            usersAuthArray = dao.getAllUsers();
        }
    }

    public boolean checkAuth(String unm, String pass){
        getUsersDAO();
        if(usersAuthArray!=null)
            for (int i = 0; i < usersAuthArray.length(); i++) {
            try {
                JSONObject temp = usersAuthArray.getJSONObject(i);
                if(temp.getString("username").equals(unm) && temp.getString("password").equals(pass)){
                    UserInfo.setProfileView(this, dao.getUserInfo(unm));
                    return true;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
        Log.d(TAG, "nothing found on Auth");
        return false;
    }

    private void goToLoginActivity(){
        Intent k = new Intent(LoginActivity.this, ProfileActivity.class);
        k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        k.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(k);
        finish();
    }

}
