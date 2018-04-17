package com.example.disen.chefu;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disen.chefu.Ui.Circle;
import com.example.disen.chefu.Ui.Nutritional;
import com.example.disen.chefu.Ui.NutritionalAdapter;
import com.example.disen.chefu.Utils_functions.FoodClass_infos;
import com.example.disen.chefu.Utils_functions.Recipes_loader;
import com.example.disen.chefu.Utils_functions.Utils;
import com.example.disen.chefu.data.RecipeContract;
import com.example.disen.chefu.service.RecipeAppWidgetService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<FoodClass_infos>>{
    RecyclerView health_labels_recyclerView;
    RecyclerView diet_labels_recyclerView;
    RecyclerView allergies_recyclerView;
    RecyclerView nutritional_recyclerView;
    ImageView imageView;
    String request;
    FloatingActionButton more;
    ArrayList<String> HealthLabels;
    ArrayList<String> DietLabels;
    ArrayList<String> Allergies;
    ArrayList<FoodClass_infos> nutrients;
    ArrayList<FoodClass_infos> nutrient_percentage;
    ArrayList<FoodClass_infos> daily_value;
    String url;
    View view;
    String shareAs;
    ArrayList<String> ingredients;
    String image;
    ArrayList<FoodClass_infos> data;
    String calorie;
    String from_activity;
    String title;
    String uri;
    int serving;
    String source;
    TextView serving_view;
    TextView calorie_view;
    TextView source_view;
    TextView daily_quantity;
    Circle circle;
    int benefit_icon;
    int allergy_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        invalidateOptionsMenu();
        health_labels_recyclerView = (RecyclerView) findViewById(R.id.label_recyclview);
        view = findViewById(R.id.detail_view);
        nutritional_recyclerView = (RecyclerView) findViewById(R.id.nutritional_recyclv);
        benefit_icon = R.drawable.ic_favorite_border_black_18dp;
        allergy_icon = R.drawable.ic_priority_high_black_36dp;
        diet_labels_recyclerView = (RecyclerView) findViewById(R.id.diet_recycl);
        more = (FloatingActionButton) findViewById(R.id.see_ingredient);
        allergies_recyclerView = (RecyclerView) findViewById(R.id.allergies_recycl);
        imageView = (ImageView) findViewById(R.id.image_detail);
        calorie_view = (TextView) findViewById(R.id.calories_detail);
        serving_view = (TextView) findViewById(R.id.servings_detail);
        source_view = (TextView) findViewById(R.id.source_detail);
        daily_quantity = (TextView) findViewById(R.id.daily_quantity);
        circle = (Circle) findViewById(R.id.daily_circle);
        assignValues();

        more.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
        more.setOnClickListener(new View.OnClickListener() {
            //modify so if there no lollipop regular shift
            @Override
            public void onClick(View view) {
                Bundle bundle1 = null;
                Intent intent = new Intent(DetailActivity.this, IngredientsActivity.class);
                intent.putExtra("ingredients", ingredients);
                intent.putExtra("shareAs", shareAs);
                intent.putExtra("url",url);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bundle1 = ActivityOptions.makeSceneTransitionAnimation(DetailActivity.this, view, view.getTransitionName()).toBundle();
                    startActivity(intent, bundle1);
                } else startActivity(intent);
            }
        });

    }

    public int getIntValue(Double number) {
        Double d = new Double(number);
        int int_number = d.intValue();
        return int_number;

    }
    //update the health and diet benefits view
    public void updateUI(ArrayList<String> label, int icon, RecyclerView recyclerView) {
        Nutritional labelUI = new Nutritional(getApplicationContext(), label, icon);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(labelUI);
    }
//update nutritional facts view
    public void updateNutritionalUI(int serving, ArrayList<FoodClass_infos> nutrients, ArrayList<FoodClass_infos> daily, RecyclerView recyclerView) {
        NutritionalAdapter labelUI = new NutritionalAdapter(getApplicationContext(), nutrients, daily, serving);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(labelUI);
    }

    //update the main circle view
    public void updatedailyCircle(Circle circle, ArrayList<FoodClass_infos> daily_value, TextView daily_quantity) {
        if(circle!=null){
        circle.updatedrawUpto((float) daily_value.get(0).getQuantity() / serving);
        float quanity_float = Utils.getIntValue(daily_value.get(0).getQuantity()) / (float) serving;
        String quantity = Utils.roundFloat(quanity_float);
        daily_quantity.setText(quantity + "%");}
    }

    public void assignValues() {
        if (getIntent() != null) {
            from_activity = getIntent().getStringExtra("activity");
            //if user navigated from main activity
            if(from_activity.equals("main")){
                data = (ArrayList<FoodClass_infos>) getIntent().getSerializableExtra("data");
                HealthLabels = getIntent().getStringArrayListExtra("health");
                DietLabels = getIntent().getStringArrayListExtra("diet");
                Allergies = getIntent().getStringArrayListExtra("cautions");
                title = getIntent().getStringExtra("title");
                uri = getIntent().getStringExtra("uri");
                url = getIntent().getStringExtra("url");
                shareAs = getIntent().getStringExtra("shareAs");
                ingredients = getIntent().getStringArrayListExtra("ingredients");
                nutrients = (ArrayList<FoodClass_infos>) getIntent().getSerializableExtra("nutrients");
                nutrient_percentage = (ArrayList<FoodClass_infos>) getIntent().getSerializableExtra("daily");
                daily_value = (ArrayList<FoodClass_infos>) getIntent().getSerializableExtra("dailyvalue");
                image = getIntent().getStringExtra("image");
                serving = getIntent().getIntExtra("servings", 1);
                source = getIntent().getStringExtra("source");
                Double calorie_double = Double.parseDouble(getIntent().getStringExtra("calories"));
                calorie = String.valueOf((getIntValue(calorie_double)) / serving);

                setTitle(title);
                Picasso.with(getApplicationContext()).load(image).into(imageView);
                calorie_view.setText(calorie + getString(R.string.kcal));
                serving_view.setText(String.valueOf(serving));
                source_view.setText(getString(R.string.by) + source);

                //health labels
                updateUI(HealthLabels, benefit_icon, health_labels_recyclerView);
                //diet
                updateUI(DietLabels, benefit_icon, diet_labels_recyclerView);
                //allergy
                updateUI(Allergies, allergy_icon, allergies_recyclerView);
                //top_view
                updateNutritionalUI(serving, nutrients, nutrient_percentage, nutritional_recyclerView);
                //circle
                updatedailyCircle(circle, daily_value, daily_quantity);

            }
            //user naviagted from favorites
            else{
                uri = getIntent().getStringExtra("uri");
                title = getIntent().getStringExtra("title");
                request = Utils.createSpecificRequest(uri);
                getSupportLoaderManager().initLoader(0,null,this);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!checkIfAlreadySaved(title)) {
            menu.findItem(R.id.save).setVisible(true);
            menu.findItem(R.id.delete).setVisible(false);
        } else {
            menu.findItem(R.id.save).setVisible(false);
            menu.findItem(R.id.delete).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_unsave_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                insertRecipe();
                ActivityCompat.invalidateOptionsMenu(this);
                //put it in database
                break;
            //restore previous screen display
            case R.id.delete:
                deletion_confirmation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Check and see if the recipe is already in database
    public Boolean checkIfAlreadySaved(String name) {
        String[] projection = new String[]{RecipeContract.recipeEntry.data};
        String selection = RecipeContract.recipeEntry.data + "=? ";
        String[] args = new String[]{name};
        Cursor cursor = getContentResolver().query(RecipeContract.contentRecipe, projection, selection, args, null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    //insert recipe into database
    public void insertRecipe() {
        ContentValues contentValues = new ContentValues();
        Gson json = new Gson();
        Snackbar snackbar;
        String dataString = json.toJson(data);
       // if(checkIfAlreadySaved(dataString)) {
            contentValues.put(RecipeContract.recipeEntry.data, dataString);
        contentValues.put(RecipeContract.recipeEntry.uri, uri);
            contentValues.put(RecipeContract.recipeEntry.image, image);
            contentValues.put(RecipeContract.recipeEntry.recipe_title, title);
            getContentResolver().insert(RecipeContract.contentRecipe, contentValues);
            RecipeAppWidgetService.StartupdateWidgets(this);
            snackbar = Snackbar.make(view, getString(R.string.saved), Snackbar.LENGTH_LONG);
            snackbar.setAction(getString(R.string.gotofav), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailActivity.this, Favorites.class);
                    startActivity(intent);
                }
            });
            snackbar.show();
       // }else{snackbar = Snackbar.make(view, "Already!! "+getString(R.string.saved), Snackbar.LENGTH_LONG);snackbar.show();}
    }
    //delete recipe from database
    public void deleteRecipe(String name) {
        String s = RecipeContract.recipeEntry.data + "=?";
        String[] args = new String[]{name};
        getContentResolver().delete(RecipeContract.contentRecipe, s, args);
        RecipeAppWidgetService.StartupdateWidgets(this);
        ActivityCompat.invalidateOptionsMenu(this);
        Snackbar snackbar = Snackbar.make(view, getString(R.string.remove), Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    //confirm deletion
    public void deletion_confirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.remove_conf));
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteRecipe(title);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public Loader<ArrayList<FoodClass_infos>> onCreateLoader(int id, Bundle args) {
        Log.e(DetailActivity.class.getSimpleName(), ""+request );
        return new Recipes_loader(this,request,uri);

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FoodClass_infos>> loader, ArrayList<FoodClass_infos> data) {
        if(data!= null){
            updateTheUI(data);
        }
    }
    //
    private void updateTheUI(ArrayList<FoodClass_infos> data) {
        HealthLabels = data.get(0).getHealthLabels();
        DietLabels = data.get(0).getDietLabels();
        Allergies = data.get(0).getCautions();
        title = data.get(0).getLabel();
        uri = data.get(0).getUri();
        url = data.get(0).getUrl();
        shareAs = data.get(0).getShareAs();
        ingredients = data.get(0).getIngredientLines();
        nutrients = data.get(0).getTotalNutrients();
        nutrient_percentage = data.get(0).getTotalDaily();
        daily_value = data.get(0).getDailyValue();
        image = data.get(0).getImage();
        serving = data.get(0).getServings();
        source = data.get(0).getSource();
        Double calorie_double = Double.parseDouble(data.get(0).getCalories());
        calorie = String.valueOf((getIntValue(calorie_double)) / serving);

        setTitle(title);
        Picasso.with(getApplicationContext()).load(image).into(imageView);
        calorie_view.setText(calorie + getString(R.string.kcal));
        serving_view.setText(String.valueOf(serving));
        source_view.setText(getString(R.string.by) + source);

        //health labels
        updateUI(HealthLabels, benefit_icon, health_labels_recyclerView);
        //diet
        updateUI(DietLabels, benefit_icon, diet_labels_recyclerView);
        //allergy
        updateUI(Allergies, allergy_icon, allergies_recyclerView);
        //top_view
        updateNutritionalUI(serving, nutrients, nutrient_percentage, nutritional_recyclerView);
        //circle
        updatedailyCircle(circle, daily_value, daily_quantity);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FoodClass_infos>> loader) {
        loader.reset();
    }
}
