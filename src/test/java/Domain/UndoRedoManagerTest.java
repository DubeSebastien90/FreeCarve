package Domain;

import Common.Exceptions.NullActionException;
import Common.Interfaces.IDoAction;
import Common.Interfaces.IUndoAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

class UndoRedoManagerTest {

    @Test
    void undo_HappyPath_UndoesActionAndFillsPile() {
        // Arrange
        IUndoAction undoAction = mock(IUndoAction.class);
        IDoAction doAction = mock(IDoAction.class);

        LinkedList<UndoRedoManager.ActionPair> undo = new LinkedList<>(List.of(new UndoRedoManager.ActionPair(doAction, undoAction)));
        LinkedList<UndoRedoManager.ActionPair> redo = new LinkedList<>();

        UndoRedoManager undoRedoManager = new UndoRedoManager(undo, redo);

        // Act
        Assertions.assertDoesNotThrow(undoRedoManager::undo);

        // Assert
        verify(doAction, times(0)).execute();
        verify(undoAction, times(1)).execute();
        Assertions.assertEquals(0, undo.size());
        Assertions.assertEquals(1, redo.size());
    }

    @Test
    void undo_StackEmpty_DoesNothing() {
        // Arrange
        IUndoAction undoAction = mock(IUndoAction.class);
        IDoAction doAction = mock(IDoAction.class);
        UndoRedoManager undoRedoManager = new UndoRedoManager(new LinkedList<>(), new LinkedList<>(List.of(new UndoRedoManager.ActionPair(doAction, undoAction))));

        // Act
        Assertions.assertDoesNotThrow(undoRedoManager::undo);

        // Assert
        verify(doAction, times(0)).execute();
        verify(undoAction, times(0)).execute();
    }


    @Test
    void redo_HappyPath_RedoesActionAndFillsPile() {
        // Arrange
        IUndoAction undoAction = mock(IUndoAction.class);
        IDoAction doAction = mock(IDoAction.class);

        LinkedList<UndoRedoManager.ActionPair> undo = new LinkedList<>();
        LinkedList<UndoRedoManager.ActionPair> redo = new LinkedList<>(List.of(new UndoRedoManager.ActionPair(doAction, undoAction)));

        UndoRedoManager undoRedoManager = new UndoRedoManager(undo, redo);

        // Act
        Assertions.assertDoesNotThrow(undoRedoManager::redo);

        // Assert
        verify(doAction, times(1)).execute();
        verify(undoAction, times(0)).execute();
        Assertions.assertEquals(1, undo.size());
        Assertions.assertEquals(0, redo.size());
    }

    @Test
    void redo_StackEmpty_DoesNothing() {
        // Arrange
        IUndoAction undoAction = mock(IUndoAction.class);
        IDoAction doAction = mock(IDoAction.class);
        UndoRedoManager undoRedoManager = new UndoRedoManager(new LinkedList<>(List.of(new UndoRedoManager.ActionPair(doAction, undoAction))), new LinkedList<>());

        // Act
        Assertions.assertDoesNotThrow(undoRedoManager::redo);

        // Assert
        verify(doAction, times(0)).execute();
        verify(undoAction, times(0)).execute();
    }

    @Test
    void memorize_GivenAction_ExecutesAndRecordsIt_EmptiesRedoPile() {
        // Arrange
        Queue<UndoRedoManager.ActionPair> redoStack = mock(Queue.class);

        UndoRedoManager undoRedoManager = new UndoRedoManager(new LinkedList<>(), redoStack);
        IDoAction doAction = mock(IDoAction.class);
        IUndoAction undoAction = mock(IUndoAction.class);

        // Act
        undoRedoManager.executeAndMemorize(doAction, undoAction);

        // Assert
        verify(doAction, times(1)).execute();
        verify(undoAction, times(0)).execute();
        verify(redoStack, times(1)).clear();
    }

    @Test
    void memorize_GivenNullAction_ThrowsErrorAndDoesNothing() {
        // Arrange
        UndoRedoManager undoRedoManager = new UndoRedoManager(new LinkedList<>(), new LinkedList<>());
        IUndoAction undoAction = mock(IUndoAction.class);

        // Act
        Assertions.assertThrows(NullActionException.class, ()-> undoRedoManager.executeAndMemorize(null, undoAction));

        // Assert
        verify(undoAction, times(0)).execute();
    }
}