package UI.Display2D;

import UI.Widgets.PersoPoint;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Afficheur {

    private final Rendering2DWindow rend;

    public Afficheur(Rendering2DWindow rend) {
        this.rend = rend;
    }

    /**
     * Draws the board on this JPanel.
     *
     * @param graphics2D A graphics object which is painted on this JPanel.
     */
    void drawRectangle(Graphics2D graphics2D) {
        Color color = new Color(222, 184, 135);
        graphics2D.setColor(color);
        Rectangle2D panneauOffset = rend.convertBoardTomm(rend.getBoard());
        graphics2D.draw(panneauOffset);
        graphics2D.fill(panneauOffset);
    }

    /**
     * Draws the points on this JPanel.
     *
     * @param graphics2D A graphics object which is painted on the JPanel.
     */
    void drawPoints(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        for (PersoPoint point : rend.getPoints()) {
            if (point.getFilled()) {
                graphics2D.fillOval(((int) point.getLocationX()), ((int) point.getLocationY()), ((int) point.getRadius()), ((int) point.getRadius()));
            } else {
                graphics2D.drawOval(((int) point.getLocationX()), ((int) point.getLocationY()), ((int) point.getRadius()), ((int) point.getRadius()));

            }
        }
    }

    /**
     * Draws the mouse coordinates in the top left corner of the screen.
     *
     * @param graphics2D A graphics object which is painted on this JPanel
     */
    void drawMousePos(Graphics2D graphics2D) {
        if (rend.isPointonPanel()) {
            graphics2D.setColor(Color.BLACK);
            double displayposX = Math.round(rend.getFakeMousePt().getX() * 100);
            displayposX = displayposX / 100;
            double displayposY = Math.round(rend.getFakeMousePt().getY() * 100);
            displayposY = displayposY / 100;
            graphics2D.drawString(displayposX + "; " + displayposY, 20, 20);
        }
    }
}
