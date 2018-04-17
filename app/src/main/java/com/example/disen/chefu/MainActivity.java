package com.example.disen.chefu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.disen.chefu.Ui.Settings;
import com.example.disen.chefu.food_categories.customMade_category;
import com.example.disen.chefu.food_categories.first_category;
import com.example.disen.chefu.food_categories.second_category;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SharedPreferences.OnSharedPreferenceChangeListener{

    SharedPreferences sharedPref;
    public static String old_label;
    String new_label;
    public static String old_label_secndfrag;
    String new_label_secondfrag;
    Boolean protein_state;
    Boolean carbs_state;
    Boolean fat_state;
    Boolean balance_state;
    ViewPager viewPager;
    Main_Viewpager main_viewpager;
    TabLayout tabLayout;
    //Contains a list of the labels chosen/on switches
    static ArrayList<String> onSwitches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpshared();
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        invalidateOptionsMenu();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.food_viewpager);
        main_viewpager = new Main_Viewpager(getSupportFragmentManager());
        ArrayList<String> label = MainActivity.onSwitches;
        if(label.size()>1) {Log.e(MainActivity.class.getSimpleName(), "old label: "+label.get(1));
        old_label = label.get(1);}
        viewPager.setAdapter(main_viewpager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //get the state of the swicthes from the settings
    public void getStateofSwitches(){
        protein_state = sharedPref.getBoolean(getString(R.string.protein_key),false);
        carbs_state = sharedPref.getBoolean(getString(R.string.carbs_key),false);
        fat_state = sharedPref.getBoolean(getString(R.string.fat_key),false);
        balance_state = sharedPref.getBoolean(getString(R.string.balanced_key),false);
    }
    //return the on switches
    public ArrayList<String> getOnswitches(){
        getStateofSwitches();
        onSwitches = new ArrayList<>();
        ArrayList <String> onSwitches_list = new ArrayList<>();
        if(protein_state){
            onSwitches_list.add(getString(R.string.protein));
        }
        if(carbs_state){
            onSwitches_list.add(getString(R.string.carbs));
        }
        if(fat_state){
            onSwitches_list.add(getString(R.string.fat));
        }
        if(balance_state){
            onSwitches_list.add(getString(R.string.balanced));
        }
        onSwitches = onSwitches_list;
        //Log.e(MainActivity.class.getSimpleName(), "getOnswitches: "+onSwitches.size() );
        return onSwitches_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this,Favorites.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }
    //Set up the the shared so we get the appropriate viewpager titles and make the proper requests
    private void setUpshared(){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        getOnswitches();
        //In case the user had chosen less than 3 labels and the view happens to be destroyed
        //Send the user to this page next time the app is open so he/she is prompts to pick
        //3 labels.
        if(onSwitches.size()<2){
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
        }
    }
    //if the settings have changed make sure you update views
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        //Log.e(MainActivity.class.getSimpleName(), "onSharedPreferenceChanged: ###XHANGEDD!!!" );
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        getOnswitches();
        ArrayList<String> label = MainActivity.onSwitches;
        if(label.size()>1) {
            new_label = label.get(1);
            //if user changed preferences update label
            if (!new_label.equals(old_label)) {
                first_category.old_label = old_label;
                old_label = new_label;
            }
        }

        if(label.size()>0) {
            new_label_secondfrag = label.get(0);
            //if user changed preferences update label
            if (!new_label_secondfrag.equals(old_label_secndfrag)) {
                second_category.old_label = old_label_secndfrag;
                old_label_secndfrag = new_label_secondfrag;
            }
        }
        main_viewpager.notifyDataSetChanged();
        viewPager.setAdapter(main_viewpager);
        //after you pass the arguments for refreshing argument update old label to new label
        tabLayout.setupWithViewPager(viewPager);
    }

    private class Main_Viewpager extends FragmentStatePagerAdapter {

        public Main_Viewpager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ArrayList<String> label = MainActivity.onSwitches;
            Bundle bundle = new Bundle();
            switch (position){
                case 1:
                    //only set bundle if label has been set
                    second_category secondCategory = new second_category();
                    if(label.size()>0){bundle.putString("label",label.get(0));}
                    secondCategory.setArguments(bundle);
                    return secondCategory;
                case 2:
                    return new customMade_category();
                default:
                    first_category firstCategory = new first_category();
                    if(label.size()>1){bundle.putString("label",label.get(1));}
                    firstCategory.setArguments(bundle);
                    return firstCategory;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            ArrayList<String> label = MainActivity.onSwitches;
            switch (position){
                case 1:
                    if(label.size()>0){
                    return label.get(0);}
                case 2:
                    return getString(R.string.custom);
                default:
                    if(label.size()>1){
                    return label.get(1);}
            }
            return super.getPageTitle(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
