package util;

import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles ALL disk operations for students.
 *
 * Responsibilities:
 *  - load students from data/students.txt when the app starts,
 *  - save the full list back after every change,
 *  - export the list to a user-chosen CSV file.
 *
 * It knows the file FORMAT (comma-separated, 10 fields per line) and is the
 * only class that touches the disk. No other class needs to know how data
 * is stored - this is the benefit of putting it behind one class.
 */
public final class FileManager {

    // Relative path: works when the app is run from the project root.
    private static final String FILE_PATH = "data/students.txt";
    private static final String DELIMITER = ",";

    private FileManager() {
    }

    /**
     * Reads every line and converts it into a Student object.
     * If the file is missing, we create an empty one and return an empty list
     * (a fresh install simply has no students yet).
     */
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(FILE_PATH);

        // First run: no file yet. Create the folder + empty file, return empty list.
        if (!file.exists()) {
            createEmptyFile(file);
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // skip blank lines safely
                }
                Student s = lineToStudent(line);
                if (s != null) {
                    students.add(s);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading students file: " + e.getMessage());
        }
        return students;
    }

    /** Writes the entire list back to disk, one student per line. */
    public static void saveStudents(List<Student> students) {
        File file = new File(FILE_PATH);
        ensureParentFolder(file);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Student s : students) {
                writer.write(studentToLine(s));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving students file: " + e.getMessage());
        }
    }

    /** Exports students to a chosen CSV file, with a readable header row. */
    public static void exportToCsv(List<Student> students, File target) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(target))) {
            writer.write("StudentID,FirstName,LastName,Age,Gender,Department,Semester,CGPA,Email,Phone");
            writer.newLine();
            for (Student s : students) {
                writer.write(studentToLine(s));
                writer.newLine();
            }
        }
    }

    // ---- Conversion helpers (the heart of file handling) ----

    /** Turn one Student object into a single comma-separated line. */
    private static String studentToLine(Student s) {
        return String.join(DELIMITER,
                s.getStudentId(),
                s.getFirstName(),
                s.getLastName(),
                String.valueOf(s.getAge()),
                s.getGender(),
                s.getDepartment(),
                String.valueOf(s.getSemester()),
                String.valueOf(s.getCgpa()),
                s.getEmail(),
                s.getPhoneNumber());
    }

    /** Turn one line back into a Student object. Returns null if the line is malformed. */
    private static Student lineToStudent(String line) {
        String[] p = line.split(DELIMITER);
        if (p.length != 10) {
            System.err.println("Skipping malformed line: " + line);
            return null;
        }
        try {
            return new Student(
                    p[0].trim(),
                    p[1].trim(),
                    p[2].trim(),
                    Integer.parseInt(p[3].trim()),
                    p[4].trim(),
                    p[5].trim(),
                    Integer.parseInt(p[6].trim()),
                    Double.parseDouble(p[7].trim()),
                    p[8].trim(),
                    p[9].trim());
        } catch (NumberFormatException e) {
            System.err.println("Skipping line with bad number format: " + line);
            return null;
        }
    }

    private static void createEmptyFile(File file) {
        ensureParentFolder(file);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Could not create data file: " + e.getMessage());
        }
    }

    private static void ensureParentFolder(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}
