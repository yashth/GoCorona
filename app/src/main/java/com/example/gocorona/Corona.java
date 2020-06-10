package com.example.gocorona;

public class Corona {

    private String mCountrySlug;
    private String mCountryName;
    private int mTotalConfirmed;


    public Corona(String countrySlug, String countryName, int totalConfirmed){

        mCountrySlug = countrySlug;
        mCountryName = countryName;
        mTotalConfirmed = totalConfirmed;

    }

    public String getCountrySlug(){
        return mCountrySlug;
    }

    public String getCountryName(){
        return mCountryName;
    }

    public int getTotalConfirmed(){
        return mTotalConfirmed;
    }


}
