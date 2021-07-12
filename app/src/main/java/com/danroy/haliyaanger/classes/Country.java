package com.danroy.haliyaanger.classes;

public class Country {
    private String countryName,cityName,latitude,longitude;
    public Country(String countryName, String cityName, String latitude, String longitude){
        this.countryName = countryName;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getCountryName() {
        return countryName;
    }
    public String getCityName() {
        return cityName;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
}
