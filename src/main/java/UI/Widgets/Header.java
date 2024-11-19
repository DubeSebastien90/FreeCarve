package UI.Widgets;

import UI.UiUtil;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The {@code Header} class encapsulates the {@code JPanel} header object of the {@code BasicWindow}
 *
 * @author Louis-Etienne Messier
 * @version 1.0
 * @since 2024-10-12
 */
public class Header extends JPanel {

    /**
     * Basic constructor of the {@code Header} that gives it a name
     * @param name name of the {@code Header}
     */
    public Header(String name){
        this.setBackground(UIManager.getColor("SubWindow.header"));
        FlatSVGIcon headerIcon = new FlatSVGIcon("UI/" + "rectangle" + ".svg", 15, 15);
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Button.foreground"));
        headerIcon.setColorFilter(filter);
        JLabel headerLabel = new JLabel(headerIcon);
        headerLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        JLabel headerNameLabel = new JLabel(name);
        headerNameLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        this.add(headerLabel);
        this.add(headerNameLabel);
        this.setBorder(new EmptyBorder(5,5,5,5));
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(boxLayout);
    }

    @Override
    public void paintComponent(Graphics graphics){
        Graphics2D graphics2D = (Graphics2D) graphics;
        UiUtil.makeJPanelHeaderCorner(this, graphics2D);
        super.paintComponent(graphics2D);
    }
}
