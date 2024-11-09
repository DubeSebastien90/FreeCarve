package Common.Interfaces;

/**
 * Functional interface that represents an action to be done
 * @author Kamran Charles Nayebi
 * @since 2024-11-08
 */
@FunctionalInterface
public interface IUndoAction {
    void execute();
}
