package be.llodavid.domain.helperClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BelgianDateFormatter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            .withLocale(new Locale.Builder().setLanguage("nl").setRegion("BE").build());

    public static String dateToString(LocalDate date) {
        if (date==null) {
            return "";
        }
        return date.format(formatter);
    }
    public static LocalDate stringToDate(String date) {
        if (date==null || date.isEmpty()) {
            return LocalDate.now();
        }
        return LocalDate.parse(date, formatter);
    }
}
