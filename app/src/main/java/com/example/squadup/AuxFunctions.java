package com.example.squadup;

import com.example.squadup.MainActivity;

public class AuxFunctions {

    public static Boolean isGhostMode(){
        if(MainActivity.getGhostMode() == true)
            return true;
        else
            return false;
    }


}
