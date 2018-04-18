package com.example.disen.chefu.food_categories;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disen.chefu.MainActivity;
import com.example.disen.chefu.R;
import com.example.disen.chefu.Utils_functions.FoodClass_infos;
import com.example.disen.chefu.Utils_functions.RecipeAdapter;
import com.example.disen.chefu.Utils_functions.Recipes_loader;
import com.example.disen.chefu.Utils_functions.Utils;
import com.example.disen.chefu.network.MyApplication;
import com.example.disen.chefu.network.NetworkChangeReceiver;
import com.example.disen.chefu.network.NetworkUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class customMade_category extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<FoodClass_infos>>,RecipeAdapter.OnItemClickListener, NetworkChangeReceiver.ConnectionReceiverListener {
    EditText carbs;
    EditText fat;
    EditText fiber;
    EditText sugar;
    EditText sodium;
    int id = 0;
    EditText cholesterol;
    RadioGroup radioGroup;
    NumberPicker min;
    NumberPicker max;
    ProgressBar progress_bar;
    FloatingActionButton done;
    String health_request;
    String calories_request;
    String nutrients_request;
    String custom_request;
    NestedScrollView customize_display;
    RecyclerView recyclerView;
    View scrollView;
    String[] min_values;
    String[] max_values;
    ArrayList<FoodClass_infos> data_copy;
    SharedPreferences.Editor editor;
    AdView mAdView;
    GridLayoutManager LM;
    int scrollTo;


    public customMade_category() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_custom_made_category, container, false);
        //Initiate values
        editor = getActivity().getSharedPreferences(getString(R.string.cus_prefs), MODE_PRIVATE).edit();
        carbs = view.findViewById(R.id.carbs);
        scrollView = view.findViewById(R.id.scroll_view_custom);
        fat = view.findViewById(R.id.fat);
        fiber = view.findViewById(R.id.fiber);
        sugar = view.findViewById(R.id.sugar);
        sodium = view.findViewById(R.id.sodium);
        cholesterol = view.findViewById(R.id.chol);
        customize_display = view.findViewById(R.id.scroll_view_custom);
        recyclerView = view.findViewById(R.id.cust_recV);
        progress_bar = view.findViewById(R.id.custom_progress);
        radioGroup = view.findViewById(R.id.radio_group);
        min = view.findViewById(R.id.min);
        max = view.findViewById(R.id.max);
        done = view.findViewById(R.id.custom_done);
        LM = new GridLayoutManager(getContext(),3);
        // Sample AdMob app ID:
        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        min_values= new String[] {"--","5","10","50","100","150","200","250","300","350","400","450","500","550","600","650","700","750","800","850","900","950","1000"};
        max_values=  new String[]{"--","50","100","150","200","250","300","350","400","450","500","550","600","650","700","750","800","850","900","950","1000","2000"};
        max.setMaxValue(max_values.length-1);
        min.setMaxValue(min_values.length-1);
        min.setWrapSelectorWheel(true);
        min.setDisplayedValues(min_values);
        max.setDisplayedValues(max_values);
        max.setWrapSelectorWheel(true);
        if(savedInstanceState!= null){
            //retrieve data and update UI accordingly on rotation
            data_copy = (ArrayList<FoodClass_infos>) savedInstanceState.getSerializable("data");
            custom_request = savedInstanceState.getString("request");
            if(data_copy!= null){
                customize_display.setVisibility(View.GONE);
                updateUI(data_copy);
            }
        }
        //If user just opened app, check and see if last request was saved
        else{
            SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.cus_prefs), MODE_PRIVATE);
            String saved_data = prefs.getString(getString(R.string.cus_recipe),null);
            if(saved_data != null){
                custom_request = saved_data;
                launchLoader();
            }
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence selected_health_label = null;
                String getcarbs = extractTextfromEditText(carbs);
                String getFat = extractTextfromEditText(fat);
                String getFiber = extractTextfromEditText(fiber);
                String getSugar = extractTextfromEditText(sugar);
                String getSodium = extractTextfromEditText(sodium);
                String getCholsterol = extractTextfromEditText(cholesterol);
                // find the radiobutton by returned id
                int selected_health_label_id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(selected_health_label_id);
                //if at least one health label is selected get the hint
                if(radioButton != null){
                selected_health_label = radioButton.getHint();}
                //
                health_request = make_health_request(selected_health_label);
                calories_request = make_calories_request(min_values[min.getValue()],max_values[max.getValue()]);
                nutrients_request = build_nutrients_request(getcarbs,getFat,getFiber,getSugar,getSodium,getCholsterol);
                custom_request = Utils.makeCustomRecipeRequest(health_request,calories_request,nutrients_request);
                if(custom_request != null){
                    Log.e(MainActivity.class.getSimpleName(), ""+custom_request );
                    //Call loader
                    launchLoader();

                }
            }
        });
        return view;
    }

    public void launchLoader(){
        customize_display.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
        getLoaderManager().initLoader(id,null, customMade_category.this);
        id++;
    }
    //create a function for appropriate health label
    public String make_health_request(CharSequence label){
        if(label == null){
            return null;
        }
        if(label.equals(getString(R.string.peanut))){
            health_request = "&health=peanut-free";
        }
        else if(label.equals(getString(R.string.vegan))){
            health_request = "&health=vegan";
        }
        else if(label.equals(getString(R.string.vegetarian))){
            health_request = "&health=vegetarian";
        }
        else if(label.equals(getString(R.string.fish))){
            health_request = "&health=fish-free";
        }
        else if(label.equals(getString(R.string.shellfish))){
            health_request = "&health=shellfish-free";
        }
        else{
            health_request = null;
        }
        return health_request;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //if the user is still editing, don't display additional items
        if(data_copy == null){
            menu.findItem(R.id.save_custom_view).setVisible(false);
            menu.findItem(R.id.restore).setVisible(false);
        }
        else{
            menu.findItem(R.id.save_custom_view).setVisible(true);
            menu.findItem(R.id.restore).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.custom_view,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_custom_view :
                //put it in database
                Toast.makeText(getContext(),"save",Toast.LENGTH_SHORT).show();
                break;
            //restore previous screen display
            case R.id.restore:
                restore_confirmation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Build the calories request
    public String make_calories_request(String min, String max){
        String calories_request = null;
        int min_int = 0;
        int max_int = 0;
        //If no number is picked return null
        if(min.equals("--") && max.equals("--")){
            return null;
        }
        //If only the maximum value is picked
        if(min.equals("--")&& !max.equals("--")){
            calories_request = "&calories=lte"+max;
        }
        //If only the minimum value is picked
        else if(!min.equals("--")&& max.equals("--")){
            calories_request = "&calories=gte"+min;
        }
        //If both values are picked
        else if(!min.equals("--")&& !max.equals("--")){
            min_int = Integer.parseInt(min);
            max_int = Integer.parseInt(max);
            //If minimum value is more than maximum value prompt error
            if(min_int>max_int){
                //error
            }
            else{
            calories_request = "&calories="+min+"-"+max;}
        }
        return calories_request;
    }

    public String build_nutrients_request(String carbs, String fat, String fiber, String sugar, String sodium, String chol){
        StringBuilder nutrient_request = new StringBuilder();
        if(carbs!= null && !carbs.equals("")){
            nutrient_request.append("&nutrients[CHOCDF]="+carbs);
        }
        if(fat!= null && !fat.equals("")){
            nutrient_request.append("&nutrients[FAT]="+fat);
        }
        if(fiber!= null && !fiber.equals("")){
            nutrient_request.append("&nutrients[FIBTG]="+fiber);
        }
        if(sugar!= null && !sugar.equals("")){
            nutrient_request.append("&nutrients[SUGAR]="+sugar);
        }
        if(sodium!= null && !sodium.equals("")){
            nutrient_request.append("&nutrients[NA]="+sodium);
        }
        if(chol!= null && !chol.equals("")){
            nutrient_request.append("&nutrients[CHOLE]="+chol);
        }
        return nutrient_request.toString();
    }

    //Get nutrients values from editText
    public String extractTextfromEditText(EditText editText){
        String text = null;
        if(editText != null){
            text = editText.getText().toString().trim();
        }
        return text;
    }

    @Override
    public Loader<ArrayList<FoodClass_infos>> onCreateLoader(int id, Bundle args) {
        return new Recipes_loader(getContext(),custom_request);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FoodClass_infos>> loader, ArrayList<FoodClass_infos> data) {
        Log.e(MainActivity.class.getSimpleName(), ""+custom_request );
        data_copy = data;
        if(data!= null){
            updateUI(data_copy);
        }
        else {
            resetView();
        }
    }

    //update the UI once the user make a request
    private void updateUI(ArrayList<FoodClass_infos> data) {
        ActivityCompat.invalidateOptionsMenu(getActivity());
        //make the same request next time the app is opened
        updateSharedPreferences(custom_request);
        //
        recyclerView.setVisibility(View.VISIBLE);
        RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(),data,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(LM);
        progress_bar.setVisibility(View.GONE);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setVerticalScrollbarPosition(scrollTo);
    }

    //update preferences..
    private void updateSharedPreferences(String data){
        editor.putString(getString(R.string.cus_recipe),data);
        editor.apply();
    }

    //Revert the display back the customize recipe search screen if no recipe was found
    public void resetView(){
        ActivityCompat.invalidateOptionsMenu(getActivity());
        progress_bar.setVisibility(View.GONE);
        customize_display.setVisibility(View.VISIBLE);
        Snackbar snack = Snackbar.make(scrollView,getString(R.string.no_results),Snackbar.LENGTH_LONG);
        snack.show();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FoodClass_infos>> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //On rotation of the device after the request has been made, save data received
        if(recyclerView.getVisibility() == View.VISIBLE){
            outState.putSerializable("data",data_copy);
            outState.putString("request",custom_request);
            scrollTo = LM.findFirstCompletelyVisibleItemPosition();
            outState.putInt("scrollto",scrollTo);
        }
    }
    //Restore screen to the custom display
    public void restore(){
        data_copy = null;
        //update preferences so the correct screen is display next time app is relaunched
        updateSharedPreferences(null);
        recyclerView.setVisibility(View.GONE);
        customize_display.setVisibility(View.VISIBLE);
        ActivityCompat.invalidateOptionsMenu(getActivity());
    }
    //Restore the screen to the custom display if the user confirms it
    public void restore_confirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.restore));
        builder.setMessage(getString(R.string.restore_conf));
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                restore();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void itemClicked(View v, int position) {
        Utils.GotoDetailActivity(getContext(),data_copy,position);
    }

    private void connectToInternet() {
        Toast.makeText(getContext(),getString(R.string.internet),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(String status) {
        if(status.equals(NetworkUtil.not_connected)){
            connectToInternet();
        }
        else{
            launchLoader();
        }
    }
}
