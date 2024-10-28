package UI.Widgets;

import UI.ConfigChoiceWindow;
import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import Domain.BitDTO;


import javax.swing.*;
import java.awt.*;

//TODO, Supposed to be a small jpanel linked with a bit and when the attributes in the JPanel changes the bit changes
public class BitInfoDisplay extends BasicWindow {
    private BitDTO bit;
    private Boolean editable;
    private JButton modifyButton;
    private ConfigChoiceWindow configChoiceWindow;
    private JTextArea widthTextArea;
    private JTextArea nameTextArea;

    public BitInfoDisplay(BitDTO bit, boolean editable, ConfigChoiceWindow configChoiceWindow) {
        super(false);
        this.bit = bit;
        this.editable = editable;
        this.configChoiceWindow = configChoiceWindow;
        init();
        setEventHandlers();
    }

    private void init() {
        JLabel dimension = new JLabel("Outil");
        dimension.setFont(dimension.getFont().deriveFont(20f));
        widthTextArea = new JTextArea(String.valueOf(bit.getDiameter()));
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

    public void setEventHandlers(){
        modifyButton.addActionListener(e -> {
            if (editable) {
                MainWindow.INSTANCE.getController().modifyBit(configChoiceWindow.getSelectedBit(), new BitDTO(nameTextArea.getText(), Float.parseFloat(widthTextArea.getText())));
            }
            configChoiceWindow.getBitWindow().refresh();
            System.out.println("Devrais repeindre");
        });
    }
}
