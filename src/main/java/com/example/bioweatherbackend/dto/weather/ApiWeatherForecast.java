package com.example.bioweatherbackend.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiWeatherForecast {
    private ApiValueAndUnit<Double> temp;
    private ApiValueAndUnit<Double> tempAvg;
    private ApiValueAndUnit<Double> tempMin;
    private ApiValueAndUnit<Double> tempMax;
    private ApiValueAndUnit<Double> tempWind;
    private ApiValueAndUnit<Double> tempFelt;
    private ApiValueAndUnit<Double> temp5Cm;
    private ApiValueAndUnit<Double> tempM5Cm;
    private ApiValueAndUnit<Double> tempM20Cm;
    private ApiValueAndUnit<Double> tempM100Cm;
    private ApiValueAndUnit<Double> windforce;
    private ApiValueAndUnit<Double> winddir;
    private ApiValueAndUnit<Double> gustforce;
    private String symb;
    private String daynight;
    private String txt;
    private ApiValueAndUnit<Double> sunProb;
    private ApiValueAndUnit<Double> sun;
    private ApiValueAndUnit<Double> precipProb;
    private ApiValueAndUnit<Double> precip;
    private ApiValueAndUnit<Double> precipPrecise;
    private ApiValueAndUnit<Double> clouds;
    private ApiValueAndUnit<Double> tempDew;
    private String accur;
    private ApiValueAndUnit<Double> hum;
    private ApiValueAndUnit<Double> evapo;
    private ApiValueAndUnit<Double> precipRangemin;
    private ApiValueAndUnit<Double> precipRangemax;
    private ApiValueAndUnit<Double> precipRangeminPrecise;
    private ApiValueAndUnit<Double> precipRangemaxPrecise;
    private ApiSnowLine snowLine;
    private ApiSnowLine freezLevel;
    private ApiValueAndUnit<Double> fogLevel;
    private String snowOnRoadIndex;
    private ApiValueAndUnit<Double> snow;
    private ApiValueAndUnit<Double> snowRangemin;
    private ApiValueAndUnit<Double> snowRangemax;
    private ApiValueAndUnit<Double> globRad;
    private String uv;
    private ApiValueAndUnit<Double> pres;
    private String lastUpdate;
    private ApiValueAndUnit<Double> tempMor;
    private ApiValueAndUnit<Double> tempAft;
    private String symbMor;
    private String symbAft;
    private String daynightMor;
    private String daynightAft;
    private String txtMor;
    private String txtAft;
    private String endDatetime;
    private Integer cumulation;
}
