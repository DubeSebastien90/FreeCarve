package UI.SubWindows;

import UI.LeftBar;
import UI.MainWindow;
import UI.Widgets.PersoPoint;
import Domain.Grid;
import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The {@code Rendering2DWindow} class is used to construct and display a board which represent the panel on the CNC. This
 * class also manage how the board is perceived. This includes the zoom, and the display of the mouse position relative to the board.
 *
 * @author Sébastien Dubé & Adam Côté
 * @version 1.1
 * @since 2024-10-22
 */
public class Rendering2DWindow extends JPanel {
    private final Rectangle board = new Rectangle(0, 0, 200, 100);
    private Point mousePt = new Point(0, 0);
    private final Point mmMousePt = new Point(0, 0);
    private double offsetX = 100;
    private double offsetY = 100;
    private double zoom = 1;
    private double prevZoom;
    private int wW;
    private int wH;
    private MainWindow mainWindow;
    ArrayList<Double> areammBoard = new ArrayList<>();
    private boolean draggingAPoint = false;
    private final ArrayList<PersoPoint> points = new ArrayList<>();
    private MouseMotionListener scaleListener;

    /**
     * Constructs a new {@code Rendering2dWindow} object and set the different listener useful for the zooming and more.
     */
    public Rendering2DWindow(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        zoom = 1;
        mousePt = new Point(0, 0);
        offsetY = 100;
        offsetX = 100;
        mainWindow.getController().putGrid(7, 10);
        addMouseListener();
        addMouseMotionListener();
        addMouseWheelListener();
        addComponentListener();
        initScalePointMouseListener();
    }

    /**
     * @return The points displayed on the board.
     */
    public ArrayList<PersoPoint> getPoints() {
        return points;
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
                if (isPointonPanel() && MainWindow.INSTANCE.getLeftBar().getToolBar().getTool(LeftBar.ToolBar.Tool.SCALE).isEnabled()) {
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
                    offsetX -= ((mousePt.x - e.getPoint().x) / zoom);
                    offsetY += ((mousePt.y - e.getPoint().y) / zoom);
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
                    mmMousePt.setLocation((mousePt.x - (offsetX * zoom)) / zoom, ((-1 * (mousePt.y - wH)) - (offsetY * zoom)) / zoom);
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
            offsetX = (mousePt.x - (mmMousePt.x * zoom)) / zoom;
            offsetY = ((-1 * (mousePt.y - wH)) - (mmMousePt.y * zoom)) / zoom;
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
                offsetX = 100;
                offsetY = 100;
                zoom = 1;
                repaint();
            }
        });
    }

    /**
     * Paint the component on this Rendering2DWindow.
     *
     * @param graphics A graphics object which is painted on this panel.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(UIManager.getColor("SubWindow.background"));
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);
        drawRectangle(graphics2D);
        drawGrid(graphics2D);
        drawMousePos(graphics2D);
        drawPoints(graphics2D);
    }


    /**
     * Draws the board on the screen
     *
     * @param graphics2D the <code>Graphics</code> object to protect
     */
    private void drawRectangle(Graphics2D graphics2D) {
        Color color = new Color(222, 184, 135);
        graphics2D.setColor(color);
        Rectangle panneauOffset = convertToPixels(board);
        graphics2D.draw(panneauOffset);
        graphics2D.fill(panneauOffset);
    }

    /**
     * Draws the points on this JPanel.
     *
     * @param graphics2D A graphics object which is painted on the JPanel.
     */
    private void drawPoints(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        for (PersoPoint point : points) {
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
    private void drawMousePos(Graphics2D graphics2D) {
        if (isPointonPanel()) {
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(mmMousePt.x + ", " + mmMousePt.y, 20, 20);
        }
    }

    /**
     * Draws the grid on the board
     *
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


    /**
     * @return True if the mouse is on the board.
     */
    private boolean isPointonPanel() {
        return mmMousePt.x >= 0 && mmMousePt.y >= 0 && mmMousePt.x <= board.width && mmMousePt.y <= board.height;
    }

    /**
     * Changes the width and the height of the board
     *
     * @param newWidth  The new width.
     * @param newHeight The new height.
     */
    private void resizePanneau(int newWidth, int newHeight) {
        board.setSize(newWidth, newHeight);
        MainWindow.INSTANCE.getController().resizePanel(board.width, board.height);
    }

    /**
     * Changes the size of the board using a delta to subtract.
     *
     * @param deltaWidth  The width difference.
     * @param deltaHeight The height difference.
     */
    private void deltaResizePanneau(int deltaWidth, int deltaHeight) {
        board.setSize(((int) board.getWidth()) - deltaWidth, ((int) board.getHeight()) - deltaHeight);
        MainWindow.INSTANCE.getController().resizePanel(board.width, board.height);
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
     * Initializes the points and behavior for resizing the board.
     * This includes setting up the necessary components and defining how
     * the board responds to resize events.
     */
    public void scale() {
        double radius = 5;
        double locationX = (offsetX + board.x + board.width) * zoom - radius / 2;
        double locationY = getHeight() - (board.y + offsetY + board.height) * zoom - radius / 2;
        PersoPoint p = new PersoPoint(locationX, locationY, radius, false);
        PersoPoint p1 = new PersoPoint(locationX - board.width * zoom, locationY, radius, true);
        PersoPoint p2 = new PersoPoint(locationX, locationY + board.height * zoom, radius, true);
        points.add(p1);
        points.add(p);
        points.add(p2);
        activateScaleListener();
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
    public boolean mouseOnSomething(MouseEvent e) {
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
    private boolean isPointClose(MouseEvent e, PersoPoint point) {
        double dx = e.getX() - point.getLocationX();
        double dy = e.getY() - point.getLocationY();
        return Math.sqrt(dx * dx + dy * dy) < 10;
    }

    /**
     * Sets up a mouse motion listener to handle resizing the board when a
     * point is being dragged.
     */
    private void initScalePointMouseListener() {
        scaleListener = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingAPoint) {
                    super.mouseDragged(e);
                    PersoPoint p = points.get(1);
                    double ratio = 1 / zoom;
                    double newWidth = Math.max((e.getX() - offsetX * zoom) * ratio + p.getRadius() / 2, 0);
                    double newHeight = Math.max((getHeight() - e.getY() - offsetY * zoom) * ratio - p.getRadius() / 2, 0);
                    points.get(0).movePoint(offsetX * zoom - p.getRadius() / 2, p.getLocationY());
                    points.get(2).movePoint(p.getLocationX(), getHeight() - offsetY * zoom - p.getRadius() / 2);
                    resizePanneau((int) newWidth, (int) newHeight);
                    repaint();
                }
            }
        };
    }

    /**
     * Activates the scaleListener so the board will change size when the points are dragged
     */
    private void activateScaleListener() {
        addMouseMotionListener(scaleListener);
    }

    /**
     * Deactivate the scaleListener so the board won't resize when points are dragged.
     */
    private void clearPoints() {
        points.clear();
        removeMouseMotionListener(scaleListener);
    }

    /**
     * Function that returns the magnetised position of the mouse based on the grid
     *
     * @param mousePt The mouse coordinates point in pixel
     * @return the mouse coordinates if it's not close enough to an intersection, the intersection coordinates in pixels if the mouse is close enough
     */
    public Point getMagnetisedPos(Point mousePt) {
        Grid grid = mainWindow.getController().getGrid();
        for (double i = areammBoard.get(0); i < areammBoard.get(1); i += grid.getSize() * zoom) {
            if (Math.abs((mousePt.x - i)) <= grid.getMagnetPrecision()) {
                for (double j = areammBoard.get(3); j > areammBoard.get(2); j -= grid.getSize() * zoom) {
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
     *
     * @param mmPt The point in mm
     * @return The point in pixel
     */
    public Point mmTopixel(Point mmPt) {
        return new Point((int) ((mmPt.x * zoom) + (offsetX * zoom)), (int) ((-1 * ((mmPt.y * zoom) + (offsetY * zoom))) + wH));
    }

    /**
     * Converts a point in pixels into milimeters
     *
     * @param pixelPt The point in pixel
     * @return The point in mm
     */
    public Point pixelTomm(Point pixelPt) {
        return new Point((int) ((pixelPt.x - (offsetX * zoom)) / zoom), (int) (((-1 * (pixelPt.y - wH)) - (offsetY * zoom)) / zoom));
    }

    /**
     * Converts a board to be displayed with the zoom and offset
     *
     * @param rectangle the board
     * @return A rectangle object shaped with the zoom
     */
    private Rectangle convertToPixels(Rectangle rectangle) {
        areammBoard.clear();
        areammBoard.add((rectangle.x + offsetX) * zoom);
        areammBoard.add((rectangle.x + offsetX) * zoom + (rectangle.width * zoom));
        areammBoard.add((((-1 * (rectangle.y - wH)) - ((offsetY + rectangle.height) * zoom))));
        areammBoard.add((((-1 * (rectangle.y - wH)) - ((offsetY + rectangle.height) * zoom))) + (rectangle.height * zoom));
        return new Rectangle(areammBoard.get(0).intValue(), areammBoard.get(2).intValue(), (int) (rectangle.width * zoom), (int) (rectangle.height * zoom));
    }

}
