package ui;

import model.Student;
import service.StudentManager;

import javax.swing.*;
import java.awt.*;

/**
 * The home screen. Shows summary statistics (total students, average CGPA,
 * highest CGPA, topper) as cards, plus a bar chart of students per
 * department. Everything recomputes from the manager on refreshData().
 */
public class DashboardPanel extends JPanel implements Refreshable {

    private final StudentManager manager;

    private final JLabel totalValue   = bigValueLabel();
    private final JLabel avgValue      = bigValueLabel();
    private final JLabel highestValue  = bigValueLabel();
    private final JLabel topperValue   = bigValueLabel();

    private final DepartmentChartPanel chart = new DepartmentChartPanel();

    public DashboardPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Dashboard");
        title.setFont(UIHelper.TITLE_FONT);
        add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 15));
        cards.add(statCard("Total Students", totalValue));
        cards.add(statCard("Average CGPA", avgValue));
        cards.add(statCard("Highest CGPA", highestValue));
        cards.add(statCard("Topper", topperValue));

        JPanel center = new JPanel(new BorderLayout(15, 15));
        center.add(cards, BorderLayout.NORTH);
        center.add(chart, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private JPanel statCard(String heading, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 215)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        JLabel head = new JLabel(heading);
        head.setFont(UIHelper.LABEL_FONT);
        card.add(head, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private static JLabel bigValueLabel() {
        JLabel label = new JLabel("-");
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        return label;
    }

    /** Let the dashboard recolour its custom-painted chart on theme change. */
    public void updateChartColors(Color bar, Color text) {
        chart.setColors(bar, text);
    }

    @Override
    public void refreshData() {
        totalValue.setText(String.valueOf(manager.getCount()));
        avgValue.setText(String.format("%.2f", manager.getAverageCgpa()));
        highestValue.setText(String.format("%.2f", manager.getHighestCgpa()));

        Student topper = manager.getTopper();
        topperValue.setText(topper == null ? "-" : topper.getFullName());

        chart.setData(manager.getStudentsByDepartment());
    }
}
