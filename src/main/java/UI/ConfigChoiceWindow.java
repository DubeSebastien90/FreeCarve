package UI;

import Common.DTO.BitDTO;
import Common.Exceptions.InvalidFileExtensionException;
import UI.Events.ChangeCutEvent;
import UI.Events.ChangeCutListener;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.Listeners.SaveToolsActionListener;
import UI.SubWindows.AttributePanel;
import UI.SubWindows.BitConfigurationPanel;
import UI.Display2D.Rendering2DWindow;

import UI.Widgets.BigButton;

import UI.Widgets.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

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
public class ConfigChoiceWindow extends JPanel implements ChangeCutListener, ChangeAttributeListener {
    private final Rendering2DWindow rend;
    private final BigButton nextButton = new BigButton("Suivant");
    private final JButton saveToolsButton = UiUtil.createSVGButton("upload", true, 50);
    private final JButton loadToolsButton = UiUtil.createSVGButton("download", true, 50);
    private final MainWindow mainWindow;
    private final BitConfigurationPanel bitWindow;
    private final AttributePanel attributePanel;

    /**
     * Constructs a ConfigChoiceWindow and initializes its components and layout.
     */
    public ConfigChoiceWindow(MainWindow mainWindow) {
        this.setLayout(new GridBagLayout());
        rend = new Rendering2DWindow(mainWindow, this, this);
        bitWindow = new BitConfigurationPanel(this, mainWindow);
        attributePanel = new AttributePanel(true, mainWindow);
        this.mainWindow = mainWindow;
        setFocusable(true);
        requestFocusInWindow();
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

    /**
     * Initializes the layout and adds the necessary components to the window.
     * This method sets up the grid bag constraints for arranging components.
     */
    public void init() {
        GridBagConstraints gbc = new GridBagConstraints();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rend, attributePanel);
        splitPane.setResizeWeight(.8);

        attributePanel.setPreferredSize(new Dimension(0, 0));

        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.gridwidth = 5;
        gbc.weightx = 2;
        gbc.weighty = 0.90;
        gbc.fill = GridBagConstraints.BOTH;
        add(splitPane, gbc);

        gbc.insets = new Insets(5, 0, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 2;
        gbc.weighty = 0.10;
        gbc.fill = GridBagConstraints.BOTH;
        add(bitWindow, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.25;
        gbc.fill = GridBagConstraints.BOTH;
        add(nextButton, gbc);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        add(saveToolsButton, gbc);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        add(loadToolsButton, gbc);

    }

    /**
     * Sets the event handler for the buttons
     */
    private void setButtonEventHandlers() {
        nextButton.getButton().addActionListener(e-> mainWindow.getMiddleContent().nextWindow());

        saveToolsButton.addActionListener(new SaveToolsActionListener(mainWindow));

        loadToolsButton.addActionListener(e->{
            File file = Utils.chooseFile("Charger outils", "defaut.CNC", mainWindow.getFrame(), FileCache.INSTANCE.getLastToolSave(), "Fichier outils (CNC)", "CNC");
            if (file != null) {
                try {
                    mainWindow.getController().loadTools(file);
                } catch (InvalidFileExtensionException ex) {
                    System.out.println("Invalid file extension");
                } catch (IOException ex) {
                    System.out.println("Unable to load file");
                } catch (ClassNotFoundException ex) {
                    System.out.println("File content is not valid");
                }
                bitWindow.refresh();
                attributePanel.update();
                mainWindow.getMiddleContent().getCutWindow().notifyObservers();
                FileCache.INSTANCE.setLastToolSave(file);
            }
        });
    }

    /**
     * Paints the component. This method calls the superclass's paint method
     * and revalidates the next button.
     *
     * @param graphics The graphics context used for painting.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        nextButton.revalidate();
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
        this.attributePanel.updateAttribute(selectedAttributable);
    }

    @Override
    public void modifiedAttributeEventOccured(ChangeAttributeEvent event) {
        //todo this function is called upon by the subwindows when an attribute is changed, it is currently empty because nothing to implement
        // but could be useful when sharing informations between the CutList and the 2D Afficheur
    }
}
