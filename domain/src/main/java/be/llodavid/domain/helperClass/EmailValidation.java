package be.llodavid.domain.helperClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidation {

    private static final Pattern VALID_EMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static boolean isValidEmail(String emailAddress) {
        return isValidEmailAddress(emailAddress);
    }

    private static boolean isValidEmailAddress(String email) {
        Matcher matcher = VALID_EMAIL.matcher(email);
        return matcher.find();
    }
}
