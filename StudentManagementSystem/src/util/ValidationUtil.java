package util;

import java.util.regex.Pattern;

/**
 * Holds all input-validation rules in ONE place.
 *
 * Keeping validation here (instead of inside the UI) means:
 *  - the rules can be reused by Add, Update, or any future screen,
 *  - the UI code stays clean,
 *  - we follow the Single Responsibility Principle.
 *
 * Every method is static because validation needs no object state -
 * it just takes input and returns true/false.
 */
public final class ValidationUtil {

    // Compile the email pattern once (efficient) and reuse it.
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Private constructor: this is a utility class, never instantiated.
    private ValidationUtil() {
    }

    /** A field must not be null or blank. */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /** Basic email format check, e.g. name@domain.com */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /** Phone must be exactly 10 digits. */
    public static boolean isValidPhone(String phone) {
        return isNotEmpty(phone) && phone.trim().matches("\\d{10}");
    }

    /** A student must be older than 16. */
    public static boolean isValidAge(int age) {
        return age > 16;
    }

    /** CGPA must be between 0 and 10 (inclusive). */
    public static boolean isValidCgpa(double cgpa) {
        return cgpa >= 0.0 && cgpa <= 10.0;
    }

    /** Semester must be a sensible 1..8. */
    public static boolean isValidSemester(int semester) {
        return semester >= 1 && semester <= 8;
    }
}
