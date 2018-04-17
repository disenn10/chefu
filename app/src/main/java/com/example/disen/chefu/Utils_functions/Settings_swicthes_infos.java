package com.example.disen.chefu.Utils_functions;

/**
 * Created by disen on 3/18/2018.
 */

public class Settings_swicthes_infos {
    Boolean state;
    String key;

    public  Settings_swicthes_infos(Boolean switch_state, String switch_key){
        this.state = switch_state;
        this.key = switch_key;
    }

    public Boolean getState() {
        return state;
    }

    public String getKey() {
        return key;
    }
}
