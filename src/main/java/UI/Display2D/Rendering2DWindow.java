package UI.Display2D;

import Domain.CutType;

import Domain.GridDTO;
import UI.Events.ChangeAttributeListener;
import UI.Events.ChangeCutListener;
import UI.LeftBar;
import UI.MainWindow;
import UI.Widgets.PersoPoint;
import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * The {@code Rendering2DWindow} class is used to construct and display a board which represent the panel on the CNC. This
 * class also manage how the board is perceived. This includes the zoom, and the display of the mouse position relative to the board.
 *
 * @author Sébastien Dubé & Adam Côté
 * @version 1.2
 * @since 2024-10-22
 */
public class Rendering2DWindow extends JPanel {
    private final Rectangle2D board = new Rectangle2D.Double(0, 0, 1219.2, 914.4);
    private Point2D mousePt = new Point2D.Double(0, 0);
    private final Point2D fakeMousePt = new Point2D.Double(0, 0);
    private double offsetX = 100;
    private double offsetY = 100;
    private double zoom = 1;
    private Point2D mmMousePt;
    private double prevZoom;
    private int wW;
    private int wH;
    private ChangeCutListener changeCutListener;
    private final ChangeAttributeListener changeAttributeListener;
    private final MainWindow mainWindow;
    ArrayList<Double> areammBoard = new ArrayList<>();
    private boolean draggingAPoint = false;
    private final ArrayList<PersoPoint> points = new ArrayList<>();
    private final Afficheur afficheur;
    private final Scaling scaling;
    private final Drawing drawing;

    /**
     * Constructor for Renderinf2DWIndow
     *
     * @param mainWindow The main window to get the controller
     */

    public Rendering2DWindow(MainWindow mainWindow, ChangeAttributeListener changeAttributeListener, ChangeCutListener changeCutListener) {
        super();
        this.mainWindow = mainWindow;
        this.changeCutListener = changeCutListener;
        this.changeAttributeListener = changeAttributeListener;
        zoom = 1;
        mousePt = new Point(0, 0);
        mmMousePt = new Point2D.Double(0, 0);
        offsetY = 100;
        offsetX = 100;
        mainWindow.getController().putGrid(100, 10);
        addMouseListener();
        addMouseMotionListener();
        addMouseWheelListener();
        addComponentListener();
        afficheur = new Afficheur(this);
        scaling = new Scaling(this);
        drawing = new Drawing(this, mainWindow);
        drawing.updateCuts();
    }


    public Point2D.Double getMmMousePt() {
        return (Point2D.Double) mmMousePt;
    }

    /**
     * Returns the listener responsible for handling attribute changes.
     *
     * @return the ChangeAttributeListener instance.
     */
    public ChangeAttributeListener getAttributeListener() {
        return changeAttributeListener;
    }

    public ChangeCutListener getChangeCutListener(){
        return this.changeCutListener;
    }

    /**
     * Returns the board area as a 2D rectangle.
     *
     * @return the Rectangle2D representing the board area.
     */
    public Rectangle2D getBoard() {
        return this.board;
    }

    /**
     * Returns the coordinates of the simulated mouse point.
     *
     * @return the Point2D representing the fake mouse point.
     */
    public Point2D getFakeMousePt() {
        return this.fakeMousePt;
    }

    /**
     * Returns the coordinates of the actual mouse point.
     *
     * @return the Point2D representing the mouse point.
     */
    public Point2D getMousePt() {
        return mousePt;
    }

    /**
     * Returns the X-axis offset for positioning.
     *
     * @return the X-axis offset as a double.
     */
    public double getOffsetX() {
        return offsetX;
    }

    /**
     * Returns the Y-axis offset for positioning.
     *
     * @return the Y-axis offset as a double.
     */
    public double getOffsetY() {
        return offsetY;
    }

    /**
     * Returns the current zoom level.
     *
     * @return the current zoom level as a double.
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Returns the previous zoom level before the last change.
     *
     * @return the previous zoom level as a double.
     */
    public double getPrevZoom() {
        return prevZoom;
    }

    /**
     * Returns the width of the window or canvas in pixels.
     *
     * @return the window width as an integer.
     */
    public int getwW() {
        return wW;
    }

    /**
     * Returns the height of the window or canvas in pixels.
     *
     * @return the window height as an integer.
     */
    public int getwH() {
        return wH;
    }

    /**
     * Indicates whether a point is currently being dragged.
     *
     * @return true if a point is being dragged, false otherwise.
     */
    public boolean isDraggingAPoint() {
        return draggingAPoint;
    }

    /**
     * Returns the display object responsible for visual rendering.
     *
     * @return the Afficheur instance.
     */
    public Afficheur getAfficheur() {
        return afficheur;
    }


    /**
     * @return The points displayed on the board.
     */
    public ArrayList<PersoPoint> getPoints() {
        return points;
    }

    /**
     * Update the cuts of the Drawing class
     */
    public void updateCuts(){
        this.drawing.updateCuts();
    }

    /**
     * Sets the dragging a point attribute to a new value.
     *
     * @param draggingAPoint The new boolean value.
     */
    public void setDraggingAPoint(boolean draggingAPoint) {
        this.draggingAPoint = draggingAPoint;
    }

    /**
     * Initiates the basic mouse listener for when the mouse is pressed.
     */
    private void addMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                super.mousePressed(e);
                if (!mouseOnSomething(e)) {
                    clearPoints();
                    repaint();
                } else {
                    for (PersoPoint point : points) {
                        if (isPointClose(e, point) && !point.getFilled()) {
                            draggingAPoint = true;
                        }
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPointonPanel() && mainWindow.getLeftBar().getToolBar().getTool(LeftBar.ToolBar.Tool.SCALE).isEnabled()) {
                    scale();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggingAPoint) {
                    super.mouseReleased(e);
                    draggingAPoint = false;
                    clearPoints();
                    repaint();
                }
            }
        });
    }

    /**
     * Instantiate and manage the mouse movement related action.
     * When the mouse is dragged or simply moved different listener defined in this function are called.
     */
    private void addMouseMotionListener() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!draggingAPoint) {
                    offsetX -= ((mousePt.getX() - e.getPoint().x) / zoom);
                    offsetY += ((mousePt.getY() - e.getPoint().y) / zoom);
                    mousePt = e.getPoint();
                    points.clear();
                    repaint();
                } else {
                    super.mouseDragged(e);
                    for (PersoPoint point : points) {
                        point.movePoint(Math.max(offsetX * zoom, e.getX()), Math.min(getHeight() - offsetY * zoom, e.getY()));
                        repaint();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (!draggingAPoint) {
                    mousePt = e.getPoint();
                    mmMousePt.setLocation((mousePt.getX() - (offsetX * zoom)) / zoom, ((-1 * (mousePt.getY() - wH)) - (offsetY * zoom)) / zoom);
                    repaint();
                }
            }

        });
    }

    /**
     * Instantiates a listener for the mouse wheel. This is used to manage the zoom property of the board.
     */
    private void addMouseWheelListener() {
        addMouseWheelListener(e -> {
            double zoomFactor = ((double) 25 / Math.signum(e.getWheelRotation()));
            zoom -= zoom / zoomFactor;
            offsetX = (mousePt.getX() - (mmMousePt.getX() * zoom)) / zoom;
            offsetY = ((-1 * (mousePt.getY() - wH)) - (mmMousePt.getY() * zoom)) / zoom;
            points.clear();
            repaint();
        });
    }

    /**
     * Instantiate a global listener for all component in the {@code Rendering2DWindow}. It reinitialises the view of the board when a component is resized.
     */
    private void addComponentListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                wW = getWidth();
                wH = getHeight();
                repaint();
            }
        });
    }

    /**
     * Function executed when rendering the screen
     *
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(UIManager.getColor("SubWindow.background"));
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);
        afficheur.drawRectangle(graphics2D);
        afficheur.drawMousePos(graphics2D);
        afficheur.drawPoints(graphics2D);
        afficheur.drawGrid(graphics2D);
        afficheur.drawCuts(graphics2D, this, drawing);
    }


    /**
     * @return True if the mouse is on the board.
     */
    boolean isPointonPanel() {
        return fakeMousePt.getX() >= 0 && fakeMousePt.getY() >= 0 && fakeMousePt.getX() <= board.getWidth() && fakeMousePt.getY() <= board.getHeight();
    }

    /**
     * Changes the width and the height of the board
     *
     * @param newWidth  The new width.
     * @param newHeight The new height.
     */
    public void resizePanneau(double newWidth, double newHeight) {
        board.setRect(board.getX(), board.getY(), newWidth, newHeight);
        mainWindow.getController().resizePanel(board.getWidth(), board.getHeight());
        repaint();
    }

    /**
     * Changes the size of the board using a delta to subtract.
     *
     * @param deltaWidth  The width difference.
     * @param deltaHeight The height difference.
     */
    private void deltaResizePanneau(double deltaWidth, double deltaHeight) {
        board.setRect(board.getX(), board.getY(), board.getWidth() - deltaWidth, board.getHeight() - deltaHeight);
        mainWindow.getController().resizePanel(board.getWidth(), board.getHeight());
        repaint();
    }

    /**
     * Function that returns the magnetised position of the mouse based on the grid
     *
     * @param mousePt The mouse coordinates point in pixel
     * @return the mouse coordinates if it's not close enough to an intersection, the intersection coordinates in pixels if the mouse is close enough
     */
    public Point2D getMagnetisedPos(Point2D mousePt) {
        GridDTO grid = mainWindow.getController().getGrid();
        for (double i = areammBoard.get(0); i < areammBoard.get(1); i += grid.getSize() * zoom) {
            if (Math.abs((mousePt.getX() - i)) <= grid.getMagnetPrecision()) {
                for (double j = areammBoard.get(3); j > areammBoard.get(2); j -= grid.getSize() * zoom) {
                    if (Point.distance(mousePt.getX(), mousePt.getY(), i, j) <= grid.getMagnetPrecision()) {
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
     *
     * @param mmPt The point in mm
     * @return The point in pixel
     */
    public Point2D mmTopixel(Point2D mmPt) {
        return new Point2D.Double(((mmPt.getX() * zoom) + (offsetX * zoom)), ((-1 * ((mmPt.getY() * zoom) + (offsetY * zoom))) + wH));
    }

    /**
     * Scales a mm measure into pixel according to the zoom
     * @param mm measure
     * @return pixel measure with zoom
     */
    public double scaleMMToPixel(double mm){
        return mm / zoom;
    }

    /**
     * Converts a point in pixels into milimeters, the Point's value are integers
     *
     * @param pixelPt The point in pixel
     * @return The point in double mm
     */
    public Point2D pixelTomm(Point2D pixelPt) {
        return new Point2D.Double(((pixelPt.getX() - (offsetX * zoom)) / zoom), (((-1 * (pixelPt.getY() - wH)) - (offsetY * zoom)) / zoom));
    }

    /**
     * Converts a board to be displayed with the zoom and offset
     *
     * @param rectangle the board
     * @return A rectangle object shaped with the zoom
     */
    public Rectangle2D convertBoardTomm(Rectangle2D rectangle) {
        areammBoard.clear();
        areammBoard.add((rectangle.getX() + offsetX) * zoom);
        areammBoard.add((rectangle.getX() + offsetX) * zoom + (rectangle.getWidth() * zoom));
        areammBoard.add((((-1 * (rectangle.getY() - wH)) - ((offsetY + rectangle.getHeight()) * zoom))));
        areammBoard.add((((-1 * (rectangle.getY() - wH)) - ((offsetY + rectangle.getHeight()) * zoom))) + (rectangle.getHeight() * zoom));
        return new Rectangle2D.Double(areammBoard.get(0).intValue(), areammBoard.get(2).intValue(), (rectangle.getWidth() * zoom), (rectangle.getHeight() * zoom));
    }

    /**
     * Zooms to the center of this panel.
     *
     * @param zoomFactor The zooming delta. A negative one will make the board seems bigger.
     */
    public void zoomOrigin(double zoomFactor) {
        points.clear();
        zoom -= zoom / zoomFactor;
        repaint();
    }

    /**
     * Determines whether the mouse cursor is currently positioned over
     * the board or a any other component.
     *
     * @param e The mouse event that triggered this check, providing details
     *          about the current mouse position and state.
     * @return {@code true} if the mouse is over the board or another component; {@code false} otherwise.
     */
    boolean mouseOnSomething(MouseEvent e) {
        if (isPointonPanel()) {
            return true;
        }
        for (PersoPoint point : points) {
            if (isPointClose(e, point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given mouse event is close to a specified point.
     *
     * @param e     The mouse event that contains the current mouse position.
     * @param point The point to check against.
     * @return {@code true} if the mouse event's position is within a threshold
     * distance of the point; {@code false} otherwise.
     */
    boolean isPointClose(MouseEvent e, PersoPoint point) {
        double dx = e.getX() - point.getLocationX();
        double dy = e.getY() - point.getLocationY();
        return Math.sqrt(dx * dx + dy * dy) < 10;
    }

    /**
     * Initializes the points and behavior for resizing the board.
     * This includes setting up the necessary components and defining how
     * the board responds to resize events.
     */
    public void scale() {
        scaling.initiateScaling();
        activateScaleListener();
        repaint();
    }

    /**
     * Initialize the cut behaviours by setting up the Drawing class
     * @param type type of the cut initialized
     */
    public void cut(CutType type){
        drawing.initCut(type);
        repaint();
    }

    /**
     * Activates the scaleListener so the board will change size when the points are dragged
     */
    private void activateScaleListener() {
        addMouseMotionListener(scaling.getScaleListener());
    }

    /**
     * Deactivate the scaleListener so the board won't resize when points are dragged.
     */
    private void clearPoints() {
        points.clear();
        removeMouseMotionListener(scaling.getScaleListener());
    }

    /**
     * Getter for main window
     * @return mainWindow
     */
    public MainWindow getMainWindow(){
        return mainWindow;
    }

    /**
     * Getter for areammBoard
     * @return areammBoard
     */
    public ArrayList<Double> getAreammBoard(){
        return areammBoard;
    }


}

