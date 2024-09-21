package UI;

import Domain.ThirdDimension.Mesh;
import Domain.ThirdDimension.Vertex;
import UI.SubWindows.BasicWindow;
import UI.Widgets.BigButton;
import UI.SubWindows.Renderer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    private Renderer renderer;

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
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Initializes the layout and adds components to the export window.
     * This method creates various 3D meshes and sets up the renderer with
     * those meshes.
     */
    public void init() {
        GridBagConstraints gbc = new GridBagConstraints();

        Mesh cubeBleu = new Domain.ThirdDimension.Box(new Vertex(200, 200, 200), 100, 100, 30, Color.BLUE);
        Mesh cubeRouge = new Domain.ThirdDimension.Box(new Vertex(0, 0, 0), 75, 75, 75, Color.RED);
        Mesh pyramidVerte = new Pyramid(new Vertex(-110, 110, 110), Color.GREEN);
        Mesh ground = new Plane(new Vertex(0, 0, 0), 1000, Color.white);
        renderer = (new Renderer(new ArrayList<Mesh>(List.of(cubeRouge, cubeBleu, pyramidVerte, ground))));
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
        add(renderer, gbc);

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
