package UI.Widgets;

import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import Common.DTO.BitDTO;
import UI.SubWindows.BitSelectionPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a display panel that shows and allows editing of bit information.
 * The display can be set to editable or read-only mode.
 *
 * @author Antoine Morin & Adam Côté
 * @version 1.1
 * @since 2024-11-01
 */
public class BitInfoDisplay extends BasicWindow implements Attributable {
    private final BitDTO bit;
    private final Boolean editable;
    private JButton modifyButton;
    private final BitSelectionPanel bitSelectionPanel;
    private NumberTextField widthTextArea;
    private JTextArea nameTextArea;
    private final MainWindow mainWindow;

    /**
     * Constructs a BitInfoDisplay with the specified bit data, editability, and selection panel.
     *
     * @param bit               the BitDTO object containing information about the bit.
     * @param editable          a boolean indicating if the bit information can be edited.
     * @param bitSelectionPanel the panel that displays the list of selectable bits.
     */
    public BitInfoDisplay(BitDTO bit, boolean editable, BitSelectionPanel bitSelectionPanel, MainWindow mainWindow) {
        super(false);
        this.mainWindow = mainWindow;
        this.bit = bit;
        this.editable = editable;
        this.bitSelectionPanel = bitSelectionPanel;
        init();
        setEventHandlers();
    }

    /**
     * Initializes the layout and components of the BitInfoDisplay, such as labels, text areas,
     * and the modify button. Sets up layout constraints and adds components to the panel.
     */
    private void init() {
        JLabel dimension = new JLabel("Outil");
        dimension.setFont(dimension.getFont().deriveFont(20f));
        widthTextArea = new NumberTextField(String.valueOf(bit.getDiameter()), diameter -> modifyBit(diameter.floatValue()));

        JLabel widthinfo = new JLabel("bitWidth");
        JLabel widthLabel = new JLabel("Diamètre");
        nameTextArea = new JTextArea(bit.getName());
        JLabel nameLabel = new JLabel("Nom");
        modifyButton = new JButton("Modifier");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(0, 10, 15, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(dimension, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 15, 10);
        add(nameLabel, gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        add(nameTextArea, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 15, 10);
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(widthLabel, gbc);
        gbc.insets = new Insets(0, 0, 15, 10);
        gbc.gridx = 4;
        gbc.gridy = 2;
        if (editable) {
            add(widthTextArea, gbc);
        } else {
            add(widthinfo);
        }

        gbc.insets = new Insets(0, 10, 15, 10);
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(modifyButton, gbc);
    }

    /**
     * Sets up event handlers for the modify button. When clicked, it updates the bit's information
     * in the controller if the display is editable, then refreshes the bit selection panel.
     */
    public void setEventHandlers() {
        modifyButton.addActionListener(e -> {
            if (editable) {
                mainWindow.getController().modifyBit(
                        bitSelectionPanel.getSelectedBit(),
                        new BitDTO(nameTextArea.getText(), Float.parseFloat(widthTextArea.getText()))
                );
            }
            bitSelectionPanel.refresh();
        });
    }

    /**
     * Updates the bit information using the Text in the text area for the name and a float value for the bit size
     *
     * @param diameter The diameter of the bit
     */
    private void modifyBit(float diameter) {
        if (editable) {
            mainWindow.getController().modifyBit(
                    bitSelectionPanel.getSelectedBit(),
                    new BitDTO(nameTextArea.getText(), diameter)
            );
        }
        bitSelectionPanel.refresh();

    }

    /**
     * Shows the name of this attribute, primarily used for display purposes.
     *
     * @return a JLabel containing the text "Bit modification".
     */
    @Override
    public JLabel showName() {
        return new JLabel("Bit modification");
    }

    /**
     * Displays the attributes panel with editable or non-editable components based on
     * the editability setting.
     *
     * @return the JPanel containing the attributes display.
     */
    @Override
    public JPanel showAttribute() {
        return this;
    }
}
