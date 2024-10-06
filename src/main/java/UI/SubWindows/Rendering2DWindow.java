package UI.SubWindows;

import Util.UiUtil;

import javax.swing.*;
import java.awt.*;

public class Rendering2DWindow extends JPanel {
    private Rectangle panneau = new Rectangle(100, 100, 500, 300);

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
        graphics2D.draw(panneau);
        graphics2D.fill(panneau);
    }


    private void resizePanneau(int newWidth, int newHeight) {
        panneau.setSize(newWidth, newHeight);
    }

    private void deltaResizePanneau(int deltaWidth, int deltaHeight) {
        panneau.setSize(((int) panneau.getWidth()) - deltaWidth, ((int) panneau.getHeight()) - deltaHeight);
    }
}
