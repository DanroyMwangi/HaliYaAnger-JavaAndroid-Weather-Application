package com.danroy.haliyaanger.classes;

import android.os.AsyncTask;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class CountryAPI extends AsyncTask<String, Void, ArrayList<Country>> {
    private static HttpURLConnection connection;
    private ArrayList<Country> countriesList = new ArrayList<>();

    public CountryAPI(){
    }

    public ArrayList<Country> getCountries() {
        return countriesList;
    }

    public ArrayList<Country> FetchCountryData() throws IOException, JSONException {
        URL url = new URL("https://raw.githubusercontent.com/lutangar/cities.json/master/cities.json");

        connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.connect();

        int statusCode = connection.getResponseCode();
        String line;
        BufferedReader reader;
        StringBuffer response = new StringBuffer();
        if(statusCode<=200 && statusCode<300){
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }
        else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        while((line = reader.readLine())!=null){
            response.append(line);
        }
        reader.close();
        connection.disconnect();
        JSONArray countriesJSON = new JSONArray(response.toString());
        for(int i=0;i<10;i++){
            JSONObject countryOBJ = countriesJSON.getJSONObject(i);
            String countryName = countryOBJ.getString("country");
            String cityName = countryOBJ.getString("name");
            String longitude = countryOBJ.getString("lng");
            String latitude = countryOBJ.getString("lat");
            this.countriesList.add(new Country(countryName,cityName,latitude,longitude));
        }
        return countriesList;
    }
    protected ArrayList<Country> doInBackground(String... strings) {
        try {
            return FetchCountryData();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Country> s) {
        super.onPostExecute(s);
    }
}