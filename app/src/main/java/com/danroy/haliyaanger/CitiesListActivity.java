package com.danroy.haliyaanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.danroy.haliyaanger.classes.CitiesRecylerViewAdapter;
import com.danroy.haliyaanger.classes.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CitiesListActivity extends AppCompatActivity {

    public ArrayList<Country> countries;
    public CitiesRecylerViewAdapter citiesAdapter;
    public RecyclerView citiesRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);


        citiesRV = findViewById(R.id.citiesRV);
        backgroundTask backgroundTask = new backgroundTask();
        backgroundTask.execute();
    }
    @SuppressLint("StaticFieldLeak")
    public class backgroundTask extends AsyncTask<Void,Integer,ArrayList<Country>>{
        ProgressDialog progBar;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progBar = new ProgressDialog(CitiesListActivity.this);
            progBar.setTitle("Fetching Cities.");
            progBar.setMessage("Please wait");
            progBar.setCancelable(false);
            progBar.show();
        }

        @Override
        protected ArrayList<Country> doInBackground(Void... voids) {
            String jsonString;
            try {
                InputStream is = getAssets().open("countries.json");

                countries = new ArrayList<>();
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                jsonString = new String(buffer, "UTF-8");
                JSONArray countriesJSONArray = new JSONArray(jsonString);
                for(int i = 0;i<countriesJSONArray.length();i++){
                    JSONObject countryObj = countriesJSONArray.getJSONObject(i);
                    String countryName = countryObj.getString("country");
                    String cityName = countryObj.getString("name");
                    String lat = String.valueOf(countryObj.getDouble("lat"));
                    String lng = String.valueOf(countryObj.getDouble("lng"));
                    if(countryName.equals("KE")){
                        countries.add(new Country(countryName,cityName,lat,lng));
                    }
                }
                publishProgress(countries.size());
                return countries;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> countries) {
            super.onPostExecute(countries);

            progBar.dismiss();

            citiesAdapter = new CitiesRecylerViewAdapter(CitiesListActivity.this,countries);
            citiesRV.setAdapter(citiesAdapter);
            citiesRV.setLayoutManager(new LinearLayoutManager(CitiesListActivity.this));
        }
    }
}