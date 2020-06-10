package com.example.gocorona;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gocorona.adapter.CoronaAdapter;
import com.example.gocorona.loader.CoronaLoader;
import com.example.gocorona.maps.MapsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager.LoaderCallbacks;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Corona>> {

    public static final String LOG_TAG = "MainActivity";

    private TextView mEmptyStateTextView;

    private static final String COVID19_DATA = "https://api.covid19api.com/summary";

    private CoronaAdapter mAdapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    FusedLocationProviderClient fusedLocationProviderClient;
    int LOCATION_REQUEST_CODE = 10001;
    public double currentLongitude;
    public double currentLatitude;

    ArrayList<String> countrySlug = new ArrayList<>();

    static GlobalVariables globalVariables = new GlobalVariables();

    private static final int CORONA_LOADER_ID = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open, R.string.close);

      //  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.add_hotspots:
                        Log.d(LOG_TAG,"onNavigationItemSelected id==R.id.add_hotspots");
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            getLastLocation();
                        } else {
                            askLocationPermission();
                        }
                        return true;
                }

                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);


        if(true){
            //android.app.LoaderManager loaderManager = getLoaderManager();
            Log.d(LOG_TAG,"initLoader called");
            getSupportLoaderManager().initLoader(CORONA_LOADER_ID,null, this);
        } else{
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setEmptyView(mEmptyStateTextView);

        mAdapter = new CoronaAdapter(this,new ArrayList<Corona>());
        listView.setAdapter(mAdapter);

       // countrySlug = globalVariables.getCountryArrayList();

       // Log.d(LOG_TAG,"onItemClick: country "+countrySlug);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Log.d(LOG_TAG,"onItemClick: country "+countrySlug.get(position));

                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("Country",countrySlug.get(position));
                startActivity(intent);
            }

        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        Log.d(LOG_TAG,"onOptionsItemSelected called id: "+id);

        if (mToggle.onOptionsItemSelected(item)){
            Log.d(LOG_TAG,"mToggle.onOptionsItemSelected");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<List<Corona>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(LOG_TAG,"TEST: onCreateLoader called");

        return new CoronaLoader(this, COVID19_DATA);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Corona>> loader, List<Corona> data) {

        Log.d(LOG_TAG,"TEST: onLoadFinished called data: "+data);
        mEmptyStateTextView.setText(R.string.no_corona);

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();

        if(data!=null || !data.isEmpty()){
            mAdapter.addAll(data);

            countrySlug = globalVariables.getCountryArrayList();

        }else if(data==null){
            mAdapter.clear();
            mEmptyStateTextView.setText(R.string.no_corona);
        }

        Log.d(LOG_TAG,"TEST: onLoadFinished countrySlug:  "+countrySlug);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Corona>> loader) {
        mAdapter.clear();
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(LOG_TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getLastLocation();
            } else {
                currentLongitude = 151;
                currentLatitude = -34;
            }
        }
    }

    public void getLastLocation(){
        Log.d(LOG_TAG,"getLastLocation inside");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    Log.d(LOG_TAG,"onSuccess:  "+location.toString());
                    Log.d(LOG_TAG,"Location longtitude: "+location.getLongitude());
                    Log.d(LOG_TAG,"Location latitude: "+location.getLatitude());

                    currentLongitude = location.getLongitude();
                    currentLatitude = location.getLatitude();
                    Log.d(LOG_TAG,"getLastLocation currentLongitude: "+currentLongitude+" currentLatitude: "+currentLatitude);

                    Intent addHotspotIntent = new Intent(MainActivity.this, MapsActivity.class);
                    addHotspotIntent.putExtra("currentLongitude",currentLongitude);
                    addHotspotIntent.putExtra("currentLatitude",currentLatitude);
                    startActivity(addHotspotIntent);

                }else{
                    Log.d(LOG_TAG,"OnSuccess Location was null....");
                    currentLongitude = 151;
                    currentLatitude = -34;
                    Intent addHotspotIntent = new Intent(MainActivity.this, MapsActivity.class);
                    addHotspotIntent.putExtra("currentLongitude",currentLongitude);
                    addHotspotIntent.putExtra("currentLatitude",currentLatitude);
                    startActivity(addHotspotIntent);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(LOG_TAG,"onFailure: "+e.getLocalizedMessage());
                currentLongitude = 151;
                currentLatitude = -34;
                Intent addHotspotIntent = new Intent(MainActivity.this, MapsActivity.class);
                addHotspotIntent.putExtra("currentLongitude",currentLongitude);
                addHotspotIntent.putExtra("currentLatitude",currentLatitude);
                startActivity(addHotspotIntent);

            }
        });
    }
}
