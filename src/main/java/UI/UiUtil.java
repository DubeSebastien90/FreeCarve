package UI;

import Domain.CutType;
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
        FlatSVGIcon icon = getIcon(iconName, size, buttonColor);
        JButton button = new JButton(icon);
        button.setBorder(new FlatButtonBorder());
        button.setEnabled(enable);
        return button;
    }


    /**
     * Get an icon with an icon name
     * @param iconName naem of the icon
     * @param size size of the icon
     * @param iconColor color of the icon
     * @return FlatSVGIcon
     */
    public static FlatSVGIcon getIcon(String iconName, int size, Color iconColor){
        FlatSVGIcon icon = new FlatSVGIcon("UI/" + iconName + ".svg", size, size);
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(color -> iconColor);
        icon.setColorFilter(filter);
        return icon;
    }

    /**
     * Draws a RoundRect of a JPanel
     * @param panel JPanel that wants to be drawned
     * @param graphics2D reference to the graphics2d object
     */
    public static void makeJPanelRoundCorner(JPanel panel, Graphics2D graphics2D) {
        panel.setOpaque(false);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(panel.getBackground());
        graphics2D.fillRoundRect(0, 0, panel.getWidth(), panel.getHeight(), 20, 20);
    }

    /**
     * Draws a RoundRect but extends the height in order to hide the rounded bottom
     * @param panel JPanel that wants to be drawned
     * @param graphics2D reference to the graphics2d object
     */
    public static void makeJPanelHeaderCorner(JPanel panel, Graphics2D graphics2D) {
        panel.setOpaque(false);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(panel.getBackground());
        graphics2D.fillRoundRect(0, 0, panel.getWidth(), panel.getHeight() + 30, 20, 20);
    }

    /**
     * Get the name of the icon file of the CutType based on the enum value
     * @param type type of the Cut
     * @return the filename of the icon
     */
    public static String getIconFileName(CutType type){
            return switch (type) {
                case BORDER -> "forbidden";
                case L_SHAPE -> "coupeL";
                case RECTANGULAR -> "rectangle";
                case LINE_HORIZONTAL -> "horizontal";
                case LINE_VERTICAL -> "vertical";
                case LINE_FREE -> "modify";
                default -> "forbidden"; // default in case of bad name of icon
            };
    }

    /**
     * Get the French name of the CutType based on the enum value
     * @param type type of the Cut
     * @return the name of the Cut
     */
    public static  String getIconName(CutType type){
        String iconName = "";
        switch(type){
            case BORDER -> iconName = "Interdit";
            case L_SHAPE -> iconName = "Coupe en L";
            case RECTANGULAR -> iconName = "Rectangle";
            case LINE_HORIZONTAL -> iconName = "Ligne horizontale";
            case LINE_VERTICAL -> iconName = "Ligne verticale";
            case LINE_FREE -> iconName = "Coupe droite libre";
            default -> iconName = "Interdit"; // default in case of bad name of icon
        }
        return iconName;
    }
}
