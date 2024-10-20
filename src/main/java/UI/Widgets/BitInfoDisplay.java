package UI.Widgets;

import UI.SubWindows.BasicWindow;
import Domain.Bit;

import javax.swing.*;
import java.awt.*;

public class BitInfoDisplay extends BasicWindow {
    private Bit bit;
    private Boolean editable;

    public BitInfoDisplay(Bit bit, boolean editable) {
        super(false);
        this.bit = bit;
        this.editable = editable;
        init();
    }

    private void init() {
        JLabel dimension = new JLabel("Bit name");
        dimension.setFont(dimension.getFont().deriveFont(20f));
        JTextArea widthTextArea = new JTextArea();
        JLabel widthinfo = new JLabel("bitWidth");
        JLabel widthLabel = new JLabel("width");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(0, 10, 15, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(dimension, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 15, 10);
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(widthLabel, gbc);
        gbc.insets = new Insets(0, 0, 15, 10);
        gbc.gridx = 4;
        gbc.gridy = 1;
        if (editable) {
            add(widthTextArea, gbc);
        } else {
            add(widthinfo);
        }

    }
}
