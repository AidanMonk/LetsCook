package com.example.letscook.EdamamApi;

import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NutrientService {
    private static final String APP_ID = "f2b3a32e";
    private static final String APP_KEY = "762e75792b3f6d315b4f913f19d2c818";

    public static void fetchNutritionData(String ingredient, NutrientApiCallback callback) {
        NutrientApi api = RetrofitClient.getInstance().create(NutrientApi.class);

        Call<NutrientApiResponse> call = api.getNutritionData(APP_ID, APP_KEY, "logging", ingredient);
        call.enqueue(new Callback<NutrientApiResponse>() {
            @Override
            public void onResponse(Call<NutrientApiResponse> call, Response<NutrientApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle the response
                    NutrientApiResponse nutritionResponse = response.body();
                    System.out.println("Calories: " + nutritionResponse.getCalories());
                    callback.onSuccess(nutritionResponse);
                } else {
                    System.err.println("API Response Error: " + response.code());
                    callback.onError("API Response Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<NutrientApiResponse> call, Throwable t) {
                System.err.println("API Call Failed: " + t.getMessage());
                callback.onError("API Call Failed: " + t.getMessage());
            }
        });
    }

    public static void getNutritionalInfo(String[] ingredients, NutritionalInfoCallback callback) {
        NutritionalInfo nutritionalInfo = new NutritionalInfo();
        //need this to keep track of how many ingredients have been added, to ensure they are all returned
        AtomicInteger counter = new AtomicInteger(ingredients.length);

        for (String ingredient : ingredients) {
            NutrientService.fetchNutritionData(ingredient, new NutrientService.NutrientApiCallback() {
                @Override
                public void onSuccess(NutrientApiResponse response) {
                    nutritionalInfo.addNutrient("calories", response.getCalories());
                    nutritionalInfo.addNutrient("protein", response.getTotalNutrients().get("PROCNT").getQuantity());
                    nutritionalInfo.addNutrient("fat", response.getTotalNutrients().get("FAT").getQuantity());
                    nutritionalInfo.addNutrient("saturated fat", response.getTotalNutrients().get("FASAT").getQuantity());
                    nutritionalInfo.addNutrient("trans fat", response.getTotalNutrients().get("FATRN").getQuantity());
                    nutritionalInfo.addNutrient("carbohydrates", response.getTotalNutrients().get("CHOCDF").getQuantity());
                    nutritionalInfo.addNutrient("fibre", response.getTotalNutrients().get("FIBTG").getQuantity());
                    nutritionalInfo.addNutrient("sugars", response.getTotalNutrients().get("SUGAR").getQuantity());
                    nutritionalInfo.addNutrient("cholesterol", response.getTotalNutrients().get("CHOLE").getQuantity());
                    nutritionalInfo.addNutrient("sodium", response.getTotalNutrients().get("NA").getQuantity());

                    if (counter.decrementAndGet() == 0) {
                        callback.onComplete(nutritionalInfo);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    if (counter.decrementAndGet() == 0) {
                        callback.onComplete(nutritionalInfo);
                    }
                }
            });
        }
    }

    public interface NutritionalInfoCallback {
        void onComplete(NutritionalInfo nutritionalInfo);
    }

    public interface NutrientApiCallback{
        void onSuccess(NutrientApiResponse response);
        void onError(String errorMessage);
    }
}

