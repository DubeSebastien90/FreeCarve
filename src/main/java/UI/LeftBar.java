package UI;

import javax.swing.*;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatButtonBorder;

public class LeftBar extends JToolBar {

    UIConfig uiConfig = UIConfig.INSTANCE;

    public LeftBar() {
        this.setVisible(true);
        init();
    }

    private void init() {
        this.add(createSVGBUtton("save"));
        JButton undo = new JButton("undo");
        JButton redo = new JButton("redo");
    }

    private JButton createSVGBUtton(String iconName) {
        FlatSVGIcon icon = new FlatSVGIcon("UI/" + iconName + ".svg", uiConfig.getToolIconSize(), uiConfig.getToolIconSize());
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Button.foreground"));
        icon.setColorFilter(filter);
        JButton button = new JButton(icon);
        button.setBorder(new FlatButtonBorder());
        return button;
    }
}
