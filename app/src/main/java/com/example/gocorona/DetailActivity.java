package com.example.gocorona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gocorona.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    TextView mCountryName,mTotalConfirmed,mTotalDeath,mTotalActive,mTotalRecovered;
    String country_slug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle(R.string.corona_details);

        mCountryName = (TextView) findViewById(R.id.country_name_detail);
        mTotalConfirmed = (TextView) findViewById(R.id.total_confirmed_detail);
        mTotalDeath = (TextView) findViewById(R.id.total_deaths);
        mTotalActive = (TextView) findViewById(R.id.total_active);
        mTotalRecovered = (TextView) findViewById(R.id.total_recovered);

        Intent intent = getIntent();
        country_slug = intent.getStringExtra("Country");

        FetchCoronaDetails fetchCoronaDetails = new FetchCoronaDetails();
        fetchCoronaDetails.execute(country_slug);




    }


    public class FetchCoronaDetails extends AsyncTask<String, Void, Void> {


        String countryName;
        Integer totalConfirmed, totalDeath, totalRecovered, totalActive;
        Double ratings;

        @Override
        protected Void doInBackground(String... voids) {


            URL coronaDetailsURL;
            coronaDetailsURL = NetworkUtils.buildUrlWithCountry(voids[0]);
            Log.d("DetailActivity"," coronaDetailsURL: "+coronaDetailsURL);

            try {

                String jsonCoronaDetails = NetworkUtils.getResponseFromHttpUrl(coronaDetailsURL);
                Log.d("DetailActivity"," jsonCoronaDetails: "+jsonCoronaDetails);

                //JSONObject jsonObject = new JSONObject(jsonCoronaDetails);
                //Log.d("DetailActivity"," jsonObject: "+jsonObject);

                JSONArray jsonArray = new JSONArray(jsonCoronaDetails);
                Log.d("DetailActivity"," jsonArray: "+jsonArray);

                JSONObject coronaJsonObject = jsonArray.getJSONObject(jsonArray.length()-1);
                Log.d("DetailActivity"," coronaJsonObject: "+coronaJsonObject);

                countryName = coronaJsonObject.getString("Country");
                totalConfirmed = coronaJsonObject.getInt("Confirmed");
                totalDeath = coronaJsonObject.getInt("Deaths");
                totalRecovered = coronaJsonObject.getInt("Recovered");
                totalActive = coronaJsonObject.getInt("Active");

                Log.d("DetailActivity","countryName: "+countryName+" totalConfirmed: "+ totalConfirmed+" totalDeath: "+totalDeath+" totalRecovered: "+totalRecovered+" totalActive: "+totalActive);





            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mCountryName.setText(countryName);
            mTotalConfirmed.setText(totalConfirmed.toString());
            mTotalDeath.setText(totalDeath.toString());
            mTotalActive.setText(totalRecovered.toString());
            mTotalRecovered.setText(totalActive.toString());

        }
    }
}
