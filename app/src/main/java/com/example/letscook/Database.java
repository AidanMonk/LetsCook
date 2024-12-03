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
        recipesRef.child(recipe.getId()).setValue(recipe);
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

    public static void getRecipe(String recipeId, RecipeCallback callback) {
        if (recipeId == null || recipeId.isEmpty()) {
            Log.e("DatabaseError", "Invalid recipe ID");
            callback.onCallback(null);
            return;
        }

        recipesRef.child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    callback.onCallback(recipe);
                } else {
                    Log.e("DatabaseError", "Recipe not found for ID: " + recipeId);
                    callback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error retrieving recipe: " + error.getMessage());
                callback.onCallback(null);
            }
        });
    }

    public static void updateRecipeRating(String recipeId, float newRating) {
        recipesRef.child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        // Calculate new average rating
                        int currentCount = recipe.getRatingCount();
                        float currentAverage = recipe.getAverageRating();
                        float totalRating = (currentAverage * currentCount) + newRating;
                        currentCount++;
                        float updatedAverage = totalRating / currentCount; // new average rating

                        // Update recipe with new average and count
                        recipe.setAverageRating(updatedAverage);
                        recipe.setRatingCount(currentCount);
                        recipesRef.child(recipeId).setValue(recipe); // Update in database
                    }
                } else {
                    Log.e("DatabaseError", "Recipe not found for ID: " + recipeId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error updating rating: " + error.getMessage());
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

   public interface RecipeCallback {
       void onCallback(Recipe recipe);
   }

    public interface RecipesCallback {
        void onCallback(List<Recipe> recipes);
    }
}
