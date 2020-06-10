package com.example.gocorona.loader;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.gocorona.Corona;
import com.example.gocorona.utils.NetworkUtils;

import java.util.List;

public class CoronaLoader extends AsyncTaskLoader<List<Corona>> {

    private static final String LOG_TAG = "CoronaLoader";

    private String mUrl;

    public CoronaLoader(@NonNull Context context, String url) {
        super(context);

        Log.d(LOG_TAG,"TEST: QuakeLoader called ");
        mUrl = url;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<Corona> loadInBackground() {
        Log.d(LOG_TAG,"TEST: loadInBackground called ");

        if(mUrl==null){
            return null;
        }

        List<Corona> corona = NetworkUtils.fetchCoronaDetail(mUrl);

        return corona;

    }
}
