package UI;

import UI.Display2D.Rendering2DWindow;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.Events.ChangeCutEvent;
import UI.Events.ChangeCutListener;
import UI.SubWindows.AttributePanel;
import UI.SubWindows.BasicWindow;
import UI.SubWindows.BitConfigurationPanel;
import UI.Widgets.Attributable;
import UI.Widgets.BigButton;
import UI.Widgets.BitInfoDisplay;

import javax.swing.*;


/**
 * Represents a configuration choice window that allows users to select
 * options for the project. This window contains various components
 * such as a rendering area, a bit selection window, and dimension
 * configuration options.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-25
 */
public class ConfigChoiceWindow implements ChangeCutListener, ChangeAttributeListener {
    private final Rendering2DWindow rend;
    private final BigButton nextButton = new BigButton("Suivant");
    private final MainWindow mainWindow;
    private final BitConfigurationPanel bitWindow;
    private final AttributePanel attributePanel;
    private JSplitPane mainSplitPane;

    /**
     * Constructs a ConfigChoiceWindow and initializes its components and layout.
     */
    public ConfigChoiceWindow(MainWindow mainWindow) {
        rend = new Rendering2DWindow(mainWindow, this, this);
        bitWindow = new BitConfigurationPanel(this, mainWindow);
        attributePanel = new AttributePanel(true, mainWindow);
        this.mainWindow = mainWindow;
        init();
        setButtonEventHandlers();
    }

    /**
     * Returns the Rendering2DWindow contained in this configuration choice window.
     *
     * @return The Rendering2DWindow instance.
     */
    public Rendering2DWindow getRendering2DWindow() {
        return this.rend;
    }

    public JSplitPane getMainSplitPane() {
        return mainSplitPane;
    }

    public AttributePanel getAttributePanel() {
        return attributePanel;
    }

    /**
     * Initializes the layout and adds the necessary components to the window.
     * This method sets up the grid bag constraints for arranging components.
     */
    public void init() {

        BasicWindow rendHeaderWrapper = new BasicWindow(true);
        rendHeaderWrapper.setupHeader("Vue 2D", rend);
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, attributePanel, bitWindow);
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rendHeaderWrapper, splitPane1);
        splitPane1.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowHeight() / 3);
        mainSplitPane.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowWidth() / 3 * 2);
        mainSplitPane.setResizeWeight(0);

    }

    /**
     * Sets the event handler for the buttons
     */
    private void setButtonEventHandlers() {
        nextButton.getButton().addActionListener(e -> mainWindow.getMiddleContent().nextWindow());
    }

    public int getSelectedBit() {
        return bitWindow.getSelectedBit();
    }

    public BitConfigurationPanel getBitWindow() {
        return bitWindow;
    }

    @Override
    public void addCutEventOccured(ChangeCutEvent event) {
        //todo a parent of the RendererWindow2D should always implement the addCutEventOccured, add all
        // interesting functionalities when an cut event happens in the RendererWindow2D
    }

    @Override
    public void deleteCutEventOccured(ChangeCutEvent event) {
        //todo if usefull to remove a cut in the ConfigChoiceWindow
    }

    /**
     * Set the selected element of the CutWindow and changed the AttributePanel accordingly
     *
     * @param event ChangeAttributeEvent being called by a child class
     */
    @Override
    public void changeAttributeEventOccurred(ChangeAttributeEvent event) {
        Attributable selectedAttributable = event.getAttribute();
        if (selectedAttributable.getClass() != BitInfoDisplay.class) {
            mainWindow.getLeftBar().getToolBar().disableTool(LeftBar.ToolBar.Tool.TRASH);
        }
        this.attributePanel.updateAttribute(selectedAttributable);
    }

    @Override
    public void modifiedAttributeEventOccured(ChangeAttributeEvent event) {
        //todo this function is called upon by the subwindows when an attribute is changed, it is currently empty because nothing to implement
        // but could be useful when sharing informations between the CutList and the 2D Afficheur
    }
}
