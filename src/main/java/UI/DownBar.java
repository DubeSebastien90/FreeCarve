package UI;


import UI.Widgets.ColoredBox;

import static Common.UiUtil.createSVGButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private final UIConfig uiConfig = UIConfig.INSTANCE;
    private JScrollPane scrollPane;

    private JButton arrowLeft;
    private JButton arrowRight;
    private final Color seenColor = UIManager.getColor("Button.blue");
    private final Color currentColor = UIManager.getColor("Button.green");
    private final Color notSeenColor = UIManager.getColor("Button.background");

    ArrayList<JButton> buttons;
    ArrayList<JComponent> components;

    private final MainWindow mainWindow;

    /**
     * Constructs a {@code DownBar} instance initializing all of it's sub-component,
     * setting up all of the actions of the buttons and setting the {@code folderButton} as the
     * initial state
     */
    public DownBar(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        init();
        this.setupButtonAction();
        setButtonBlueToIndex(0);
    }

    /**
     * @return the panel container of the {@code DownBar}
     */
    public JScrollPane getDownBar() {
        return scrollPane;
    }

    /**
     * Sets all the buttons before the one clicked to blue, the button clicked to green and all the buttons after to the default button background color.
     *
     * @param index the index of the clicked button
     */
    public void setButtonBlueToIndex(int index) {
        index = Math.max(Math.min(index, buttons.size()), 0);
        for (int i = 0; i < index; i++) {
            buttons.get(i).setBackground(seenColor);
            if (i > 0) {
                components.get(i * 2 - 1).setBackground(seenColor);
            }
        }
        if (index * 2 - 1 > 0) {
            components.get(index * 2 - 1).setBackground(currentColor);
        }
        buttons.get(index).setBackground(currentColor);
        for (int i = index + 1; i < buttons.size(); i++) {
            buttons.get(i).setBackground(notSeenColor);
            components.get(i * 2 - 1).setBackground(notSeenColor);
        }
    }

    /**
     * Gives every button it's function when clicked
     */
    private void setupButtonAction() {
        for (int i = 0; i < buttons.size(); i++) {
            JButton button = buttons.get(i);
            int finalI = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setButtonBlueToIndex(finalI);
                    mainWindow.getMiddleContent().changePanel(MiddleContent.MiddleWindowType.values()[finalI]);
                }
            });
        }

        arrowLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getMiddleContent().previousWindow();
            }
        });

        arrowRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getMiddleContent().nextWindow();
            }
        });
    }

    /**
     * Initiates all of the {@code DownBar} components
     */
    private void init() {
        JPanel panel = new JPanel();
        scrollPane = new JScrollPane();
        JPanel container = new JPanel();
        BoxLayout horizontalLayout = new BoxLayout(container, BoxLayout.X_AXIS);
        Dimension coloredBoxDimension = new Dimension(20, 10);
        Color coloredBoxColor = UIManager.getColor("Button.background");

        JButton folderButton = new JButton("Fichier");
        folderButton.setToolTipText("Menu fichier");
        JButton bitsButton = new JButton("Configuration");
        bitsButton.setToolTipText("Menu configuration");
        JButton cutButton = new JButton("Coupes");
        cutButton.setToolTipText("Menu coupes");
        JButton simulationButton = new JButton("Simulation");
        simulationButton.setToolTipText("Menu simulation");
        JButton exportButton = new JButton("Exportation");
        exportButton.setToolTipText("Menu exportation");
        arrowLeft = createSVGButton("leftArrow", true, "Menu précédent", uiConfig.getToolIconSize());
        arrowLeft.setBorder(null);
        arrowLeft.putClientProperty("JButton.buttonType", "toolBarButton");
        arrowRight = createSVGButton("rightArrow", true, "Menu suivant", uiConfig.getToolIconSize());
        arrowRight.setBorder(null);
        arrowRight.putClientProperty("JButton.buttonType", "toolBarButton");

        buttons = new ArrayList<JButton>();
        buttons.add(folderButton);
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
        scrollPane.setBorder(null);
    }
}
