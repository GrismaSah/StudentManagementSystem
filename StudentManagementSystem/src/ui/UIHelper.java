package ui;

import javax.swing.*;
import java.awt.*;

/** Small shared UI constants and helpers, to avoid repeating styling code. */
public final class UIHelper {

    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 22);
    public static final Font HEADING_FONT = new Font("SansSerif", Font.BOLD, 16);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);

    public static final String[] DEPARTMENTS = {
            "Computer Science", "Information Technology", "Electronics",
            "Mechanical", "Civil", "Electrical", "Data Science", "AI & ML"
    };
    public static final String[] GENDERS = {"Male", "Female", "Other"};
    public static final Integer[] SEMESTERS = {1, 2, 3, 4, 5, 6, 7, 8};

    private UIHelper() {
    }

    /** A label styled consistently for form rows. */
    public static JLabel formLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }

    /** A button with consistent padding and a hand cursor. */
    public static JButton button(String text) {
        JButton b = new JButton(text);
        b.setFont(LABEL_FONT);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return b;
    }
}
