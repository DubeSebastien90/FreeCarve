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
        this.add(createSVGBUtton("save", true));
        this.addSeparator();
        this.add(createSVGBUtton("undo", true));
        this.add(createSVGBUtton("redo", true));
        this.addInvisibleSeparator();

        JButton button = new JButton("afficher grillage");
        button.setVisible(false);
        this.add(button);
    }

    private JButton createSVGBUtton(String iconName, boolean visible) {
        FlatSVGIcon icon = new FlatSVGIcon("UI/" + iconName + ".svg", uiConfig.getToolIconSize(), uiConfig.getToolIconSize());
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Button.foreground"));
        icon.setColorFilter(filter);
        JButton button = new JButton(icon);
        button.setBorder(new FlatButtonBorder());
        button.setVisible(visible);
        return button;
    }

    private void addInvisibleSeparator() {
        JToolBar.Separator s = new JToolBar.Separator(null);
        s.setVisible(false);
        this.add(s);
    }

    public void makeGridAvailable() {

    }
}
