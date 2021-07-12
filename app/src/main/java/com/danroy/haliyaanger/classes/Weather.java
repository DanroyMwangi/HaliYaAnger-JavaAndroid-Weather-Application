package com.danroy.haliyaanger.classes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;

public class Weather {
    private JSONObject weatherJSON;
    private String coordinates,weather,temperaturexOthers,wind,clouds,sys,timezone,cityName;
    private String mainWeather;
    private String weatherDescription;

    public String getCoordinates() {
        return coordinates;
    }

    public String getWeather() {
        return weather;
    }

    public String getTemperaturexOthers() {
        return temperaturexOthers;
    }

    public String getWind() {
        return wind;
    }

    public String getClouds() {
        return clouds;
    }

    public String getSys() {
        return sys;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getCityName() {
        return capitalizeWord(cityName);
    }

    public String getMainWeather() {
        return capitalizeWord(mainWeather);
    }

    public String getWeatherDescription() {
        return capitalizeWord(weatherDescription);
    }

    public String getCountry() {
        return country;
    }

    public int getMainTemp() {
        return mainTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDegree() {
        return degree;
    }

    public int getGust() {
        return gust;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getSunrise() {
        Date date = Date.from(Instant.ofEpochSecond(sunrise));
        return (date.getHours()+":"+(date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes())+":"+(date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds())+" am");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getSunset() {
        Date date = Date.from(Instant.ofEpochSecond(sunset));
        return ((date.getHours()-12)+":"+(date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes())+":"+(date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds())+" pm");
    }

    private String country;
    int mainTemp,minTemp,maxTemp,humidity,pressure,statusCode,speed,degree,gust,sunrise,sunset,id;


    public Weather(JSONObject weatherJSON) throws JSONException {
        this.weatherJSON = weatherJSON;
        WeatherJSONParser();
        WeatherStringParser();
        TempXOtherStringParser();
        WindStringParser();
        SysStringParse();
    }

    private void WeatherJSONParser() throws JSONException {
        coordinates = weatherJSON.getJSONObject("coord").toString();
        weather = weatherJSON.getJSONArray("weather").toString();
        temperaturexOthers = weatherJSON.getJSONObject("main").toString();
        wind = weatherJSON.getJSONObject("wind").toString();
        clouds = Integer.toString(weatherJSON.getJSONObject("clouds").getInt("all"));
        sys = weatherJSON.getJSONObject("sys").toString();
        timezone = Integer.toString(weatherJSON.getInt("timezone"));
        cityName = weatherJSON.getString("name");
        statusCode = weatherJSON.getInt("cod");
    }

    public int getId() {
        return id;
    }

    private void WeatherStringParser() throws JSONException {
        JSONArray weatherArray = new JSONArray(this.weather);
        for(int j=0;j<weatherArray.length();j++){
            JSONObject weatherElements = weatherArray.getJSONObject(j);
            mainWeather = weatherElements.getString("main");
            weatherDescription = weatherElements.getString("description");
            id = weatherElements.getInt("id");
        }
    }
    private void TempXOtherStringParser() throws JSONException {
        JSONObject temperature = new JSONObject(this.temperaturexOthers);
        mainTemp = temperature.getInt("temp");
        minTemp = temperature.getInt("temp_min");
        maxTemp = temperature.getInt("temp_max");
        humidity = temperature.getInt("humidity");
        pressure = temperature.getInt("pressure");
    }
    private void WindStringParser() throws JSONException {
        JSONObject wind = new JSONObject(this.wind);
        speed = wind.getInt("speed");
        degree = wind.getInt("deg");
        if(wind.has("gust")){
            gust = wind.getInt("gust");
        }
        else{
            gust = 0;
        }
    }
    private void SysStringParse() throws JSONException {
        JSONObject sys = new JSONObject(this.sys);
        country = sys.getString("country");
        sunrise = sys.getInt("sunrise");
        sunset = sys.getInt("sunset");
    }
    public static String capitalizeWord(String str){
        String[] words =str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }
}
