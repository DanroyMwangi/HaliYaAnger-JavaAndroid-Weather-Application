package com.danroy.haliyaanger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.danroy.haliyaanger.classes.Weather;
import com.danroy.haliyaanger.classes.WeatherAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public TextView cityTV, weatherDescriptionTV, windSpeedTV, windDirectionTV, windGustTV, tempMaxTV, humidityTV, sunriseTV, sunsetTV;
    public ImageView weatherIV;
    public ImageButton settingsBtn;
    public String latitude, longitude, data;
    public ArrayList<View> views = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize views
        ViewsInitializer();

        //Initialize location
        if(latitude == null && longitude == null){
            Location();
        }

        //Settings Button Click Listener
        BtnListeners();
    }

    public void ViewsInitializer() {
        //TextViews(TV)
        cityTV = (TextView) findViewById(R.id.cityTV);
        weatherDescriptionTV = (TextView) findViewById(R.id.weatherDescriptionTV);
        windSpeedTV = (TextView) findViewById(R.id.windSpeedTV);
        windDirectionTV = (TextView) findViewById(R.id.windDirectionTV);
        windGustTV = (TextView) findViewById(R.id.windGustTV);
        tempMaxTV = (TextView) findViewById(R.id.tempMaxValueTV);
        humidityTV = (TextView) findViewById(R.id.humidityTV);
        sunriseTV = (TextView) findViewById(R.id.sunriseTV);
        sunsetTV = (TextView) findViewById(R.id.sunsetTV);
        //ImageView(IV)
        weatherIV = (ImageView) findViewById(R.id.weatherIV);
        //ImageButton
        settingsBtn = (ImageButton) findViewById(R.id.settingsBtn);

        //Adding the views to the array
        views.add(cityTV);
        views.add(weatherDescriptionTV);
        views.add(windDirectionTV);
        views.add(windSpeedTV);
        views.add(windGustTV);
        views.add(tempMaxTV);
        views.add(humidityTV);
        views.add(sunriseTV);
        views.add(sunsetTV);
        views.add(weatherIV);
    }

    public void BtnListeners() {
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        cityTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CitiesListActivity.class);
                startActivityForResult(intent,12345);
            }
        });
    }

    private void Location() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE},12);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location!=null){
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            new WeatherAPI(latitude,longitude,views).execute();
        }
        else {
            if (permission) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
                        50, new LocationListener() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {
                                latitude = String.valueOf(location.getLatitude());
                                longitude = String.valueOf(location.getLongitude());
                                new WeatherAPI(latitude,longitude,views).execute();
                            }
                            @Override
                            public void onProviderEnabled(@NonNull String provider) {

                            }

                            @Override
                            public void onProviderDisabled(@NonNull String provider) {

                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                new WeatherAPI(latitude,longitude,views).execute();
                            }
                        });
            }
            else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE},12);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 12345){
            if (resultCode == Activity.RESULT_OK){
                assert data != null;
                String cityName = data.getStringExtra("cityName");

                try {
                    new WeatherAPI(cityName,views).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        else{
            Toast.makeText(this, "Cannot Find City", Toast.LENGTH_SHORT).show();
        }
    }
}