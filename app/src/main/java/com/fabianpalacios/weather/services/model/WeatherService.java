package com.fabianpalacios.weather.services.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLQueryItem;
import cafsoft.foundation.URLSession;

public class WeatherService {

    private  String theAPIKey = "";

    public WeatherService(String newAPIKey){
        theAPIKey = newAPIKey;
    }

    public void requestWeatherData(String cityName, String countryISOCode, OnDataResponse delegate){
        URL url = null;
        URLComponents components = new URLComponents();

        components.setScheme("https");
        components.setHost("api.openweathermap.org");
        components.setPath("/data/2.5/weather");
        components.setQueryItems(new URLQueryItem[]{
                new URLQueryItem("appid", theAPIKey),
                new URLQueryItem("units", "metric"),
                new URLQueryItem("lang", "es"),
                new URLQueryItem("q",cityName +"," + countryISOCode)
        });

        URLSession.getShared().dataTask(components.getURL(), (data, response, error) -> {
            HTTPURLResponse resp = (HTTPURLResponse) response;

            Root root = null;
            int statusCode = -1;
            if(error == null && resp.getStatusCode() == 200){
                String text = data.toText();
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
                Gson gson = gsonBuilder.create();
                root = gson.fromJson(text, Root.class);
                statusCode = resp.getStatusCode();
            }
            if(delegate != null){
                delegate.onChange(error != null, statusCode,root);
            }
        }).resume();
    }

    public interface OnDataResponse{
        public abstract void onChange(boolean isNetworkError, int statusCode, Root root);
    }
}
