package UI;

import javax.swing.*;
import java.awt.*;

public class ConfigWindow extends JPanel {

    public ConfigWindow() {
        this.setLayout(new GridBagLayout());
    }

    public void addNewComponent(Component component, int gridx, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = gridy;
        gbc.gridx = gridx;
        this.add(component, gbc);
    }
}
