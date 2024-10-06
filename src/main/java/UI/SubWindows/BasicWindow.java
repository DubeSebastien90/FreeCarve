package UI.SubWindows;

import Util.UiUtil;

import javax.swing.*;
import java.awt.*;

public class BasicWindow extends JPanel {

    public BasicWindow(boolean haveBackground) {
        this.setLayout(new GridBagLayout());
        if (haveBackground) {
            this.setBackground(UIManager.getColor("SubWindow.background"));
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);
    }

}
