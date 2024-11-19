package UI.Display2D;

import UI.Display2D.DrawCutWrapper.DrawCutWrapper;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

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
            double displayposX = Math.round(rend.getMmMousePt().getX() * 100);
            displayposX = displayposX / 100;
            double displayposY = Math.round(rend.getMmMousePt().getY() * 100);
            displayposY = displayposY / 100;
            graphics2D.drawString(displayposX + "; " + displayposY, 20, 20);
        }
    }

    /**
     * <<<<<<< HEAD
     * Draws the cuts of on the board
     */
    void drawCuts(Graphics2D graphics2D, Rendering2DWindow renderer, Drawing drawing) {
        for (DrawCutWrapper cutWrapper : drawing.getCutWrappers()) {
            cutWrapper.draw(graphics2D, renderer);
        }

        if (drawing.getCursorPoint() != null) {
            drawing.getCursorPoint().drawMM(graphics2D, renderer);
        }

        if (drawing.getCurrentDrawingCut() != null && drawing.getCursorPoint() != null) {
            drawing.getCurrentDrawingCut().drawWhileChanging(graphics2D, renderer, drawing.getCursorPoint());
        }
    }


    /**
     * =======
     * >>>>>>> ff140ea (grid button and same rendering2dWindow)
     * Draws the grid on the board
     *
     * @param graphics2D the <code>Graphics</code> object to protect
     */
    void drawGrid(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(0, 0, 0, (int) (Math.min(255, 127 * rend.getZoom()))));
        double size = rend.getMainWindow().getController().getGrid().getSize();
        size = size * (rend.getZoom());
        ArrayList<Double> areammBoard = rend.getAreammBoard();
        for (double i = areammBoard.get(0); i < areammBoard.get(1); i += size) {
            graphics2D.drawLine((int) i, areammBoard.get(3).intValue(), (int) i, areammBoard.get(2).intValue());
        }
        for (double i = areammBoard.get(3); i > areammBoard.get(2); i -= size) {
            graphics2D.drawLine(areammBoard.get(0).intValue(), (int) i, areammBoard.get(1).intValue(), (int) i);
        }
    }
}
