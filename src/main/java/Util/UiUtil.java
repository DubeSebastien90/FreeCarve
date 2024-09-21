package Util;

import UI.UIConfig;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatButtonBorder;

import javax.swing.*;

public class UiUtil {
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

    public static JButton createSVGButton(String iconName, boolean enable, int size) {
        FlatSVGIcon icon = new FlatSVGIcon("UI/" + iconName + ".svg", size, size);
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Button.foreground"));
        icon.setColorFilter(filter);
        JButton button = new JButton(icon);
        button.setBorder(new FlatButtonBorder());
        button.setEnabled(enable);
        return button;
    }
}
