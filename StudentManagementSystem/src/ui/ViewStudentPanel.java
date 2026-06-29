package ui;

import model.Student;
import service.StudentManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Shows all students in a JTable: Serial No, ID, Name, Department,
 * Semester and CGPA. Reloads from the manager every time it is shown.
 */
public class ViewStudentPanel extends JPanel implements Refreshable {

    private final StudentManager manager;
    private final DefaultTableModel tableModel;
    private final JTable table;

    private static final String[] COLUMNS =
            {"S.No", "Student ID", "Name", "Department", "Semester", "CGPA"};

    public ViewStudentPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("All Students");
        title.setFont(UIHelper.TITLE_FONT);
        add(title, BorderLayout.NORTH);

        // The table model is read-only: cells cannot be edited directly here.
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = UIHelper.button("Refresh");
        refresh.addActionListener(e -> refreshData());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(refresh);
        add(south, BorderLayout.SOUTH);
    }

    /** Reusable helper: fill a table model from a list of students. */
    static void populate(DefaultTableModel model, List<Student> students) {
        model.setRowCount(0); // clear existing rows
        int serial = 1;
        for (Student s : students) {
            model.addRow(new Object[]{
                    serial++,
                    s.getStudentId(),
                    s.getFullName(),
                    s.getDepartment(),
                    s.getSemester(),
                    s.getCgpa()
            });
        }
    }

    @Override
    public void refreshData() {
        populate(tableModel, manager.getAllStudents());
    }
}
