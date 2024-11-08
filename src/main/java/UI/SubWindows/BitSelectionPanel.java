package UI.SubWindows;

import Common.BitDTO;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.MainWindow;
import UI.Widgets.BitInfoDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents a panel allowing the selection of bits through toggle buttons.
 * The panel supports up to a maximum of 12 bits and communicates bit selection
 * changes to a specified listener.
 *
 * @author Antoine Morin
 * @version 1.1
 * @since 2024-11-01
 */
public class BitSelectionPanel extends BasicWindow {
    private final int MAX_BIT = 12;
    private final JToggleButton[] bitList = new JToggleButton[MAX_BIT];
    private BitDTO[] bitDTOList;
    private final ChangeAttributeListener listener;
    private int selectedBit = 0;
    private final MainWindow mainWindow;

    /**
     * Constructs a BitSelectionPanel with a specified attribute change listener.
     *
     * @param listener the listener for attribute changes triggered by bit selection.
     */
    public BitSelectionPanel(ChangeAttributeListener listener, MainWindow mainWindow) {
        super(true);
        this.mainWindow = mainWindow;
        this.listener = listener;
        bitDTOList = mainWindow.getController().getBitsDTO();
        for (int i = 0; i < bitList.length; i++) {
            bitList[i] = new JToggleButton(bitDTOList[i].getName());
        }
        init();
        setButtonEvent();
    }

    /**
     * Returns the index of the currently selected bit.
     *
     * @return the index of the selected bit as an integer.
     */
    public int getSelectedBit() {
        return selectedBit;
    }

    /**
     * Returns the list of toggle buttons representing each bit.
     *
     * @return an array of JToggleButton, where each button represents a bit.
     */
    public JToggleButton[] getBitList() {
        return bitList;
    }

    /**
     * Initializes the layout of the bit selection panel and adds the toggle buttons
     * to the panel using a grid layout.
     */
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

    /**
     * Refreshes the labels of the bit toggle buttons to reflect the latest bit names.
     */
    public void refresh() {
        bitDTOList = mainWindow.getController().getBitsDTO();
        for (int i = 0; i < bitList.length; i++) {
            bitList[i].setText(bitDTOList[i].getName());
        }
    }

    /**
     * Sets up the action listeners for each toggle button. When a button is selected,
     * it triggers an attribute change event, updates the selected bit index, and ensures
     * only the selected button is toggled.
     */
    public void setButtonEvent() {
        for (int i = 0; i < getBitList().length; i++) {
            JToggleButton bit = getBitList()[i];
            int finalI = i;
            bit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChangeAttributeEvent event = new ChangeAttributeEvent(
                            this,
                            new BitInfoDisplay(
                                    mainWindow.getController().getBitsDTO()[finalI],
                                    true,
                                    BitSelectionPanel.this, mainWindow
                            )
                    );
                    listener.changeAttributeEventOccurred(event);
                    for (int j = 0; j < getBitList().length; j++) {
                        getBitList()[j].setSelected(finalI == j);
                    }
                    selectedBit = finalI;
                }
            });
        }
    }
}
