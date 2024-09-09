package Screen;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code Renderer} class provides function to render meshes on it, it extends {@code JPanel}.
 *
 * @author Adam Côté
 * @since 2024-09-08
 */
public class Renderer extends JPanel {
    private List<Mesh> meshes;
    private Vertex mousePos;
    private BoutonRotation boutonRotation;

    /**
     * @return the {@code List} of {@code Mesh} that need to be rendered
     */
    public List<Mesh> getMeshes() {
        return meshes;
    }

    /**
     * Sets the {@code List} of {@code Mesh} that needs to be rendered to a new one
     *
     * @param meshes the new {@code List} of {@code Mesh}
     */
    public void setMeshes(List<Mesh> meshes) {
        this.meshes = meshes;
    }

    /**
     * Constructs a new {@code Renderer} object with a {@code List} of {@code Mesh} and initialize {@code Renderer}
     *
     * @param meshes the {@code List} of {@code Mesh}
     */
    public Renderer(List<Mesh> meshes) {
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();
        requestFocus();
        setMeshes(meshes);
        boutonRotation = new BoutonRotation(this);
        addKeyListener(boutonRotation);
        addMouseListener(boutonRotation);
        this.mousePos = new Vertex(0, 0, 0);
    }

    /**
     * Sets the mouse position variable at a specific {@code Vertex}
     *
     * @param mousePos the position of the mouse
     */
    public void setMousePos(Vertex mousePos) {
        this.mousePos = mousePos;
    }

    /**
     * Paint the {@code List} of {@code Mesh} on a {@code Graphics2D} and then draw it on the {@code Renderer}
     *
     * @param graphics a {@code Graphics} object on which the meshes will be drawn
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(Color.GRAY);
        super.paintComponent(graphics2D);
        Mesh mesh = Triangle.printTriangles(this, graphics2D, meshes, mousePos);
        if (mousePos.getZ() == 1) {
            boutonRotation.setSelectedMesh(mesh);
        }
        setMousePos(new Vertex(0, 0, 0));
    }

}
