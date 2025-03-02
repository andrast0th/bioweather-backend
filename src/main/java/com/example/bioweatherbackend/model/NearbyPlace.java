package com.example.bioweatherbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyPlace {
    public String adminCode1;
    public String lng;
    public String distance;
    public int geonameId;
    public String toponymName;
    public String countryId;
    public String fcl;
    public int population;
    public String countryCode;
    public String name;
    public String fclName;
    public String countryName;
    public String fcodeName;
    public String adminName1;
    public String lat;
    public String fcode;
}