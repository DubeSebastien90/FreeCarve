package UI.SubWindows;

import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Rendering2DWindow extends JPanel {
    private Rectangle panneau = new Rectangle(100, 100, 500, 300);
    private Point mousePt;
    private int offsetX;
    private int offsetY;

    public Rendering2DWindow() {
        super();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //paintComponent(getGraphics());
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                offsetX -= mousePt.x - e.getPoint().x;
                offsetY -= mousePt.y - e.getPoint().y;
                mousePt = e.getPoint();
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
        Rectangle panneauOffset = selonContexte(panneau,offsetX,offsetY);
        graphics2D.draw(panneauOffset);
        graphics2D.fill(panneauOffset);
    }


    private void resizePanneau(int newWidth, int newHeight) {
        panneau.setSize(newWidth, newHeight);
    }

    private void deltaResizePanneau(int deltaWidth, int deltaHeight) {
        panneau.setSize(((int) panneau.getWidth()) - deltaWidth, ((int) panneau.getHeight()) - deltaHeight);
    }

    static private Rectangle selonContexte(Rectangle panneau, int offsetX, int offsetY) {
        return new Rectangle(panneau.x+offsetX,panneau.y+offsetY,panneau.width,panneau.height);
    }

}
