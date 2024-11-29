package Domain;

import Common.Interfaces.IDoAction;
import Common.Interfaces.IRefreshable;
import Common.Interfaces.IUndoAction;
import Common.Exceptions.NullActionException;
import Common.Interfaces.IMemorizer;

import java.util.*;

/**
 * Records and plays back actions. Works in tandem with the controller to have an effective undo redo system.
 *
 * @author Kamran Charles Nayebi
 * @since 2024-11-08
 */
class UndoRedoManager implements IMemorizer {
    private final Deque<ActionPair> undoStack;
    private final Deque<ActionPair> redoStack;
    private final Collection<IRefreshable> refreshables;

    /**
     * Construct a new {@code UndoRedo} object with two empty {@code CNCMachine Stack}
     */
    UndoRedoManager(){
        this(new LinkedList<>(), new LinkedList<>());
    }

    UndoRedoManager(Deque<ActionPair> undoStack, Deque<ActionPair> redoStack) {
        this.undoStack = undoStack;
        this.redoStack = redoStack;
        this.refreshables = new ArrayList<>();
    }

    /**
     * Undoes recorded action, does nothing if there are none
     */
    void undo() {
        if(!undoStack.isEmpty()){
            ActionPair pair = undoStack.pop();
            pair.undoAction().execute();
            redoStack.push(pair);
            refreshables.forEach(IRefreshable::refresh);
        }
    }

    /**
     * Redoes undone action, does nothing if there was no undo prior to this
     */
    void redo() {
        if(!redoStack.isEmpty()){
            ActionPair pair = redoStack.pop();
            pair.doAction().execute();
            undoStack.push(pair);
            refreshables.forEach(IRefreshable::refresh);
        }
    }

    /**
     * Executes the doAction and memorizes it
     *
     * @param doAction   lambda of method to execute
     * @param undoAction lambda of method undoing the first one
     * @throws NullActionException if any of the actions are null
     */
    public void executeAndMemorize(IDoAction doAction, IUndoAction undoAction) {
        if(doAction == null || undoAction == null)
            throw new NullActionException("Impossible to memorize null actions");

        undoStack.push(new ActionPair(doAction, undoAction));
        redoStack.clear();
        doAction.execute();
    }

    public void addRefreshListener(IRefreshable refreshable){
        if(refreshable == null)
            throw new NullPointerException("Cannot register null refreshable");

        refreshables.add(refreshable);
    }

    record ActionPair(IDoAction doAction, IUndoAction undoAction) {}
}