package de.ukowohl.audibene.utils.web;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.Locale;

public class OffsetDateTimeFormatter implements Formatter<OffsetDateTime> {
    @Override
    public OffsetDateTime parse(String text, Locale locale) throws ParseException {
        return OffsetDateTime.parse(text);
    }

    @Override
    public String print(OffsetDateTime dateTime, Locale locale) {
        return dateTime.toString();
    }
}
