import ui.LoginFrame;

import javax.swing.SwingUtilities;

/**
 * Application entry point. Swing UIs must be built and shown on the
 * Event Dispatch Thread (EDT), which is what SwingUtilities.invokeLater
 * guarantees. We start with the admin login screen.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
