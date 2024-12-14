package Common.Interfaces;

import Common.Exceptions.ClampZoneException;
import Common.Exceptions.InvalidBitException;

/**
 * Functional interface that represents an action to be done
 * @author Kamran Charles Nayebi
 * @since 2024-11-08
 */
@FunctionalInterface
public interface IDoAction {
    void execute();
}
