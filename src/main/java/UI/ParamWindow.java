package UI;

import UI.SubWindows.BasicWindow;
import UI.SubWindows.Rendering2DWindow;
import UI.Widgets.BigButton;
import UI.Widgets.ChooseDimension;
import Util.UiUtil;

import javax.swing.*;
import java.awt.*;

public class ParamWindow extends JPanel {

    public ParamWindow() {
        this.setLayout(new GridBagLayout());
        init();
    }

    public void init() {
        GridBagConstraints gbc = new GridBagConstraints();
        Rendering2DWindow rend = new Rendering2DWindow();
        BasicWindow configWindow = new BasicWindow(true);
        ChooseDimension dimensions = new ChooseDimension();
        configWindow.setPreferredSize(new Dimension(0, 0));
        configWindow.add(dimensions);
        gbc.insets = new Insets(0, 0, 0, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(rend, gbc);
        gbc.insets = new Insets(0, 0, 0, 5);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.75;
        gbc.fill = GridBagConstraints.BOTH;
        add(configWindow, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.25;
        gbc.fill = GridBagConstraints.BOTH;
        add(new BigButton("Suivant"), gbc);

    }
}
