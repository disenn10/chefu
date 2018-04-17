package com.example.disen.chefu;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.disen.chefu.Ui.FavoritesCursorLoaderAdapter;
import com.example.disen.chefu.Utils_functions.FoodClass_infos;
import com.example.disen.chefu.Utils_functions.Utils;
import com.example.disen.chefu.data.RecipeContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    FavoritesCursorLoaderAdapter cursorAdapter;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        listview = (ListView) findViewById(R.id.favorites_recy);
        listview.setEmptyView(findViewById(R.id.empty_view));
        cursorAdapter = new FavoritesCursorLoaderAdapter(this,null);
        getSupportLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = new String[]{RecipeContract.recipeEntry.recipe_id,RecipeContract.recipeEntry.data,
                RecipeContract.recipeEntry.recipe_title,RecipeContract.recipeEntry.image};
        return new CursorLoader(this,RecipeContract.contentRecipe,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data!= null || data.getCount() == 0){
            updateUI(data);
        }
        else{
            listview.setEmptyView(findViewById(R.id.empty_view));
        }
    }

    private void updateUI(Cursor data) {
        //update views
        cursorAdapter.swapCursor(data);
        listview.setAdapter(cursorAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor item = (Cursor) cursorAdapter.getItem(i);
                int dataColumnName = item.getColumnIndex(RecipeContract.recipeEntry.data);
                String dataString = item.getString(dataColumnName);
                Gson json = new Gson();
                ArrayList<FoodClass_infos> data = json.fromJson(dataString,new TypeToken<ArrayList<FoodClass_infos>>()
                {}.getType());

                //String activity = getString(R.string.fav_activity);
                //Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                //startActivity(intent);
                Utils.GotoDetailActivity(getApplicationContext(),data,0);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
