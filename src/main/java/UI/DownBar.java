package UI;


import UI.Widgets.ColoredBox;
import com.formdev.flatlaf.ui.FlatArrowButton;

import static Util.UiUtil.createSVGButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The {@code DownBar} class encapsulates the UI elements of the navigation bar
 * at the bottom of the screen, allowing users to toggle between relevant menus
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class DownBar {
    private UIConfig uiConfig = UIConfig.INSTANCE;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JPanel container;
    private BoxLayout horizontalLayout;

    private JButton folderButton;
    private JButton canvasButton;
    private JButton bitsButton;
    private JButton cutButton;
    private JButton simulationButton;
    private JButton exportButton;
    private JButton arrowLeft;
    private JButton arrowRight;
    private FlatArrowButton arrowTest;
    ArrayList<JButton> buttons;
    ArrayList<JComponent> components;

    private Dimension coloredBoxDimension;
    private Color coloredBoxColor;

    int selectedButtonIndex;

    /**
     * Constructs a {@code DownBar} instance initializing all of it's sub-component,
     * setting up all of the actions of the buttons and setting the {@code folderButton} as the
     * initial state
     */
    public DownBar() {
        init();
        this.setupButtonAction();
        updateDownBar(folderButton);
    }

    /**
     * @return the panel container of the {@code DownBar}
     */
    public JScrollPane getDownBar() {
        return scrollPane;
    }

    /**
     * @return the current selected button of the {@code DownBar}
     */
    public JButton getSelectedButtonIndex() {
        return buttons.get(selectedButtonIndex);
    }

    /**
     * Updates the appearance/functions of the buttons of the {@code DownBar}
     * according to the button that was just clicked
     *
     * @param buttonClicked the button that was just clicked
     */
    private void updateDownBar(JButton buttonClicked) {
        boolean beforeButtonClicked = false;
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i) == buttonClicked) {
                buttonClicked.setBackground(UIConfig.INSTANCE.getColorGreen1());
                selectedButtonIndex = i;
                beforeButtonClicked = true;
            } else if (beforeButtonClicked) {
                buttons.get(i).setBackground(UIConfig.INSTANCE.getColorButtonBackground());
            } else {
                buttons.get(i).setBackground(UIConfig.INSTANCE.getColorBlue1());
            }

            // Updates the ColoredBox
            if (i > 0) {
                Color col = buttons.get(i).getBackground();
                int index = i * 2 - 1;
                components.get(index).setBackground(col);

            }
        }
    }

    /**
     * Gives every button it's function when clicked
     */
    private void setupButtonAction() {
        for (JButton button : buttons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateDownBar(button);
                }
            });
        }

        arrowLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBackward();
            }
        });

        arrowRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveForward();
            }
        });
    }

    /**
     * Select the next {@code DownBar} button, while respecting constraints
     */
    private void moveForward() {
        if (selectedButtonIndex < buttons.size() - 1) {
            selectedButtonIndex++;
            updateDownBar(buttons.get(selectedButtonIndex));
        }
    }

    /**
     * Select the previous {@code DownBar} button, while respecting constraints
     */
    private void moveBackward() {
        if (selectedButtonIndex > 0) {
            selectedButtonIndex--;
            updateDownBar(buttons.get(selectedButtonIndex));
        }
    }

    /**
     * Initiates all of the {@code DownBar} components
     */
    private void init() {
        panel = new JPanel();
        scrollPane = new JScrollPane();
        container = new JPanel();
        horizontalLayout = new BoxLayout(container, BoxLayout.X_AXIS);
        coloredBoxDimension = new Dimension(20, 10);
        coloredBoxColor = UIManager.getColor("Button.background");

        folderButton = new JButton("Fichier");
        folderButton.setToolTipText("Menu fichier");
        canvasButton = new JButton("Canvas");
        canvasButton.setToolTipText("Menu canvas");
        bitsButton = new JButton("Outils");
        bitsButton.setToolTipText("Menu outils");
        cutButton = new JButton("Coupes");
        cutButton.setToolTipText("Menu coupes");
        simulationButton = new JButton("Simulation");
        simulationButton.setToolTipText("Menu simulation");
        exportButton = new JButton("Exportation");
        exportButton.setToolTipText("Menu exportation");
        arrowLeft = createSVGButton("leftArrow", true, uiConfig.getToolIconSize());
        arrowLeft.setToolTipText("Menu précédent");
        arrowRight = createSVGButton("rightArrow", true, uiConfig.getToolIconSize());
        arrowRight.setToolTipText("Menu suivant");

        buttons = new ArrayList<JButton>();
        buttons.add(folderButton);
        buttons.add(canvasButton);
        buttons.add(bitsButton);
        buttons.add(cutButton);
        buttons.add(simulationButton);
        buttons.add(exportButton);

        // Creates the component list by alternating between JButton and ColoredBox
        components = new ArrayList<JComponent>();
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setMargin(new Insets(5, 20, 5, 20));
            buttons.get(i).setFocusPainted(false);
            components.add(buttons.get(i));
            if (i < buttons.size() - 1) {
                ColoredBox cb = new ColoredBox(coloredBoxColor,
                        ((int) coloredBoxDimension.getWidth()),
                        ((int) coloredBoxDimension.getHeight()));
                components.add(cb);
            }
        }
        // Adds all component to the container
        container.add(arrowLeft);
        for (JComponent component : components) {
            container.add(component);
        }
        container.add(arrowRight);
        panel.add(container);

        panel.setBorder(new EmptyBorder(10, 0, 10, 0));
        container.setLayout(horizontalLayout);
        scrollPane.setViewportView(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }
}
