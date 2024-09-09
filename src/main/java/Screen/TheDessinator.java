package Screen;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheDessinator extends JPanel {
    private List<Mesh> meshes;
    private Vertex mousePos;
    private BoutonRotation boutonRotation;

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public void setMeshes(List<Mesh> meshes) {
        this.meshes = meshes;
    }

    public TheDessinator(List<Mesh> meshes) {
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

    public void setMousePos(Vertex mousePos) {
        this.mousePos = mousePos;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(Color.GRAY);
        super.paintComponent(graphics2D);
        Mesh mesh = Triangle.printTriangles(this, graphics2D, meshes, mousePos);
        if (mousePos.getZ() == 1) {
            boutonRotation.setSelectedMesh(mesh);
        }
        setMousePos(new Vertex(0,0,0));
    }

}
