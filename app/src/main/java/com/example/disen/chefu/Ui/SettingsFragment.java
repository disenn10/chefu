package com.example.disen.chefu.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

import com.example.disen.chefu.MainActivity;
import com.example.disen.chefu.R;
import com.example.disen.chefu.Utils_functions.Settings_swicthes_infos;

import java.util.ArrayList;

/**
 * Created by disen on 3/18/2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences sharedPreferences;
    //switches
    static SwitchPreferenceCompat proteinSwitch;
    static SwitchPreferenceCompat carbsSwitch;
    static SwitchPreferenceCompat fatSwitch;
    static SwitchPreferenceCompat balanceSwitch;
    //state of switches
    boolean protein;
    boolean carbs;
    boolean fat;
    boolean balance;

    //keys
    String protein_key;
    String carbs_key;
    String fat_key;
    String balance_key;
    static int switches;
    Activity activity;

    public SettingsFragment(){

    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_pref);
        //Initiate values
        activity = getActivity();
        proteinSwitch = (SwitchPreferenceCompat) findPreference(getActivity().getString(R.string.protein_key));
        balanceSwitch = (SwitchPreferenceCompat) findPreference(getActivity().getString(R.string.balanced_key));
        fatSwitch = (SwitchPreferenceCompat) findPreference(getActivity().getString(R.string.fat_key));
        carbsSwitch = (SwitchPreferenceCompat) findPreference(getActivity().getString(R.string.carbs_key));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //set up UI
        setUpshared();
    }

    //get the state of each switches-whether they are switched on or not
    public void getstateofSwitches(){
        protein = sharedPreferences.getBoolean(activity.getString(R.string.protein_key),false);
        carbs = sharedPreferences.getBoolean(activity.getString(R.string.carbs_key),false);
        fat = sharedPreferences.getBoolean(activity.getString(R.string.fat_key),false);
        balance = sharedPreferences.getBoolean(activity.getString(R.string.balanced_key),false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity=(Activity) context;
        }
    }

    //get the state of each switches-whether they are switched on or not
    public void getSwitchesKey(){
        protein_key = activity.getString(R.string.protein_key);
        carbs_key = activity.getString(R.string.carbs_key);
        fat_key = activity.getString(R.string.fat_key);
        balance_key = activity.getString(R.string.balanced_key);
    }

    //Arraylist that return swicthes info such as their keys and states
    public  ArrayList<Settings_swicthes_infos> getSwitchesInfos(){
        ArrayList<Settings_swicthes_infos> swicthes_infosArrayList = new ArrayList<>();
        getstateofSwitches();
        getSwitchesKey();
        swicthes_infosArrayList.add(new Settings_swicthes_infos(protein,protein_key));
        swicthes_infosArrayList.add(new Settings_swicthes_infos(carbs,carbs_key));
        swicthes_infosArrayList.add(new Settings_swicthes_infos(fat,fat_key));
        swicthes_infosArrayList.add(new Settings_swicthes_infos(balance,balance_key));
        return swicthes_infosArrayList;
    }
    //return the number of enabled switches
    public int checkNumberOfenabledSwitches(){
        ArrayList<Boolean> switchedOn = new ArrayList<>();
        getstateofSwitches();
        switchedOn.add(protein);switchedOn.add(carbs);switchedOn.add(fat);
        switchedOn.add(balance);
        int onSwitches = 0;
        for (int i =0; i<switchedOn.size();i++){
            if(switchedOn.get(i) == true){
                onSwitches++;
            }
        }
        switches = onSwitches;
        return onSwitches;
    }

    public static int getswitches(){
        return switches;
    }

    private void setUpshared(){
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        //number of enabled switches
        int onSwitches = checkNumberOfenabledSwitches();
        //reference the enabled switches
        ArrayList<String>off_switches = offSwitches();
        //control the swicthes flow
        switchesControlFlow(onSwitches,off_switches);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        int onSwitches = checkNumberOfenabledSwitches();
        ArrayList<String>off_switches = offSwitches();
        switchesControlFlow(onSwitches,off_switches);

    }
    //control the UI of switches
    public void switchesControlFlow(int onSwitches, ArrayList<String>offSwitches){
        //disbale switches if 3 already selected
        if(onSwitches == 2){
            disableoffSwitches(offSwitches);
        }
        //Enable off swicthes is less than 3 are selected
        else if(onSwitches<2){
            enableoffSwitches(offSwitches);
        }
    }

    //Return the keys/name of the switches that are off
    public ArrayList<String> offSwitches(){
        ArrayList<Settings_swicthes_infos> switches = new ArrayList<>();
        ArrayList<String> switchedOff = new ArrayList<>();
        switches = getSwitchesInfos();
        //Toast.makeText(getContext()," switches number "+ switches.size(),Toast.LENGTH_LONG).show();
        for (int i =0; i<switches.size();i++){
            if(switches.get(i).getState() == false){
                switchedOff.add(switches.get(i).getKey());
            }
        }
        return switchedOff;
    }


    //Disable switches that are turned off
    public void disableoffSwitches(ArrayList<String> offSwitches){
        for (int i = 0;i<offSwitches.size();i++) {
            if (offSwitches.get(i).equals(activity.getString(R.string.balanced_key))) {
                balanceSwitch.setEnabled(false);
            }
            if (offSwitches.get(i).equals(activity.getString(R.string.protein_key))) {
                proteinSwitch.setEnabled(false);
            }
            if (offSwitches.get(i).equals(activity.getString(R.string.fat_key))) {
                fatSwitch.setEnabled(false);
            }
            if (offSwitches.get(i).equals(activity.getString(R.string.carbs_key))) {
                carbsSwitch.setEnabled(false);
            }
        }
    }

    //Disable switches that are turned off
    public void enableoffSwitches(ArrayList<String> offSwitches){
        for (int i = 0;i<offSwitches.size();i++) {
            if (offSwitches.get(i).equals(activity.getString(R.string.balanced_key))) {
                balanceSwitch.setEnabled(true);
            }
            if (offSwitches.get(i).equals(activity.getString(R.string.protein_key))) {
                proteinSwitch.setEnabled(true);
            }
            if (offSwitches.get(i).equals(activity.getString(R.string.fat_key))) {
                fatSwitch.setEnabled(true);
            }
            if (offSwitches.get(i).equals(activity.getString(R.string.carbs_key))) {
                carbsSwitch.setEnabled(true);
            }
        }
    }

}
