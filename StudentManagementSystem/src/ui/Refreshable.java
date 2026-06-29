package ui;

/**
 * Implemented by any panel whose contents depend on the student data.
 * MainFrame calls refreshData() right before showing the panel, so the
 * screen is always up to date after an add / update / delete elsewhere.
 */
public interface Refreshable {
    void refreshData();
}
