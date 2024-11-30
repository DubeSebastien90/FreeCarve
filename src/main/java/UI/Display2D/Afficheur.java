package UI.Display2D;

import UI.Display2D.DrawCutWrapper.DrawCutWrapper;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import javax.swing.*;
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
     * Draws the cuts of on the board
     */
    void drawCuts(Graphics2D graphics2D, Rendering2DWindow renderer, Drawing drawing, MainWindow mainWindow) {
        for (DrawCutWrapper cutWrapper : drawing.getCutWrappers()) {
            double diameter = mainWindow.getController().getBitDiameter(cutWrapper.getCutDTO().getBitIndex());
            double scaledStroke = renderer.scaleMMToPixel(diameter);
            cutWrapper.setStrokeSize(scaledStroke);
            cutWrapper.draw(graphics2D, renderer);
        }

        if (drawing.getState() == Drawing.DrawingState.CREATE_CUT) {
            double diameter = mainWindow.getController().getBitDiameter(drawing.getCurrentDrawingCut().getCutDTO().getBitIndex());
            double scaledStroke = renderer.scaleMMToPixel(diameter);
            drawing.getCurrentDrawingCut().setStrokeSize(scaledStroke);

            drawing.getCurrentDrawingCut().drawWhileChanging(graphics2D, renderer, drawing.getCreateCursorPoint());
            drawing.getCreateCursorPoint().drawMM(graphics2D, renderer, false);
        }

        if (drawing.getState() == Drawing.DrawingState.MODIFY_ANCHOR){
            drawing.getCurrentModifiedCut().drawWhileModifyingAnchor(graphics2D, renderer, drawing.getCreateCursorPoint());
            drawing.getModifyingAnchorCursorPoint().drawMM(graphics2D, renderer, false);
        }
    }


    /**
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

    void drawMask(Graphics2D graphics2D){
        Rectangle2D panneauOffset = rend.convertBoardTomm(rend.getBoard());
        int rectX = (int) panneauOffset.getX();
        int rectY = (int) panneauOffset.getY();
        int rectWidth = (int) panneauOffset.getWidth();
        int rectHeight = (int) panneauOffset.getHeight();

        Color maskColor = UIManager.getColor("SubWindow.background");
        graphics2D.setColor(maskColor);

        graphics2D.fillRect(0, 0, rend.getWidth(), rectY);
        graphics2D.fillRect(0, rectY + rectHeight, rend.getWidth(), rend.getHeight());
        graphics2D.fillRect(0, rectY, rectX, rectHeight);
        graphics2D.fillRect(rectX + rectWidth, rectY, rend.getWidth(), rectHeight);
    }
}
