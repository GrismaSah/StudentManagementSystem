package ui;

import service.StudentManager;
import util.Theme;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The main application window. It owns the single StudentManager instance
 * and switches between feature panels using a CardLayout. A left navigation
 * bar drives the switching; the top bar offers theme toggle, CSV export and
 * logout. This class wires the whole UI together but delegates all logic to
 * the panels and the manager (separation of concerns).
 */
public class MainFrame extends JFrame {

    private final StudentManager manager;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final Map<String, JComponent> panels = new LinkedHashMap<>();

    private final DashboardPanel dashboardPanel;
    private Theme currentTheme = Theme.light();
    private boolean darkMode = false;

    public MainFrame() {
        this.manager = new StudentManager();

        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Build feature panels once and register them as cards.
        dashboardPanel = new DashboardPanel(manager);
        register("dashboard", dashboardPanel);
        register("add",    new AddStudentPanel(manager));
        register("view",   new ViewStudentPanel(manager));
        register("search", new SearchStudentPanel(manager));
        register("update", new UpdateStudentPanel(manager));
        register("delete", new DeleteStudentPanel(manager));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildNav(), BorderLayout.WEST);
        add(cards, BorderLayout.CENTER);

        applyTheme();           // start in light mode
        showCard("dashboard");  // open on the dashboard
    }

    private void register(String name, JComponent panel) {
        panels.put(name, panel);
        cards.add(panel, name);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("Student Management System");
        title.setFont(UIHelper.TITLE_FONT);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton themeButton  = UIHelper.button("Dark Mode");
        JButton exportButton = UIHelper.button("Export CSV");
        JButton logoutButton = UIHelper.button("Logout");

        themeButton.addActionListener(e -> {
            toggleTheme();
            themeButton.setText(darkMode ? "Light Mode" : "Dark Mode");
        });
        exportButton.addActionListener(e -> onExportCsv());
        logoutButton.addActionListener(e -> onLogout());

        actions.add(themeButton);
        actions.add(exportButton);
        actions.add(logoutButton);

        header.add(title, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);
        return header;
    }

    private JPanel buildNav() {
        JPanel nav = new JPanel();
        nav.setLayout(new GridLayout(8, 1, 0, 10));
        nav.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        nav.add(navButton("Dashboard", () -> showCard("dashboard")));
        nav.add(navButton("Add Student", () -> showCard("add")));
        nav.add(navButton("View Students", () -> showCard("view")));
        nav.add(navButton("Search Student", () -> showCard("search")));
        nav.add(navButton("Update Student", () -> showCard("update")));
        nav.add(navButton("Delete Student", () -> showCard("delete")));
        nav.add(navButton("Sort Students", this::onSort));
        nav.add(navButton("Exit", this::onExit));
        return nav;
    }

    private JButton navButton(String text, Runnable action) {
        JButton b = UIHelper.button(text);
        b.addActionListener(e -> action.run());
        return b;
    }

    /** Refresh the target panel (if it can) and bring it to the front. */
    private void showCard(String name) {
        JComponent panel = panels.get(name);
        if (panel instanceof Refreshable refreshable) {
            refreshable.refreshData();
        }
        cardLayout.show(cards, name);
    }

    private void onSort() {
        String[] options = {"Name", "CGPA", "Department", "Semester", "Student ID"};
        String choice = (String) JOptionPane.showInputDialog(this,
                "Sort students by:", "Sort",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == null) return;

        switch (choice) {
            case "Name"       -> manager.sort(StudentManager.byName());
            case "CGPA"       -> manager.sort(StudentManager.byCgpa());
            case "Department" -> manager.sort(StudentManager.byDepartment());
            case "Semester"   -> manager.sort(StudentManager.bySemester());
            case "Student ID" -> manager.sort(StudentManager.byId());
        }
        showCard("view"); // show the freshly sorted list
    }

    private void onExportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export students to CSV");
        chooser.setSelectedFile(new File("students_export.csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                manager.exportCsv(chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Exported successfully!",
                        "Export", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        currentTheme = darkMode ? Theme.dark() : Theme.light();
        applyTheme();
    }

    private void applyTheme() {
        currentTheme.apply(getContentPane());
        dashboardPanel.updateChartColors(currentTheme.accent, currentTheme.foreground);
        getContentPane().repaint();
    }

    private void onLogout() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    private void onExit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?", "Exit",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
