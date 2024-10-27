package UI.Events;

import UI.Widgets.Attributable;

import java.util.EventObject;

/**
 * Class {@code ChangeAttributeEvent} that encapsulates the idea of the event of wanting to change the attribute. It
 * allows for decoupling of objects and more general passing of events between them
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-27
 */
public class ChangeAttributeEvent extends EventObject {
    private Attributable attribute;
    public ChangeAttributeEvent(Object source, Attributable newAttribute){
        super(source);
        this.attribute = newAttribute;
    }
    public Attributable getAttribute(){return attribute;}
}