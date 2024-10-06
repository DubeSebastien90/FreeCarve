package UI.Widgets;

import UI.SubWindows.BasicWindow;
import Util.UiUtil;

import javax.swing.*;
import java.awt.*;

public class ChooseDimension extends BasicWindow {

    public ChooseDimension() {
        super(false);
        init();
    }

    public void init() {
        JLabel dimension = new JLabel("Dimensions");
        dimension.setFont(dimension.getFont().deriveFont(20f));
        JTextArea xTextArea = new JTextArea();
        JTextArea yTextArea = new JTextArea();
        JLabel xLabel = new JLabel("x");
        JLabel yLabel = new JLabel("y");


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(0, 10, 15, 10);
        //gbc.weightx = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 4;
        //gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;

        add(dimension, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 15, 10);
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(xLabel, gbc);

        gbc.insets = new Insets(0, 0, 15, 10);
        //gbc.weightx = 4;
        gbc.gridx = 4;
        gbc.gridy = 1;
        add(xTextArea, gbc);

        gbc.insets = new Insets(0, 10, 15, 10);
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(yLabel, gbc);

        gbc.insets = new Insets(0, 0, 15, 10);
        //gbc.weightx = 4;

        gbc.gridx = 4;
        gbc.gridy = 2;
        add(yTextArea, gbc);

    }
}
