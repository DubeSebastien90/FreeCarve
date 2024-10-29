package UI;

import Domain.BitDTO;
import UI.SubWindows.BasicWindow;
import UI.SubWindows.BitSelectionPanel;
import UI.SubWindows.Rendering2DWindow;
import UI.Widgets.BigButton;
import UI.Widgets.BitInfoDisplay;
import UI.Widgets.ChooseDimension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Represents a configuration choice window that allows users to select
 * options for the project. This window contains various components
 * such as a rendering area, a bit selection window, and dimension
 * configuration options.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-25
 */
public class ConfigChoiceWindow extends JPanel {
    private final Rendering2DWindow rend;
    private final BigButton nextButton = new BigButton("Suivant");
    private BitSelectionPanel bitWindow;
    private BasicWindow attributeWindow;
    private int selectedBit;

    /**
     * Constructs a ConfigChoiceWindow and initializes its components and layout.
     */
    public ConfigChoiceWindow() {
        this.setLayout(new GridBagLayout());
        rend = new Rendering2DWindow();
        bitWindow = new BitSelectionPanel();
        attributeWindow = new BasicWindow(true);
        setFocusable(true);
        requestFocusInWindow();
        init();
        setButtonEventHandler();
        this.selectedBit = 0;
    }

    /**
     * Returns the Rendering2DWindow contained in this configuration choice window.
     *
     * @return The Rendering2DWindow instance.
     */
    public Rendering2DWindow getRendering2DWindow() {
        return this.rend;
    }

    /**
     * Initializes the layout and adds the necessary components to the window.
     * This method sets up the grid bag constraints for arranging components.
     */
    public void init() {
        GridBagConstraints gbc = new GridBagConstraints();

        ChooseDimension dimensions = new ChooseDimension();
        attributeWindow.setPreferredSize(new Dimension(0, 0));
        attributeWindow.add(dimensions);

        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 2;
        gbc.weighty = 0.75;
        gbc.fill = GridBagConstraints.BOTH;
        add(rend, gbc);

        gbc.insets = new Insets(5, 0, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 2;
        gbc.weighty = 0.25;
        gbc.fill = GridBagConstraints.BOTH;
        add(bitWindow, gbc);

        gbc.insets = new Insets(0, 0, 0, 5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.75;
        gbc.fill = GridBagConstraints.BOTH;
        add(attributeWindow, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.25;
        gbc.fill = GridBagConstraints.BOTH;
        add(nextButton, gbc);
    }

    /**
     * Sets the event handler for the next button. When the button is clicked,
     * it triggers the action to proceed to the next window in the main interface.
     */
    private void setButtonEventHandler() {
        nextButton.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.INSTANCE.getMiddleContent().nextWindow();
            }
        });

        for (int i = 0; i < bitWindow.getBitList().length; i++) {
            JToggleButton bit = bitWindow.getBitList()[i];
            int finalI = i;
            bit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridy = 1;
                    attributeWindow.add(new JLabel(bit.getText()), gbc);
                    for (int j = 0; j < bitWindow.getBitList().length; j++) {
                        bitWindow.getBitList()[j].setSelected(finalI == j);
                    }
                    attributeWindow.removeAll();
                    attributeWindow.repaint();
                    BitInfoDisplay bitInfo = new BitInfoDisplay(MainWindow.INSTANCE.getController().getBits()[finalI], true, ConfigChoiceWindow.this);
                    attributeWindow.add(bitInfo, gbc);
                    selectedBit = finalI;
                }
            });
        }
    }

    /**
     * Paints the component. This method calls the superclass's paint method
     * and revalidates the next button.
     *
     * @param graphics The graphics context used for painting.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        nextButton.revalidate();
    }

    public int getSelectedBit() {
        return selectedBit;
    }

    public BitSelectionPanel getBitWindow() {
        return bitWindow;
    }
}
