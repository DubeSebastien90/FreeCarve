package UI.SubWindows;

import Domain.BitDTO;
import UI.MainWindow;

import javax.swing.*;
import java.awt.*;

public class BitSelectionPanel extends BasicWindow {
    private final int MAX_BIT = 12;
    private JToggleButton[] bitList = new JToggleButton[MAX_BIT];
    private BitDTO[] bitDTOList;

    public BitSelectionPanel() {
        super(true);
        bitDTOList = MainWindow.INSTANCE.getController().getBitsDTO();
        for (int i = 0; i < bitList.length; i++) {
            bitList[i] = new JToggleButton(bitDTOList[i].getName());
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
        for (int i = 0; i < MAX_BIT / 2; i++) {
            for (int j = 0; j < 2; j++) {
                gbc.gridy = j;
                gbc.gridx = i;
                add(bitList[i + (MAX_BIT / 2) * j], gbc);
            }
        }
    }

    public void refresh() {
        bitDTOList = MainWindow.INSTANCE.getController().getBitsDTO();
        for (int i = 0; i < bitList.length; i++) {
            bitList[i].setText(bitDTOList[i].getName());
        }
    }
}
