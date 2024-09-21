package UI.Widgets;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code ColoredBox} class is a child of {@code JPanel} an overrides his
 * {@code paintComponent} method in order to draw custom colored rectangles ]
 * of specific sizes
 */
public class ColoredBox extends JPanel {
    public ColoredBox(Color color, int width, int height){
        super();
        this.setBackground(color);
        this.setMinimumSize(new Dimension(width,height));
        this.setMaximumSize(new Dimension(width,height));
    }
}
