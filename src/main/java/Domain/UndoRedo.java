package Domain;

import Domain.ThirdDimension.Vertex;

import java.util.Stack;

/**
 * The {@code UndoRedo} class regroups functions useful to navigate between multiples {@code ProjectState} using two stack of {@code ProjectState}.<br>
 * The {@code ProjectState} on top of the undoList represent the current state.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class UndoRedo {
    private final Stack<ProjectState> redoList = new Stack<>();
    private final Stack<ProjectState> undoList = new Stack<>();

    /**
     * Construct a new {@code UndoRedo} object with two empty {@code ProjectState Stack}
     */
    UndoRedo() {
        undoList.add(new ProjectState(new Bit[12],  new PanelCNC(new Vertex(15.0, 15.0, 0.0), 5)));
    }

    /**
     * @return The current state of the project
     */
    ProjectState getCurrentState() {
        return undoList.peek();
    }

    /**
     * Pops the {@code ProjectState} on top of the undoList and put it in the redoList.
     */
    void undo() {
        //todo
    }

    /**
     * Pops the {@code ProjectState} on top of the redoList and put it in the undoList.
     */
    void redo() {
        //todo
    }

    /**
     * Adds a {@code ProjectState} on top of the undoList and clears the redoList.
     *
     * @param state The new {@code ProjectState}
     */
    void addAction(ProjectState state) {
        undoList.add(state);
        redoList.clear();
    }

}
