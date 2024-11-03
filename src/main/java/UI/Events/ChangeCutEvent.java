package UI.Events;

import java.util.EventObject;
import java.util.UUID;

/**
 * Class {@code ChangeCutEvent} that encapsulates the idea of the event of changing cuts. It
 * allows for decoupling of objects and more general passing of events between them
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-27
 */
public class ChangeCutEvent extends EventObject {
    private UUID cutId;
    public ChangeCutEvent(Object source, UUID cutId){
        super(source);
        this.cutId = cutId;
    }
    public UUID getCutId(){return cutId;}
}
