package Domain;

import Common.Interfaces.IDoAction;
import Common.Interfaces.IUndoAction;
import Common.Exceptions.NullActionException;

import java.util.Deque;
import java.util.Queue;

/**
 * Records and plays back actions. Works in tandem with the controller to have an effective undo redo system.
 *
 * @author Kamran Charles Nayebi
 * @since 2024-11-08
 */
public class UndoRedoManager {
    private final Queue<ActionPair> undoStack;
    private final Queue<ActionPair> redoStack;

    /**
     * Construct a new {@code UndoRedo} object with two empty {@code ProjectState Stack}
     */
    UndoRedoManager(Queue<ActionPair> undoStack, Queue<ActionPair> redoStack) {
        this.undoStack = undoStack;
        this.redoStack = redoStack;
    }

    /**
     * Undoes recorded action, does nothing if there are none
     */
    void undo() {
        if(!undoStack.isEmpty()){
            ActionPair pair = undoStack.remove();
            pair.undoAction().execute();
            redoStack.add(pair);
        }
    }

    /**
     * Redoes undone action, does nothing if there was no undo prior to this
     */
    void redo() {
        if(!redoStack.isEmpty()){
            ActionPair pair = redoStack.remove();
            pair.doAction().execute();
            undoStack.add(pair);
        }
    }

    /**
     * Executes the doAction and memorizes it
     *
     * @param doAction   lambda of method to execute
     * @param undoAction lambda of method undoing the first one
     * @throws NullActionException if any of the actions are null
     */
    void executeAndMemorize(IDoAction doAction, IUndoAction undoAction) {
        if(doAction == null || undoAction == null)
            throw new NullActionException("Impossible to memorize null actions");

        undoStack.add(new ActionPair(doAction, undoAction));
        redoStack.clear();
        doAction.execute();
    }

    record ActionPair(IDoAction doAction, IUndoAction undoAction) {}
}