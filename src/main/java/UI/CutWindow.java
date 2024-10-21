package UI;

import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.SubWindows.AttributePanel;
import UI.SubWindows.BasicWindow;
import UI.SubWindows.CutListPanel;
import UI.SubWindows.Rendering2DWindow;
import UI.Widgets.Attributable;
import UI.Widgets.ChooseDimension;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code CutWindow} class encapsulates the sub-windows necessary for using the
 * CutEdition Menu
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class CutWindow implements ChangeAttributeListener {
    private JSplitPane mainSplitPane;
    private JSplitPane splitPane1;
    private JSplitPane splitPane2;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private CutListPanel cutListPanel;
    private AttributePanel attributePanel;
    private Attributable selectedAttributable;

    /**
     * Constructs a {@code CutWindow} instance initializing all of it's sub-panels
     * and sub-components
     */
    public CutWindow(MainWindow mainWindow) {
        this.init(mainWindow);
    }

    /**
     * @return the {@code JSplitPane} container of the {@code CutWindow}
     */
    public JSplitPane getCutWindow() {
        return mainSplitPane;
    }

    public JPanel getScreen(int i) {
        switch (i) {
            case 1 -> {
                return panel1;
            }
        }
        return null;
    }

    /**
     * Set the selected element of the CutWindow and changed the AttributePanel accordingly
     * @param event ChangeAttributeEvent being called by a child class
     */
    @Override
    public void changeAttributeEventOccurred(ChangeAttributeEvent event) {
        this.selectedAttributable = event.getAttribute();
        this.attributePanel.updateAttribute(this.selectedAttributable);
    }

    @Override
    public void modifiedAttributeEventOccured(ChangeAttributeEvent event){
        //todo this function is called upon by the subwindows when an attribute is changed, it is currently empty because nothing to implement
        // but could be useful when sharing informations between the CutList and the 2D Afficheur
    }

    /**
     * Initiates all of the {@code CutWindow} components
     */
    private void init(MainWindow mainWindow) {
        panel1 = new Rendering2DWindow(mainWindow);

        attributePanel = new AttributePanel(true);
        panel2 = attributePanel;

        cutListPanel = new CutListPanel(true, this);
        panel3 = cutListPanel;

        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel2, panel3);
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, splitPane1);

        splitPane1.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowHeight() / 2);
        mainSplitPane.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowWidth() / 2);
    }
}
