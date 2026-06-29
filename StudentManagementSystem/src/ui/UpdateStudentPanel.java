package ui;

import model.Student;
import service.StudentManager;
import util.ValidationUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Update an existing student: enter an ID, load the record into an editable
 * form, change any field (except the ID), then save. Changes are persisted
 * automatically by the manager.
 */
public class UpdateStudentPanel extends JPanel implements Refreshable {

    private final StudentManager manager;

    private final JTextField searchIdField = new JTextField(12);

    private final JTextField firstNameField = new JTextField(18);
    private final JTextField lastNameField  = new JTextField(18);
    private final JTextField ageField        = new JTextField(18);
    private final JComboBox<String>  genderBox     = new JComboBox<>(UIHelper.GENDERS);
    private final JComboBox<String>  departmentBox = new JComboBox<>(UIHelper.DEPARTMENTS);
    private final JComboBox<Integer> semesterBox   = new JComboBox<>(UIHelper.SEMESTERS);
    private final JTextField cgpaField  = new JTextField(18);
    private final JTextField emailField = new JTextField(18);
    private final JTextField phoneField = new JTextField(18);

    private final JButton saveButton = UIHelper.button("Save Changes");
    private String loadedId = null;

    public UpdateStudentPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Update Student");
        title.setFont(UIHelper.TITLE_FONT);
        add(title, BorderLayout.NORTH);

        add(buildBody(), BorderLayout.CENTER);

        saveButton.setEnabled(false);
        saveButton.addActionListener(e -> onSave());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(saveButton);
        add(south, BorderLayout.SOUTH);
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(10, 10));

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(UIHelper.formLabel("Student ID:"));
        searchRow.add(searchIdField);
        JButton loadButton = UIHelper.button("Load");
        loadButton.addActionListener(e -> onLoad());
        searchRow.add(loadButton);
        body.add(searchRow, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;
        int row = 0;
        addRow(form, gc, row++, "First Name:", firstNameField);
        addRow(form, gc, row++, "Last Name:",  lastNameField);
        addRow(form, gc, row++, "Age:",        ageField);
        addRow(form, gc, row++, "Gender:",     genderBox);
        addRow(form, gc, row++, "Department:", departmentBox);
        addRow(form, gc, row++, "Semester:",   semesterBox);
        addRow(form, gc, row++, "CGPA:",       cgpaField);
        addRow(form, gc, row++, "Email:",      emailField);
        addRow(form, gc, row++, "Phone:",      phoneField);
        body.add(form, BorderLayout.CENTER);

        setFormEnabled(false);
        return body;
    }

    private void addRow(JPanel form, GridBagConstraints gc, int row,
                        String label, JComponent field) {
        gc.gridx = 0; gc.gridy = row;
        form.add(UIHelper.formLabel(label), gc);
        gc.gridx = 1; gc.gridy = row;
        form.add(field, gc);
    }

    private void onLoad() {
        String id = searchIdField.getText().trim();
        Student s = manager.findById(id);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "No student found with ID: " + id,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            setFormEnabled(false);
            saveButton.setEnabled(false);
            return;
        }
        loadedId = s.getStudentId();
        firstNameField.setText(s.getFirstName());
        lastNameField.setText(s.getLastName());
        ageField.setText(String.valueOf(s.getAge()));
        genderBox.setSelectedItem(s.getGender());
        departmentBox.setSelectedItem(s.getDepartment());
        semesterBox.setSelectedItem(s.getSemester());
        cgpaField.setText(String.valueOf(s.getCgpa()));
        emailField.setText(s.getEmail());
        phoneField.setText(s.getPhoneNumber());
        setFormEnabled(true);
        saveButton.setEnabled(true);
    }

    private void onSave() {
        if (loadedId == null) return;

        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        int age;
        double cgpa;
        try {
            age = Integer.parseInt(ageField.getText().trim());
            cgpa = Double.parseDouble(cgpaField.getText().trim());
        } catch (NumberFormatException ex) {
            showError("Age must be a whole number and CGPA must be a number.");
            return;
        }
        if (!ValidationUtil.isNotEmpty(firstNameField.getText())
                || !ValidationUtil.isNotEmpty(lastNameField.getText())) {
            showError("First and Last name cannot be empty.");
            return;
        }
        if (!ValidationUtil.isValidAge(age))     { showError("Age must be greater than 16."); return; }
        if (!ValidationUtil.isValidCgpa(cgpa))   { showError("CGPA must be between 0 and 10."); return; }
        if (!ValidationUtil.isValidEmail(email)) { showError("Please enter a valid email address."); return; }
        if (!ValidationUtil.isValidPhone(phone)) { showError("Phone number must be exactly 10 digits."); return; }

        Student updated = new Student(
                loadedId,
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                age,
                (String) genderBox.getSelectedItem(),
                (String) departmentBox.getSelectedItem(),
                (Integer) semesterBox.getSelectedItem(),
                cgpa, email, phone);

        if (manager.updateStudent(updated)) {
            JOptionPane.showMessageDialog(this, "Student updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showError("Update failed: student no longer exists.");
        }
    }

    private void setFormEnabled(boolean enabled) {
        firstNameField.setEnabled(enabled);
        lastNameField.setEnabled(enabled);
        ageField.setEnabled(enabled);
        genderBox.setEnabled(enabled);
        departmentBox.setEnabled(enabled);
        semesterBox.setEnabled(enabled);
        cgpaField.setEnabled(enabled);
        emailField.setEnabled(enabled);
        phoneField.setEnabled(enabled);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void refreshData() {
        searchIdField.setText("");
        loadedId = null;
        setFormEnabled(false);
        saveButton.setEnabled(false);
        firstNameField.setText(""); lastNameField.setText("");
        ageField.setText(""); cgpaField.setText("");
        emailField.setText(""); phoneField.setText("");
    }
}
