package com.example.disen.chefu.Ui;

import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.disen.chefu.R;

public class Settings extends AppCompatActivity {
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        view = findViewById(R.id.fragment_setting);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowHomeEnabled(true);
        }
        firstTimer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        int x  = SettingsFragment.getswitches();
        //Make user pick exactly three health labels
        if(item_id == android.R.id.home) {
            if (x == 2) {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            final Snackbar snackbar = Snackbar.make(view,getString(R.string.pick_three),Snackbar.LENGTH_LONG);
            snackbar.setAction(getString(R.string.dismiss), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void firstTimer(){
        int x  = SettingsFragment.getswitches();
        if(x ==0){
            final Snackbar snackbar = Snackbar.make(view,getString(R.string.first_timer),Snackbar.LENGTH_INDEFINITE);
            snackbar.show();}
    }
    //If user try pressing the back button from his/her device
    @Override
    public void onBackPressed()
    {
        int x  = SettingsFragment.getswitches();
        if (x == 2) {
            NavUtils.navigateUpFromSameTask(this);
        }else{
        final Snackbar snackbar = Snackbar.make(view,getString(R.string.pick_three),Snackbar.LENGTH_LONG);
        snackbar.setAction(getString(R.string.dismiss), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();}
    }
}
