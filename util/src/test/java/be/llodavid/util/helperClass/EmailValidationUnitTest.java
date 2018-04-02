package be.llodavid.util.helperClass;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class EmailValidationUnitTest {

    @Test
    public void validateEmail_givenValidEmailFormat_returnTrue() {
        assertThat(EmailValidation.isValidEmail("david.vdb@hotmail.com")).isTrue();
        assertThat(EmailValidation.isValidEmail("david.vdb@h0tm4il.com")).isTrue();
    }
    @Test
    public void validateEmail_givenInValidEmailFormat_returnsFalse() {
        assertThat(EmailValidation.isValidEmail("david.vdbAThotmail.com")).isFalse();
        assertThat(EmailValidation.isValidEmail("david.vdb@hotmailDOTcom")).isFalse();
        assertThat(EmailValidation.isValidEmail("david.vdb@hotmail@com")).isFalse();
    }
}