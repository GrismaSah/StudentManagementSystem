package model;

/**
 * Represents a single Student in the Student Management System.
 *
 * This is a MODEL class (a POJO - Plain Old Java Object).
 * Its only job is to hold the data for ONE student.
 * It contains no file handling, no validation, and no GUI code.
 */
public class Student {

    // ---- Fields: private = encapsulation ----
    private String studentId;
    private String firstName;
    private String lastName;
    private int    age;
    private String gender;
    private String department;
    private int    semester;
    private double cgpa;
    private String email;
    private String phoneNumber;

    /** No-argument constructor: empty Student, filled later via setters. */
    public Student() {
    }

    /** Parameterized constructor: fully populated Student in one statement. */
    public Student(String studentId, String firstName, String lastName, int age,
                   String gender, String department, int semester, double cgpa,
                   String email, String phoneNumber) {
        this.studentId   = studentId;
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.age         = age;
        this.gender      = gender;
        this.department  = department;
        this.semester    = semester;
        this.cgpa        = cgpa;
        this.email       = email;
        this.phoneNumber = phoneNumber;
    }

    // ---- Getters and Setters ----
    public String getStudentId()           { return studentId; }
    public void   setStudentId(String v)   { this.studentId = v; }

    public String getFirstName()           { return firstName; }
    public void   setFirstName(String v)   { this.firstName = v; }

    public String getLastName()            { return lastName; }
    public void   setLastName(String v)    { this.lastName = v; }

    public int    getAge()                 { return age; }
    public void   setAge(int v)            { this.age = v; }

    public String getGender()              { return gender; }
    public void   setGender(String v)      { this.gender = v; }

    public String getDepartment()          { return department; }
    public void   setDepartment(String v)  { this.department = v; }

    public int    getSemester()            { return semester; }
    public void   setSemester(int v)       { this.semester = v; }

    public double getCgpa()                { return cgpa; }
    public void   setCgpa(double v)        { this.cgpa = v; }

    public String getEmail()               { return email; }
    public void   setEmail(String v)       { this.email = v; }

    public String getPhoneNumber()         { return phoneNumber; }
    public void   setPhoneNumber(String v) { this.phoneNumber = v; }

    /** Reusable helper: full name. */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /** Bonus from Class 1 practice task. */
    public boolean isHonours() {
        return cgpa >= 9.0;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID='"        + studentId    + '\'' +
                ", name='"    + getFullName() + '\'' +
                ", age="      + age +
                ", gender='"  + gender       + '\'' +
                ", dept='"    + department   + '\'' +
                ", semester=" + semester +
                ", cgpa="     + cgpa +
                ", email='"   + email        + '\'' +
                ", phone='"   + phoneNumber  + '\'' +
                '}';
    }
}
