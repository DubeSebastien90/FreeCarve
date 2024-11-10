package Domain.Interfaces;

import Common.Interfaces.IDoAction;
import Common.Interfaces.IUndoAction;

public interface IMemorizer {
    void executeAndMemorize(IDoAction doAction, IUndoAction undoAction);
}
