package UI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatButtonBorder;

import static Util.UiUtil.createSVGButton;

import javax.swing.*;
import java.awt.*;

public class ProjectWindow extends JPanel {
    private final UIConfig uiConfig = UIConfig.INSTANCE;
    private MiddleContent parentWindow;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JPanel westPanel = new JPanel(new GridBagLayout());

    public ProjectWindow(MiddleContent parentWindow) {
        super(new BorderLayout());
        this.parentWindow = parentWindow;
        this.add(westPanel, BorderLayout.WEST);
        this.setVisible(true);
        init();
    }

    private void init() {
        gbc.weightx = 1;
        gbc.weighty = 0;

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 150, 40, 0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel projets = new JLabel("Projets");
        projets.setFont(projets.getFont().deriveFont(40f));
        westPanel.add(projets, gbc);


        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 150, 20, 0);  // Espacement de 15 pixels en bas
        westPanel.add(createSpecialWindowButton("newFile", "Nouveau"), gbc);

        gbc.gridy = 3;
        westPanel.add(createSpecialWindowButton("openFile", "ouvrir"), gbc);
    }


    private JButton createSpecialWindowButton(String iconName, String text) {
        JButton button = createSVGButton(iconName, true, uiConfig.getProjectSelectionMenuButtonSize(), UIManager.getColor("Button.secondaryBackground"));
        button.setBorder(null);
        button.putClientProperty("JButton.buttonType", "toolBarButton");
        button.setText(text);
        button.setForeground(UIManager.getColor("Button.secondaryBackground"));
        button.setFont(button.getFont().deriveFont(20f));
        return button;
    }
}
