package com.danroy.haliyaanger.classes;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import com.danroy.haliyaanger.R;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class WeatherAPI extends AsyncTask<String,Void,String>{
    private static HttpsURLConnection connection;

    private String city;
    private String latitude,longitude;
    protected  ArrayList<View> views;

    public WeatherAPI(String city, ArrayList<View> views) throws IOException {
        this.city = city;
        this.views =views;
    }
    public WeatherAPI(String latitude,String longitude, ArrayList<View> views){
        this.latitude =latitude;
        this.longitude = longitude;
        this.views =views;
    }
    public String FetchData() throws IOException {
        try{
            String apiKey = "09a794a45f31c571ea588fb5c1d8d8bb";
            if(this.city != null&& latitude==null && longitude==null){
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+ this.city +"&appid="+apiKey);

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
                return response.toString();
            }
            else if(city == null && latitude!=null && longitude!=null){
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+ this.latitude+ "&lon=" +this.longitude+"&appid="+apiKey);

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
                return response.toString();
            }
        }catch (Exception e){
            //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return null;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            return FetchData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null)
        {
            try {
                Weather weather = new Weather(new JSONObject(s));
                TextView cityTV = (TextView) views.get(0);
                TextView weatherDescriptionTV =(TextView) views.get(1);
                TextView windDirectionTV =(TextView) views.get(2);
                TextView windSpeedTV =(TextView) views.get(3);
                TextView windGustTV =(TextView) views.get(4);
                TextView tempMaxTV =(TextView) views.get(5);
                TextView humidityTV =(TextView) views.get(6);
                TextView sunriseTV =(TextView) views.get(7);
                TextView sunsetTV =(TextView) views.get(8);
                ImageView weatherIV =(ImageView) views.get(9);


                cityTV.setText(weather.getCityName());
                weatherDescriptionTV.setText(weather.getWeatherDescription());
                windDirectionTV.setText("Direction: "+weather.getDegree());
                windSpeedTV.setText("Speed: "+weather.getSpeed());
                windGustTV.setText("Gust: "+weather.getGust());
                tempMaxTV.setText(String.valueOf(weather.getMainTemp()));
                humidityTV.setText(String.valueOf(weather.getHumidity()));
                sunriseTV.setText(weather.getSunrise()+" EAT");
                sunsetTV.setText(weather.getSunset()+" EAT");

                //Weather Image
                int weatherId = weather.getId();
                if(weatherId>=200 && weatherId<=232){
                    weatherIV.setImageResource(R.drawable._050_heavy_rain);
                }
                else if(weatherId>=300 && weatherId<=322){
                    weatherIV.setImageResource(R.drawable._049_rain);
                }
                else if(weatherId>=500 && weatherId<=531){
                    weatherIV.setImageResource(R.drawable._047_umbrella);
                }
                else if(weatherId>=600 && weatherId<=622){
                    weatherIV.setImageResource(R.drawable._010_snowy);
                }
                else if(weatherId==800){
                    weatherIV.setImageResource(R.drawable._011_sunny);
                }
                else if(weatherId>=801 && weatherId<=804){
                    weatherIV.setImageResource(R.drawable._022_cloudy);
                }
                else if(weatherId==701){
                    weatherIV.setImageResource(R.drawable._018_mist);
                }
                else if(weatherId==781){
                    weatherIV.setImageResource(R.drawable._024_hurricane);
                }
                else if(weatherId==741){
                    weatherIV.setImageResource(R.drawable._016_foggy);
                }
                else{
                    weatherIV.setImageResource(R.drawable._034_satellite);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
