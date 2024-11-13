package com.example.letscook;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.letscook.Models.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final Database instance = new Database();

    private static FirebaseDatabase database;
    private static DatabaseReference recipesRef;
    private static StorageReference storageRef;

    private Database() {
        database = FirebaseDatabase.getInstance();
        recipesRef = database.getReference("recipes");
    }

    public static Database getInstance() {
        return instance;
    }

    public static void addRecipe(Recipe recipe) {
        recipesRef.child(recipe.getName()).setValue(recipe);
    }

    public static void getRecipes(RecipesCallback callback) {
        List<Recipe> recipes = new ArrayList<>();

        recipesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Recipe recipe = data.getValue(Recipe.class);
                    if (recipe != null) {
                        recipes.add(recipe);
                    }
                }
                // Pass the recipes list to the callback
                callback.onCallback(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error retrieving recipes", error.toException());
            }
        });
    }

    public static void getImageUrl(String imageId, UrlCallback callback) {
        storageRef = FirebaseStorage.getInstance().getReference("recipe_images/" + imageId + ".png");

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callback.onUrlLoaded(uri.toString()); // Return the URL as a string
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e); // Return the error if something goes wrong
            }
        });
    }

    public static void uploadImage(String imageId, Uri imageUri) {
        storageRef = FirebaseStorage.getInstance().getReference("recipe_images/" + imageId + ".png");

        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //do nothing, all is well
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Image upload failed!" + e.getMessage());
            }
        });
    }

   public interface UrlCallback {
       void onUrlLoaded(String url);
       void onError(Exception e);
   }

    public interface RecipesCallback {
        void onCallback(List<Recipe> recipes);
    }
}
