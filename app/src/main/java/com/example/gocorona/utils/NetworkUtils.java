package com.example.gocorona.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.gocorona.Corona;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.example.gocorona.GlobalVariables;

public class NetworkUtils  {


    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    final static String CORONA_BASE_URL = "https://api.covid19api.com/summary";

    final static String CORONA_COUNTRY_URL = "https://api.covid19api.com/total/country/";

    static ArrayList<String> countrySlugList;

    static GlobalVariables globalVariables = new GlobalVariables();

    public static URL buildUrlWithCountry(String country) {

        URL url = null;
        try {
            url = new URL(CORONA_COUNTRY_URL+country);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

    public static URL buildUrlAllStatus(){

        URL url = null;

        try {
            url = new URL(CORONA_BASE_URL);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static List<Corona> fetchCoronaDetail(String requestUrl){

        URL url = buildUrlAllStatus();
        Log.d(LOG_TAG, "fetchCoronaDetail requestUrl: "+requestUrl);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = "";
        try{
            jsonResponse = makeHttpResponse(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);

        }

        List<Corona> coronaDetails = extractFeature(jsonResponse);

        return coronaDetails;
    }

    private static List<Corona> extractFeature(String jsonResponse){

        countrySlugList = new ArrayList<>();
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        Log.d(LOG_TAG, "extractFeature jsonResponse: "+jsonResponse);

        List<Corona> coronaDetails = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("Countries");
            JSONObject coronaJsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                coronaJsonObject = jsonArray.getJSONObject(i);
                String countrySlug = coronaJsonObject.getString("Slug");
                String country = coronaJsonObject.getString("Country");
                int totalConfirmed = coronaJsonObject.getInt("TotalConfirmed");

                Log.d(LOG_TAG, "extractFeature countrySlug: "+countrySlug+" country: "+country+" totalConfirmed: "+totalConfirmed);


                Corona corona = new Corona(countrySlug, country, totalConfirmed);
                coronaDetails.add(corona);
                countrySlugList.add(countrySlug);

            }



        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        globalVariables.setCountryArrayList(countrySlugList);

        Log.d(LOG_TAG, "extractFeature coronaDetails: "+coronaDetails);

        return coronaDetails;

    }

    private static String makeHttpResponse(URL url) throws IOException {
        String jsonResponse="";
        Log.d(LOG_TAG, "makeHttpResponse url: "+url);
        if(url==null){
            return null;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            }else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            Log.d(LOG_TAG,"line: "+line);
            while(line!=null){
                output.append(line);
                Log.d(LOG_TAG,"line: "+line);
                line = reader.readLine();
            }

        }
        return output.toString();
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        try{

            InputStream in = urlConnection.getInputStream();
            Scanner sc = new Scanner(in);
            sc.useDelimiter("\\A");


            boolean hasInput = sc.hasNext();
            if (hasInput){
                return sc.next();
            }else{
                return null;
            }

        }finally {
            urlConnection.disconnect();
        }

    }

}
