package ui;

import model.Student;
import service.StudentManager;

import javax.swing.*;
import java.awt.*;

/**
 * Delete a student by ID. Looks up the student first so the user can confirm
 * they are deleting the right person, then asks for explicit confirmation.
 */
public class DeleteStudentPanel extends JPanel implements Refreshable {

    private final StudentManager manager;
    private final JTextField idField = new JTextField(12);
    private final JTextArea preview = new JTextArea(6, 30);

    public DeleteStudentPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Delete Student");
        title.setFont(UIHelper.TITLE_FONT);
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(UIHelper.formLabel("Student ID:"));
        row.add(idField);
        JButton findButton = UIHelper.button("Find");
        findButton.addActionListener(e -> onFind());
        row.add(findButton);
        center.add(row, BorderLayout.NORTH);

        preview.setEditable(false);
        preview.setFont(new Font("Monospaced", Font.PLAIN, 13));
        center.add(new JScrollPane(preview), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JButton deleteButton = UIHelper.button("Delete");
        deleteButton.addActionListener(e -> onDelete());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(deleteButton);
        add(south, BorderLayout.SOUTH);
    }

    private void onFind() {
        Student s = manager.findById(idField.getText().trim());
        if (s == null) {
            preview.setText("No student found with that ID.");
        } else {
            preview.setText("ID    : " + s.getStudentId() + "\n"
                    + "Name  : " + s.getFullName() + "\n"
                    + "Dept  : " + s.getDepartment() + "\n"
                    + "CGPA  : " + s.getCgpa());
        }
    }

    private void onDelete() {
        String id = idField.getText().trim();
        Student s = manager.findById(id);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "No student found with ID: " + id,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Delete " + s.getFullName() + " (" + s.getStudentId() + ")?\n"
                        + "This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            manager.deleteStudent(id);
            JOptionPane.showMessageDialog(this, "Student deleted.",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);
            refreshData();
        }
    }

    @Override
    public void refreshData() {
        idField.setText("");
        preview.setText("");
    }
}
