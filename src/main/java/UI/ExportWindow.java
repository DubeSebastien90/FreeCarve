package UI;

import UI.SubWindows.BasicWindow;
import UI.Widgets.BigButton;
import UI.SubWindows.Rendering3DWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Represents an export window that displays a 3D renderer for visualizing
 * the final board, and provides an export button to convert the board into GCode instructions.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-25
 */
public class ExportWindow extends JPanel {

    private BigButton nextButton = new BigButton("Export");
    private Rendering3DWindow rendering3DWindow;
    private MainWindow mainWindow;

    /**
     * Constructs an ExportWindow and initializes its layout and components.
     */
    public ExportWindow() {
        this.setLayout(new GridBagLayout());
        init();
    }

    /**
     * Returns the renderer used in this export window.
     *
     * @return The Renderer instance responsible for displaying 3D shapes.
     */
    public Rendering3DWindow getRenderer() {
        return rendering3DWindow;
    }

    /**
     * Initializes the layout and adds components to the export window.
     * This method creates various 3D meshes and sets up the renderer with
     * those meshes.
     */
    public void init() {
        GridBagConstraints gbc = new GridBagConstraints();

        mainWindow = MainWindow.INSTANCE;

        rendering3DWindow = new Rendering3DWindow(mainWindow.getController().getCameraId());
        gbc.insets = new Insets(0, 0, 0, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.weightx = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new BasicWindow(true), gbc);
        gbc.insets = new Insets(0, 0, 5, 5);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 3;
        gbc.weighty = 0.40;
        gbc.fill = GridBagConstraints.BOTH;
        add(rendering3DWindow, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 3;
        gbc.weighty = 0.40;
        gbc.fill = GridBagConstraints.BOTH;
        add(new BasicWindow(true), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.weightx = 3;
        gbc.weighty = 0.20;
        gbc.fill = GridBagConstraints.BOTH;
        add(nextButton, gbc);
    }

    /**
     * Paints the component. This method calls the superclass's paint method
     * and revalidates the export button.
     *
     * @param graphics The graphics context used for painting.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        nextButton.revalidate();
    }
}
