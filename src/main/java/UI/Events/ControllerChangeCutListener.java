package UI.Events;

import java.util.EventListener;

/**
 * Class {@code ControllerChangeCutListener} that encapsulates the listener of {@code ControllerChangeCutEvent}
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-27
 */
public interface ControllerChangeCutListener extends EventListener {
    void controllerChangeCutEventOccured(ControllerChangeCutEvent event);
}
