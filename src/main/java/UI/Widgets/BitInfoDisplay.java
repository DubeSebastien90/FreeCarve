package UI.Widgets;

import Common.DTO.BitDTO;
import Common.Exceptions.InvalidBitException;
import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import UI.SubWindows.BitConfigurationPanel;
import UI.UIConfig;
import UI.UiUnits;
import UI.UiUtil;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Represents a display panel that shows and allows editing of bit information.
 * The display can be set to editable or read-only mode.
 *
 * @author Antoine Morin & Adam Côté
 * @version 1.1
 * @since 2024-11-01
 */
public class BitInfoDisplay extends GenericAttributeBox implements Attributable {
    private final BitDTO bit;
    private final Boolean editable;
    private JButton modifyButton;
    private JButton removeButton;
    private final BitConfigurationPanel bitConfigurationPanel;
    private MeasurementInputField widthTextArea;
    private JTextArea nameTextArea;
    private final MainWindow mainWindow;

    /**
     * Constructs a BitInfoDisplay with the specified bit data, editability, and selection panel.
     *
     * @param bit                   the BitDTO object containing information about the bit.
     * @param editable              a boolean indicating if the bit information can be edited.
     * @param bitConfigurationPanel the panel that displays the list of selectable bits.
     */
    public BitInfoDisplay(BitDTO bit, boolean editable, BitConfigurationPanel bitConfigurationPanel, MainWindow mainWindow) {
        super(false, "Outils");
        this.mainWindow = mainWindow;
        this.bit = bit;
        this.editable = editable;
        this.bitConfigurationPanel = bitConfigurationPanel;
        init();
        setEventHandlers();
    }

    /**
     * Initializes the layout and components of the BitInfoDisplay, such as labels, text areas,
     * and the modify button. Sets up layout constraints and adds components to the panel.
     */
    private void init() {
        widthTextArea = new MeasurementInputField(mainWindow, "Diamètre   ", bit.getDiameter(), UiUnits.MILLIMETERS);
        BasicWindow nameContainer = new BasicWindow(false);
        nameTextArea = new JTextArea(bit.getName());
        nameTextArea.setBackground(UIManager.getColor("SubWindow.lightBackground2"));
        nameTextArea.setColumns(10);
        nameTextArea.setBorder(new FlatRoundBorder());
        BoxLayout layout = new BoxLayout(nameContainer, BoxLayout.X_AXIS);
        nameContainer.setLayout(layout);
        nameContainer.setBackground(null);
        JLabel nameLabel = new JLabel("Nom");
        nameLabel.setOpaque(true);
        nameLabel.setBackground(UIManager.getColor("SubWindow.lightBackground1"));
        nameLabel.setBorder(new FlatEmptyBorder());
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setBorder(new EmptyBorder(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding()));

        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameTextArea.setAlignmentX(Component.RIGHT_ALIGNMENT);
        nameContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameContainer.add(nameLabel);
        nameContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        nameContainer.add(nameTextArea);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;

        add(nameContainer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(widthTextArea, gbc);
//        JLabel displayTitle = new JLabel("Outil");
//        displayTitle.setFont(displayTitle.getFont().deriveFont(20f));
//        widthTextArea = new MeasurementInputField(mainWindow, "", bit.getDiameter(), 0, 300, UiUnits.MILLIMETERS);
//
//        JLabel widthLabel = new JLabel("Diamètre");
//        nameTextArea = new JTextArea(bit.getName());
//        nameTextArea.setBackground(UIManager.getColor("SubWindow.lightBackground2"));
//        JLabel nameLabel = new JLabel("Nom");
//        modifyButton = new JButton("Modifier");
        removeButton = UiUtil.createSVGButton("trash", true, UIConfig.INSTANCE.getToolIconSize(), Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        removeButton.setBackground(null);
        removeButton.setBorder(null);
        add(removeButton, gbc);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        gbc.insets = new Insets(0, 10, 15, 10);
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 2;
//        gbc.fill = GridBagConstraints.BOTH;
//        add(displayTitle, gbc);
//
//        gbc.gridwidth = 1;
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.insets = new Insets(0, 10, 15, 10);
//        add(nameLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        gbc.weightx = 1;
//        gbc.insets = new Insets(0, 10, 15, 10);
//        add(nameTextArea, gbc);
//
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.insets = new Insets(0, 10, 15, 10);
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        add(widthLabel, gbc);
//        gbc.insets = new Insets(0, 10, 15, 10);
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//        gbc.weightx = 1;
//        add(widthTextArea, gbc);
//
//        gbc.insets = new Insets(0, 10, 15, 10);
//        gbc.gridy = 3;
//        gbc.gridx = 0;
//        gbc.weightx = 3;
//        gbc.anchor = GridBagConstraints.CENTER;
//        add(removeButton, gbc);
//
//        gbc.insets = new Insets(0, 10, 15, 10);
//        gbc.gridy = 3;
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.CENTER;
//        add(modifyButton, gbc);
    }

    /**
     * Sets up event handlers for the modify button. When clicked, it updates the bit's information
     * in the controller if the display is editable, then refreshes the bit selection panel.
     */
    public void setEventHandlers() {
//        modifyButton.addActionListener(e -> {
//            if (editable) {
//                modifyBit(widthTextArea.getMMValue());
//            }
//            bitConfigurationPanel.refresh();
//        });
        widthTextArea.getNumericInput().addPropertyChangeListener("value", evt -> {
            if (widthTextArea.getMMValue() > 0) {
                modifyBit(widthTextArea.getMMValue());
            }
            bitConfigurationPanel.refresh();
        });
        nameTextArea.addPropertyChangeListener("value", evt -> {
            if (widthTextArea.getMMValue() > 0) {
                modifyBit(widthTextArea.getMMValue());
                bitConfigurationPanel.refresh();
            }
        });
        nameTextArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "loseFocus");
        nameTextArea.getActionMap().put("loseFocus", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextArea.transferFocus();
                if (widthTextArea.getMMValue() > 0) {
                    modifyBit(widthTextArea.getMMValue());
                    bitConfigurationPanel.refresh();
                }
            }
        });

        removeButton.addActionListener(e -> {
            removeBit();
        });
    }

    /**
     * Updates the bit information using the Text in the text area for the name and a double value for the bit size
     *
     * @param diameter The diameter of the bit
     */
    private void modifyBit(double diameter) {
        BitDTO newBit = new BitDTO(nameTextArea.getText(), diameter);
        if (editable) {
            mainWindow.getController().modifyBit(
                    bitConfigurationPanel.getSelectedBit(),
                    newBit
            );
        }
        bitConfigurationPanel.refresh();
        mainWindow.getMiddleContent().getCutWindow().notifyObservers();
    }

    private void removeBit() {
        if (widthTextArea.getMMValue() == 0.0)
            //Todo: Gérer message qui dit qu'on ne peut pas delete si aucun bit configuré
            return;

        try {
            mainWindow.getController().removeBit(bitConfigurationPanel.getSelectedBit());
            this.mainWindow.getMiddleContent().getCutWindow().getBitSelectionPanel().setSelectedBit(
                    (Integer) this.mainWindow.getController().getConfiguredBitsMap().keySet().toArray()[0]
            );
        } catch (InvalidBitException e) {
            //Todo: Gérer le message d'erreur, might never happen
            return;
        }
        nameTextArea.setText("Aucun outil assigné");
        widthTextArea.getNumericInput().setValue(0.0);
        bitConfigurationPanel.refresh();
        mainWindow.getMiddleContent().getCutWindow().notifyObservers();
        //mainWindow.getMiddleContent().getCutWindow().getCutListPanel().refreshAttributeContainer();
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
