package Screen;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheDessinator extends JPanel {
    private List<Mesh> meshes;

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
        addKeyListener(new BoutonRotation(this));
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(Color.GRAY);
        super.paintComponent(graphics2D);
        Triangle.printTriangles(this, graphics2D, meshes);

    }

}
