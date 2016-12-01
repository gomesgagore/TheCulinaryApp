package com.carlos.theculinaryapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class GenericDAO {
    public static final String FIREBASE_URL = "https://the-culinary-app.firebaseio.com/";
    private static GenericDAO singleton;
    private static String TAG = "DAO";
    private static Firebase firebase;
    private static FirebaseAuth auth;
    private static FirebaseDatabase db;
    private static DatabaseReference mDatabase;

    public static GenericDAO getInstance(){
        if(singleton == null)singleton = new GenericDAO();
        return singleton;
    }

    public static Firebase getFirebase(){
        if( firebase == null ){
            firebase = new Firebase(FIREBASE_URL);
        }
        return( firebase );
    }

    public FirebaseAuth getFirebaseAuth(){
        if( auth == null ){
            getFirebase();
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public FirebaseDatabase getDb() {
        if(firebase == null) getFirebase();
        if(db == null)db = FirebaseDatabase.getInstance();
        return db;
    }

    public DatabaseReference getDatabaseReference(){
        if(mDatabase == null) mDatabase = getDb().getReference();
        return mDatabase;
    }

    public void retriveProfileData(final String useruuid, final ProfileActivity activity) {
        getDatabaseReference().child("users").child(useruuid).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snap){
                activity.setProfilePictureByURL((String) snap.child("profile_pic").getValue());
                activity.setProfileName((String) snap.child("name").getValue());
                ArrayList<ProfileItem> profileItems = new ArrayList<ProfileItem>();
                DataSnapshot snapfriends = snap.child("friends");
                Iterator<DataSnapshot> iter = snapfriends.getChildren().iterator();
                while(iter.hasNext()){
                    DataSnapshot next = iter.next();
                    String uuidfriend = next.getKey();
                    String friendpic;
                    String friendname = (String) next.child("name").getValue();
                    if(next.child("profile_pic").exists())
                        friendpic = (String)next.child("profile_pic").getValue();
                    else friendpic = "people1";
                    ProfileItem p = new ProfileItem(friendname, friendpic);
                    p.setUseruuid(uuidfriend);
                    profileItems.add(p);
                }

                ArrayList<RecipeItem> recipeItems = new ArrayList<RecipeItem>();
                DataSnapshot snapRecipes = snap.child("recipes");
                Iterator<DataSnapshot> iter2 = snapRecipes.getChildren().iterator();
                while(iter2.hasNext()){
                    DataSnapshot next = iter2.next();
                    String uuidrecipe = next.getKey();
                    String recipepic;
                    String recipename = (String) next.child("name").getValue();
                    if(next.child("recipe_pic").exists())
                        recipepic = (String)next.child("recipe_pic").getValue();
                    else recipepic = "food1";
                    RecipeItem r = new RecipeItem(recipename, recipepic);
                    r.setRecipeUuid(uuidrecipe);
                    recipeItems.add(r);
                }

                activity.updateProfileList(profileItems);
                activity.updateRecipeList(recipeItems);
            }
            public void onCancelled(DatabaseError databaseError) {}});
    }

    public void RegistrarUsuario(final Activity act, String email, String password, final String name){
        getFirebaseAuth().createUserWithEmailAndPassword(email,password).addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(act , "Usuario criado com sucesso! :" + task.isSuccessful(), Toast.LENGTH_LONG).show();
                    String useruuid =  task.getResult().getUser().getUid();
                    getDatabaseReference().child("users").child(useruuid).child("name").setValue(name);
                } else {
                    Toast.makeText(act, "Authentication failed." + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private GenericDAO(){
    }

    public void setFirebaseContext(MainApplication firebaseContext) {
        firebase.setAndroidContext(firebaseContext);
    }

    public void addAsFriend(final String user, final String useruuid, Context context) {
        getDatabaseReference().child("users").child(useruuid).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snap) {
                String name = (String) snap.child("name").getValue();
                String profile_pic = (String) snap.child("profile_pic").getValue();
                updateFriends(user, name, profile_pic, useruuid);
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateFriends(String user, String friendName, String friendPic, String friendUuid) {
        HashMap<String, Object> yourNewFriend = new HashMap<String, Object>();
        yourNewFriend.put("name", friendName);
        yourNewFriend.put("profile_pic", friendPic);
        getDatabaseReference().child("users").child(user).child("friends").child(friendUuid).updateChildren(yourNewFriend);
    }

    public void searchUsersByText(final String query, final SearchProfile activity){
        getDatabaseReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snap) {
                Iterable<DataSnapshot> data = snap.getChildren();
                ArrayList<ProfileItem> found = new ArrayList<ProfileItem>();
                while(data.iterator().hasNext()) {
                    DataSnapshot snapChild = data.iterator().next();
                    String name = (String) snapChild.child("name").getValue();
                    String useruuid = snapChild.getKey();
                    if(name.contains(query)){
                        String pic = (String) snapChild.child("profile_pic").getValue();
                        ProfileItem p = new ProfileItem(name, pic);
                        p.setUseruuid(useruuid);
                        found.add(p);
                    }
                }
                activity.updateResultView(found);
            }
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void searchRecipesByText(final String query, final SearchRecipe activity){
        getDatabaseReference().child("recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snap) {
                Iterable<DataSnapshot> data = snap.getChildren();
                ArrayList<RecipeItem> found = new ArrayList<RecipeItem>();
                while(data.iterator().hasNext()) {
                    DataSnapshot snapChild = data.iterator().next();
                    String recipeuuid = snapChild.getKey();
                    String name = (String) snapChild.child("name").getValue();
                    if(name.contains(query)){
                        String pic = (String) snapChild.child("recipe_pic").getValue();
                        RecipeItem p = new RecipeItem(name, pic);
                        p.setRecipeUuid(recipeuuid);
                        found.add(p);
                    }
                }
                activity.updateResultView(found);
            }
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void retriveRecipeData(String recipeUuid, final RecipeActivity recipeActivity) {
        getDatabaseReference().child("recipes").child(recipeUuid).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snap) {
                recipeActivity.setRecipePictureByURL((String) snap.child("recipe_pic").getValue());
                recipeActivity.setRecipeName((String) snap.child("name").getValue());
                Iterable<DataSnapshot> data = snap.child("ingredients").getChildren();
                IngredientItem[] found = new IngredientItem[(int) snap.child("ingredients").getChildrenCount()];
                int i = 0;
                while(data.iterator().hasNext()) {
                    IngredientItem p = new IngredientItem();
                    DataSnapshot snapChild = data.iterator().next();
                    String uuid = snapChild.getKey();
                    p.setUuid(uuid);
                    p.setName((String) snapChild.child("name").getValue());
                    Log.e(TAG,(String) snapChild.child("name").getValue());
                    found[i] = p;
                    i++;
                }
                recipeActivity.setIngredientList(found);
            }
            public void onCancelled(DatabaseError databaseError) {}});
    }

    public void retriveIngredientData(final IngredientItem i, final RecipeActivity recipeActivity) {
        getDatabaseReference().child("ingredients").child(i.getUuid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snap) {
                IngredientItem newI = i;
                Iterable<DataSnapshot> data = snap.child("sellers").getChildren();
                ArrayList<IngredientMapInformation> array = new ArrayList<IngredientMapInformation>();
                while(data.iterator().hasNext()) {
                    DataSnapshot snapChild = data.iterator().next();
                    String sellerName = snapChild.getKey();
                    Float lat = Float.parseFloat((String)snapChild.child("lat").getValue()) ;
                    Float longi =  Float.parseFloat((String) snapChild.child("long").getValue());
                    String price = (String) snapChild.child("price").getValue();
                    Log.e(TAG, String.valueOf(lat)+ "  ---------" + String.valueOf(longi)+ " ----------- "+ price);
                    IngredientMapInformation o = new IngredientMapInformation(sellerName, lat, longi, price);
                   array.add(o);
                }
                i.setMapInfo(array);
                recipeActivity.goToMapSellers(i);
            }
            public void onCancelled(DatabaseError databaseError) {}});
    }
}
