package UI.Widgets;

import UI.UIConfig;
import Util.UiUtil;

import javax.swing.*;
import java.awt.*;

public class BigButton extends JPanel {
    private JButton button;

    public BigButton(String text) {
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(0, 0));
        button = new JButton(text);
        button.setBorder(null);
        button.setFont(button.getFont().deriveFont(20f));
        button.setBackground(UIManager.getColor("Button.secondaryBackground"));
        button.setForeground(this.getBackground());
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        UiUtil.makeJPanelRoundCorner(this, graphics2D);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        button.setPreferredSize(new Dimension(this.getWidth() / 3 * 2, this.getHeight() / 2));
        add(button, gbc);

    }
}
