package be.llodavid.domain.helperClass;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmailValidationUnitTest {

    @Test
    public void validateEmail_givenValidEmailFormat_returnTrue() {
        Assertions.assertThat(EmailValidation.isValidEmail("david.vdb@hotmail.com")).isTrue();
        Assertions.assertThat(EmailValidation.isValidEmail("david.vdb@h0tm4il.com")).isTrue();
    }
    @Test
    public void validateEmail_givenInValidEmailFormat_returnsFalse() {
        Assertions.assertThat(EmailValidation.isValidEmail("david.vdbAThotmail.com")).isFalse();
        Assertions.assertThat(EmailValidation.isValidEmail("david.vdb@hotmailDOTcom")).isFalse();
        Assertions.assertThat(EmailValidation.isValidEmail("david.vdb@hotmail@com")).isFalse();
    }
}