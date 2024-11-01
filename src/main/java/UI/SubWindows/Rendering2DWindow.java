package UI.SubWindows;

import Domain.Grid;
import Domain.GridDTO;
import UI.MainWindow;
import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Rendering2DWindow extends JPanel {
    private Rectangle board = new Rectangle(0, 0, 200, 100);
    private Point mousePt;
    private Point mmMousePt;
    private double offsetX;
    private double offsetY;
    private double zoom;
    private double prevZoom;
    private int wW;
    private int wH;
    private MainWindow mainWindow;
    ArrayList<Double> areammBoard = new ArrayList<>();

    /**
     * Constructor for Renderinf2DWIndow
     * @param mainWindow The main window to get the controller
     */
    public Rendering2DWindow(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        zoom = 1;
        mousePt = new Point(0, 0);
        mmMousePt = new Point(0, 0);
        offsetY = 100;
        offsetX = 100;
        mainWindow.getController().putGrid(7, 10);

        //add listeners
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                offsetX -= ((mousePt.x - e.getPoint().x) / zoom);
                offsetY += ((mousePt.y - e.getPoint().y) / zoom);
                mousePt = e.getPoint();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePt = e.getPoint();
                mmMousePt.setLocation(pixelTomm(mousePt));
                repaint();
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                double zoomFactor = ((double) 25 / Math.signum(e.getWheelRotation()));
                zoom -= zoom / zoomFactor;
                offsetX = (mousePt.x - (mmMousePt.x * zoom)) / zoom;
                offsetY = ((-1 * (mousePt.y - wH)) - (mmMousePt.y * zoom)) / zoom;
                repaint();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                wW = getWidth();
                wH = getHeight();
                offsetX = 100;
                offsetY = 100;
                zoom = 1;
                repaint();
            }
        });
    }

    /**
     * Function executed when rendering the screen
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(UIManager.getColor("SubWindow.background"));
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);
        drawRectangle(graphics2D);
        drawGrid(graphics2D);
    }

    /**
     * Draws the board on the screen
     * @param graphics2D the <code>Graphics</code> object to protect
     */
    private void drawRectangle(Graphics2D graphics2D) {
        Color color = new Color(222, 184, 135);
        graphics2D.setColor(color);
        Rectangle panneauOffset = convertBoardTomm(board);
        graphics2D.draw(panneauOffset);
        graphics2D.fill(panneauOffset);
    }

    /**
     * Draws the mouse coordinates next to it
     * @param graphics2D the <code>Graphics</code> object to protect
     */
    private void drawMousePos(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(mmMousePt.x + ", " + mmMousePt.y, mousePt.x, mousePt.y);
        Point magnetisedPt = getMagnetisedPos(mousePt);
        graphics2D.drawString(magnetisedPt.x + ", " + magnetisedPt.y, mousePt.x, mousePt.y - 20);
    }

    /**
     * Draws the grid on the board
     * @param graphics2D the <code>Graphics</code> object to protect
     */
    private void drawGrid(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(0, 0, 0, (int) (Math.min(255, 127 * zoom))));
        double size = mainWindow.getController().getGrid().getSize();
        size = size * (zoom);
        for (double i = areammBoard.get(0); i < areammBoard.get(1); i += size) {
            graphics2D.drawLine((int) i, areammBoard.get(3).intValue(), (int) i, areammBoard.get(2).intValue());
        }
        for (double i = areammBoard.get(3); i > areammBoard.get(2); i -= size) {
            graphics2D.drawLine(areammBoard.get(0).intValue(), (int) i, areammBoard.get(1).intValue(), (int) i);
        }
    }


    private void resizePanneau(int newWidth, int newHeight) {
        board.setSize(newWidth, newHeight);
    }

    private void deltaResizePanneau(int deltaWidth, int deltaHeight) {
        board.setSize(((int) board.getWidth()) - deltaWidth, ((int) board.getHeight()) - deltaHeight);
    }

    /**
     * Function that returns the magnetised position of the mouse based on the grid
     * @param mousePt The mouse coordinates point in pixel
     * @return the mouse coordinates if it's not close enough to an intersection, the intersection coordinates in pixels if the mouse is close enough
     */
    public Point getMagnetisedPos(Point mousePt) {
        GridDTO grid = mainWindow.getController().getGrid();
        for (double i = areammBoard.get(0); i < areammBoard.get(1); i += grid.getSize()*zoom) {
            if (Math.abs((mousePt.x - i)) <= grid.getMagnetPrecision()) {
                for (double j = areammBoard.get(3); j > areammBoard.get(2); j -= grid.getSize()*zoom) {
                    if (Point.distance(mousePt.x, mousePt.y, i, j) <= grid.getMagnetPrecision()) {
                        return new Point((int) i, (int) j);
                    }
                }
                break;
            }
        }
        return mousePt;
    }

    /**
     * Converts a point in mm into pixels
     * @param mmPt The point in mm
     * @return The point in pixel
     */
    public Point mmTopixel(Point mmPt) {
        return new Point((int) ((mmPt.x * zoom) + (offsetX * zoom)), (int) ((-1 * ((mmPt.y * zoom) + (offsetY * zoom))) + wH));
    }

    /**
     * Converts a point in pixels into milimeters
     * @param pixelPt The point in pixel
     * @return The point in mm
     */
    public Point pixelTomm(Point pixelPt) {
        return new Point((int) ((pixelPt.x - (offsetX * zoom)) / zoom), (int) (((-1 * (pixelPt.y - wH)) - (offsetY * zoom)) / zoom));
    }

    /**
     * Converts a board to be displayed with the zoom and offset
     * @param rectangle the board
     * @return A rectangle object shaped with the zoom
     */
    private Rectangle convertBoardTomm(Rectangle rectangle) {
        areammBoard.clear();
        areammBoard.add((rectangle.x + offsetX) * zoom);
        areammBoard.add((rectangle.x + offsetX) * zoom + (rectangle.width * zoom));
        areammBoard.add((((-1 * (rectangle.y - wH)) - ((offsetY + rectangle.height) * zoom))));
        areammBoard.add((((-1 * (rectangle.y - wH)) - ((offsetY + rectangle.height) * zoom))) + (rectangle.height * zoom));
        return new Rectangle(areammBoard.get(0).intValue(), areammBoard.get(2).intValue(), (int) (rectangle.width * zoom), (int) (rectangle.height * zoom));
    }

}

