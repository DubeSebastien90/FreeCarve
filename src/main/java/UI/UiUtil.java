package UI;

import Common.DTO.VertexDTO;
import Common.Units;
import Domain.CutType;
import UI.Display2D.Rendering2DWindow;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatButtonBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;

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
                case RETAILLER -> "retailler";
                default -> "forbidden"; // default in case of bad name of icon
            };
    }

    /**
     * Get the French name of the CutType based on the enum value
     * @param type type of the Cut
     * @return the name of the Cut
     */
    public static String getIconName(CutType type){
        String iconName = "";
        switch(type){
            case BORDER -> iconName = "Interdit";
            case L_SHAPE -> iconName = "Coupe en L";
            case RECTANGULAR -> iconName = "Rectangle";
            case LINE_HORIZONTAL -> iconName = "Ligne horizontale";
            case LINE_VERTICAL -> iconName = "Ligne verticale";
            case LINE_FREE -> iconName = "Coupe droite libre";
            case RETAILLER -> iconName = "Bordure";
            default -> iconName = "Interdit"; // default in case of bad name of icon
        }
        return iconName;
    }

    public static String getUnitSymbole(Units units){
        String unitSymbol = "";
        switch (units){
            case Units.M -> unitSymbol = "m";
            case Units.MM -> unitSymbol = "mm";
            case Units.CM -> unitSymbol = "cm";
            case Units.FEET -> unitSymbol = "'";
            case Units.INCH -> unitSymbol = "\"";
        }
        return unitSymbol;
    }

    public static void drawArrowWidthNumber(Graphics2D g, Rendering2DWindow renderer, VertexDTO p1, VertexDTO p2, double value, Color arrowColor, int arrowDiameter, Color textColor){
        if(arrowDiameter < 0){throw new IllegalArgumentException("The arrow diameter cannot be negative");}

        g.setColor(arrowColor);
        g.setStroke(new BasicStroke(arrowDiameter));

        // Draw arrow
        Point2D temp1 = renderer.mmTopixel(new Point2D.Double(p1.getX(), p1.getY()));
        Point2D temp2 = renderer.mmTopixel(new Point2D.Double(p2.getX(), p2.getY()));
        VertexDTO middlePoint = p1.add(p2).mul(0.5);
        Point2D tempMiddle = renderer.mmTopixel(new Point2D.Double(middlePoint.getX(), middlePoint.getY()));
        g.drawLine((int) temp1.getX(), (int) temp1.getY(), (int) temp2.getX(), (int) temp2.getY());

        int arrowHeadSize = 10;
        double angle = Math.atan2(temp2.getY() - temp1.getY(), temp2.getX()- temp1.getX());

        int x3 = (int) (temp2.getX() - arrowHeadSize * Math.cos(angle - Math.PI / 6));
        int y3 = (int) (temp2.getY() - arrowHeadSize * Math.sin(angle - Math.PI / 6));

        int x4 = (int) (temp2.getX() - arrowHeadSize * Math.cos(angle + Math.PI / 6));
        int y4 = (int) (temp2.getY() - arrowHeadSize * Math.sin(angle + Math.PI / 6));

        g.drawLine((int)temp2.getX(), (int)temp2.getY(), x3, y3);
        g.drawLine((int)temp2.getX(), (int)temp2.getY(), x4, y4);


//         Draw infoBox
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedNumber = df.format(value);
        formattedNumber += getUnitSymbole(UIConfig.INSTANCE.getDefaultUnit().unit);
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(formattedNumber);
        int textHeight = fontMetrics.getHeight();
        double padding = 4;
        double rectWidth = textWidth + 2*padding;
        double rectHeight = textHeight + 2*padding;
        int rectX = (int) (tempMiddle.getX() - rectWidth/2);
        int rectY = (int) (tempMiddle.getY() - rectHeight/2);
        g.fillRect(rectX, rectY, (int) rectWidth, (int) rectHeight);

        int textX = (int) (rectX + (rectWidth - textWidth) / 2);
        int textY = (int) (rectY + (rectHeight - textHeight) /2 + fontMetrics.getAscent());

        g.setColor(textColor);
        g.drawString(formattedNumber, textX, textY);
    }
}
