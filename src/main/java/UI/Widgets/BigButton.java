package UI.Widgets;

import UI.UIConfig;
import UI.UiUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class is used to create a button which respect certain conditions, so it is the same throughout the project
 *
 * @author Adam côté
 * @version 1.0
 * @since 2024-11-02
 */
public class BigButton extends JPanel {
    private final JButton button;

    /**
     * @return The button
     */
    public JButton getButton() {
        return button;
    }

    /**
     * Constructs a BigButton, a JPanel that contains a JButton. The JButton is orange and have round corners.
     *
     * @param text The text displayed on the button.
     */
    public BigButton(String text) {
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(0, 0));
        button = new JButton(text);
        button.setBorder(null);
        button.setFont(button.getFont().deriveFont(20f));
        button.setBackground(UIManager.getColor("Button.secondaryBackground"));
        button.setForeground(this.getBackground());
        addHoverListener();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        button.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding() * 3,
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding() * 3));
        add(button, gbc);
    }

    /**
     * Paints the button in the panel and do all the manipulations so the buttons look like it should
     *
     * @param graphics A graphic object to be paint on the JPanel
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = ((Graphics2D) graphics);
        UiUtil.makeJPanelRoundCorner(this, graphics2D);

    }

    private void addHoverListener() {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(UIManager.getColor("Button.secondaryBackgroundHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIManager.getColor("Button.secondaryBackground"));
            }
        });
    }
}
