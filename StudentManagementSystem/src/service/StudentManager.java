package service;

import model.Student;
import util.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The "brain" of the application (SERVICE layer).
 *
 * It holds the in-memory list of students and exposes all business
 * operations: add, find, search, update, delete, sort, statistics.
 * It uses FileManager for persistence, so the UI never touches the disk.
 *
 * Data flow:  UI  ->  StudentManager  ->  FileManager  ->  students.txt
 */
public class StudentManager {

    private final List<Student> students;

    /** On creation, load whatever is already saved on disk. */
    public StudentManager() {
        this.students = FileManager.loadStudents();
    }

    /** Returns a COPY so callers cannot modify our internal list directly. */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public int getCount() {
        return students.size();
    }

    /** True if a student with this ID already exists (case-insensitive). */
    public boolean isDuplicateId(String id) {
        return findById(id) != null;
    }

    /** Find one student by exact ID, or null if not found. */
    public Student findById(String id) {
        if (id == null) return null;
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    /** Search by ID OR name fragment (case-insensitive). Powers live search. */
    public List<Student> search(String keyword) {
        List<Student> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        String k = keyword.trim().toLowerCase();
        for (Student s : students) {
            if (s.getStudentId().toLowerCase().contains(k)
                    || s.getFullName().toLowerCase().contains(k)) {
                result.add(s);
            }
        }
        return result;
    }

    /** Add a student, auto-saving. Rejects duplicate IDs. */
    public void addStudent(Student s) throws DuplicateStudentException {
        if (isDuplicateId(s.getStudentId())) {
            throw new DuplicateStudentException(
                    "A student with ID '" + s.getStudentId() + "' already exists.");
        }
        students.add(s);
        save();
    }

    /** Replace an existing student (matched by ID) with updated details. */
    public boolean updateStudent(Student updated) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equalsIgnoreCase(updated.getStudentId())) {
                students.set(i, updated);
                save();
                return true;
            }
        }
        return false;
    }

    /** Delete by ID, auto-saving. Returns true if a student was removed. */
    public boolean deleteStudent(String id) {
        Student target = findById(id);
        if (target != null) {
            students.remove(target);
            save();
            return true;
        }
        return false;
    }

    /** Sort the in-memory list using any Comparator and persist the new order. */
    public void sort(Comparator<Student> comparator) {
        students.sort(comparator);
        save();
    }

    // ---- Ready-made comparators (used by the Sort feature) ----
    public static Comparator<Student> byName()       { return Comparator.comparing(Student::getFullName, String.CASE_INSENSITIVE_ORDER); }
    public static Comparator<Student> byCgpa()        { return Comparator.comparingDouble(Student::getCgpa).reversed(); }
    public static Comparator<Student> byDepartment()  { return Comparator.comparing(Student::getDepartment, String.CASE_INSENSITIVE_ORDER); }
    public static Comparator<Student> bySemester()    { return Comparator.comparingInt(Student::getSemester); }
    public static Comparator<Student> byId()          { return Comparator.comparing(Student::getStudentId, String.CASE_INSENSITIVE_ORDER); }

    // ---- Statistics (power the dashboard) ----

    public double getAverageCgpa() {
        if (students.isEmpty()) return 0.0;
        double sum = 0;
        for (Student s : students) {
            sum += s.getCgpa();
        }
        return sum / students.size();
    }

    public double getHighestCgpa() {
        double max = 0.0;
        for (Student s : students) {
            if (s.getCgpa() > max) {
                max = s.getCgpa();
            }
        }
        return max;
    }

    public Student getTopper() {
        Student top = null;
        for (Student s : students) {
            if (top == null || s.getCgpa() > top.getCgpa()) {
                top = s;
            }
        }
        return top;
    }

    /** Map of department -> number of students (insertion order preserved). */
    public Map<String, Integer> getStudentsByDepartment() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Student s : students) {
            counts.merge(s.getDepartment(), 1, Integer::sum);
        }
        return counts;
    }

    /** Save current state to disk. */
    public void save() {
        FileManager.saveStudents(students);
    }

    /** Export current list to a CSV file chosen by the user. */
    public void exportCsv(File target) throws IOException {
        FileManager.exportToCsv(students, target);
    }
}
