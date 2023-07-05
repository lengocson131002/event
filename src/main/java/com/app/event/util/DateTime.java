package com.app.event.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateTime {

    public static OffsetDateTime toOffsetDateTime(String datetimeString, String format) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
            LocalDateTime localDateTime = LocalDateTime.parse(datetimeString + " 00:00:00 +00:00", dtf);
            return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
