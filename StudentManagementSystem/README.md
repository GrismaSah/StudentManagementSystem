# Student Management System

A professional Java **desktop application** that lets an administrator manage student
records through a clean, modern **Swing** interface. It performs full **CRUD**
(Create, Read, Update, Delete) operations with **searching**, **sorting**, **input
validation**, and **file-based persistence** — built entirely with **Core Java**, no
frameworks, no database, no build tools.

---

## Features

**Core**
- Admin login screen (hardcoded credentials for now)
- Add, View, Search, Update, Delete students (full CRUD)
- Input validation (unique ID, email format, 10-digit phone, age > 16, CGPA 0–10)
- Sort by Name, CGPA, Department, Semester, or Student ID (using `Comparator`)
- Automatic save to `students.txt` after every change; auto-load on startup

**Extra (to stand out)**
- Dashboard with live statistics: total students, average CGPA, highest CGPA, topper
- Bar chart of students per department, drawn with pure Java2D (no chart library)
- Live search that filters results as you type
- Export the student list to a CSV file
- Light / Dark mode toggle

---

## Folder Structure

```
StudentManagementSystem/
├── src/
│   ├── model/
│   │   └── Student.java                # data model (POJO)
│   ├── service/
│   │   ├── StudentManager.java         # business logic + statistics
│   │   └── DuplicateStudentException.java
│   ├── util/
│   │   ├── FileManager.java            # read/write/export (file handling)
│   │   ├── ValidationUtil.java         # all input rules in one place
│   │   └── Theme.java                  # light/dark colour palettes
│   ├── ui/
│   │   ├── LoginFrame.java             # admin login
│   │   ├── MainFrame.java              # main window + navigation
│   │   ├── DashboardPanel.java         # stats + chart
│   │   ├── DepartmentChartPanel.java   # custom-painted bar chart
│   │   ├── AddStudentPanel.java
│   │   ├── ViewStudentPanel.java
│   │   ├── SearchStudentPanel.java     # live search
│   │   ├── UpdateStudentPanel.java
│   │   ├── DeleteStudentPanel.java
│   │   ├── UIHelper.java               # shared fonts/constants
│   │   └── Refreshable.java            # interface for refreshable panels
│   └── Main.java                       # entry point
├── data/
│   └── students.txt                    # persistent storage (20 sample records)
└── README.md
```

---

## Architecture

The project is split into layers so each class has one job (Single Responsibility):

```
UI (Swing panels)  ->  StudentManager (logic)  ->  FileManager  ->  students.txt
                                |
                              Student (model)
```

A higher layer may call a lower one, never the reverse. The `Student` model knows
nothing about files or the GUI, which is what would let you swap `students.txt` for
a real database later without rewriting the UI.

---

## How to Run

**Using IntelliJ IDEA (recommended)**
1. Open the `StudentManagementSystem` folder as a project.
2. Mark `src` as the *Sources Root* (right-click `src` → Mark Directory as → Sources Root).
3. Run `Main.java`.
4. Log in with **username:** `admin`  **password:** `admin123`.

**Using the command line (JDK required)**
```bash
cd StudentManagementSystem
javac -d out $(find src -name "*.java")
java -cp out Main
```
> Run the app from the project root so the relative path `data/students.txt` resolves.

---

## Technologies Used

- Java 17+ (developed and tested on Java 21)
- Java Swing (GUI)
- Java Collections Framework (`List`, `Map`, `Comparator`)
- File Handling (`BufferedReader`, `BufferedWriter`)
- Exception Handling (including a custom `DuplicateStudentException`)
- Object-Oriented Programming (encapsulation, abstraction, modular design)

---

## Screenshots

> Add screenshots here after running the app.

- `screenshots/login.png`
- `screenshots/dashboard.png`
- `screenshots/view-students.png`
- `screenshots/add-student.png`
- `screenshots/dark-mode.png`

---

## Future Improvements

- MySQL integration (replace `students.txt`)
- Spring Boot backend and REST APIs
- Proper user authentication with hashed passwords and multiple admin roles
- Cloud database
- Export to Excel and printable PDF reports
- Richer analytics and charts

---

## License

Released under the MIT License. You are free to use, modify, and distribute this
project with attribution.
