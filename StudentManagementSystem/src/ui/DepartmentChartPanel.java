package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * A simple bar chart drawn with pure Java2D (no external chart library).
 * It shows how many students are in each department. Demonstrates custom
 * rendering by overriding paintComponent - a good interview talking point.
 */
public class DepartmentChartPanel extends JPanel {

    private Map<String, Integer> data = Map.of();
    private Color barColor  = new Color(37, 99, 235);
    private Color textColor = new Color(33, 37, 41);

    public DepartmentChartPanel() {
        setPreferredSize(new Dimension(600, 260));
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
        repaint();
    }

    public void setColors(Color bar, Color text) {
        this.barColor = bar;
        this.textColor = text;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        int labelSpace = 50;
        int usableHeight = height - padding - labelSpace;

        int max = 1;
        for (int v : data.values()) {
            max = Math.max(max, v);
        }

        int count = data.size();
        int gap = 20;
        int barWidth = Math.max(20, (width - padding * 2 - gap * (count - 1)) / count);

        g2.setColor(textColor);
        g2.drawString("Students by Department", padding, 24);

        int x = padding;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int value = entry.getValue();
            int barHeight = (int) ((value / (double) max) * usableHeight);
            int y = height - labelSpace - barHeight;

            g2.setColor(barColor);
            g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

            g2.setColor(textColor);
            g2.drawString(String.valueOf(value), x + barWidth / 2 - 4, y - 6);
            drawCenteredLabel(g2, shorten(entry.getKey()), x, barWidth, height - labelSpace + 16);

            x += barWidth + gap;
        }
    }

    private void drawCenteredLabel(Graphics2D g2, String text, int x, int barWidth, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2.drawString(text, x + (barWidth - textWidth) / 2, y);
    }

    /** Abbreviate long department names so labels fit under the bars. */
    private String shorten(String dept) {
        if (dept.length() <= 10) return dept;
        String[] words = dept.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty() && Character.isLetter(w.charAt(0))) {
                initials.append(Character.toUpperCase(w.charAt(0)));
            }
        }
        return initials.length() >= 2 ? initials.toString() : dept.substring(0, 10);
    }
}
