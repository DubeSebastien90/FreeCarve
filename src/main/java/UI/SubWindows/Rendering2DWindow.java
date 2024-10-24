package UI.SubWindows;

import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The {@code Rendering2DWindow} class is used to construct and display a board which represent the panel on the CNC. This
 * class also manage how the board is perceived. This includes the zoom, and the display of the mouse position relative to the board.
 *
 * @author Sébastien Dubé
 * @version 1.1
 * @since 2024-10-22
 */
public class Rendering2DWindow extends JPanel {
    private final Rectangle board = new Rectangle(0, 0, 200, 100);
    private Point mousePt = new Point(0, 0);
    private final Point fakeMousePt = new Point(0, 0);
    private double offsetX = 100;
    private double offsetY = 100;
    private double zoom = 1;
    private double prevZoom;
    private int wW;
    private int wH;

    /**
     * Constructs a new {@code Rendering2dWindow} object and set the different listener useful for the zooming and more.
     */
    public Rendering2DWindow() {
        super();
        addMouseListener();
        addMouseMotionListener();
        addMouseWheelListener();
        addComponentListener();
    }

    /**
     * Initiates the basic mouse listener for when the mouse is pressed.
     */
    private void addMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                System.out.println(zoom);
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
                offsetX -= ((mousePt.x - e.getPoint().x) / zoom);
                offsetY += ((mousePt.y - e.getPoint().y) / zoom);
                mousePt = e.getPoint();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePt = e.getPoint();
                fakeMousePt.setLocation((mousePt.x - (offsetX * zoom)) / zoom, ((-1 * (mousePt.y - wH)) - (offsetY * zoom)) / zoom);
                repaint();
            }
        });
    }

    /**
     * Instantiates a listener for the mouse wheel. This is used to manage the zoom property of the board.
     */
    private void addMouseWheelListener() {
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                double zoomFactor = ((double) 25 / Math.signum(e.getWheelRotation()));
                zoom -= zoom / zoomFactor;
                offsetX = (mousePt.x - (fakeMousePt.x * zoom)) / zoom;
                offsetY = ((-1 * (mousePt.y - wH)) - (fakeMousePt.y * zoom)) / zoom;
                repaint();
            }
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
        drawMousePos(graphics2D);
    }


    /**
     * Draws the board on this JPanel.
     *
     * @param graphics2D A graphics object which is painted on this JPanel.
     */
    private void drawRectangle(Graphics2D graphics2D) {
        Color color = new Color(222, 184, 135);
        graphics2D.setColor(color);
        Rectangle panneauOffset = convertTomm(board);
        graphics2D.draw(panneauOffset);
        graphics2D.fill(panneauOffset);
    }

    /**
     * Draws the mouse coordinates in the top left corner of the screen.
     *
     * @param graphics2D A graphics object which is painted on this JPanel
     */
    private void drawMousePos(Graphics2D graphics2D) {
        if (isPointonPanel()) {
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(fakeMousePt.x + ", " + fakeMousePt.y, 20, 20);
        }
    }


    /**
     * @return True if the mouse is on the board.
     */
    private boolean isPointonPanel() {
        return fakeMousePt.x >= 0 && fakeMousePt.y >= 0 && fakeMousePt.x <= board.width && fakeMousePt.y <= board.height;
    }

    /**
     * Changes the width and the height of the board
     *
     * @param newWidth  The new width.
     * @param newHeight The new height.
     */
    private void resizePanneau(int newWidth, int newHeight) {
        board.setSize(newWidth, newHeight);
    }

    /**
     * Changes the size of the board using a delta to subtract.
     *
     * @param deltaWidth  The width difference.
     * @param deltaHeight The height difference.
     */
    private void deltaResizePanneau(int deltaWidth, int deltaHeight) {
        board.setSize(((int) board.getWidth()) - deltaWidth, ((int) board.getHeight()) - deltaHeight);
    }


    private Rectangle convertTomm(Rectangle rectangle) {
        return new Rectangle((int) ((rectangle.x + offsetX) * zoom), (int) (((-1 * (rectangle.y - wH)) - ((offsetY + rectangle.height) * zoom))), (int) (rectangle.width * zoom), (int) (rectangle.height * zoom));
    }

    /**
     * Zooms to the center of this panel.
     *
     * @param zoomFactor The zooming delta. A negative one will make the board seems bigger.
     */
    public void zoomOrigin(double zoomFactor) {
        zoom -= zoom / zoomFactor;
        repaint();
    }

}
