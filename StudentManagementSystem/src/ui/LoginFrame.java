package ui;

import util.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * A simple admin login screen. Credentials are hardcoded for now
 * (username: admin, password: admin123). In a real system these would be
 * stored securely and hashed - see the Future Scope section of the README.
 */
public class LoginFrame extends JFrame {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    private final JTextField userField = new JTextField(16);
    private final JPasswordField passField = new JPasswordField(16);

    public LoginFrame() {
        setTitle("Admin Login - Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("Admin Login");
        title.setFont(UIHelper.TITLE_FONT);
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        panel.add(title, gc);
        gc.gridwidth = 1;

        gc.gridx = 0; gc.gridy = 1; panel.add(UIHelper.formLabel("Username:"), gc);
        gc.gridx = 1; gc.gridy = 1; panel.add(userField, gc);
        gc.gridx = 0; gc.gridy = 2; panel.add(UIHelper.formLabel("Password:"), gc);
        gc.gridx = 1; gc.gridy = 2; panel.add(passField, gc);

        JButton loginButton = UIHelper.button("Login");
        loginButton.addActionListener(e -> attemptLogin());
        gc.gridx = 1; gc.gridy = 3; gc.anchor = GridBagConstraints.EAST;
        panel.add(loginButton, gc);

        // Pressing Enter in the password field also logs in.
        getRootPane().setDefaultButton(loginButton);

        add(panel);
        Theme.light().apply(getContentPane());
    }

    private void attemptLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());

        if (ADMIN_USER.equals(user) && ADMIN_PASS.equals(pass)) {
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passField.setText("");
        }
    }
}
