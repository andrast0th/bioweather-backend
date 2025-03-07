package com.example.bioweatherbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Place {
    public String adminCode1;
    public String lng;
    public String distance;

    private int id;

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

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("geonameId")
    public void setId(int id) {
        this.id = id;
    }
}