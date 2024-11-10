package UI.SubWindows;

import Common.DTO.BitDTO;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.MainWindow;
import UI.UIConfig;
import UI.Widgets.BitInfoDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a panel allowing the selection of bits through toggle buttons.
 * The panel supports up to a maximum of 12 bits and communicates bit selection
 * changes to a specified listener.
 *
 * @author Antoine Morin
 * @version 1.1
 * @since 2024-11-01
 */
public class BitConfigurationPanel extends BasicWindow {
    private final JToggleButton[] bitList = new JToggleButton[UIConfig.INSTANCE.getMAX_NB_BITS()];
    private BitDTO[] bitDTOList;
    private final ChangeAttributeListener listener;
    private int selectedBit = 0;
    private final MainWindow mainWindow;

    // Map of the configured Bits, with the index of the bit as key
    private Map<Integer, BitDTO> configuredBitsMap;

    /**
     * Constructs a BitSelectionPanel with a specified attribute change listener.
     *
     * @param listener the listener for attribute changes triggered by bit selection.
     */
    public BitConfigurationPanel(ChangeAttributeListener listener, MainWindow mainWindow) {
        super(true);
        this.mainWindow = mainWindow;
        this.listener = listener;
        bitDTOList = mainWindow.getController().getBitsDTO();
        for (int i = 0; i < bitList.length; i++) {
            bitList[i] = new JToggleButton(bitDTOList[i].getName());
        }
        configuredBitsMap = new HashMap<Integer, BitDTO>();
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
        for (int i = 0; i < UIConfig.INSTANCE.getMAX_NB_BITS() / 2; i++) {
            for (int j = 0; j < 2; j++) {
                gbc.gridy = j;
                gbc.gridx = i;
                add(bitList[i + (UIConfig.INSTANCE.getMAX_NB_BITS() / 2) * j], gbc);
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
                                    BitConfigurationPanel.this, mainWindow
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



    public Map<Integer, BitDTO> getConfiguredBitsMap() {
        return configuredBitsMap;
    }
}
