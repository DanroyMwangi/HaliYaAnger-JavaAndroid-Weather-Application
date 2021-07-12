package com.danroy.haliyaanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.danroy.haliyaanger.classes.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {

    public ArrayList<String> countryName;
    public ArrayList<String> cityName;
    public ArrayList<String> lat;
    public ArrayList<String> lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        String jsonString;
        try {
            InputStream is = getAssets().open("countries.json");

            countryName= new ArrayList<>();
            cityName= new ArrayList<>();
            lat= new ArrayList<>();
            lng= new ArrayList<>();

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
                this.countryName.add(countryName);
                this.cityName.add(cityName);
                this.lat.add(lat);
                this.lng.add(lng);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        try {
            Intent intent = new Intent(this,CitiesListActivity.class);
            intent.putExtra("countryName",countryName);
            intent.putExtra("cityName",cityName);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            Thread.sleep(20*1000);
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}