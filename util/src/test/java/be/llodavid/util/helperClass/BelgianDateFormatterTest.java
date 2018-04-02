package be.llodavid.util.helperClass;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class BelgianDateFormatterTest {

    @Test
    public void dateToString() {
        assertThat(BelgianDateFormatter.dateToString(LocalDate.of(2018,03,26))).isEqualTo("26/03/2018");
    }

    @Test
    public void stringToDate() {
        assertThat(BelgianDateFormatter.stringToDate("26/03/2018")).isEqualTo(LocalDate.of(2018,03,26));
    }
}