package UI.SubWindows;

import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Rendering2DWindow extends JPanel {
    private Rectangle panneau = new Rectangle(100, 100, 500, 300);
    private Point mousePt;
    private double offsetX;
    private double offsetY;
    private double zoom;

    public Rendering2DWindow() {
        super();
        zoom = 1;
        offsetX = 0;
        offsetY = 0;
        mousePt = new Point(0, 0);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                offsetX -= ((mousePt.x - e.getPoint().x)/zoom);
                offsetY -= ((mousePt.y - e.getPoint().y)/zoom);
                mousePt = e.getPoint();
                repaint();
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    zoom -= zoom/((double) 15 /e.getWheelRotation());
                }
                if (e.getWheelRotation() < 0) {
                    zoom -= zoom/((double) 15 /e.getWheelRotation());
                }
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
    }

    private void drawRectangle(Graphics2D graphics2D) {
        Color color = new Color(222, 184, 135);
        graphics2D.setColor(color);
        Rectangle panneauOffset = selonContexte(panneau,offsetX,offsetY, zoom);
        graphics2D.draw(panneauOffset);
        graphics2D.fill(panneauOffset);
    }


    private void resizePanneau(int newWidth, int newHeight) {
        panneau.setSize(newWidth, newHeight);
    }

    private void deltaResizePanneau(int deltaWidth, int deltaHeight) {
        panneau.setSize(((int) panneau.getWidth()) - deltaWidth, ((int) panneau.getHeight()) - deltaHeight);
    }

    static private Rectangle selonContexte(Rectangle panneau, double offsetX, double offsetY, double zoom) {
        return new Rectangle((int)((panneau.x+offsetX)*zoom),(int)((panneau.y+offsetY)*zoom),(int) (panneau.width*zoom),(int) (panneau.height*zoom));
    }

}
