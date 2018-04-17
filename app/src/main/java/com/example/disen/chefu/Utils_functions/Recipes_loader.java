package com.example.disen.chefu.Utils_functions;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by disen on 3/14/2018.
 */

public class Recipes_loader extends AsyncTaskLoader<ArrayList<FoodClass_infos>> {
    String request;
    ArrayList<FoodClass_infos> food_infos;
    String recipe_id = null;
    public Recipes_loader(Context context, String request) {
        super(context);
        this.request = request;
    }
    public Recipes_loader(Context context, String request,String recipe_id) {
        super(context);
        this.request = request;
        this.recipe_id = recipe_id;
    }

    @Override
    public ArrayList<FoodClass_infos> loadInBackground() {
        food_infos = new ArrayList<>();
        if(recipe_id!= null){
            //do something
            try {
                food_infos = Utils.fetchSpecificData(request);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return food_infos;
        }
        else {
            try {
                food_infos = Utils.fetchData(request);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return food_infos;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(food_infos == null){
            forceLoad();
        }
        else{
            deliverResult(food_infos);
        }

    }

    @Override
    public void deliverResult(ArrayList<FoodClass_infos> data) {
        super.deliverResult(data);
        food_infos = data;
    }
}
