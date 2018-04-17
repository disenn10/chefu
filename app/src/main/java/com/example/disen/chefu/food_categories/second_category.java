package com.example.disen.chefu.food_categories;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import java.util.ArrayList;

public class second_category extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<FoodClass_infos>>,RecipeAdapter.OnItemClickListener,NetworkChangeReceiver.ConnectionReceiverListener {
    RecyclerView recyclerView;
    String request;
    String label;
    ProgressBar progressBar;
    int id;
    public static String old_label;
    ArrayList<FoodClass_infos> data_copy;
    SwipeRefreshLayout refresh;
    public second_category() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_second_category, container, false);
        recyclerView = view.findViewById(R.id.second_category_rcV);
        progressBar = view.findViewById(R.id.second_progress);
        id = 0;
        refresh = view.findViewById(R.id.refresh_second_catg);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                launchLoader(id,0);
            }
        });
        //if app is just opened launch loader
        if(savedInstanceState == null){
            if(getArguments()!= null){
                label = getArguments().getString("label");
            }
            launchLoader(id,0);
        }
        //else do not relaunch it..just save data and re-display them to minimize requests
        else{
            data_copy = (ArrayList<FoodClass_infos>) savedInstanceState.getSerializable("data");
            updateUI(data_copy);
        }
        return view;
    }

    @Override
    public Loader<ArrayList<FoodClass_infos>> onCreateLoader(int id, Bundle args) {
        Log.e(MainActivity.class.getSimpleName(), "second oncreateloader: "+request );
        Log.e(MainActivity.class.getSimpleName(), "second label: "+label );
        return new Recipes_loader(getContext(),request);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FoodClass_infos>> loader, ArrayList<FoodClass_infos> data) {
        if(data!= null){
            data_copy = data;
            updateUI(data_copy);
        }
        else {
            Toast.makeText(getContext(),getString(R.string.sorry),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
        label = getArguments().getString("label");
        //if the user changed preferences relaunch loader
        if(!label.equals(old_label)){
            launchLoader(id,0);
        }
    }

    public void launchLoader(int id, int restart){
        String status_connectivity = NetworkUtil.getConnectivityStatusString(getContext());
        request = Utils.makeHomeScreenRequest(label,getContext());
        if(restart == 1){
            getLoaderManager().restartLoader(id,null,this);
        }else {
            if(status_connectivity.equals(NetworkUtil.connected)){
                progressBar.setVisibility(View.VISIBLE);
                id++;
                getLoaderManager().initLoader(id,null,this);}
            else{
                connectToInternet();
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data",data_copy);
    }

    private void updateUI(ArrayList<FoodClass_infos> data) {
        progressBar.setVisibility(View.GONE);
        refresh.setRefreshing(false);
        RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(),data,this);
        GridLayoutManager LM = new GridLayoutManager(getContext(),3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(LM);
        recyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FoodClass_infos>> loader) {

    }

    @Override
    public void itemClicked(View v, int position) {
        Utils.GotoDetailActivity(getContext(),data_copy,position);
    }
    private void connectToInternet() {
        Toast.makeText(getContext(),getString(R.string.internet),Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNetworkConnectionChanged(String status) {
        if(status.equals(NetworkUtil.not_connected)){
            connectToInternet();
        }
        else{
            launchLoader(id,1);
        }
    }
}
