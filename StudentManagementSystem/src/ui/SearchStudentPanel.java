package ui;

import model.Student;
import service.StudentManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Search students by ID or name with LIVE filtering: results update on
 * every keystroke using a DocumentListener. Selecting a row shows that
 * student's complete details.
 */
public class SearchStudentPanel extends JPanel implements Refreshable {

    private final StudentManager manager;
    private final JTextField searchField = new JTextField(25);
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextArea detailArea = new JTextArea(8, 30);

    private static final String[] COLUMNS =
            {"S.No", "Student ID", "Name", "Department", "Semester", "CGPA"};

    public SearchStudentPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(buildTop(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.getSelectionModel().addListSelectionListener(e -> showSelectedDetails());

        detailArea.setEditable(false);
        detailArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(table), new JScrollPane(detailArea));
        split.setResizeWeight(0.65);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout(8, 8));
        JLabel title = new JLabel("Search Student");
        title.setFont(UIHelper.TITLE_FONT);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(UIHelper.formLabel("Type ID or name:"));
        searchRow.add(searchField);

        // LIVE SEARCH: react to every text change.
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { applyFilter(); }
            public void removeUpdate(DocumentEvent e)  { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });

        top.add(title, BorderLayout.NORTH);
        top.add(searchRow, BorderLayout.SOUTH);
        return top;
    }

    private void applyFilter() {
        ViewStudentPanel.populate(tableModel, manager.search(searchField.getText()));
        detailArea.setText("");
    }

    private void showSelectedDetails() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String id = String.valueOf(tableModel.getValueAt(row, 1));
        Student s = manager.findById(id);
        if (s != null) {
            detailArea.setText(formatDetails(s));
        }
    }

    private String formatDetails(Student s) {
        return "Student ID : " + s.getStudentId() + "\n"
                + "Name       : " + s.getFullName() + "\n"
                + "Age        : " + s.getAge() + "\n"
                + "Gender     : " + s.getGender() + "\n"
                + "Department : " + s.getDepartment() + "\n"
                + "Semester   : " + s.getSemester() + "\n"
                + "CGPA       : " + s.getCgpa() + "\n"
                + "Email      : " + s.getEmail() + "\n"
                + "Phone      : " + s.getPhoneNumber() + "\n"
                + "Honours    : " + (s.isHonours() ? "Yes" : "No");
    }

    @Override
    public void refreshData() {
        searchField.setText("");
        ViewStudentPanel.populate(tableModel, manager.getAllStudents());
        detailArea.setText("");
    }
}
