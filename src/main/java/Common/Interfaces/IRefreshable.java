package Common.Interfaces;

/**
 * Implemented by UI to be refreshed when an undo or a redo is done
 * @author Kamran Charles Nayebi
 * @since 2024-11-09
 */
@FunctionalInterface
public interface IRefreshable {
    void refresh();
}
