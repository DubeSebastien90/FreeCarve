package UI.SubWindows;

import UI.Widgets.BitInfoDisplay;
import Util.Bit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class BitSelectionWindow extends BasicWindow {
    private JToggleButton[] bitList = new JToggleButton[12];

    public BitSelectionWindow() {
        super(true);
        for (int i = 0; i < bitList.length; i++) {
            bitList[i] = new JToggleButton("bit" + i);
        }
        init();
    }

    public JToggleButton[] getBitList() {
        return bitList;
    }

    private void init() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                gbc.gridy = j;
                gbc.gridx = i;
                add(bitList[i + 6 * j], gbc);
            }
        }
    }
}
