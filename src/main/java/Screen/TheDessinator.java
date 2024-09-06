package Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TheDessinator extends JPanel {
    private ArrayList<Triangle> currentShape;

    public ArrayList<Triangle> getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(ArrayList<Triangle> currentShape) {
        this.currentShape = currentShape;
    }

    public TheDessinator(ArrayList<Triangle> triangles) {
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
        super.paintComponent(graphics2D);

        BufferedImage img =
                new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Triangle.printTriangles(this, graphics2D, currentShape);
    }

}
