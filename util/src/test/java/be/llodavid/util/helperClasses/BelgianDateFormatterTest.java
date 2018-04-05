package be.llodavid.util.helperClasses;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class BelgianDateFormatterTest {

    @Test
    public void dateToString_happyPath() {
        assertThat(BelgianDateFormatter.dateToString(LocalDate.of(2018,03,26))).isEqualTo("26/03/2018");
    }

    @Test
    public void stringToDate_happyPath() {
        assertThat(BelgianDateFormatter.stringToDate("26/03/2018")).isEqualTo(LocalDate.of(2018,03,26));
    }

    @Test
    public void stringToDate_givenEmptyString_returnsCurrentDate() {
        assertThat(BelgianDateFormatter.stringToDate("")).isEqualTo(LocalDate.now());
    }
}