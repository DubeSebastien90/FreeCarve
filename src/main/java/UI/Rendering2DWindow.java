package UI;

import Domain.Mesh;
import Domain.Triangle;
import Domain.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rendering2DWindow extends JPanel {
    private Rectangle panneau = new Rectangle(100, 100, 500, 300);

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(Color.WHITE);
        super.paintComponent(graphics2D);
        drawRectangle(graphics2D);
    }

    private void drawRectangle(Graphics2D graphics2D) {
        graphics2D.setColor(Color.ORANGE);
        graphics2D.draw(panneau);
        graphics2D.fill(panneau);
        System.out.println("hello");
    }


    private void resizePanneau(int newWidth, int newHeight) {
        panneau.setSize(newWidth, newHeight);
    }

    private void deltaResizePanneau(int deltaWidth, int deltaHeight) {
        panneau.setSize(((int) panneau.getWidth()) - deltaWidth, ((int) panneau.getHeight()) - deltaHeight);
    }
}
