package UI.Widgets;

import javax.swing.*;

/**
 * The {@code Attributable} interface is an abstract class in order to override
 * the panels appearing in the AttributePanel
 *
 * @author Louis-Etienne messier
 * @version 1.0
 * @since 2024-10-23
 */


public interface Attributable {

    /**
     * virtual function that need to be overriden by every child of Attributable
     * @return {@JLabel} of the name wanted to appear on the attributeWindow
     */
    public JLabel showName();

    /**
     * virtual function that needs to be overriden by every child of Attributable
     * @return {@Code JPanel} of the possible attributs that can be modified
     */
    public JPanel showAttribute();
}
