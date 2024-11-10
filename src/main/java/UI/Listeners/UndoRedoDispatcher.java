package UI.Listeners;

import Domain.Controller;

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
    public UndoRedoDispatcher(Controller controller){
        this.controller = controller;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
            controller.undo();
            return true;
        } else if (e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y){
            controller.redo();
            return true;
        }
        return false;
    }
}
