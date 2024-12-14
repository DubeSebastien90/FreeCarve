package UI.Listeners;

import Domain.Controller;
import UI.MainWindow;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This class ensures the redo and undo commands are caught regardless of window focus
 *
 * @author Kamran Charles Nayebi
 * @since 2024-11-09
 */
public class UndoRedoDispatcher implements KeyEventDispatcher {
    private final Controller controller;
    private final MainWindow mainWindow;

    public UndoRedoDispatcher(MainWindow mainWindow, Controller controller) {
        this.controller = controller;
        this.mainWindow = mainWindow;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            controller.undo();
            mainWindow.getMiddleContent().getExportWindow().calculateGcode();
            controller.setScene();
            mainWindow.getMiddleContent().getConfigChoiceWindow().getBitWindow().refresh();
            mainWindow.getMiddleContent().getConfigChoiceWindow().getBitWindow().repaint();
            mainWindow.getMiddleContent().getCutWindow().notifyObservers();
            mainWindow.getMiddleContent().getCutWindow().verifyWithBitChange();
            return true;
        } else if (e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
            controller.redo();
            mainWindow.getMiddleContent().getExportWindow().calculateGcode();
            controller.setScene();
            mainWindow.getMiddleContent().getConfigChoiceWindow().getBitWindow().refresh();
            mainWindow.getMiddleContent().getConfigChoiceWindow().getBitWindow().repaint();
            mainWindow.getMiddleContent().getCutWindow().notifyObservers();
            mainWindow.getMiddleContent().getCutWindow().verifyWithBitChange();
            return true;
        }
        return false;
    }
}
