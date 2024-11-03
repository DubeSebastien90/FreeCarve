package UI.Events;

import java.util.EventListener;

/**
 * Class {@code ChangeAttributeListenert} that encapsulates the listener of {@code ChangeCutEvent}
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-27
 */
public interface ChangeCutListener extends EventListener {
    void addCutEventOccured(ChangeCutEvent event);
    void deleteCutEventOccured(ChangeCutEvent event);
}
