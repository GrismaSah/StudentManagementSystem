package service;

/**
 * A custom checked exception thrown when someone tries to add a student
 * whose ID already exists. A dedicated exception type makes the error
 * meaningful and easy for the UI to catch and report.
 */
public class DuplicateStudentException extends Exception {
    public DuplicateStudentException(String message) {
        super(message);
    }
}
