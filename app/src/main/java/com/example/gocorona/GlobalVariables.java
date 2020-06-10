package com.example.gocorona;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariables extends Application {


    static ArrayList<String> countryArrayList = new ArrayList<String>();

   /* public static float currentLongitude;
    public static float currentLatitude;


    public float getCurrentLongitude(){
        return currentLongitude;
    }

    public float getCurrentLatitude(){
        return currentLatitude;
    }

    public void setCurrentLongitude(float currentLongitude){
        this.currentLongitude = currentLongitude;

        Log.d("GlobalVariables","setCurrentLatitude currentLongitude: "+currentLongitude);
        Log.d("GlobalVariables","setCurrentLatitude this.currentLongitude: "+this.currentLongitude);
    }

    public void setCurrentLatitude(float currentLatitude){
        this.currentLatitude = currentLatitude;
        Log.d("GlobalVariables","setCurrentLatitude currentLatitude: "+currentLatitude);
        Log.d("GlobalVariables","setCurrentLatitude this.currentLatitude: "+this.currentLatitude);
    }*/

    public ArrayList<String> getCountryArrayList() {
        Log.d("GlobalVariables","getCountryArrayList countryArrayList: "+this.countryArrayList);
        return this.countryArrayList;
    }

    public void setCountryArrayList(ArrayList<String> countryArrayList) {
        Log.d("GlobalVariables","setCountryArrayList countryArrayList: "+countryArrayList);
        //this.countryArrayList = null;
        this.countryArrayList = countryArrayList;

        Log.d("GlobalVariables","setCountryArrayList countryArrayList: "+this.countryArrayList);
    }
}
