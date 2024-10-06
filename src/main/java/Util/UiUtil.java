package Util;

import UI.UIConfig;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatButtonBorder;

import javax.swing.*;
import java.awt.*;

/**
 * This class regroup useful function for the UI of the application
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-09-21
 */
public class UiUtil {

    /**
     * Creates a {@code JButton} with an SVG icon instead of some text.
     *
     * @param iconName       The name of the SVG file, must not contain the path or the extension
     * @param enable         Indicate if the {@code JButton} must be enabled or not.
     * @param tooltipMessage The message that must be displayed when the mouse stops on top of the {@code JButton}
     * @param size           The size of the button
     * @return the newly created {@code JButton}
     */
    public static JButton createSVGButton(String iconName, boolean enable, String tooltipMessage, int size) {
        FlatSVGIcon icon = new FlatSVGIcon("UI/" + iconName + ".svg", size, size);
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Button.foreground"));
        icon.setColorFilter(filter);
        JButton button = new JButton(icon);
        button.setBorder(new FlatButtonBorder());
        button.setEnabled(enable);
        button.setToolTipText(tooltipMessage);
        return button;
    }

    /**
     * Creates a {@code JButton} with an SVG icon instead of some text.
     *
     * @param iconName The name of the SVG file, must not contain the path or the extension
     * @param enable   Indicate if the {@code JButton} must be enabled or not.
     * @param size     The size of the button
     * @return the newly created {@code JButton}
     */
    public static JButton createSVGButton(String iconName, boolean enable, int size) {
        FlatSVGIcon icon = new FlatSVGIcon("UI/" + iconName + ".svg", size, size);
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Button.foreground"));
        icon.setColorFilter(filter);
        JButton button = new JButton(icon);
        button.setBorder(new FlatButtonBorder());
        button.setEnabled(enable);
        return button;
    }

    /**
     * Creates a {@code JButton} with an SVG icon instead of some text.
     *
     * @param iconName    The name of the SVG file, must not contain the path or the extension
     * @param enable      Indicate if the {@code JButton} must be enabled or not.
     * @param size        The size of the button
     * @param buttonColor The color the icon should be.
     * @return the newly created {@code JButton}
     */
    public static JButton createSVGButton(String iconName, boolean enable, int size, Color buttonColor) {
        FlatSVGIcon icon = new FlatSVGIcon("UI/" + iconName + ".svg", size, size);
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(color -> buttonColor);
        icon.setColorFilter(filter);
        JButton button = new JButton(icon);
        button.setBorder(new FlatButtonBorder());
        button.setEnabled(enable);
        return button;
    }

    public static void makeJPanelRoundCorner(JPanel panel, Graphics2D graphics2D) {
        panel.setOpaque(false);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(panel.getBackground());
        graphics2D.fillRoundRect(0, 0, panel.getWidth(), panel.getHeight(), 20, 20);
    }
}
