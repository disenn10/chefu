package com.example.disen.chefu.Utils_functions;

import android.content.Context;
import android.content.Intent;

import com.example.disen.chefu.BuildConfig;
import com.example.disen.chefu.DetailActivity;
import com.example.disen.chefu.Favorites;
import com.example.disen.chefu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by disen on 3/14/2018.
 */

public class Utils {
    private static String api_key = BuildConfig.Api_key;
    private static String app_id = BuildConfig.App_id;
    public static ArrayList<FoodClass_infos> fetchData(String request) throws MalformedURLException {
        String output = null;
        ArrayList<FoodClass_infos> food_infos = new ArrayList<>();
        URL url = createURL(request);
        try {
            output = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            food_infos = extractData(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return food_infos;
    }
    public static String createSpecificRequest(String uri){
        StringBuilder builder = new StringBuilder();
        uri = uri.replaceAll(".owl#", ".owl%23");
        String request = "https://api.edamam.com/search?r=";
        String appkey_id = "&app_id="+app_id+"&app_key="+api_key;
        builder.append(request).append(uri).append(appkey_id);
        return builder.toString();
    }

    public static ArrayList<FoodClass_infos> fetchSpecificData(String request) throws MalformedURLException {

        String output = null;
        ArrayList<FoodClass_infos> food_infos = new ArrayList<>();
        URL url = createURL(request);
        try {
            output = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            food_infos = extractSpecificData(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return food_infos;
    }


    public static URL createURL(String request) throws MalformedURLException {
        URL url = new URL(request);
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        InputStream in = null;
        String output = null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(10000);
        if(connection.getResponseCode()==200){
            in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if(scanner.hasNext()){
                output =  scanner.next();
            }
            else {
                return null;
            }
            connection.disconnect();
        }
        return output;
    }

    public static void GotoDetailActivity(Context context, ArrayList<FoodClass_infos>data_copy,int position){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("nutrients",data_copy.get(position).getTotalNutrients());
        intent.putExtra("data",data_copy);
        intent.putExtra("title",data_copy.get(position).getLabel());
        intent.putExtra("uri",data_copy.get(position).getUri());
        intent.putExtra("daily",data_copy.get(position).getTotalDaily());
        intent.putExtra("calories",data_copy.get(position).getCalories());
        intent.putExtra("servings",data_copy.get(position).getServings());
        intent.putExtra("image",data_copy.get(position).getImage());
        intent.putExtra("source",data_copy.get(position).getSource());
        intent.putExtra("url",data_copy.get(position).getUrl());
        intent.putExtra("shareAs",data_copy.get(position).getShareAs());
        intent.putExtra("diet",data_copy.get(position).getDietLabels());
        intent.putExtra("health",data_copy.get(position).getHealthLabels());
        intent.putExtra("cautions",data_copy.get(position).getCautions());
        intent.putExtra("ingredients",data_copy.get(position).getIngredientLines());
        intent.putExtra("dailyvalue",data_copy.get(position).getDailyValue());
        String activity = "main";
        intent.putExtra("activity",activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String createBaseUrl(String label,Context context){
        String baseUrl = null;
        if(label == null){
            return null;
        }
        if(label.equals(context.getString(R.string.balanced))){
            baseUrl = "https://api.edamam.com/search?q=chicken,food&app_id="+app_id+"&app_key="+api_key+"&from=0&to=100";
        }
        else if(label.equals(context.getString(R.string.carbs))){
            baseUrl = "https://api.edamam.com/search?q=chicken,turkey,fish&app_id="+app_id+"&app_key="+api_key+"&from=0&to=100";

        }
        else if(label.equals(context.getString(R.string.protein))){
            baseUrl = "https://api.edamam.com/search?q=meat,food&app_id="+app_id+"&app_key="+api_key+"&from=0&to=100";

        }
        else if(label.equals(context.getString(R.string.fat))){
            baseUrl = "https://api.edamam.com/search?q=food,pasta&app_id="+app_id+"&app_key="+api_key+"&from=0&to=100";
        }
        return baseUrl;
    }

    public static String unitContentDescription(Context context,String unit){
        if(unit.equals("g")){
            return context.getString(R.string.g);
        }
        else if(unit.equals("mg")){
            return context.getString(R.string.mg);
        }
        else{
            return context.getString(R.string.ug);
        }
    }
    public static String makeHomeScreenRequest(String label, Context context){
        //if the label == high protein chicken turkey and stuff
        String baseUrl = createBaseUrl(label,context);
        StringBuilder request = new StringBuilder();
        request.append(baseUrl);
        if(label == null){
            return null;
        }
        if(label.equals(context.getString(R.string.balanced))){
            request.append("&diet=balanced");
        }
        else if(label.equals(context.getString(R.string.carbs))){
            request.append("&diet=low-carb");
        }
        else if(label.equals(context.getString(R.string.protein))){
            request.append("&diet=high-protein");
        }
        else if(label.equals(context.getString(R.string.fat))){
            request.append("&diet=low-fat");
        }
        return request.toString();
    }

    public static String makeCustomRecipeRequest(String health, String calories,String nutrients){
        String baseUrl = "https://api.edamam.com/search?q=meat&app_id="+app_id+"&app_key="+api_key+"&from=0&to=13";
        StringBuilder request = new StringBuilder();
        request.append(baseUrl);
        if(health == null && calories == null && nutrients == null){
            return null;
        }
        else {
            if(health!= null){
                request.append(health);
            }
            if(calories!= null){
                request.append(calories);
            }
            if(nutrients!= null){
                request.append(nutrients);
            }
        }

        return request.toString();
    }

    public static ArrayList<FoodClass_infos> extractData(String output) throws JSONException {
        ArrayList<FoodClass_infos> food_infos = new ArrayList<>();
        String label = null;
        String image = null;
        String source = null;
        String calories = null;
        String uri = null;
        int servings = 0;
        String url = null;
        String shareAs = null;
        ArrayList<String>dietLabels = new ArrayList<>();
        ArrayList<String>healthLabels = new ArrayList<>();
        ArrayList<String>cautions = new ArrayList<>();
        ArrayList<String>ingredients = new ArrayList<>();
        ArrayList<FoodClass_infos>totalNutrients = new ArrayList<>();
        ArrayList<FoodClass_infos>totalDaily = new ArrayList<>();
        ArrayList<FoodClass_infos>dailyValue = new ArrayList<>();
        if(output == null || output.equals("")){
            return null;
        }
        JSONObject food_hits = new JSONObject(output);
        JSONArray food_hits_array = food_hits.getJSONArray("hits");
        for(int i=0;i<food_hits_array.length();i++){
            JSONObject foodObject = food_hits_array.getJSONObject(i);
            JSONObject recipe = foodObject.getJSONObject("recipe");
            if(recipe.has("label")){
                label = recipe.getString("label");
            }
            if(recipe.has("source")){
                source = recipe.getString("source");
            }
            if(recipe.has("image")){
                image = recipe.getString("image");
            }
            if(recipe.has("dietLabels")){
                dietLabels = extractArray(recipe.getJSONArray("dietLabels"));
            }
            if(recipe.has("healthLabels")){
                healthLabels = extractArray(recipe.getJSONArray("healthLabels"));
            }
            if(recipe.has("cautions")){
                cautions = extractArray(recipe.getJSONArray("cautions"));
            }
            if(recipe.has("ingredientLines")){
                ingredients = extractArray(recipe.getJSONArray("ingredientLines"));
            }
            if(recipe.has("calories")){
                calories = recipe.getString("calories");
            }
            if(recipe.has("yield")){
                servings = recipe.getInt("yield");
            }
            if(recipe.has("url")){
                url = recipe.getString("url");
            }
            if(recipe.has("uri")){
                uri = recipe.getString("uri");
            }
            if(recipe.has("shareAs")){
                shareAs = recipe.getString("shareAs");
            }
            if(recipe.has("totalNutrients")){
                totalNutrients = getAllnutrients(recipe.getJSONObject("totalNutrients"));
            }
            if(recipe.has("totalDaily")){
                totalDaily = getAllnutrients(recipe.getJSONObject("totalDaily"));
                dailyValue = getDailyValue(recipe.getJSONObject("totalDaily"));
            }
            food_infos.add(new FoodClass_infos(uri,label,image,source,dietLabels,healthLabels,cautions,ingredients,calories,servings,url,shareAs,totalNutrients,totalDaily,dailyValue));
        }
        return food_infos;
    }

    //convert double to int
    public static int getIntValue(int number){
        Double d = new Double(number);
        int int_number = d.intValue();
        return int_number;
    }

    public static String roundFloat(Float f){
        String s = String.format("%.1f", f);
        return s;
    }

    public static String getvalueperServing(int number, int servings){
        String result = "-";
        Float new_number = number/(float)servings;
        result = roundFloat(new_number);
        return result;
    }
    //extract json Array such as health labels, diet labels...
    public static ArrayList<String> extractArray(JSONArray Array) throws JSONException {
        ArrayList<String> result = new ArrayList<>();
        if(Array == null){
            return null;
        }
        for(int i =0;i<Array.length();i++){
            result.add(Array.getString(i));
        }
        return result;
    }

    //Extract nested objects such as nutritional facts
    public static FoodClass_infos extractObject(JSONObject ingredientObject,String code) throws JSONException {
        String label = null;
        int quantity = 0;
        String unit = null;
        JSONObject nutrition_object = ingredientObject.getJSONObject(code);
            if (nutrition_object.has("label")) {
                label = nutrition_object.getString("label");
            }
            if (nutrition_object.has("quantity")) {
                quantity = nutrition_object.getInt("quantity");
            }
            if (nutrition_object.has("unit")) {
                unit = nutrition_object.getString("unit");
            }
        return new FoodClass_infos(label,quantity,unit);
    }
    //get all the nutritional facts
    public static  ArrayList<FoodClass_infos> getAllnutrients(JSONObject nutrients_object) throws JSONException {
        String [] nutritionalCode = {"FAT","FASAT","FATRN","FAMS","FAPU","CHOCDF","FIBTG","SUGAR","CA","FE","VITA_RAE"
        ,"VITC","NA","CHOLE","PROCNT"};
        ArrayList<FoodClass_infos> result = new ArrayList<>();
       for(int i =0;i<nutritionalCode.length;i++) {
           if(nutrients_object.has(nutritionalCode[i])){
           result.add(extractObject(nutrients_object,nutritionalCode[i]));}
           else {
               result.add(dummyObject());
           }
       }
       return result;
    }

    public static  ArrayList<FoodClass_infos> getDailyValue(JSONObject nutrients_object) throws JSONException {
        String [] nutritionalCode = {"ENERC_KCAL"};
        ArrayList<FoodClass_infos> result = new ArrayList<>();
            if(nutrients_object.has(nutritionalCode[0])){
                result.add(extractObject(nutrients_object,nutritionalCode[0]));}
            else {
                result.add(dummyObject());
            }
        return result;
    }

    //
    public static FoodClass_infos dummyObject() {
        String [] labelName = {};
        String label = "dummy";
        int quantity = 0;
        String unit = "dumy";
        return new FoodClass_infos(label,quantity,unit);
    }

    public static ArrayList<FoodClass_infos> extractSpecificData(String output) throws JSONException {
        ArrayList<FoodClass_infos> food_infos = new ArrayList<>();
        String label = null;
        String image = null;
        String source = null;
        String calories = null;
        String uri = null;
        int servings = 0;
        String url = null;
        String shareAs = null;
        ArrayList<String>dietLabels = new ArrayList<>();
        ArrayList<String>healthLabels = new ArrayList<>();
        ArrayList<String>cautions = new ArrayList<>();
        ArrayList<String>ingredients = new ArrayList<>();
        ArrayList<FoodClass_infos>totalNutrients = new ArrayList<>();
        ArrayList<FoodClass_infos>totalDaily = new ArrayList<>();
        ArrayList<FoodClass_infos>dailyValue = new ArrayList<>();
        if(output == null || output.equals("")){
            return null;
        }
        JSONArray food_hits_array = new JSONArray(output);
        for(int i=0;i<food_hits_array.length();i++){
            JSONObject recipe = food_hits_array.getJSONObject(i);
            if(recipe.has("label")){
                label = recipe.getString("label");
            }
            if(recipe.has("source")){
                source = recipe.getString("source");
            }
            if(recipe.has("image")){
                image = recipe.getString("image");
            }
            if(recipe.has("dietLabels")){
                dietLabels = extractArray(recipe.getJSONArray("dietLabels"));
            }
            if(recipe.has("healthLabels")){
                healthLabels = extractArray(recipe.getJSONArray("healthLabels"));
            }
            if(recipe.has("cautions")){
                cautions = extractArray(recipe.getJSONArray("cautions"));
            }
            if(recipe.has("ingredientLines")){
                ingredients = extractArray(recipe.getJSONArray("ingredientLines"));
            }
            if(recipe.has("calories")){
                calories = recipe.getString("calories");
            }
            if(recipe.has("yield")){
                servings = recipe.getInt("yield");
            }
            if(recipe.has("url")){
                url = recipe.getString("url");
            }
            if(recipe.has("uri")){
                uri = recipe.getString("uri");
            }
            if(recipe.has("shareAs")){
                shareAs = recipe.getString("shareAs");
            }
            if(recipe.has("totalNutrients")){
                totalNutrients = getAllnutrients(recipe.getJSONObject("totalNutrients"));
            }
            if(recipe.has("totalDaily")){
                totalDaily = getAllnutrients(recipe.getJSONObject("totalDaily"));
                dailyValue = getDailyValue(recipe.getJSONObject("totalDaily"));
            }
            food_infos.add(new FoodClass_infos(uri,label,image,source,dietLabels,healthLabels,cautions,ingredients,calories,servings,url,shareAs,totalNutrients,totalDaily,dailyValue));
        }
        return food_infos;
    }


}
