package com.example.bioweatherbackend.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public static String convertXmlDateTimeFormatToIso(String xmlDateTime) {
        var date = inputFormatter.parse(xmlDateTime);
        var localDateTime = LocalDateTime.from(date);
        var zonedDateTime = localDateTime.atZone(ZoneOffset.ofHours(0));
        return outputFormatter.format(zonedDateTime);
    }
}