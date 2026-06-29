package util;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * A tiny theming system that supports Light and Dark modes without any
 * external library. A Theme is just a palette of colours; applyTheme()
 * walks the component tree and recolours everything.
 *
 * This demonstrates recursion over a Swing component hierarchy - a neat
 * trick worth being able to explain in an interview.
 */
public class Theme {

    public final Color background;   // window / panel background
    public final Color surface;      // cards, input fields
    public final Color foreground;   // text colour
    public final Color accent;       // buttons, highlights
    public final Color accentText;   // text on top of accent
    public final Color tableGrid;    // table grid lines

    private Theme(Color background, Color surface, Color foreground,
                  Color accent, Color accentText, Color tableGrid) {
        this.background = background;
        this.surface = surface;
        this.foreground = foreground;
        this.accent = accent;
        this.accentText = accentText;
        this.tableGrid = tableGrid;
    }

    public static Theme light() {
        return new Theme(
                new Color(245, 246, 250),
                new Color(255, 255, 255),
                new Color(33, 37, 41),
                new Color(37, 99, 235),
                Color.WHITE,
                new Color(222, 226, 230));
    }

    public static Theme dark() {
        return new Theme(
                new Color(24, 26, 32),
                new Color(36, 40, 48),
                new Color(230, 232, 236),
                new Color(59, 130, 246),
                Color.WHITE,
                new Color(60, 64, 72));
    }

    /** Recursively apply this theme to a component and all its children. */
    public void apply(Component component) {
        if (component instanceof JPanel || component instanceof JScrollPane
                || component instanceof JViewport) {
            component.setBackground(background);
        }

        if (component instanceof JLabel) {
            component.setForeground(foreground);
        }

        if (component instanceof JButton button) {
            button.setBackground(accent);
            button.setForeground(accentText);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
        }

        if (component instanceof JTextField || component instanceof JPasswordField) {
            component.setBackground(surface);
            component.setForeground(foreground);
            ((JTextField) component).setCaretColor(foreground);
        }

        if (component instanceof JComboBox<?> combo) {
            combo.setBackground(surface);
            combo.setForeground(foreground);
        }

        if (component instanceof JTextArea area) {
            area.setBackground(surface);
            area.setForeground(foreground);
        }

        if (component instanceof JTable table) {
            table.setBackground(surface);
            table.setForeground(foreground);
            table.setGridColor(tableGrid);
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                header.setBackground(accent);
                header.setForeground(accentText);
            }
        }

        // Recurse into children of any container.
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                apply(child);
            }
        }
    }
}
