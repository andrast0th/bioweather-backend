
package com.example.bioweatherbackend.model.weather;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lat",
    "lon",
    "timezone",
    "timezone_offset",
    "current",
    "minutely",
    "hourly",
    "daily"
})
@Generated("jsonschema2pojo")
public class WeatherForecastDto {
    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("timezone_offset")
    private Integer timezoneOffset;
    @JsonProperty("current")
    private Current current;
    @JsonProperty("minutely")
    private List<Minutely> minutely;
    @JsonProperty("hourly")
    private List<Hourly> hourly;
    @JsonProperty("daily")
    private List<Daily> daily;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }

    @JsonProperty("timezone")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @JsonProperty("timezone_offset")
    public Integer getTimezoneOffset() {
        return timezoneOffset;
    }

    @JsonProperty("timezone_offset")
    public void setTimezoneOffset(Integer timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    @JsonProperty("current")
    public Current getCurrent() {
        return current;
    }

    @JsonProperty("current")
    public void setCurrent(Current current) {
        this.current = current;
    }

    @JsonProperty("minutely")
    public List<Minutely> getMinutely() {
        return minutely;
    }

    @JsonProperty("minutely")
    public void setMinutely(List<Minutely> minutely) {
        this.minutely = minutely;
    }

    @JsonProperty("hourly")
    public List<Hourly> getHourly() {
        return hourly;
    }

    @JsonProperty("hourly")
    public void setHourly(List<Hourly> hourly) {
        this.hourly = hourly;
    }

    @JsonProperty("daily")
    public List<Daily> getDaily() {
        return daily;
    }

    @JsonProperty("daily")
    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
