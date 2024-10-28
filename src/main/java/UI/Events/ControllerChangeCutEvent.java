package UI.Events;

import Domain.CutDTO;

import java.util.EventObject;

/**
 * Class {@code ControllerChangeCutEvent} that encapsulates the idea of the event of the controller
 * wanting to change a Cut
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-27
 */
public class ControllerChangeCutEvent extends EventObject {
    private CutDTO cutDTO;
    public ControllerChangeCutEvent(Object source, CutDTO cutDTO){
        super(source);
        this.cutDTO = cutDTO;
    }
    public CutDTO getCutDTO(){return cutDTO;}
}
