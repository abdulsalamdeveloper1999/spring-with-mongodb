package com.asdevify.springWithMongo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.asdevify.springWithMongo.cache.AppCache;
import com.asdevify.springWithMongo.entities.WeatherPojo;

@Component
public class WeatherService {


    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

   public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        
    }

    @Value("${weather.api.key}")
    public String API_KEY;
    // public static  final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    public WeatherPojo getWeather(String city) {
        ResponseEntity<WeatherPojo> exchange = restTemplate.exchange(appCache.APP_CACHE.get("weather_api").replace("<city>", city).replace("<apiKey>", API_KEY), HttpMethod.GET, null, WeatherPojo.class);
        return exchange.getBody();
        
    }

}