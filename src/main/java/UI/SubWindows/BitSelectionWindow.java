package UI.SubWindows;

import Util.Bit;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BitSelectionWindow extends BasicWindow {
    private Bit[] bitList = new Bit[12];

    public BitSelectionWindow() {
        super(true);
        init();
    }

    private void init() {
        GridBagConstraints gbc = new GridBagConstraints();
        //       gbc.gridwidth = 6;
//        gbc.gridheight = 2;

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                gbc.gridy = j;
                gbc.gridx = i;
                add(bitToPanel(bitList[j * i]), gbc);
            }
        }
    }

    private JPanel bitToPanel(Bit bit) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("bit"));
        return panel;
    }
}
