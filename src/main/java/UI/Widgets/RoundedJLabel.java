package UI.Widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * The {@code RoundedJLabel} class is a normal JLabel that appear with rounded corner on screen
 */
public class RoundedJLabel extends JLabel {
    private final int cornerRadius;

    public RoundedJLabel(String text, int radius) {
        super(text);
        this.cornerRadius = radius;
        setOpaque(false);
    }

    /**
     * Paints the JLabel using. To make the round corners, the JLabel is drawn inside a {@code RoundRectangle} object.
     *
     * @param g A graphics object which is painted on this instance.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        super.paintComponent(g);
    }
}
