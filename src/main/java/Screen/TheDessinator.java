package Screen;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheDessinator extends JPanel {
    private List<Triangle> currentShape;

    public List<Triangle> getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(List<Triangle> currentShape) {
        this.currentShape = currentShape;
    }

    public TheDessinator(List<Triangle> triangles) {
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();
        requestFocus();
        currentShape = triangles;
        addKeyListener(new BoutonRotation(this));

    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(Color.GRAY);
        super.paintComponent(graphics2D);
        Triangle.printTriangles(this, graphics2D, currentShape);

    }

}
