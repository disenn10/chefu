package com.example.disen.chefu.Utils_functions;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by disen on 3/14/2018.
 */

public class FoodClass_infos implements Serializable {
    String label;
    String image;
    String source;
    String uri;
    ArrayList<String> dietLabels;
    ArrayList<String> healthLabels;
    ArrayList<String> cautions;
    ArrayList<String> ingredientLines;
    String calories;
    int servings;
    String url;
    String shareAs;
    String nutr_label;
    int quantity;
    String unit;
    ArrayList<FoodClass_infos> totalNutrients;
    ArrayList<FoodClass_infos> totalDaily;
    ArrayList<FoodClass_infos> dailyValue;

    public FoodClass_infos(String uri,String label,String image, String source,ArrayList<String> dietLabels,ArrayList<String> healthLabels,ArrayList<String> cautions, ArrayList<String> ingredientLines,String calories, int servings,String url, String shareAs,ArrayList<FoodClass_infos>totalNutrients,ArrayList<FoodClass_infos>totalDaily,ArrayList<FoodClass_infos> dailyValue){
        this.label = label;
        this.image = image;
        this.source = source;
        this.dietLabels = dietLabels;
        this.healthLabels = healthLabels;
        this.cautions = cautions;
        this.ingredientLines = ingredientLines;
        this.calories = calories;
        this.servings = servings;
        this.url = url;
        this.shareAs = shareAs;
        this.totalNutrients = totalNutrients;
        this.totalDaily = totalDaily;
        this.dailyValue = dailyValue;
        this.uri = uri;
    }
    public FoodClass_infos(String label,int quantity, String unit){
        this.nutr_label = label;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getImage() {
        return image;
    }

    public String getLabel() {
        return label;
    }

    public String getSource() {
        return source;
    }

    public ArrayList<String> getDietLabels() {
        return dietLabels;
    }

    public ArrayList<String> getCautions() {
        return cautions;
    }

    public ArrayList<String> getHealthLabels() {
        return healthLabels;
    }

    public ArrayList<String> getIngredientLines() {
        return ingredientLines;
    }

    public String getCalories() {
        return calories;
    }

    public int getServings() {
        return servings;
    }

    public String getShareAs() {
        return shareAs;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<FoodClass_infos> getTotalNutrients() {
        return totalNutrients;
    }

    public String getNutr_label() {
        return nutr_label;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getUri() {
        return uri;
    }

    public ArrayList<FoodClass_infos> getTotalDaily() {
        return totalDaily;
    }

    public ArrayList<FoodClass_infos> getDailyValue() {
        return dailyValue;
    }
}
