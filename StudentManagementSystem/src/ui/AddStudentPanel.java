package ui;

import model.Student;
import service.DuplicateStudentException;
import service.StudentManager;
import util.ValidationUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Form to add a new student. Collects input, validates it, then asks the
 * StudentManager to store it. All validation rules come from ValidationUtil
 * so this UI class stays focused on layout and user feedback.
 */
public class AddStudentPanel extends JPanel implements Refreshable {

    private final StudentManager manager;

    private final JTextField idField        = new JTextField(18);
    private final JTextField firstNameField = new JTextField(18);
    private final JTextField lastNameField  = new JTextField(18);
    private final JTextField ageField        = new JTextField(18);
    private final JComboBox<String>  genderBox     = new JComboBox<>(UIHelper.GENDERS);
    private final JComboBox<String>  departmentBox = new JComboBox<>(UIHelper.DEPARTMENTS);
    private final JComboBox<Integer> semesterBox   = new JComboBox<>(UIHelper.SEMESTERS);
    private final JTextField cgpaField  = new JTextField(18);
    private final JTextField emailField = new JTextField(18);
    private final JTextField phoneField = new JTextField(18);

    public AddStudentPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Add New Student");
        title.setFont(UIHelper.TITLE_FONT);
        add(title, BorderLayout.NORTH);

        add(buildForm(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addRow(form, gc, row++, "Student ID:",  idField);
        addRow(form, gc, row++, "First Name:",  firstNameField);
        addRow(form, gc, row++, "Last Name:",   lastNameField);
        addRow(form, gc, row++, "Age:",         ageField);
        addRow(form, gc, row++, "Gender:",      genderBox);
        addRow(form, gc, row++, "Department:",  departmentBox);
        addRow(form, gc, row++, "Semester:",    semesterBox);
        addRow(form, gc, row++, "CGPA:",        cgpaField);
        addRow(form, gc, row++, "Email:",       emailField);
        addRow(form, gc, row++, "Phone:",       phoneField);
        return form;
    }

    private void addRow(JPanel form, GridBagConstraints gc, int row,
                        String label, JComponent field) {
        gc.gridx = 0; gc.gridy = row;
        form.add(UIHelper.formLabel(label), gc);
        gc.gridx = 1; gc.gridy = row;
        form.add(field, gc);
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton  = UIHelper.button("Save Student");
        JButton clearButton = UIHelper.button("Clear");
        saveButton.addActionListener(e -> onSave());
        clearButton.addActionListener(e -> clearForm());
        panel.add(clearButton);
        panel.add(saveButton);
        return panel;
    }

    /** Validate every field, then add the student. Shows clear error messages. */
    private void onSave() {
        String id        = idField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName  = lastNameField.getText().trim();
        String email     = emailField.getText().trim();
        String phone     = phoneField.getText().trim();

        if (!ValidationUtil.isNotEmpty(id) || !ValidationUtil.isNotEmpty(firstName)
                || !ValidationUtil.isNotEmpty(lastName)) {
            showError("ID, First Name and Last Name cannot be empty.");
            return;
        }

        int age;
        double cgpa;
        try {
            age = Integer.parseInt(ageField.getText().trim());
            cgpa = Double.parseDouble(cgpaField.getText().trim());
        } catch (NumberFormatException ex) {
            showError("Age must be a whole number and CGPA must be a number.");
            return;
        }

        if (!ValidationUtil.isValidAge(age))      { showError("Age must be greater than 16."); return; }
        if (!ValidationUtil.isValidCgpa(cgpa))    { showError("CGPA must be between 0 and 10."); return; }
        if (!ValidationUtil.isValidEmail(email))  { showError("Please enter a valid email address."); return; }
        if (!ValidationUtil.isValidPhone(phone))  { showError("Phone number must be exactly 10 digits."); return; }

        Student student = new Student(
                id, firstName, lastName, age,
                (String) genderBox.getSelectedItem(),
                (String) departmentBox.getSelectedItem(),
                (Integer) semesterBox.getSelectedItem(),
                cgpa, email, phone);

        try {
            manager.addStudent(student);
            JOptionPane.showMessageDialog(this,
                    "Student added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (DuplicateStudentException ex) {
            showError(ex.getMessage());
        }
    }

    private void clearForm() {
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        ageField.setText("");
        cgpaField.setText("");
        emailField.setText("");
        phoneField.setText("");
        genderBox.setSelectedIndex(0);
        departmentBox.setSelectedIndex(0);
        semesterBox.setSelectedIndex(0);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void refreshData() {
        // Nothing to reload; a fresh blank form is fine each time.
    }
}
