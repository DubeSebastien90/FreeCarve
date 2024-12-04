package UI.Listeners;

import Domain.Controller;
import UI.MainWindow;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This class ensures the save commands are caught regardless of window focus
 *
 * @author Kamran Charles Nayebi
 * @since 2024-12-03
 */
public class SaveDispatcher implements KeyEventDispatcher {
    private final MainWindow mainWindow;
    public SaveDispatcher(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
            new SaveProjectActionListener(mainWindow).actionPerformed(null);
            return true;
        } else if (e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_S){
            new SaveProjectAsActionListener(mainWindow).actionPerformed(null);
            return true;
        }
        return false;
    }
}