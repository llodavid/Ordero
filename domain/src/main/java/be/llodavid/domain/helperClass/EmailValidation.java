package be.llodavid.domain.HelperClass;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {

    private String eMail;

    public Email(String eMail) {
        this.eMail = eMail;
    }

    public static final Pattern VALID_EMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    private void validateEmailAddress(String emailAddress) {
        if (isValidEmailAddress(emailAddress)) {
            this.eMail = emailAddress;
        } else {
            throw new IllegalArgumentException("Please provide a valid e-mail address.\nCorrect Format: \"xx@xx.xx\"");
        }
    }

    private static boolean isValidEmailAddress(String email) {
        Matcher matcher = VALID_EMAIL.matcher(email);
        return matcher.find();
    }

    public String geteMail() {
        return eMail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(eMail, email.eMail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eMail);
    }

    @Override
    public String toString() {
        return eMail;
    }
}
