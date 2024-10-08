package UI.SubWindows;

import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Rendering2DWindow extends JPanel {
    private Rectangle panneau = new Rectangle(0, 0, 200, 100);
    private Point mousePt;
    private Point fakeMousePt;
    private double offsetX;
    private double offsetY;
    private double zoom;
    private double prevZoom;
    private int wW;
    private int wH;

    public Rendering2DWindow() {
        super();
        zoom = 1;
        mousePt = new Point(0, 0);
        fakeMousePt = new Point(0, 0);
        offsetY = 100;
        offsetX = 100;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                System.out.println(zoom);
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
                fakeMousePt.setLocation((mousePt.x - (offsetX*zoom))/zoom, ((-1 * (mousePt.y - wH)) - offsetY*zoom)/zoom);
                repaint();
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                double zoomFactor = ((double) 15 / e.getWheelRotation());
                double zoomDiv = zoomFactor / zoom;
                if (e.getWheelRotation() > 0) {
                    zoom -= zoom / zoomFactor;
                }
                if (e.getWheelRotation() < 0) {
                    zoom -= zoom / zoomFactor;
                }
                repaint();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                wW = getWidth();
                wH = getHeight();
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(UIManager.getColor("SubWindow.background"));
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);
        drawRectangle(graphics2D);
        drawMousePos(graphics2D);
    }


    private void drawRectangle(Graphics2D graphics2D) {
        Color color = new Color(222, 184, 135);
        graphics2D.setColor(color);
        Rectangle panneauOffset = selonContexte(panneau);
        graphics2D.draw(panneauOffset);
        graphics2D.fill(panneauOffset);
    }

    private void drawMousePos(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(fakeMousePt.x + ", " + fakeMousePt.y, mousePt.x, mousePt.y);
    }


    private void resizePanneau(int newWidth, int newHeight) {
        panneau.setSize(newWidth, newHeight);
    }

    private void deltaResizePanneau(int deltaWidth, int deltaHeight) {
        panneau.setSize(((int) panneau.getWidth()) - deltaWidth, ((int) panneau.getHeight()) - deltaHeight);
    }

    private Rectangle selonContexte(Rectangle panneau) {
        return new Rectangle((int) ((panneau.x + offsetX) * zoom), (int) (((-1 * (panneau.y - wH)) - ((offsetY + panneau.height)*zoom))), (int) (panneau.width * zoom), (int) (panneau.height * zoom));
    }

}
