package com.asdevify.springWithMongo.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
 public class CurrentPojo{
    @JsonProperty("observation_time")
    private String observationTime;
    private int temperature;
    
    @JsonProperty("weather_descriptions")
    private List<String> weatherDescriptions;
    
    

    private int feelslike;
   }